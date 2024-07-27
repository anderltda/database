package br.com.process.integration.database.core.infrastructure;

import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.process.integration.database.core.domain.ConfigQuery;
import br.com.process.integration.database.core.domain.Model;
import br.com.process.integration.database.core.domain.ModelRepository;
import br.com.process.integration.database.core.exception.ServiceException;
import br.com.process.integration.database.core.util.Constants;

@Repository
public abstract class AbstractJdbcRepository<M extends Model<?>> implements ModelRepository<M> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJdbcRepository.class);

	@Autowired
	private ConfigQuery configQuery;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	protected M model;

	protected abstract void setModel(M model);

	private RowMapper<M> rowMapper = null;

	@SuppressWarnings("unchecked")
	@Override
	public M findBySingle(LinkedHashMap<String, Object> filters, String fileQuery, String invokerQuery) throws ServiceException {
		
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

		rowMapper = new BeanPropertyRowMapper<M>((Class<M>) model.getClass());
		
		String sql = configQuery.executeSQL(filters, fileQuery, invokerQuery, mapSqlParameterSource);
		
		LOGGER.info("FILE: {}.json - QUERY NAME: {} - QUERY: {}", fileQuery, invokerQuery, sql);
		
		M model = (M) namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, rowMapper);
				
		return model;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<M> findAll(LinkedHashMap<String, Object> filter, String fileQuery, String invokerQuery) throws ServiceException {

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

		rowMapper = new BeanPropertyRowMapper<M>((Class<M>) model.getClass());
		
		String sql = configQuery.executeSQL(filter, fileQuery, invokerQuery, mapSqlParameterSource);
		
		LOGGER.info("FILE: {}.json - QUERY NAME: {} - QUERY: {}", fileQuery, invokerQuery, sql);

		List<M> models = namedParameterJdbcTemplate.query(sql.toString(), mapSqlParameterSource, rowMapper);
		
		return models;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<M> findAll(LinkedHashMap<String, Object> filter, String fileQuery, String invokerQuery, Integer page, Integer size) throws ServiceException {

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

		rowMapper = new BeanPropertyRowMapper<M>((Class<M>) model.getClass());
		
		StringBuilder sql = new StringBuilder(); 
				
		sql.append(configQuery.executeSQL(filter, fileQuery, invokerQuery, mapSqlParameterSource));
		
		sql.append(Constants.LIMIT);
		sql.append(Constants.WRITERSPACE);
		sql.append(Constants.OFFSET);
		
		mapSqlParameterSource.addValue("size", size);
		mapSqlParameterSource.addValue("offset", page * size);

		LOGGER.info("FILE: {}.json - QUERY NAME: {} - QUERY: {}", fileQuery, invokerQuery, sql);

		List<M> models = namedParameterJdbcTemplate.query(sql.toString(), mapSqlParameterSource, rowMapper);
		
		return models;
	}
	
	@Override
	public int count(LinkedHashMap<String, Object> filter, String fileQuery, String invokerQuery) throws ServiceException {

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

		String sql = configQuery.executeCountSQL(filter, fileQuery, invokerQuery, mapSqlParameterSource);

		LOGGER.info("FILE: {}.json - QUERY NAME: {} - QUERY: {}", fileQuery, invokerQuery, sql);

		int count = namedParameterJdbcTemplate.queryForObject(sql.toString(), mapSqlParameterSource, Integer.class);
		
		return count;
	}

}