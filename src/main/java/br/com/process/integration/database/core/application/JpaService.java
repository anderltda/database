package br.com.process.integration.database.core.application;

import java.util.List;
import java.util.Map;

import org.springframework.hateoas.PagedModel;

import br.com.process.integration.database.core.exception.ServiceException;

public interface JpaService<T, E> {

	public Long count(Map<String, Object> filter) throws ServiceException;

	public List<E> findAll(Map<String, Object> filter, List<String> sortList, String sortOrder) throws ServiceException;

	public PagedModel<E> findAll(Map<String, Object> filter, Integer page, Integer size, List<String> sortList, String sortOrder) throws ServiceException;
	
	public List<E> findAll(Map<String, Object> filter, String methodQueryJPQL) throws ServiceException;
	
	public PagedModel<E> findAll(Map<String, Object> filter, String methodQueryJPQL, Integer page, Integer size, List<String> sortList, String sortOrder) throws ServiceException;

	public E findBySingle(Map<String, Object> filter, String methodQueryJPQL) throws ServiceException; 
	
	public Long count(Map<String, Object> filter, String methodQueryJPQL) throws ServiceException;

	public List<E> findAllById(List<T> ids) throws ServiceException;

	public E findById() throws ServiceException;

	public boolean existsById() throws ServiceException;

	public void deleteAll() throws ServiceException;

	public void deleteAllById(List<T> ids) throws ServiceException;

	public void delete() throws ServiceException;

	public void deleteById() throws ServiceException;

	public void save() throws ServiceException;

	public void saveAndFlush() throws ServiceException;

	public void saveAll(List<E> models) throws ServiceException;

	public void saveAllAndFlush(List<E> models) throws ServiceException;
	
}	
