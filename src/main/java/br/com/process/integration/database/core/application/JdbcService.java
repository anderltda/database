package br.com.process.integration.database.core.application;

import java.util.List;
import java.util.Map;

import org.springframework.hateoas.PagedModel;

import br.com.process.integration.database.core.exception.CheckedException;

public interface JdbcService<V> {
	
	public int executeQueryNativeCount(Map<String, Object> filter, String query) throws CheckedException;
	
	public V executeQueryNativeSingle(Map<String, Object> filter, String query) throws CheckedException;

	public List<V> executeQueryNative(Map<String, Object> filter, String query) throws CheckedException;

	public PagedModel<V> executeQueryNativePaginator(Map<String, Object> filter, String query) throws CheckedException;

}	
