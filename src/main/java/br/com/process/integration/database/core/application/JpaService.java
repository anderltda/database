package br.com.process.integration.database.core.application;

import java.util.List;
import java.util.Map;

import org.springframework.hateoas.PagedModel;

import br.com.process.integration.database.core.exception.CheckedException;

public interface JpaService<T, E> {

	public Long count(Map<String, Object> filter) throws CheckedException;

	public List<E> findAll(Map<String, Object> filter, List<String> sortList, List<String> sortOrders) throws CheckedException;

	public PagedModel<E> findAll(Map<String, Object> filter, Integer page, Integer size, List<String> sortList, List<String> sortOrders) throws CheckedException;

	public List<E> findAll(Map<String, Object> filter, String methodQueryJPQL, List<String> sortList, List<String> sortOrders) throws CheckedException;

	public PagedModel<E> findAll(Map<String, Object> filter, String methodQueryJPQL, Integer page, Integer size, List<String> sortList, List<String> sortOrders) throws CheckedException;

	public E findBySingle(Map<String, Object> filter, String methodQueryJPQL) throws CheckedException;

	public E findBySingle(Map<String, Object> filter) throws CheckedException;

	public Long count(Map<String, Object> filter, String methodQueryJPQL) throws CheckedException;

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
