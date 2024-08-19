package br.com.process.integration.database.core.domain;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.process.integration.database.core.exception.UncheckedException;

public interface EntityRepository<E> {
	
	public Long countable(Map<String, Object> filter) throws UncheckedException;
	
	public E findSingle(Map<String, Object> filter) throws UncheckedException;
	
	public List<E> findByAll(Map<String, Object> filter, List<String> sortList, List<String> sortOrders) throws UncheckedException;
	
	public Page<E> findByPageAll(Map<String, Object> filter, Pageable pageable) throws UncheckedException;

}
