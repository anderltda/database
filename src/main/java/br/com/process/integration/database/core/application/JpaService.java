package br.com.process.integration.database.core.application;

import java.util.List;
import java.util.Map;

import org.springframework.hateoas.PagedModel;

public interface JpaService<T, E> {

	public Long count(Map<String, Object> filter);

	public List<E> findAll(Map<String, Object> filter, List<String> sortList, List<String> sortOrders);

	public PagedModel<E> findAll(Map<String, Object> filter, Integer page, Integer size, List<String> sortList, String sortOrder);
	
	public List<E> findAll(Map<String, Object> filter, String methodQueryJPQL);
	
	public PagedModel<E> findAll(Map<String, Object> filter, String methodQueryJPQL, Integer page, Integer size, List<String> sortList, String sortOrder);

	public E findBySingle(Map<String, Object> filter, String methodQueryJPQL); 
	
	public Long count(Map<String, Object> filter, String methodQueryJPQL);

	public List<E> findAllById(List<T> ids);

	public E findById();

	public boolean existsById();

	public void deleteAll();

	public void deleteAllById(List<T> ids);

	public void deleteById();

	public void save();

	public void saveAndFlush();

	public void saveAll(List<E> models);

	public void saveAllAndFlush(List<E> models);
	
}	
