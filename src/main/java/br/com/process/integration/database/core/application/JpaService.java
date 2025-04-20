package br.com.process.integration.database.core.application;

import java.util.List;
import java.util.Map;

import org.springframework.hateoas.PagedModel;

import br.com.process.integration.database.core.exception.CheckedException;

/**
 * @param <E>
 * @param <I>
 */
public interface JpaService<E, I> {

	/**
	 * @param filter
	 * @return
	 * @throws CheckedException
	 */
	public int count(Map<String, Object> filter) throws CheckedException;

	/**
	 * @param filter
	 * @param methodQueryJPQL
	 * @return
	 * @throws CheckedException
	 */
	public int count(Map<String, Object> filter, String methodQueryJPQL) throws CheckedException;

	/**
	 * @param ids
	 * @return
	 */
	public List<E> findAllById(List<I> ids);

	/**
	 * @param filter
	 * @param sortList
	 * @param sortOrders
	 * @return
	 * @throws CheckedException
	 */
	public List<E> findAll(Map<String, Object> filter, List<String> sortList, List<String> sortOrders) throws CheckedException;

	/**
	 * @param filter
	 * @param method
	 * @return
	 * @throws CheckedException
	 */
	public List<E> findAll(Map<String, Object> filter, String method) throws CheckedException;

	/**
	 * @param filter
	 * @param page
	 * @param size
	 * @param sortList
	 * @param sortOrders
	 * @return
	 * @throws CheckedException
	 */
	public PagedModel<E> findPaginator(Map<String, Object> filter, Integer page, Integer size, List<String> sortList, List<String> sortOrders) throws CheckedException;

	/**
	 * @param filter
	 * @param method
	 * @return
	 * @throws CheckedException
	 */
	public PagedModel<E> findPaginator(Map<String, Object> filter, String method) throws CheckedException;

	/**
	 * @param filter
	 * @param methodQueryJPQL
	 * @return
	 * @throws CheckedException
	 */
	public E findBySingle(Map<String, Object> filter, String methodQueryJPQL) throws CheckedException;

	/**
	 * @param filter
	 * @return
	 * @throws CheckedException
	 */
	public E findBySingle(Map<String, Object> filter) throws CheckedException;

	/**
	 * @return
	 */
	public E findById();

	/**
	 * @return
	 */
	public boolean existsById();

	/**
	 * 
	 */
	public void deleteAll();

	/**
	 * @param ids
	 */
	public void deleteAllById(List<I> ids);

	/**
	 * 
	 */
	public void deleteById();

	/**
	 * 
	 */
	public void save();

	/**
	 * 
	 */
	public void saveAndFlush();

	/**
	 * @param models
	 */
	public void saveAll(List<E> models);

	/**
	 * @param models
	 */
	public void saveAllAndFlush(List<E> models);

}
