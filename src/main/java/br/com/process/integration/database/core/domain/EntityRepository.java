package br.com.process.integration.database.core.domain;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.process.integration.database.core.exception.UncheckedException;

/**
 * @param <E>
 */
public interface EntityRepository<E> {
	
	/**
	 * @param filter
	 * @return
	 * @throws UncheckedException
	 */
	public Long countable(Map<String, Object> filter) throws UncheckedException;
	
	/**
	 * @param filter
	 * @return
	 * @throws UncheckedException
	 */
	public E findSingle(Map<String, Object> filter) throws UncheckedException;
	
	/**
	 * @param filter
	 * @param sortList
	 * @param sortOrders
	 * @return
	 * @throws UncheckedException
	 */
	public List<E> findByAll(Map<String, Object> filter, List<String> sortList, List<String> sortOrders) throws UncheckedException;
	
	/**
	 * @param filter
	 * @param pageable
	 * @return
	 * @throws UncheckedException
	 */
	public Page<E> findByPageAll(Map<String, Object> filter, Pageable pageable) throws UncheckedException;

}
