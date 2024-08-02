package br.com.process.integration.database.core.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.domain.Entity;
import br.com.process.integration.database.core.infrastructure.AbstractJpaRepository;
import br.com.process.integration.database.core.reflection.MethodInvoker;
import br.com.process.integration.database.core.reflection.MethodReflection;

@Service
@Transactional
public abstract class AbstractJpaService<E extends Entity<?>, T> extends AbstractJpaRepository<E, JpaRepository<E, T>> implements JpaService<T, E> {

	protected T id;
	protected Page<E> pages;
	protected PagedModel<E> pagedModel;

	public abstract void setId(T id);
	public abstract void setEntity(E entity);
	public abstract void setPagedModel();

	@Autowired
	protected MethodInvoker methodInvoker;
	
	@Override
	public Long count(Map<String, Object> filter) {
		return super.count(filter);
	}
	
	@Override
	public List<E> findAll(Map<String, Object> filter, List<String> sortList, List<String> sortOrders) {
		return super.findAll(filter, sortList, sortOrders);
	}
	
	@Override
	public PagedModel<E> findAll(Map<String, Object> filter, Integer page, Integer size, List<String> sortList, String sortOrder) {

		try {

			Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));

			pages = super.findAll(filter, pageable);

			setPagedModel();

			return pagedModel;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<E> findAll(Map<String, Object> filter, String methodQueryJPQL) {
		
		try {
			
			Object[] methodArgs = MethodReflection.getMethodArgs(getRepository().getClass(), methodQueryJPQL, filter);
			
			return (List<E>) methodInvoker.invokeMethodReturnObjectWithParameters(
					MethodReflection.getNameRepository(entity.getClass().getSimpleName()), methodQueryJPQL, methodArgs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ArrayList<>();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public PagedModel<E> findAll(Map<String, Object> filter, String methodQueryJPQL, Integer page, Integer size, List<String> sortList, String sortOrder) {

		try {

			Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));

			filter.put("page", pageable);

			Object[] methodArgs = MethodReflection.getMethodArgs(getRepository().getClass(), methodQueryJPQL, filter);

			pages = (Page<E>) methodInvoker.invokeMethodReturnObjectWithParameters(
					MethodReflection.getNameRepository(entity.getClass().getSimpleName()), methodQueryJPQL, methodArgs);

			setPagedModel();

			return pagedModel;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public E findBySingle(Map<String, Object> filter, String methodQueryJPQL) {

		try {
			
			Object[] methodArgs = MethodReflection.getMethodArgs(getRepository().getClass(), methodQueryJPQL, filter);

			return (E) methodInvoker.invokeMethodReturnObjectWithParameters(
					MethodReflection.getNameRepository(entity.getClass().getSimpleName()), methodQueryJPQL, methodArgs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Long count(Map<String, Object> filter, String methodQueryJPQL) {

		try {

			Object[] methodArgs = MethodReflection.getMethodArgs(getRepository().getClass(), methodQueryJPQL, filter);

			return (Long) methodInvoker.invokeMethodReturnObjectWithParameters(
					MethodReflection.getNameRepository(entity.getClass().getSimpleName()), methodQueryJPQL, methodArgs);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	@Override
	public List<E> findAllById(List<T> ids) {
		return getRepository().findAllById(ids);
	}

	@Override
	public E findById() {
		Optional<E> entity = getRepository().findById(id);

		if (entity.isPresent()) {
			return entity.get();
		}

		return null;
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
	
	private List<Sort.Order> createSortOrder(List<String> sortList, String sortOrders) {
		
	    List<Sort.Order> sorts = new ArrayList<>();
	    Sort.Direction direction;
	    for (String sort : sortList) {
	        if (sortOrders != null) {
	            direction = Sort.Direction.fromString(sortOrders);
	        } else {
	            direction = Sort.Direction.DESC;
	        }
	        sorts.add(new Sort.Order(direction, sort));
	    }
	    return sorts;
	}
}
