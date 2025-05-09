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
import br.com.process.integration.database.core.util.Constants;

/**
 * @param <V>
 */
@Service
@Transactional
public abstract class AbstractJdbcService<V extends RepresentationModel<V>> extends AbstractJdbcRepository<V> implements JdbcService<V> {

	protected V view;
	protected Page<V> pages;
	protected PagedModel<V> pagedModel;

	protected abstract void setView(V view);
	protected abstract void setPagedModel();
	
	/**
	 * @param controllerClass
	 * @param resourceType
	 */
	protected AbstractJdbcService(Class<?> controllerClass, Class<V> resourceType) {
		super(controllerClass, resourceType);
	}

	/**
	 *
	 */
	@Override
	public Integer count(Map<String, Object> filter, String query) throws CheckedException {
		try {
			return count(view, filter, getPackageFileQuery(this.getClass()), query);
		} catch (UncheckedException cuex) {
			throw new CheckedException(cuex.getMessage(), String.format("Falha ao executar metodo 'count' na classe %s com filters: %s e query-name: %s", this.getClass().getSimpleName(), filter, query), cuex);
		}
	}

	/**
	 *
	 */
	@Override
	public V findBySingle(Map<String, Object> filter, String query) throws CheckedException {
		try {
			return findBySingle(view, filter, getPackageFileQuery(this.getClass()), query);
		} catch (UncheckedException cuex) {
			throw new CheckedException(cuex.getMessage(), String.format("Falha ao executar metodo 'executeQueryNativeSingle' na classe %s com filters: %s e query-name: %s", this.getClass().getSimpleName(), filter, query), cuex);
		}
	}

	/**
	 *
	 */
	@Override
	public List<V> findAll(Map<String, Object> filter, String query) throws CheckedException {
		try {
			return findAll(view, filter, getPackageFileQuery(this.getClass()), query);
		} catch (UncheckedException cuex) {
			throw new CheckedException(cuex.getMessage(), String.format("Falha ao executar metodo 'executeQueryNative' na classe %s com filters: %s e query-name: %s", this.getClass().getSimpleName(), filter, query), cuex);
		}
	}

	/**
	 *
	 */
	@Override
	public PagedModel<V> findPaginator(Map<String, Object> filter, String query) throws CheckedException {

		try {

			int page = filter.get(Constants.NAME_PAGE) != null ? Integer.parseInt((String) filter.get(Constants.NAME_PAGE)) : Constants.NUMBER_PAGE_DEFAULT;
			int size = filter.get(Constants.NAME_SIZE) != null ? Integer.parseInt((String) filter.get(Constants.NAME_SIZE)) : Constants.NUMBER_SIZE_DEFAULT;

			List<V> models = findAll(view, filter, getPackageFileQuery(this.getClass()), query, page, size);
			int totalElements = count(view, filter, getPackageFileQuery(this.getClass()), query);
			Pageable pageable = PageRequest.of(page, size);
			pages = new PageImpl<>(models, pageable, totalElements);

			setPagedModel();

			return pagedModel;

		} catch (UncheckedException cuex) {
			throw new CheckedException(cuex.getMessage(), String.format("Falha ao executar metodo 'executeQueryNativePaginator' na classe %s com filters: %s e query-name: %s", this.getClass().getSimpleName(), filter, query), cuex);
		}
	}
	
	/**
	 * @param clazz
	 * @return
	 */
	private String getPackageFileQuery(Class<?> clazz) {
	    String packageName = clazz.getPackageName();
	    int lastDot = packageName.lastIndexOf('.');
	    String packageLast = (lastDot == -1) ? packageName : packageName.substring(lastDot + 1);
	    return packageLast + "/" + clazz.getSimpleName();
	}
}
