package br.com.process.integration.database.core.application;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.domain.DataMapper;
import br.com.process.integration.database.core.exception.CheckedException;
import br.com.process.integration.database.core.exception.UncheckedException;
import br.com.process.integration.database.core.infrastructure.AbstractAssembler;
import br.com.process.integration.database.core.reflection.MethodInvoker;
import br.com.process.integration.database.core.reflection.MethodReflection;
import br.com.process.integration.database.core.util.Constants;

@Service
@Transactional
public abstract class AbstractDataService<D extends RepresentationModel<D>> extends AbstractAssembler<D> implements DataMapper<D> {

	protected D data;
	protected DataMapper<D> mapper;
	protected Page<D> pages;
	protected PagedModel<D> pagedModel;

	protected abstract void setData(D data);
	protected abstract void setPagedModel();

	@Autowired
	protected MethodInvoker methodInvoker;

	protected AbstractDataService(Class<?> controllerClass, Class<D> resourceType) {
		super(controllerClass, resourceType);
	}

	@Override
	public int executeCount(Map<String, Object> filter, String method) {
		try {
			Object[] args = MethodReflection.getMethodArgs(mapper.getClass(), method, filter);
			return (int) methodInvoker.invokeMethodReturnObjectWithParameters(
					MethodReflection.getNameMapper(data.getClass().getSimpleName()), method, args);
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public D executeSingle(Map<String, Object> filter, String method) {
		try {
			Object[] args = MethodReflection.getMethodArgs(mapper.getClass(), method, filter);
			return (D) methodInvoker.invokeMethodReturnObjectWithParameters(
					MethodReflection.getNameMapper(data.getClass().getSimpleName()), method, args);
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<D> executeAll(Map<String, Object> filter, String method) {
		try {
			
			Object[] args = MethodReflection.getMethodArgs(mapper.getClass(), method, filter);
			
			if(filter.size() != args.length) {
				throw new UncheckedException(String.format("Erro: Numero de parametros incorretos para o method: '%s'", method));
			}
			
			return (List<D>) methodInvoker.invokeMethodReturnObjectWithParameters(
					MethodReflection.getNameMapper(data.getClass().getSimpleName()), method, args);
			
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PagedModel executePaginator(Map<String, Object> filter, String method) {
		try {
			
			int page = Integer.parseInt(filter.get(Constants.NAME_PAGE).toString());
			int size = Integer.parseInt(filter.get(Constants.NAME_SIZE).toString());

			filter.put(Constants.NAME_PAGE, page * size);
			
			Pageable pageable = PageRequest.of(page, size);
			
	        List<D> entities = executeAll(filter, method);
	        
			filter.remove(Constants.NAME_PAGE);
			filter.remove(Constants.NAME_SIZE);
			filter.remove(Constants.SORT_LIST);
			filter.remove(Constants.SORT_ORDERS);
	        
	        int count = executeCount(filter, getCountMethod(method));
	        
	        pages = new PageImpl<>(entities, pageable, count);
	        
			setPagedModel();
			
			return pagedModel;
	        
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
	
	private String getCountMethod(String method) {
		return Constants.METHOD_COUNT + method.substring(0, 1).toUpperCase() + method.substring(1);
	}
}