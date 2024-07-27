package br.com.process.integration.database.core.application;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.hateoas.PagedModel;

import br.com.process.integration.database.core.exception.ServiceException;

public interface JpaService<ID, E> {

	public Long count(LinkedHashMap<String, Object> filter) throws ServiceException;

	public List<E> findAll(LinkedHashMap<String, Object> filter, ArrayList<String> sortList, String sortOrder) throws ServiceException;

	public PagedModel<E> findAll(LinkedHashMap<String, Object> filter, Integer page, Integer size, ArrayList<String> sortList, String sortOrder) throws ServiceException;
	
	public List<E> findAll(LinkedHashMap<String, Object> filter, String methodQueryJPQL) throws ServiceException;
	
	public PagedModel<E> findAll(LinkedHashMap<String, Object> filter, String methodQueryJPQL, Integer page, Integer size, ArrayList<String> sortList, String sortOrder) throws ServiceException;

	public E findBySingle(LinkedHashMap<String, Object> filter, String methodQueryJPQL) throws ServiceException; 
	
	public Long count(LinkedHashMap<String, Object> filter, String methodQueryJPQL) throws ServiceException;

	public List<E> findAllById(ArrayList<ID> ids) throws ServiceException;

	public E findById() throws ServiceException;

	public boolean existsById() throws ServiceException;

	public void deleteAll() throws ServiceException;

	public void deleteAllById(ArrayList<ID> ids) throws ServiceException;

	public void delete() throws ServiceException;

	public void deleteById() throws ServiceException;

	public void save() throws ServiceException;

	public void saveAndFlush() throws ServiceException;

	public void saveAll(ArrayList<E> models) throws ServiceException;

	public void saveAllAndFlush(ArrayList<E> models) throws ServiceException;
	
}	
