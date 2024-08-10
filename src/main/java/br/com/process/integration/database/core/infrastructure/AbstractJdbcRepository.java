package br.com.process.integration.database.core.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.process.integration.database.core.domain.ConfigQuery;
import br.com.process.integration.database.core.domain.DynamicRowMapper;
import br.com.process.integration.database.core.domain.ViewRepository;
import br.com.process.integration.database.core.util.Constants;

@Repository
public abstract class AbstractJdbcRepository<V> implements ViewRepository<V> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJdbcRepository.class);

	@Autowired
	private ConfigQuery configQuery;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private RowMapper<V> rowMapper = null;

	@SuppressWarnings("unchecked")
	@Override
	public V findBySingle(V view, Map<String, Object> filters, String fileQuery, String invokerQuery) {
		
		try {

			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

			rowMapper = new BeanPropertyRowMapper<>((Class<V>) view.getClass());

			String sql = configQuery.executeSQL(filters, fileQuery, invokerQuery, mapSqlParameterSource);

			return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, rowMapper);

		} catch (Exception ex) {
			LOGGER.error("[findBySingle]", ex);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<V> findAll(V view, Map<String, Object> filter, String fileQuery, String invokerQuery) {
		
		try {

			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

			rowMapper = new BeanPropertyRowMapper<>((Class<V>) view.getClass());

			String sql = configQuery.executeSQL(filter, fileQuery, invokerQuery, mapSqlParameterSource);

	        return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, new DynamicRowMapper<>((Class<V>) view.getClass()));

		} catch (Exception ex) {
			LOGGER.error("[findAll]", ex);
		}

		return new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<V> findAll(V view, Map<String, Object> filter, String fileQuery, String invokerQuery, Integer page, Integer size) {

		try {
			
			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

			rowMapper = new BeanPropertyRowMapper<>((Class<V>) view.getClass());
			
			StringBuilder sql = new StringBuilder(); 
					
			sql.append(configQuery.executeSQL(filter, fileQuery, invokerQuery, mapSqlParameterSource));
			
			sql.append(Constants.LIMIT);
			sql.append(Constants.WRITERSPACE);
			sql.append(Constants.OFFSET);
			
			mapSqlParameterSource.addValue("size", size);
			mapSqlParameterSource.addValue("offset", page * size);

			return namedParameterJdbcTemplate.query(sql.toString(), mapSqlParameterSource, rowMapper);
			
		} catch (Exception ex) {
			LOGGER.error("[findAll]", ex);
		}
		
		return new ArrayList<>();
	}
	
	@Override
	public int count(Map<String, Object> filter, String fileQuery, String invokerQuery) {

		try {

			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

			String sql = configQuery.executeCountSQL(filter, fileQuery, invokerQuery, mapSqlParameterSource);

			return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, Integer.class);

		} catch (Exception ex) {
			LOGGER.error("[count]", ex);
		}

		return 0;
	}
}