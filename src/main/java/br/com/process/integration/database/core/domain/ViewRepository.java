package br.com.process.integration.database.core.domain;

import java.util.List;
import java.util.Map;

import br.com.process.integration.database.core.exception.UncheckedException;

public interface ViewRepository<M> {

	public Integer count(Map<String, Object> filter, String fileQuery, String query) throws UncheckedException;

	public M findSingle(M view, Map<String, Object> filter, String fileQuery, String query) throws UncheckedException;

	public List<M> findAll(M view, Map<String, Object> filter, String fileQuery, String query) throws UncheckedException;

	public List<M> findAll(M view, Map<String, Object> filter, String fileQuery, String query, Integer page, Integer size) throws UncheckedException;

}
