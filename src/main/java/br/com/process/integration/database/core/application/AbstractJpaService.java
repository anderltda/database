package br.com.process.integration.database.core.application;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.Constants;
import br.com.process.integration.database.core.domain.BeanEntity;
import br.com.process.integration.database.core.exception.CheckedException;
import br.com.process.integration.database.core.exception.UncheckedException;
import br.com.process.integration.database.core.infrastructure.AbstractJpaRepository;
import br.com.process.integration.database.core.reflection.MethodInvoker;
import br.com.process.integration.database.core.reflection.MethodReflection;
import br.com.process.integration.database.core.utils.StringsUtils;

@Service
@Transactional
public abstract class AbstractJpaService<E extends BeanEntity<?>, M extends RepresentationModel<M>, I> extends AbstractJpaRepository<E, I, M, JpaRepository<E, I>> implements JpaService<E, I> {

	protected I id;
	protected Page<E> pages;
	protected PagedModel<E> pagedModel;

	protected abstract void setId(I id);
	protected abstract void setEntity(E entity);
	protected abstract void setPagedModel();

	@Autowired
	protected MethodInvoker methodInvoker;

	protected AbstractJpaService(Class<?> controllerClass, Class<M> resourceType) {
		super(controllerClass, resourceType);
	}
	
	 /*****************************************************************************************************************************************************************
	 *																																								  *
	 * SERVICES UTILIZANDO CRITERIA																														        	  *	
	 * 																																								  *
	 *****************************************************************************************************************************************************************/	
	
	@Override
	public int count(Map<String, Object> filter) throws CheckedException {
		try {
			return super.countable(filter).intValue();
		} catch (UncheckedException cuex) {
			throw new CheckedException(cuex.getCause().getLocalizedMessage(), cuex);
		}
	}
	
	@Override
	public E findBySingle(Map<String, Object> filter) throws CheckedException {
		try {
			return super.findSingle(filter);
		} catch (UncheckedException cuex) {
			throw new CheckedException(cuex.getCause().getLocalizedMessage(), cuex);
		}
	}

	@Override
	public List<E> findAll(Map<String, Object> filter, List<String> sortList, List<String> sortOrders) throws CheckedException {
		try {
			return super.findByAll(filter, sortList, sortOrders);
		} catch (UncheckedException cuex) {
			throw new CheckedException(cuex.getCause().getLocalizedMessage(), cuex);
		}
	}
	
	@Override
	public PagedModel<E> findPaginator(Map<String, Object> filter, Integer page, Integer size, List<String> sortList, List<String> sortOrders) throws CheckedException {
		try {
			Pageable pageable = PageRequest.of(page, size, createSortOrder(sortList, sortOrders));
			pages = super.findByPageAll(filter, pageable);
			setPagedModel();
			return pagedModel;
		} catch (UncheckedException cuex) {
			throw new CheckedException(cuex.getCause().getLocalizedMessage(), cuex);
		}
	}
	
	 /*****************************************************************************************************************************************************************
	 *																																								  *
	 * SERVICES UTILIZANDO JPQL																															        	  *	
	 *  																																							  *
	 *****************************************************************************************************************************************************************/
	
