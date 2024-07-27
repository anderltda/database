package br.com.process.integration.database.core.domain;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.process.integration.database.core.exception.ServiceException;

public interface EntityRepository<E, R> {

	public R getRepository();
	
	public Long count(Map<String, Object> filter) throws ServiceException;
	
	public List<E> findAll(Map<String, Object> filter, List<String> sortList, String sortOrder) throws ServiceException;
	
	public Page<E> findAll(Map<String, Object> filter, Pageable pageable) throws ServiceException;

}
