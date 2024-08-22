package br.com.process.integration.database.core.domain;

import java.util.List;
import java.util.Map;

public interface DataMapper<D> {
	
	int executeCount(Map<String, Object> filter, String method);

	D executeSingle(Map<String, Object> filter, String method);
	
	List<D> executeAll(Map<String, Object> filter, String method);

}