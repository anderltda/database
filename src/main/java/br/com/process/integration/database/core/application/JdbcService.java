package br.com.process.integration.database.core.application;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.hateoas.PagedModel;

import br.com.process.integration.database.core.exception.ServiceException;

public interface JdbcService<M> {
	
	public M executeQueryNativeFindBySingle(LinkedHashMap<String, Object> filter, String invokerQuery) throws ServiceException;

	public List<M> executeQueryNative(LinkedHashMap<String, Object> search, String invokerQuery) throws ServiceException;

	public PagedModel<M> executeQueryNative(LinkedHashMap<String, Object> search, String invokerQuery, Integer page, Integer size, ArrayList<String> sortList, String sortOrder) throws ServiceException;

}	