	@Override
	public int count(Map<String, Object> filter, String methodQueryJPQL) throws CheckedException {
		
		try {
			Object[] methodArgs = MethodReflection.getMethodArgs(repository.getClass(), methodQueryJPQL, filter);
			return (int) methodInvoker.invokeMethodReturnObjectWithParameters(StringsUtils.getNameRepository(entity.getClass().getSimpleName()), methodQueryJPQL, methodArgs);
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public E findBySingle(Map<String, Object> filter, String methodQueryJPQL) throws CheckedException {
		try {
			Object[] methodArgs = MethodReflection.getMethodArgs(repository.getClass(), methodQueryJPQL, filter);
			return (E) methodInvoker.invokeMethodReturnObjectWithParameters(StringsUtils.getNameRepository(entity.getClass().getSimpleName()), methodQueryJPQL, methodArgs);
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<E> findAll(Map<String, Object> filter, String method) throws CheckedException {
		
		try {
			
			createOrderBy(filter);
			
			Object[] args = MethodReflection.getMethodArgs(repository.getClass(), method, filter);
			
			if(filter.size() != args.length) {
				throw new UncheckedException(String.format("Erro: Number or Type de parametros incorretos  para o method: '%s'", method));
			}
			
			return (List<E>) methodInvoker.invokeMethodReturnObjectWithParameters(StringsUtils.getNameRepository(entity.getClass().getSimpleName()), method, args);
		
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PagedModel<E> findPaginator(Map<String, Object> filter, String method) throws CheckedException {
	
		try {
			
			createPaginator(filter);
			
			Object[] args = MethodReflection.getMethodArgs(repository.getClass(), method, filter);
			
			if(filter.size() != args.length) {
				throw new UncheckedException(String.format("Erro: Number or Type de parametros incorretos  para o method: '%s'", method));
			}
			
			pages = (Page<E>) methodInvoker.invokeMethodReturnObjectWithParameters(StringsUtils.getNameRepository(entity.getClass().getSimpleName()), method, args);
			
			setPagedModel();
			
			return pagedModel;
			
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
	
	 /*****************************************************************************************************************************************************************
	 *																																								  *
	 * SERVICES UTILIZANDO GENERIC CRUD REPOSITORY																													  *	
	 *  																																							  *
	 *****************************************************************************************************************************************************************/
	
	@Override
	public E findById() {
		Optional<E> entity = repository.findById(id);
		if (entity.isPresent()) {
			return entity.get();
		}
		return null;
	}
	
	@Override
	public List<E> findAllById(List<I> ids) {
		return repository.findAllById(ids);
	}

	@Override
	public boolean existsById() {
		return repository.existsById(id);
	}
	
	@Override
	public void deleteAll() {
		repository.deleteAll();
	}

	@Override
	public void deleteAllById(List<I> ids) {
		repository.deleteAllById(ids);
	}

	@Override
	public void deleteById() {
		repository.deleteById(id);
	}

	@Override
	public void save() {
		repository.save(entity);
	}

	@Override
	public void saveAndFlush() {
		repository.saveAndFlush(entity);
	}

	@Override
	public void saveAll(List<E> entitys) {
		repository.saveAll(entitys);
	}

	@Override
	public void saveAllAndFlush(List<E> entitys) {
		repository.saveAllAndFlush(entitys);
	}
	
	@SuppressWarnings("unchecked")
	private void createOrderBy(Map<String, Object> filter) {
		
		List<String> sortList = (List<String>) filter.get(Constants.SORT_LIST);
		List<String> sortOrders = (List<String>) filter.get(Constants.SORT_ORDERS);
		
		if(sortList != null && sortOrders != null) {
			Sort sort = createSortOrder(sortList, sortOrders);
			filter.put(Constants.SORT, sort);
		}

		filter.remove(Constants.SORT_LIST);
		filter.remove(Constants.SORT_ORDERS);
	}
	
	@SuppressWarnings("unchecked")
	private void createPaginator(Map<String, Object> filter) {
		
		int page = filter.get(Constants.NAME_PAGE) != null ? Integer.parseInt(filter.get(Constants.NAME_PAGE).toString())
				: Constants.NUMBER_PAGE_DEFAULT;
		int size = filter.get(Constants.NAME_SIZE) != null ? Integer.parseInt(filter.get(Constants.NAME_SIZE).toString())
				: Constants.NUMBER_SIZE_DEFAULT;
		
		List<String> sortList = (List<String>) filter.get(Constants.SORT_LIST);
		List<String> sortOrders = (List<String>) filter.get(Constants.SORT_ORDERS);
		
		Pageable pageable = PageRequest.of(page, size);

		if(sortList != null && sortOrders != null) {
			Sort sort = createSortOrder(sortList, sortOrders);
			pageable = PageRequest.of(page, size, sort);
		}

		filter.remove(Constants.NAME_PAGE);
		filter.remove(Constants.NAME_SIZE);
		filter.remove(Constants.SORT_LIST);
		filter.remove(Constants.SORT_ORDERS);

		filter.put(Constants.NAME_PAGE, pageable);
	}
	
	private static Sort createSortOrder(List<String> sortList, List<String> sortOrders) {
		List<Sort.Order> orders = IntStream.range(0, sortList.size())
			.filter(i -> {
				boolean hasProperty = sortList.get(i) != null && !sortList.get(i).trim().isEmpty();
				boolean hasDirection = sortOrders.size() > i && sortOrders.get(i) != null && !sortOrders.get(i).trim().isEmpty();
				return hasProperty && hasDirection;
			})
			.mapToObj(i -> {
				String property = sortList.get(i);
				String direction = sortOrders.get(i);
				return new Sort.Order(Sort.Direction.fromString(direction), property);
			})
			.toList();

		return Sort.by(orders);
	}
}
