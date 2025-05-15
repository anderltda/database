package br.com.process.integration.database.core.domain;

import java.util.List;
import java.util.Map;

import org.springframework.hateoas.PagedModel;

import br.com.process.integration.database.core.exception.UncheckedException;

/**
 * @param <D>
 */
public interface DataMapper<D> {
	
	/**
	 * @param filter
	 * @param method
	 * @return
	 * @throws UncheckedException
	 */
	Integer count(Map<String, Object> filter, String method) throws UncheckedException;
	
	/**
	 * @param id
	 * @return
	 * @throws UncheckedException
	 */
	public D findById(String id) throws UncheckedException;
	
	/**
	 * @param filter
	 * @return
	 * @throws UncheckedException
	 */
	public D findById(Map<String, Object> filter) throws UncheckedException;

	/**
	 * @param filter
	 * @param method
	 * @return
	 * @throws UncheckedException
	 */
	D findBySingle(Map<String, Object> filter, String method) throws UncheckedException;
	
	/**
	 * @param filter
	 * @param method
	 * @return
	 * @throws UncheckedException
	 */
	List<D> findAll(Map<String, Object> filter, String method) throws UncheckedException;

	/**
	 * @param filter
	 * @param method
	 * @return
	 * @throws UncheckedException
	 */
	PagedModel<D> findPaginator(Map<String, Object> filter, String method) throws UncheckedException;

}