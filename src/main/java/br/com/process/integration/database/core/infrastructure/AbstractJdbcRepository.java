package br.com.process.integration.database.core.infrastructure;

import java.util.List;
import java.util.Map;

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

	@Autowired
	private ConfigQuery configQuery;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	protected M model;

	protected abstract void setModel(M model);

	private RowMapper<M> rowMapper = null;

	@SuppressWarnings("unchecked")
	@Override
	public M findBySingle(Map<String, Object> filters, String fileQuery, String invokerQuery) throws ServiceException {
		
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

		rowMapper = new BeanPropertyRowMapper<>((Class<M>) model.getClass());
		
		String sql = configQuery.executeSQL(filters, fileQuery, invokerQuery, mapSqlParameterSource);
		
		return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, rowMapper);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<M> findAll(Map<String, Object> filter, String fileQuery, String invokerQuery) throws ServiceException {

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

		rowMapper = new BeanPropertyRowMapper<>((Class<M>) model.getClass());
		
		String sql = configQuery.executeSQL(filter, fileQuery, invokerQuery, mapSqlParameterSource);
		
		return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, rowMapper);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<M> findAll(Map<String, Object> filter, String fileQuery, String invokerQuery, Integer page, Integer size) throws ServiceException {

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

		rowMapper = new BeanPropertyRowMapper<>((Class<M>) model.getClass());
		
		StringBuilder sql = new StringBuilder(); 
				
		sql.append(configQuery.executeSQL(filter, fileQuery, invokerQuery, mapSqlParameterSource));
		
		sql.append(Constants.LIMIT);
		sql.append(Constants.WRITERSPACE);
		sql.append(Constants.OFFSET);
		
		mapSqlParameterSource.addValue("size", size);
		mapSqlParameterSource.addValue("offset", page * size);

		return namedParameterJdbcTemplate.query(sql.toString(), mapSqlParameterSource, rowMapper);
	}
	
	@Override
	public int count(Map<String, Object> filter, String fileQuery, String invokerQuery) throws ServiceException {

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

		String sql = configQuery.executeCountSQL(filter, fileQuery, invokerQuery, mapSqlParameterSource);

		return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, Integer.class);
	}
}