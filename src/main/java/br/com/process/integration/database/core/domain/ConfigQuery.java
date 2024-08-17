package br.com.process.integration.database.core.domain;

import java.io.InputStream;
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

import br.com.process.integration.database.core.exception.UncheckedException;
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

	public String executeSQL(Map<String, Object> filters, String fileQuery, String invokerQuery, MapSqlParameterSource mapSqlParameterSource) {
		return executeQuery(filters, fileQuery, invokerQuery, mapSqlParameterSource, false);
	}

	public String executeCountSQL(Map<String, Object> filters, String fileQuery, String invokerQuery, MapSqlParameterSource mapSqlParameterSource) {
		return executeQuery(filters, fileQuery, invokerQuery, mapSqlParameterSource, true);
	}

	private String executeQuery(Map<String, Object> filters, String fileQuery, String invokerQuery, MapSqlParameterSource mapSqlParameterSource, boolean isCount) {

		StringBuilder sql = new StringBuilder();

		Query query = getQuery(fileQuery, invokerQuery);

		createSelect(isCount, sql, query);

		createJoin(sql, query);

		createWhere(filters, sql, query);

		createGroupBy(sql, query);

		createOrderBy(filters, isCount, sql, query);

		if (isCount && query.getGroupby() != null) {
			sql.append(") AS VALUE" + Constants.WRITERSPACE);
		}

		filters.forEach((key, value) -> mapSqlParameterSource.addValue(key,
				value.toString().contains("*") ? value.toString().replace("*", "%") : value));

		return sql.toString().trim() + Constants.WRITERSPACE;
	}
	
	private void createOrderBy(Map<String, Object> filters, boolean isCount, StringBuilder sql, Query query) {
		
		if (!isCount && query.getOrderby() != null) {
			
			boolean isOrderby = (filters.get(Constants.SORT_LIST) != null && filters.get(Constants.SORT_ORDERS) != null);
			
			if (!isOrderby) {
				String checkValid = query.getOrderby().replaceAll("\\s?:\\S+", "").replaceAll("\\s{2,}", " ").trim();
				sql.append(
						!checkValid.isEmpty() ? Constants.ORDER_BY + Constants.WRITERSPACE + query.getOrderby() : "");
			} else {
				sql.append(createSortOrders(filters, query, isOrderby));
			}
		}
	}

	private String createSortOrders(Map<String, Object> filters, Query query, boolean isOrderby) {
		
		StringBuilder orderby = new StringBuilder();
		orderby.append(isOrderby ? Constants.ORDER_BY + Constants.WRITERSPACE : "");

		String sortList = query.getOrderby().replace(Constants.DOIS_PONTOS + Constants.SORT_LIST,
				isOrderby ? toSnakeCase(filters.get(Constants.SORT_LIST).toString().replaceAll("[\\[\\]]", "")) : "");

		String sortOrder = query.getOrderby().replace(Constants.DOIS_PONTOS + Constants.SORT_ORDERS,
				isOrderby ? filters.get(Constants.SORT_ORDERS).toString().replaceAll("[\\[\\]]", "") : "");
		
		String newSortList = sortList.replace(Constants.DOIS_PONTOS + Constants.SORT_ORDERS, "").trim();
		String newSortOrders = sortOrder.replace(Constants.DOIS_PONTOS + Constants.SORT_LIST, "").trim();
		
		String[] sortLists = newSortList.split(",");
		String[] sortOrders = newSortOrders.split(",");
		
		for (int i = 0; i < sortLists.length; i++) {
			orderby.append(sortLists[i]);
			orderby.append(" ");
			orderby.append(sortOrders.length > i ? sortOrders[i].toUpperCase() : sortOrders[i-1].toUpperCase());
			orderby.append(",");
		}
		
		return orderby.substring(0, orderby.length()-1).trim();
	}

	private void createGroupBy(StringBuilder sql, Query query) {
		if (query.getGroupby() != null) {
			sql.append(Constants.GROUP_BY + Constants.WRITERSPACE);
			sql.append(query.getGroupby() + Constants.WRITERSPACE);
		}
	}

	private void createWhere(Map<String, Object> filters, StringBuilder sql, Query query) {
		if (query.getWhere() != null) {
			sql.append(Constants.WHERE + Constants.WRITERSPACE);
			query.getWhere().forEach(where -> {
				where = createWhereOperator(filters, where);
				createWhereValues(filters, sql, where);
			});
		}
	}

	private void createWhereValues(Map<String, Object> filters, StringBuilder sql, String where) {
		Pattern pattern = Pattern.compile(":(?:[^():]*\\([^)]*\\))?([^:()\\s]+)");
		Matcher matcher = pattern.matcher(where);
		if (matcher.find() && filters.get(matcher.group(1).trim()) != null) {
			if (where.contains(Constants.IN)) {
				sql.append(where.replaceAll("[\\(\\)]", "").replace(":" + matcher.group(1).trim(),
						"(" + Constants.DOIS_PONTOS + matcher.group(1).trim() + ")") + Constants.WRITERSPACE);
			} else if (where.contains(Constants.BETWEEN)) {
				sql.append(where.replace(Constants.DOIS_PONTOS + matcher.group(1).trim(),
						Constants.DOIS_PONTOS + matcher.group(1).trim() + Constants.BETWEEN_START) + Constants.AND
						+ Constants.DOIS_PONTOS + matcher.group(1).trim() + Constants.BETWEEN_END
						+ Constants.WRITERSPACE);
			} else {
				sql.append(where + Constants.WRITERSPACE);
			}
		}
	}

	private String createWhereOperator(Map<String, Object> filters, String where) {
		Pattern pattern = Pattern.compile("\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(where);
		String operator = Constants.IGUAL;
		if (matcher.find()) {
			if (filters.get(matcher.group(1).trim()) != null) {
				operator = OPERADORES.get(filters.get(matcher.group(1).trim()).toString());
			}
			where = alterPlaceholders(where, matcher.group(1).trim(), operator);
		}
		return where;
	}

	private void createJoin(StringBuilder sql, Query query) {
		if (query.getJoin() != null) {
			query.getJoin().forEach(join -> sql.append(join + Constants.WRITERSPACE));
		}
	}

	private void createSelect(boolean isCount, StringBuilder sql, Query query) {
		if (isCount) {
			if(query.getGroupby() == null) {
				Pattern pattern = Pattern.compile("\\b(from\\b.*)", Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(query.getSelect());
				if (matcher.find()) {
					sql.append(Constants.SELECT_COUNT + Constants.WRITERSPACE + matcher.group(1).trim() + Constants.WRITERSPACE);
				}
			} else {
				sql.append(Constants.SELECT_COUNT + Constants.WRITERSPACE + Constants.FROM + Constants.WRITERSPACE + "(" + Constants.WRITERSPACE);
				sql.append(query.getSelect() + Constants.WRITERSPACE);
			}
			
		} else {
			sql.append(query.getSelect() + Constants.WRITERSPACE);
		}
	}
	
	private static String toSnakeCase(Object camelCase) {
	    StringBuilder snakeCaseString = new StringBuilder();
	    for (char c : camelCase.toString().toCharArray()) {
	        if (Character.isUpperCase(c)) {
	            snakeCaseString.append('_').append(Character.toLowerCase(c));
	        } else {
	            snakeCaseString.append(c);
	        }
	    }
	    return snakeCaseString.toString();
	}
	
	private String alterPlaceholders(String file, String name, String value) {
		ST st = new ST(file, '{', '}');
		st.add(name, value);
		return st.render();
	}

	private List<Query> loadConfig(String value) throws UncheckedException {
		try {
			String string = "classpath:/querys/{file}.json";
			String file = alterPlaceholders(string, "file", value);
			Resource resource = resourceLoader.getResource(file);
			InputStream inputStream = resource.getInputStream();
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(inputStream, new TypeReference<List<Query>>() {});
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	private Query getQuery(String fileQuery, String invokerQuery) throws UncheckedException {
		try {
			List<Query> querys = loadConfig(fileQuery);
			for (Query query : querys) {
				if (query.getName().equalsIgnoreCase(invokerQuery)) {
					return query;
				}
			}
			
			throw new UncheckedException(String.format("Query not found %s !", invokerQuery));
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}
}