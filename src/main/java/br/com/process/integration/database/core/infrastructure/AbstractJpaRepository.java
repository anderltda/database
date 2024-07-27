package br.com.process.integration.database.core.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.process.integration.database.core.domain.Entity;
import br.com.process.integration.database.core.domain.EntityRepository;
import br.com.process.integration.database.core.exception.ServiceException;
import br.com.process.integration.database.core.reflection.MethodPredicate;
import br.com.process.integration.database.core.reflection.MethodReflection;
import br.com.process.integration.database.core.util.Constants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public abstract class AbstractJpaRepository<E extends Entity<?>, R extends JpaRepository<?, ?>> implements EntityRepository<E, R> {

	protected static final Map<String, String> OPERADORES = new HashMap<>();
	static {
		OPERADORES.put(Constants.HTML_IGUAL, Constants.PREDICATE_EQUAL);
		OPERADORES.put(Constants.HTML_MAIOR_OU_IGUAL, Constants.PREDICATE_GREATER_THAN_OR_EQUAL_TO);
		OPERADORES.put(Constants.HTML_MENOR_OU_IGUAL, Constants.PREDICATE_LESS_THAN_OR_EQUAL_TO);
		OPERADORES.put(Constants.HTML_MAIOR_QUE, Constants.PREDICATE_GREATER_THAN);
		OPERADORES.put(Constants.HTML_MENOR_QUE, Constants.PREDICATE_LESS_THAN);
		OPERADORES.put(Constants.HTML_DIFERENTE, Constants.PREDICATE_NOT_EQUAL);
		OPERADORES.put(Constants.HTML_LIKE, Constants.PREDICATE_LIKE);
		OPERADORES.put(Constants.HTML_IN, Constants.PREDICATE_IN);
		OPERADORES.put(Constants.HTML_BETWEEN, Constants.PREDICATE_BETWEEN);
	}

	protected E entity;

	@Autowired(required = false)
	protected R repository;

	@Autowired
	private MethodPredicate methodPredicate;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public R getRepository() {
		return repository;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> findAll(Map<String, Object> filter, List<String> sortList, String sortOrder) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<E> query = (CriteriaQuery<E>) criteriaBuilder.createQuery(entity.getClass());

		Root<E> root = (Root<E>) query.from(entity.getClass());

		List<Predicate> predicates = buildPredicates(filter, criteriaBuilder, root);

		if (sortList != null && sortOrder != null) {
			List<Order> orders = new ArrayList<>();
			for (int i = 0; i < sortList.size(); i++) {
				String sortField = sortList.get(i);
				boolean isAscending = sortOrder.equalsIgnoreCase("asc");
				if (isAscending) {
					orders.add(criteriaBuilder.asc(root.get(sortField)));
				} else {
					orders.add(criteriaBuilder.desc(root.get(sortField)));
				}
			}
			query.orderBy(orders);
		}

		query.select(root).where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));

		return entityManager.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<E> findAll(Map<String, Object> filter, Pageable pageable) throws ServiceException {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<E> query = (CriteriaQuery<E>) criteriaBuilder.createQuery(entity.getClass());

		Root<E> root = (Root<E>) query.from(entity.getClass());

		List<Predicate> predicates = buildPredicates(filter, criteriaBuilder, root);

		query.select(root).where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));

		TypedQuery<E> typedQuery = entityManager.createQuery(query);
		
		typedQuery.setFirstResult((int) pageable.getOffset());
		
		typedQuery.setMaxResults(pageable.getPageSize());

		List<E> list = typedQuery.getResultList();
		
		Long total = count(filter);

		return new PageImpl<>(list, pageable, total);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long count(Map<String, Object> filter) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
		
		Root<E> root = (Root<E>) query.from(entity.getClass());

		List<Predicate> countPredicates = buildPredicates(filter, criteriaBuilder, root);

		query.select(criteriaBuilder.count(root)).where(countPredicates.toArray(new Predicate[0]));
		
		return entityManager.createQuery(query).getSingleResult();
	}

	private List<Predicate> buildPredicates(Map<String, Object> filter, CriteriaBuilder criteriaBuilder, Path<E> path) {

		List<Predicate> predicates = new ArrayList<>();

		filter.forEach((key, value) -> {
			
			if(key.contains(Constants.IDENTITY_OPERATOR)) return;

			String method = (OPERADORES.get(filter.get(key + Constants.IDENTITY_OPERATOR)) != null ? OPERADORES.get(filter.get(key + Constants.IDENTITY_OPERATOR)) : Constants.PREDICATE_EQUAL);
			
			try {

				if (key.contains(".")) {
					MethodReflection.executeMethod(methodPredicate, "setOperatorJoin", method);
					method = "join";
				}

				MethodReflection.executeMethod(methodPredicate, method + "Criteria", criteriaBuilder, predicates, path, key, value);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			
		});

		return predicates;
	}
}
