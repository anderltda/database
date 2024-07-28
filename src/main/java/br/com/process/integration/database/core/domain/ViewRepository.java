package br.com.process.integration.database.core.domain;

import java.util.List;
import java.util.Map;

public interface ViewRepository<V> {

	public V findBySingle(V view, Map<String, Object> filter, String fileQuery, String invokerQuery);

	public List<V> findAll(V view, Map<String, Object> filter, String fileQuery, String invokerQuery);

	public List<V> findAll(V view, Map<String, Object> filter, String fileQuery, String invokerQuery, Integer page, Integer size);

	public int count(Map<String, Object> filter, String fileQuery, String invokerQuery);

}
