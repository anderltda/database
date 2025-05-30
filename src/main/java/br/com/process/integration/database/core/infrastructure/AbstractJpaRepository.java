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
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Repository;

import br.com.process.integration.database.core.Constants;
import br.com.process.integration.database.core.domain.BeanEntity;
import br.com.process.integration.database.core.domain.EntityRepository;
import br.com.process.integration.database.core.exception.UncheckedException;
import br.com.process.integration.database.core.reflection.MethodPredicate;
import br.com.process.integration.database.core.reflection.MethodReflection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * @param <E>
 * @param <I>
 * @param <M>
 * @param <R>
 */
@Repository
public abstract class AbstractJpaRepository<E extends BeanEntity<?>, I, M extends RepresentationModel<M>, R extends JpaRepository<E, I>> extends AbstractAssembler<M> implements EntityRepository<E> {

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

	@Autowired
	protected R repository;

	@Autowired
	private MethodPredicate methodPredicate;

	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * @param controllerClass
	 * @param resourceType
	 */
	protected AbstractJpaRepository(Class<?> controllerClass, Class<M> resourceType) {
		super(controllerClass, resourceType);
	}

	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<E> findByAll(Map<String, Object> filter, List<String> sortList, List<String> sortOrders) throws UncheckedException {

		try {

			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<E> query = (CriteriaQuery<E>) criteriaBuilder.createQuery(entity.getClass());
			Root<E> root = (Root<E>) query.from(entity.getClass());
			List<Predicate> predicates = buildPredicates(filter, criteriaBuilder, root);
			if (!sortList.isEmpty()) {
				List<Order> orders = new ArrayList<>();
				for (int i = 0; i < sortList.size(); i++) {
					String sortField = sortList.get(i);
					boolean isAscending = sortOrders.size() > i ? sortOrders.get(i).equalsIgnoreCase("asc") : sortOrders.get(0).equalsIgnoreCase("asc");
					orders.add(methodPredicate.buildOrder(sortField, criteriaBuilder, root, isAscending));
				}
				query.orderBy(orders);
			}
			query.select(root).where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
			return entityManager.createQuery(query).getResultList();
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Page<E> findByPageAll(Map<String, Object> filter, Pageable pageable) throws UncheckedException {

		try {
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<E> query = (CriteriaQuery<E>) criteriaBuilder.createQuery(entity.getClass());
			Root<E> root = (Root<E>) query.from(entity.getClass());
			List<Predicate> predicates = buildPredicates(filter, criteriaBuilder, root);
			query.select(root).where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
			List<Order> orders = new ArrayList<>();
			pageable.getSort().forEach(order -> {
				try {
					boolean isAscending = order.isAscending();
					orders.add(methodPredicate.buildOrder(order.getProperty(), criteriaBuilder, root, isAscending));
				} catch (Exception ex) {
					throw new UncheckedException(ex.getCause().getLocalizedMessage(), ex);
				}
			});
			query.orderBy(orders);
			TypedQuery<E> typedQuery = entityManager.createQuery(query);
			typedQuery.setFirstResult((int) pageable.getOffset());
			typedQuery.setMaxResults(pageable.getPageSize());
			List<E> list = typedQuery.getResultList();
			Long total = countable(filter);
			return new PageImpl<>(list, pageable, total);
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Long countable(Map<String, Object> filter) {
		try {
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
			Root<E> root = (Root<E>) query.from(entity.getClass());
			List<Predicate> countPredicates = buildPredicates(filter, criteriaBuilder, root);
			query.select(criteriaBuilder.count(root)).where(countPredicates.toArray(new Predicate[0]));
			return entityManager.createQuery(query).getSingleResult();
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public E findSingle(Map<String, Object> filter) throws UncheckedException {
		try {
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<E> query = (CriteriaQuery<E>) criteriaBuilder.createQuery(entity.getClass());
			Root<E> root = (Root<E>) query.from(entity.getClass());
			List<Predicate> predicates = buildPredicates(filter, criteriaBuilder, root);
			query.select(root).where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
			return entityManager.createQuery(query).getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	/**
	 * @param filter
	 * @param criteriaBuilder
	 * @param path
	 * @return
	 * @throws UncheckedException
	 */
	private List<Predicate> buildPredicates(Map<String, Object> filter, CriteriaBuilder criteriaBuilder, Path<E> path) throws UncheckedException {
		List<Predicate> predicates = new ArrayList<>();
		filter.forEach((key, value) -> {
			if (key.contains(Constants.IDENTITY_OPERATOR))
				return;
			String method = (OPERADORES.get(filter.get(key + Constants.IDENTITY_OPERATOR)) != null
					? OPERADORES.get(filter.get(key + Constants.IDENTITY_OPERATOR))
					: Constants.PREDICATE_EQUAL);
			try {
				if (key.contains(".")) {
					MethodReflection.executeMethod(methodPredicate, "setOperatorJoin", method + "Criteria");
					method = "join";
				}
				MethodReflection.executeMethod(methodPredicate, method + "Criteria", criteriaBuilder, predicates, path, key, value);

			} catch (Exception ex) {
				throw new UncheckedException(ex.getCause().getLocalizedMessage(), ex);
			}

		});
		return predicates;
	}
}
