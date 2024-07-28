package br.com.process.integration.database.core.domain;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.stringtemplate.v4.ST;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.process.integration.database.core.util.Constants;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("database")
public class ConfigQuery {

	@Autowired
	private ResourceLoader resourceLoader;

	private static final Map<String, String> OPERADORES = new HashMap<>();

	static {
		OPERADORES.put(Constants.HTML_MAIOR_QUE, Constants.MAIOR_QUE);
		OPERADORES.put(Constants.HTML_MENOR_QUE, Constants.MENOR_QUE);
		OPERADORES.put(Constants.HTML_MAIOR_OU_IGUAL, Constants.MAIOR_OU_IGUAL);
		OPERADORES.put(Constants.HTML_MENOR_OU_IGUAL, Constants.MENOR_OU_IGUAL);
		OPERADORES.put(Constants.HTML_IGUAL, Constants.IGUAL);
		OPERADORES.put(Constants.HTML_DIFERENTE, Constants.DIFERENTE);
		OPERADORES.put(Constants.HTML_LIKE, Constants.LIKE);
		OPERADORES.put(Constants.HTML_IN, Constants.IN);
		OPERADORES.put(Constants.HTML_BETWEEN, Constants.BETWEEN);
	}

	public String executeSQL(Map<String, Object> filters, String fileQuery, String invokerQuery, MapSqlParameterSource mapSqlParameterSource) throws Exception {
		return executeQuery(filters, fileQuery, invokerQuery, mapSqlParameterSource, false);
	}

	public String executeCountSQL(Map<String, Object> filters, String fileQuery, String invokerQuery, MapSqlParameterSource mapSqlParameterSource) throws Exception {
		return executeQuery(filters, fileQuery, invokerQuery, mapSqlParameterSource, true);
	}

	private String executeQuery(Map<String, Object> filters, String fileQuery, String invokerQuery, MapSqlParameterSource mapSqlParameterSource, boolean isCount) throws Exception {

		StringBuilder sql = new StringBuilder();

		Query query = getQuery(fileQuery, invokerQuery);

		if(query != null) {
			
			if (isCount) {
				
				if(query.getGroupby() == null) {
					
					// SELECT COUNT
					Pattern pattern = Pattern.compile("\\b(from\\b.*)", Pattern.CASE_INSENSITIVE);
					Matcher matcher = pattern.matcher(query.getSelect());
					if (matcher.find()) {
						sql.append(Constants.SELECT_COUNT + Constants.WRITERSPACE + matcher.group(1).trim() + Constants.WRITERSPACE);
					}
					
				} else {
					
					// SELECT COUNT
					sql.append(Constants.SELECT_COUNT + Constants.WRITERSPACE + Constants.FROM + Constants.WRITERSPACE + "(" + Constants.WRITERSPACE);
					
					// SELECT
					sql.append(query.getSelect() + Constants.WRITERSPACE);
				}
				
			} else {
				// SELECT
				sql.append(query.getSelect() + Constants.WRITERSPACE);
			}
			
			// JOIN
			if (query.getJoin() != null) {
				query.getJoin().forEach(join -> 
				sql.append(join + Constants.WRITERSPACE)
						);			
			}
			
			// WHERE
			if (query.getWhere() != null) {
				sql.append(Constants.WHERE + Constants.WRITERSPACE);
				query.getWhere().forEach(where -> {
					Pattern pattern = Pattern.compile("\\{(.*?)\\}");
					Matcher matcher = pattern.matcher(where);
					String operator = Constants.IGUAL;
					if (matcher.find()) {
						if (filters.get(matcher.group(1).trim()) != null) {
							operator = OPERADORES.get(filters.get(matcher.group(1).trim()).toString());
						}
						where = alterPlaceholders(where, matcher.group(1).trim(), operator);
					}
					pattern = Pattern.compile(":(?:[^():]*\\([^)]*\\))?([^:()\\s]+)");
					matcher = pattern.matcher(where);
					if (matcher.find()) {
						if (filters.get(matcher.group(1).trim()) != null) {
							if (where.contains(Constants.IN)) {
								sql.append(where.replaceAll("[\\(\\)]", "").replace(":" + matcher.group(1).trim(),
										"(" + Constants.DOIS_PONTOS + matcher.group(1).trim() + ")") + Constants.WRITERSPACE);
							} else if (where.contains(Constants.BETWEEN)) {
								sql.append(where.replace(Constants.DOIS_PONTOS + matcher.group(1).trim(), Constants.DOIS_PONTOS + matcher.group(1).trim() + Constants.BETWEEN_START) 
										+ Constants.AND 
										+ Constants.DOIS_PONTOS + matcher.group(1).trim() + Constants.BETWEEN_END
										+ Constants.WRITERSPACE);
							} else {
								sql.append(where + Constants.WRITERSPACE);							
							}
						}
					}
				});
			}
			
			// SET KEY VALUE
			if(filters != null) {
				filters.forEach((key, value) -> 
				mapSqlParameterSource.addValue(key, value.toString().contains("*") ? value.toString().replace("*", "%") : value)
						);
			}
			
			// GROUP BY
			if (query.getGroupby() != null) {
				sql.append(Constants.GROUP_BY + Constants.WRITERSPACE);
				sql.append(query.getGroupby() + Constants.WRITERSPACE);
			}
			
			// CHECK IF DON'T SELECT COUNT
			if (!isCount) {
				// ORDER BY
				if (query.getOrderby() != null) {
					sql.append(Constants.ORDER_BY + Constants.WRITERSPACE);
					sql.append(query.getOrderby()
							.replace(Constants.DOIS_PONTOS + Constants.SORT_LIST,
									filters != null && filters.get(Constants.SORT_LIST) != null ? filters.get(Constants.SORT_LIST).toString().replaceAll("[\\[\\]]", "") : "")
							.replace(Constants.DOIS_PONTOS + Constants.SORT_ORDER, 
									filters != null && filters.get(Constants.SORT_ORDER) != null ? filters.get(Constants.SORT_ORDER).toString() : "")
							+ Constants.WRITERSPACE);
				}
			}
			
			if (isCount && query.getGroupby() != null) {
				sql.append(") AS VALUE" + Constants.WRITERSPACE);
			}
			
		}
		
		return sql.toString();
	}

	private List<Query> loadConfig(String value) {

		try {

			String string = "classpath:/querys/{file}.json";
			String file = alterPlaceholders(string, "file", value);
			Resource resource = resourceLoader.getResource(file);
			InputStream inputStream = resource.getInputStream();
			ObjectMapper objectMapper = new ObjectMapper();

			return objectMapper.readValue(inputStream, new TypeReference<List<Query>>() {});

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ArrayList<>();
	}

	private Query getQuery(String fileQuery, String invokerQuery) {

		try {

			List<Query> querys = loadConfig(fileQuery);

			Query query = null;

			for (Query query_ : querys) {
				if (query_.getName().equalsIgnoreCase(invokerQuery)) {
					query = query_;
					return query;
				}
			}

			return query;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private String alterPlaceholders(String file, String name, String value) {
		ST st = new ST(file, '{', '}');
		st.add(name, value);
		return st.render();
	}
}