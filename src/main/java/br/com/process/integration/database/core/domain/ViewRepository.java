package br.com.process.integration.database.core.domain;

import java.util.List;
import java.util.Map;

import br.com.process.integration.database.core.exception.UncheckedException;

/**
 * @param <V>
 */
public interface ViewRepository<V> {

	/**
	 * @param view
	 * @param filter
	 * @param fileQuery
	 * @param queryName
	 * @return
	 * @throws UncheckedException
	 */
	public Integer count(V view, Map<String, Object> filter, String fileQuery, String queryName) throws UncheckedException;

	/**
	 * @param view
	 * @param filter
	 * @param fileQuery
	 * @param queryName
	 * @return
	 * @throws UncheckedException
	 */
	public V findBySingle(V view, Map<String, Object> filter, String fileQuery, String queryName) throws UncheckedException;

	/**
	 * @param view
	 * @param filter
	 * @param fileQuery
	 * @param queryName
	 * @return
	 * @throws UncheckedException
	 */
	public List<V> findAll(V view, Map<String, Object> filter, String fileQuery, String queryName) throws UncheckedException;

	/**
	 * @param view
	 * @param filter
	 * @param fileQuery
	 * @param queryName
	 * @param page
	 * @param size
	 * @return
	 * @throws UncheckedException
	 */
	public List<V> findAll(V view, Map<String, Object> filter, String fileQuery, String queryName, Integer page, Integer size) throws UncheckedException;

}
