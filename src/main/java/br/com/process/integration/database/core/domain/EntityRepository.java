package br.com.process.integration.database.core.domain;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EntityRepository<E, R> {

	public R getRepository();
	
	public Long count(Map<String, Object> filter);
	
	public List<E> findAll(Map<String, Object> filter, List<String> sortList, String sortOrder);
	
	public Page<E> findAll(Map<String, Object> filter, Pageable pageable);

}
