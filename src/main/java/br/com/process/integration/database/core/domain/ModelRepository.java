package br.com.process.integration.database.core.domain;

import java.util.LinkedHashMap;
import java.util.List;

import br.com.process.integration.database.core.exception.ServiceException;

public interface ModelRepository<D extends Model<?>> {

	public D findBySingle(LinkedHashMap<String, Object> filter, String fileQuery, String invokerQuery) throws ServiceException;

	public List<D> findAll(LinkedHashMap<String, Object> filter, String fileQuery, String invokerQuery) throws ServiceException;

	public List<D> findAll(LinkedHashMap<String, Object> filter, String fileQuery, String invokerQuery, Integer page, Integer size) throws ServiceException;

	public int count(LinkedHashMap<String, Object> filter, String fileQuery, String invokerQuery) throws ServiceException;

}
