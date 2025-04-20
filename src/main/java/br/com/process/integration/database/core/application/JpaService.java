package br.com.process.integration.database.core.application;

import java.util.List;
import java.util.Map;

import org.springframework.hateoas.PagedModel;

import br.com.process.integration.database.core.exception.CheckedException;

public interface JpaService<E, I> {

	public int count(Map<String, Object> filter) throws CheckedException;

	public int count(Map<String, Object> filter, String methodQueryJPQL) throws CheckedException;

	public List<E> findAllById(List<I> ids);

	public List<E> findAll(Map<String, Object> filter, List<String> sortList, List<String> sortOrders) throws CheckedException;

	public List<E> findAll(Map<String, Object> filter, String method) throws CheckedException;

	public PagedModel<E> findPaginator(Map<String, Object> filter, Integer page, Integer size, List<String> sortList, List<String> sortOrders) throws CheckedException;

	public PagedModel<E> findPaginator(Map<String, Object> filter, String method) throws CheckedException;

	public E findBySingle(Map<String, Object> filter, String methodQueryJPQL) throws CheckedException;

	public E findBySingle(Map<String, Object> filter) throws CheckedException;

	public E findById();

	public boolean existsById();

	public void deleteAll();

	public void deleteAllById(List<I> ids);

	public void deleteById();

	public void save();

	public void saveAndFlush();

	public void saveAll(List<E> models);

	public void saveAllAndFlush(List<E> models);

}
