package br.com.process.integration.database.core.application;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
import br.com.process.integration.database.core.exception.ServiceException;
import br.com.process.integration.database.core.infrastructure.AbstractJpaRepository;
import br.com.process.integration.database.core.reflection.MethodInvoker;
import br.com.process.integration.database.core.reflection.MethodReflection;

@Service
@Transactional
public abstract class AbstractJpaService<E extends Entity<?>, ID> extends AbstractJpaRepository<E, JpaRepository<E, ID>> implements JpaService<ID, E> {

	protected ID id;
	protected Page<E> pages;
	protected PagedModel<E> pagedModel;

	public abstract void setId(ID id);
	public abstract void setEntity(E entity);
	public abstract void setPagedModel();

	@Autowired
	protected MethodInvoker methodInvoker;
	
	@Override
	public Long count(LinkedHashMap<String, Object> filter) {
		Long count = (Long) super.count(filter);
		return count;
	}
	
	@Override
	public List<E> findAll(LinkedHashMap<String, Object> filter, ArrayList<String> sortList, String sortOrder) {
		return super.findAll(filter, sortList, sortOrder);
	}
	
	@Override
	public PagedModel<E> findAll(LinkedHashMap<String, Object> filter, Integer page, Integer size, ArrayList<String> sortList, String sortOrder) throws ServiceException {

		Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));

		pages = (Page<E>) super.findAll(filter, pageable);

		setPagedModel();

		return pagedModel;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<E> findAll(LinkedHashMap<String, Object> filter, String methodQueryJPQL) throws ServiceException {
		
		Object[] methodArgs = MethodReflection.getMethodArgs(getRepository().getClass(), methodQueryJPQL, filter);
		
		List<E> list = (List<E>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameRepository(entity.getClass().getSimpleName()), methodQueryJPQL, methodArgs);
		
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public PagedModel<E> findAll(LinkedHashMap<String, Object> filter, String methodQueryJPQL, Integer page, Integer size, ArrayList<String> sortList, String sortOrder) throws ServiceException {

		Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
		
		filter.put("page", pageable);
		
		Object[] methodArgs = MethodReflection.getMethodArgs(getRepository().getClass(), methodQueryJPQL, filter);
		
		pages = (Page<E>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameRepository(entity.getClass().getSimpleName()), methodQueryJPQL, methodArgs);

		setPagedModel();

		return pagedModel;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public E findBySingle(LinkedHashMap<String, Object> filter, String methodQueryJPQL) throws ServiceException {
		
		Object[] methodArgs = MethodReflection.getMethodArgs(getRepository().getClass(), methodQueryJPQL, filter);
		
		E e = (E) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameRepository(entity.getClass().getSimpleName()), methodQueryJPQL, methodArgs);

		return e;
	}

	@Override
	public Long count(LinkedHashMap<String, Object> filter, String methodQueryJPQL) throws ServiceException {
		
		Object[] methodArgs = MethodReflection.getMethodArgs(getRepository().getClass(), methodQueryJPQL, filter);
		
		Long count = (Long) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameRepository(entity.getClass().getSimpleName()), methodQueryJPQL, methodArgs);

		return count;
	}
	
	@Override
	public List<E> findAllById(ArrayList<ID> ids) {
		List<E> list = (List<E>) getRepository().findAllById(ids);
		return list;
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
	public void deleteAllById(ArrayList<ID> ids) {
		getRepository().deleteAllById(ids);
	}

	@Override
	public void delete() {
		getRepository().delete(entity);
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
	public void saveAll(ArrayList<E> entitys) {
		getRepository().saveAll(entitys);
	}

	@Override
	public void saveAllAndFlush(ArrayList<E> entitys) {
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
