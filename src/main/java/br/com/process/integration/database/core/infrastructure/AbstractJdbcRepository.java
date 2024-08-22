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

import br.com.process.integration.database.core.domain.ViewRepository;
import br.com.process.integration.database.core.domain.query.ConfigQuery;
import br.com.process.integration.database.core.domain.query.DynamicRowMapper;
import br.com.process.integration.database.core.exception.UncheckedException;
import br.com.process.integration.database.core.util.Constants;

@Repository
public abstract class AbstractJdbcRepository<M extends RepresentationModel<M>> extends AbstractAssembler<M> implements ViewRepository<M> {

    protected AbstractJdbcRepository(Class<?> controllerClass, Class<M> resourceType) {
        super(controllerClass, resourceType);
    }

	@Autowired
	private ConfigQuery configQuery;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private RowMapper<M> rowMapper = null;

	@SuppressWarnings("unchecked")
	@Override
	public M findSingle(M view, Map<String, Object> filters, String fileQuery, String query) throws UncheckedException {

		try {

			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

			rowMapper = new BeanPropertyRowMapper<>((Class<M>) view.getClass());

			String sql = configQuery.executeSQL(filters, fileQuery, query, mapSqlParameterSource);

			return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, rowMapper);

		} catch (EmptyResultDataAccessException ex) {
			return null;
		} catch (BadSqlGrammarException ex) {
			throw new UncheckedException(ex.getCause().getLocalizedMessage(), ex);
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<M> findAll(M view, Map<String, Object> filter, String fileQuery, String query) throws UncheckedException {

		try {

			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

			rowMapper = new BeanPropertyRowMapper<>((Class<M>) view.getClass());

			String sql = configQuery.executeSQL(filter, fileQuery, query, mapSqlParameterSource);

			return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource,
					new DynamicRowMapper<>((Class<M>) view.getClass()));

		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<M> findAll(M view, Map<String, Object> filter, String fileQuery, String query, Integer page, Integer size) throws UncheckedException {

		try {

			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

			rowMapper = new BeanPropertyRowMapper<>((Class<M>) view.getClass());

			StringBuilder sql = new StringBuilder();

			sql.append(configQuery.executeSQL(filter, fileQuery, query, mapSqlParameterSource));

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

	@Override
	public int count(Map<String, Object> filter, String fileQuery, String query) throws UncheckedException {

		try {

			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

			String sql = configQuery.executeCountSQL(filter, fileQuery, query, mapSqlParameterSource);

			return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, Integer.class);

		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage());
		}

	}
}