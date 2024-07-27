package br.com.process.integration.database.core.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.process.integration.database.core.exception.ServiceException;

public interface EntityRepository<E, R> {

	public R getRepository();
	
	public Long count(LinkedHashMap<String, Object> filter) throws ServiceException;
	
	public List<E> findAll(LinkedHashMap<String, Object> filter, ArrayList<String> sortList, String sortOrder) throws ServiceException;
	
	public Page<E> findAll(LinkedHashMap<String, Object> filter, Pageable pageable) throws ServiceException;

}
