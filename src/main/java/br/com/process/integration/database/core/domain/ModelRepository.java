package br.com.process.integration.database.core.domain;

import java.util.List;
import java.util.Map;

import br.com.process.integration.database.core.exception.ServiceException;

public interface ModelRepository<M> {

	public M findBySingle(Map<String, Object> filter, String fileQuery, String invokerQuery) throws ServiceException;

	public List<M> findAll(Map<String, Object> filter, String fileQuery, String invokerQuery) throws ServiceException;

	public List<M> findAll(Map<String, Object> filter, String fileQuery, String invokerQuery, Integer page, Integer size) throws ServiceException;

	public int count(Map<String, Object> filter, String fileQuery, String invokerQuery) throws ServiceException;

}
