package br.com.process.integration.database.core.application;

import java.util.List;
import java.util.Map;

import org.springframework.hateoas.PagedModel;

public interface JdbcService<M> {
	
	public M executeQueryNativeFindBySingle(Map<String, Object> filter, String invokerQuery);

	public List<M> executeQueryNative(Map<String, Object> search, String invokerQuery);

	public PagedModel<M> executeQueryNative(Map<String, Object> search, String invokerQuery, Integer page, Integer size, List<String> sortList, String sortOrder);

}	
