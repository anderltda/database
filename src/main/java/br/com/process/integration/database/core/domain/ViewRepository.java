package br.com.process.integration.database.core.domain;

import java.util.List;
import java.util.Map;

import br.com.process.integration.database.core.exception.UncheckedException;

public interface ViewRepository<V> {

	public int count(V view, Map<String, Object> filter, String fileQuery, String query) throws UncheckedException;

	public V findBySingle(V view, Map<String, Object> filter, String fileQuery, String query) throws UncheckedException;

	public List<V> findAll(V view, Map<String, Object> filter, String fileQuery, String query) throws UncheckedException;

	public List<V> findAll(V view, Map<String, Object> filter, String fileQuery, String query, Integer page, Integer size) throws UncheckedException;

}
