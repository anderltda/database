package br.com.process.integration.database.core.application;

import java.util.List;
import java.util.Map;

import org.springframework.hateoas.PagedModel;

import br.com.process.integration.database.core.exception.CheckedException;

/**
 * @param <V>
 */
public interface JdbcService<V> {
	
	/**
	 * @param filter
	 * @param query
	 * @return
	 * @throws CheckedException
	 */
	public int count(Map<String, Object> filter, String query) throws CheckedException;
	
	/**
	 * @param filter
	 * @param query
	 * @return
	 * @throws CheckedException
	 */
	public V findBySingle(Map<String, Object> filter, String query) throws CheckedException;

	/**
	 * @param filter
	 * @param query
	 * @return
	 * @throws CheckedException
	 */
	public List<V> findAll(Map<String, Object> filter, String query) throws CheckedException;

	/**
	 * @param filter
	 * @param query
	 * @return
	 * @throws CheckedException
	 */
	public PagedModel<V> findPaginator(Map<String, Object> filter, String query) throws CheckedException;

}	
