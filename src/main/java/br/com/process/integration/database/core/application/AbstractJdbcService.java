package br.com.process.integration.database.core.application;

import java.util.List;
import java.util.Map;

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
	public M executeQueryNativeFindBySingle(Map<String, Object> filter, String invokerQuery) throws ServiceException {
		return findBySingle(filter, this.getClass().getSimpleName(), invokerQuery);
	}
	
	@Override
	public List<M> executeQueryNative(Map<String, Object> search, String invokerQuery) throws ServiceException {
		return findAll(search, this.getClass().getSimpleName(), invokerQuery);
	}

	@Override
	public PagedModel<M> executeQueryNative(Map<String, Object> filter, String invokerQuery, Integer page, Integer size, List<String> sortList, String sortOrder) throws ServiceException {

		List<M> models = findAll(filter, this.getClass().getSimpleName(), invokerQuery, page, size);
		int totalElements = count(filter, this.getClass().getSimpleName(), invokerQuery);
		Pageable pageable = PageRequest.of(page, size);
		pages = new PageImpl<>(models, pageable, totalElements);

		setPagedModel();

		return pagedModel;
	}
}
