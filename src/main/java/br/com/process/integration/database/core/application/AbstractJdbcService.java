package br.com.process.integration.database.core.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.infrastructure.AbstractJdbcRepository;

@Service
@Transactional
public abstract class AbstractJdbcService<V> extends AbstractJdbcRepository<V> implements JdbcService<V> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJdbcService.class);

	protected V view;
	protected abstract void setView(V view);
	
	protected Page<V> pages;
	protected PagedModel<V> pagedModel;
	public abstract void setPagedModel();
	
	@Override
	public V executeQueryNativeFindBySingle(Map<String, Object> filter, String invokerQuery) {
		try {
			return findBySingle(view, filter, this.getClass().getSimpleName(), invokerQuery);
		} catch (Exception ex) {
			LOGGER.error("[executeQueryNativeFindBySingle]", ex);
		}
		return null;
	}
	
	@Override
	public List<V> executeQueryNative(Map<String, Object> filter, String invokerQuery) {
		try {
			return findAll(view, filter, this.getClass().getSimpleName(), invokerQuery);
		} catch (Exception ex) {
			LOGGER.error("[executeQueryNative]", ex);
		}
		return new ArrayList<>();
	}

	@Override
	public PagedModel<V> executeQueryNative(Map<String, Object> filter, String invokerQuery, Integer page, Integer size, 
			List<String> sortList, String sortOrder) {

		try {
			
			List<V> models = findAll(view, filter, this.getClass().getSimpleName(), invokerQuery, page, size);
			int totalElements = count(filter, this.getClass().getSimpleName(), invokerQuery);
			Pageable pageable = PageRequest.of(page, size);
			pages = new PageImpl<>(models, pageable, totalElements);

			setPagedModel();

			return pagedModel;
			
		} catch (Exception ex) {
			LOGGER.error("[executeQueryNative]", ex);
		}
		
		return null;
	}
}
