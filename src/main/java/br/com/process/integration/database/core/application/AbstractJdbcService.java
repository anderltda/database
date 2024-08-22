package br.com.process.integration.database.core.application;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.exception.CheckedException;
import br.com.process.integration.database.core.exception.UncheckedException;
import br.com.process.integration.database.core.infrastructure.AbstractJdbcRepository;

@Service
@Transactional
public abstract class AbstractJdbcService<V extends RepresentationModel<V>> extends AbstractJdbcRepository<V> implements JdbcService<V> {
   
	protected AbstractJdbcService(Class<?> controllerClass, Class<V> resourceType) {
        super(controllerClass, resourceType);
    }

	protected V view;

	protected abstract void setView(V view);

	protected Page<V> pages;
	protected PagedModel<V> pagedModel;

	public abstract void setPagedModel();

	@Override
	public int executeQueryNativeCount(Map<String, Object> filter, String query) throws CheckedException {
		try {
			return count(filter, this.getClass().getSimpleName(), query);
		} catch (UncheckedException cuex) {
			throw new CheckedException(cuex.getMessage(),
					String.format("Falha ao executar metodo 'count' na classe %s com filters: %s e query-name: %s",
							this.getClass().getSimpleName(), filter, query), cuex);
		}
	}

	@Override
	public V executeQueryNativeSingle(Map<String, Object> filter, String query) throws CheckedException {
		try {
			return findSingle(view, filter, this.getClass().getSimpleName(), query);
		} catch (UncheckedException cuex) {
			throw new CheckedException(cuex.getMessage(), String.format(
					"Falha ao executar metodo 'executeQueryNativeSingle' na classe %s com filters: %s e query-name: %s",
					this.getClass().getSimpleName(), filter, query), cuex);
		}
	}

	@Override
	public List<V> executeQueryNative(Map<String, Object> filter, String query) throws CheckedException {
		try {
			return findAll(view, filter, this.getClass().getSimpleName(), query);
		} catch (UncheckedException cuex) {
			throw new CheckedException(cuex.getMessage(), String.format(
					"Falha ao executar metodo 'executeQueryNative' na classe %s com filters: %s e query-name: %s",
					this.getClass().getSimpleName(), filter, query), cuex);
		}
	}

	@Override
	public PagedModel<V> executeQueryNativePaginator(Map<String, Object> filter, String query) throws CheckedException {

		try {

			int page = filter.get("page") != null ? Integer.parseInt((String) filter.get("page")) : 0;
			int size = filter.get("page") != null ? Integer.parseInt((String) filter.get("size")) : 5;

			List<V> models = findAll(view, filter, this.getClass().getSimpleName(), query, page, size);
			int totalElements = count(filter, this.getClass().getSimpleName(), query);
			Pageable pageable = PageRequest.of(page, size);
			pages = new PageImpl<>(models, pageable, totalElements);

			setPagedModel();

			return pagedModel;

		} catch (UncheckedException cuex) {
			throw new CheckedException(cuex.getMessage(), String.format(
					"Falha ao executar metodo 'executeQueryNativePaginator' na classe %s com filters: %s e query-name: %s",
					this.getClass().getSimpleName(), filter, query), cuex);
		}
	}
}
