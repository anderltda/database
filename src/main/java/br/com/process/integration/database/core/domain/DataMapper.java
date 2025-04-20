package br.com.process.integration.database.core.domain;

import java.util.List;
import java.util.Map;

import org.springframework.hateoas.PagedModel;

public interface DataMapper<D> {
	
	int count(Map<String, Object> filter, String method);

	D findBySingle(Map<String, Object> filter, String method);
	
	List<D> findAll(Map<String, Object> filter, String method);

	PagedModel<D> findPaginator(Map<String, Object> filter, String method);

}