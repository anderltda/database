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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.domain.BeanEntity;
import br.com.process.integration.database.core.exception.CheckedException;
import br.com.process.integration.database.core.exception.UncheckedException;
import br.com.process.integration.database.core.infrastructure.AbstractJpaRepository;
import br.com.process.integration.database.core.reflection.MethodInvoker;
import br.com.process.integration.database.core.reflection.MethodReflection;

@Service
@Transactional
public abstract class AbstractJpaService<E extends BeanEntity<?>, T> extends AbstractJpaRepository<E, JpaRepository<E, T>> implements JpaService<T, E> {

	protected T id;
	protected Page<E> pages;
	protected PagedModel<E> pagedModel;

	public abstract void setId(T id);
	public abstract void setEntity(E entity);
	public abstract void setPagedModel();

	@Autowired
	protected MethodInvoker methodInvoker;
	
	 /*****************************************************************************************************************************************************************
	 *																																								  *
	 * SERVICES UTILIZANDO CRITERIA																														        	  *	
	 * 																																								  *
	 * INICIAL																																						  *
	 *  																																							  *
	 *****************************************************************************************************************************************************************/	
	
	@Override
	public Long count(Map<String, Object> filter) throws CheckedException {
		try {
			return super.countable(filter);
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
	public PagedModel<E> findAll(Map<String, Object> filter, Integer page, Integer size, List<String> sortList, List<String> sortOrders) throws CheckedException {
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
	 * SERVICES UTILIZANDO CRITERIA																														        	  *	
	 * 																																								  *
	 * FINAL																																						  *
	 *  																																							  *
	 *****************************************************************************************************************************************************************/	
	
	
	
	
	
	
	
	 /*****************************************************************************************************************************************************************
	 *																																								  *
	 * SERVICES UTILIZANDO JPQL																															        	  *	
	 * 																																								  *
	 * INICIAL																																						  *
	 *  																																							  *
	 *****************************************************************************************************************************************************************/
	
	@Override
	public Long count(Map<String, Object> filter, String methodQueryJPQL) throws CheckedException {
		
		try {
			Object[] methodArgs = MethodReflection.getMethodArgs(getRepository().getClass(), methodQueryJPQL, filter);
			return (Long) methodInvoker.invokeMethodReturnObjectWithParameters(
					MethodReflection.getNameRepository(entity.getClass().getSimpleName()), methodQueryJPQL, methodArgs);
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public E findBySingle(Map<String, Object> filter, String methodQueryJPQL) throws CheckedException {
		try {
			Object[] methodArgs = MethodReflection.getMethodArgs(getRepository().getClass(), methodQueryJPQL, filter);
			return (E) methodInvoker.invokeMethodReturnObjectWithParameters(
					MethodReflection.getNameRepository(entity.getClass().getSimpleName()), methodQueryJPQL, methodArgs);
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<E> findAll(Map<String, Object> filter, String methodQueryJPQL, List<String> sortList, List<String> sortOrders) throws CheckedException {
		try {
			Sort sort = createSortOrder(sortList, sortOrders);
			filter.put("sort", sort);
			Object[] methodArgs = MethodReflection.getMethodArgs(getRepository().getClass(), methodQueryJPQL, filter);
			return (List<E>) methodInvoker.invokeMethodReturnObjectWithParameters(
					MethodReflection.getNameRepository(entity.getClass().getSimpleName()), methodQueryJPQL, methodArgs);
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PagedModel<E> findAll(Map<String, Object> filter, String methodQueryJPQL, Integer page, Integer size, List<String> sortList, List<String> sortOrders) throws CheckedException {
		try {
			Pageable pageable = PageRequest.of(page, size, createSortOrder(sortList, sortOrders));
			filter.put("page", pageable);
			Object[] methodArgs = MethodReflection.getMethodArgs(getRepository().getClass(), methodQueryJPQL, filter);
			pages = (Page<E>) methodInvoker.invokeMethodReturnObjectWithParameters(
					MethodReflection.getNameRepository(entity.getClass().getSimpleName()), methodQueryJPQL, methodArgs);
			setPagedModel();
			return pagedModel;
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
	
	 /*****************************************************************************************************************************************************************
	 *																																								  *
	 * SERVICES UTILIZANDO JPQL																															        	  *	
	 * 																																								  *
	 * FINAL																																						  *
	 *  																																							  *
	 *****************************************************************************************************************************************************************/
	

	
	
	
	 /*****************************************************************************************************************************************************************
	 *																																								  *
	 * SERVICES UTILIZANDO GENERIC CRUD REPOSITORY																													  *	
	 * 																																								  *
	 * INICIAL																																						  *
	 *  																																							  *
	 *****************************************************************************************************************************************************************/
	
	@Override
	public E findById() {
		Optional<E> entity = getRepository().findById(id);
		if (entity.isPresent()) {
			return entity.get();
		}
		return null;
	}
	
	@Override
	public List<E> findAllById(List<T> ids) {
		return getRepository().findAllById(ids);
	}

	@Override
	public boolean existsById() {
		return getRepository().existsById(id);
	}
	
	@Override
	public void deleteAll() {
		getRepository().deleteAll();
	}

	@Override
	public void deleteAllById(List<T> ids) {
		getRepository().deleteAllById(ids);
	}

	@Override
	public void deleteById() {
		getRepository().deleteById(id);
	}

	@Override
	public void save() {
		getRepository().save(entity);
	}

	@Override
	public void saveAndFlush() {
		getRepository().saveAndFlush(entity);
	}

	@Override
	public void saveAll(List<E> entitys) {
		getRepository().saveAll(entitys);
	}

	@Override
	public void saveAllAndFlush(List<E> entitys) {
		getRepository().saveAllAndFlush(entitys);
	}
	
	 /*****************************************************************************************************************************************************************
	 *																																								  *
	 * SERVICES UTILIZANDO GENERIC CRUD REPOSITORY																													  *	
	 * 																																								  *
	 * FINAL																																						  *
	 *  																																							  *
	 *****************************************************************************************************************************************************************/
	
	private static Sort createSortOrder(List<String> sortList, List<String> sortOrders) {
		List<Sort.Order> orders = IntStream.range(0, sortList.size())
				.mapToObj(i -> new Sort.Order(Sort.Direction.fromString(sortOrders.get(i)), sortList.get(i)))
				.toList();
		return Sort.by(orders);
	}
}
