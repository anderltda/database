package br.com.process.integration.database.core.application;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.domain.Model;
import br.com.process.integration.database.core.exception.ServiceException;
import br.com.process.integration.database.core.infrastructure.AbstractJdbcRepository;

@Service
@Transactional
public abstract class AbstractJdbcService<M extends Model<?>> extends AbstractJdbcRepository<M> implements JdbcService<M> {

	protected Page<M> pages;
	protected PagedModel<M> pagedModel;

	public abstract void setPagedModel();

	@Override
	public M executeQueryNativeFindBySingle(LinkedHashMap<String, Object> filter, String invokerQuery) throws ServiceException {
		
		M model = (M) findBySingle(filter, this.getClass().getSimpleName(), invokerQuery);
		
		return model;
		
	}
	
	@Override
	public List<M> executeQueryNative(LinkedHashMap<String, Object> search, String invokerQuery) throws ServiceException {

		List<M> models = (List<M>) findAll(search, this.getClass().getSimpleName(), invokerQuery);

		return models;

	}

	@Override
	public PagedModel<M> executeQueryNative(LinkedHashMap<String, Object> filter, String invokerQuery, Integer page, Integer size, ArrayList<String> sortList, String sortOrder) throws ServiceException {

		List<M> models = (List<M>) findAll(filter, this.getClass().getSimpleName(), invokerQuery, page, size);
		int totalElements = count(filter, this.getClass().getSimpleName(), invokerQuery);
		Pageable pageable = PageRequest.of(page, size);
		pages = new PageImpl<M>(models, pageable, totalElements);

		setPagedModel();

		return pagedModel;
	}
}
