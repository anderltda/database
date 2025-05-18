package br.com.process.integration.database.core.infrastructure;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.process.integration.database.core.Constants;
import br.com.process.integration.database.core.domain.ViewRepository;
import br.com.process.integration.database.core.domain.query.ConfigQuery;
import br.com.process.integration.database.core.domain.query.DynamicRowMapper;
import br.com.process.integration.database.core.exception.UncheckedException;

/**
 * @param <M>
 */
@Repository
public abstract class AbstractJdbcRepository<M extends RepresentationModel<M>> extends AbstractAssembler<M> implements ViewRepository<M> {

	@Autowired
	private ConfigQuery configQuery;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private RowMapper<M> rowMapper = null;

	/**
	 * @param controllerClass
	 * @param resourceType
	 */
	protected AbstractJdbcRepository(Class<?> controllerClass, Class<M> resourceType) {
		super(controllerClass, resourceType);
	}

	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public M findBySingle(M view, Map<String, Object> filters, String fileQuery, String queryName) throws UncheckedException {

		try {

			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

			rowMapper = new BeanPropertyRowMapper<>((Class<M>) view.getClass());

			String sql = configQuery.executeSQL(view.getClass(), filters, fileQuery, queryName, mapSqlParameterSource);

			return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, rowMapper);

		} catch (EmptyResultDataAccessException ex) {
			return null;
		} catch (BadSqlGrammarException ex) {
			throw new UncheckedException(ex.getCause().getLocalizedMessage(), ex);
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}

	}

	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<M> findAll(M view, Map<String, Object> filter, String fileQuery, String queryName) throws UncheckedException {

		try {

			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

			rowMapper = new BeanPropertyRowMapper<>((Class<M>) view.getClass());
			
			String sql = configQuery.executeSQL(view.getClass(), filter, fileQuery, queryName, mapSqlParameterSource);

			return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource,
					new DynamicRowMapper<>((Class<M>) view.getClass()));

		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
		
	}

	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<M> findAll(M view, Map<String, Object> filter, String fileQuery, String queryName, Integer page, Integer size) throws UncheckedException {

		try {

			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

			rowMapper = new BeanPropertyRowMapper<>((Class<M>) view.getClass());

			StringBuilder sql = new StringBuilder();

			sql.append(configQuery.executeSQL(view.getClass(), filter, fileQuery, queryName, mapSqlParameterSource));

			sql.append(Constants.LIMIT);
			sql.append(Constants.WRITERSPACE);
			sql.append(Constants.OFFSET);

			mapSqlParameterSource.addValue("size", size);
			mapSqlParameterSource.addValue("offset", page * size);

			return namedParameterJdbcTemplate.query(sql.toString(), mapSqlParameterSource, rowMapper);

		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	/**
	 *
	 */
	@Override
	public Integer count(M view, Map<String, Object> filter, String fileQuery, String queryName) throws UncheckedException {

		try {

			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

			String sql = configQuery.executeCountSQL(view.getClass(), filter, fileQuery, queryName, mapSqlParameterSource);

			return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, Integer.class);

		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage());
		}

	}
}