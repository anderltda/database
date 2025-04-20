package br.com.process.integration.database.core.domain;

import java.util.List;
import java.util.Map;

import org.springframework.hateoas.PagedModel;

/**
 * @param <D>
 */
public interface DataMapper<D> {
	
	/**
	 * @param filter
	 * @param method
	 * @return
	 */
	int count(Map<String, Object> filter, String method);

	/**
	 * @param filter
	 * @param method
	 * @return
	 */
	D findBySingle(Map<String, Object> filter, String method);
	
	/**
	 * @param filter
	 * @param method
	 * @return
	 */
	List<D> findAll(Map<String, Object> filter, String method);

	/**
	 * @param filter
	 * @param method
	 * @return
	 */
	PagedModel<D> findPaginator(Map<String, Object> filter, String method);

}