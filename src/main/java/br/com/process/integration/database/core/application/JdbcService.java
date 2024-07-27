package br.com.process.integration.database.core.application;

import java.util.List;
import java.util.Map;

import org.springframework.hateoas.PagedModel;

import br.com.process.integration.database.core.exception.ServiceException;

public interface JdbcService<M> {
	
	public M executeQueryNativeFindBySingle(Map<String, Object> filter, String invokerQuery) throws ServiceException;

	public List<M> executeQueryNative(Map<String, Object> search, String invokerQuery) throws ServiceException;

	public PagedModel<M> executeQueryNative(Map<String, Object> search, String invokerQuery, Integer page, Integer size, List<String> sortList, String sortOrder) throws ServiceException;

}	
