package br.com.process.integration.database.core.domain;

import java.util.List;
import java.util.Map;

import org.springframework.hateoas.PagedModel;

public interface DataMapper<D> {
	
	int executeCount(Map<String, Object> filter, String method);

	D executeSingle(Map<String, Object> filter, String method);
	
	List<D> executeAll(Map<String, Object> filter, String method);

	PagedModel<D> executePaginator(Map<String, Object> filter, String method, Integer page, Integer size);

}