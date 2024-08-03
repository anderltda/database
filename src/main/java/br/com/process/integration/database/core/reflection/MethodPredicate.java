package br.com.process.integration.database.core.reflection;

import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.util.DynamicTypeConverter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class MethodPredicate {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodPredicate.class);
	
	private String operatorJoin;
	
	public void setOperatorJoin(String operatorJoin) {
		this.operatorJoin = operatorJoin;
	}
	
	public Order buildOrder(String attributePath, CriteriaBuilder criteriaBuilder, Path<?> root, boolean asc) {

		String[] pathParts = attributePath.split("\\.");

		for (int i = 0; i < pathParts.length; i++) {
			String part = pathParts[i];
			if (i < pathParts.length - 1) {
				if (root instanceof Root<?>) {
					root = ((Root<?>) root).join(part);
				} else if (root instanceof Join<?, ?>) {
					root = ((Join<?, ?>) root).join(part);
				}
			} else {
				root = root.get(part);
			}
		}

		return asc ? criteriaBuilder.asc(root) : criteriaBuilder.desc(root);
	}

	@Transactional
	public void joinCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) {

		try {

			String[] pathParts = field.split("\\.");
			Join<?, ?> join = null;

			for (int i = 0; i < pathParts.length; i++) {
				String part = pathParts[i];
				if (i == pathParts.length - 1) {
					MethodReflection.executeMethod(this, operatorJoin, criteriaBuilder, predicates, root, part, DynamicTypeConverter.convert(value));
					return;
				} else {
					if (join == null && root != null) {
						join = ((From<?, ?>) root).join(part);
						root = join;
					} else if (join != null) {
						join = join.join(part);
						root = join;
					}
				}
			}

		} catch (Exception ex) {
			LOGGER.error("[joinCriteria]", ex);
		}
	}
	
	@Transactional
	public void equalCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) {
		
		try {

			Method invokeMethod = CriteriaBuilder.class.getMethod("equal", Expression.class, Object.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), DynamicTypeConverter.convert(value));
			predicates.add(predicate);

		} catch (Exception ex) {
			LOGGER.error("[equalCriteria]", ex);
		}
	}
	
	@Transactional
	public void betweenCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) {
		
		try {
			
			String[] split = value.toString().replaceAll("[\\[\\]]", "").split(",");
			Method invokeMethod = CriteriaBuilder.class.getMethod("between", Expression.class, Comparable.class, Comparable.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), DynamicTypeConverter.convert(split[0]), DynamicTypeConverter.convert(split[1]));
			predicates.add(predicate);
			
		} catch (Exception ex) {
			LOGGER.error("[betweenCriteria]", ex);
		}
	}
	
	
	@Transactional
	public void greaterThanOrEqualToCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) {
		
		try {
			
			Method invokeMethod = CriteriaBuilder.class.getMethod("greaterThanOrEqualTo", Expression.class, Comparable.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), DynamicTypeConverter.convert(value));
			predicates.add(predicate);

		} catch (Exception ex) {
			LOGGER.error("[greaterThanOrEqualToCriteria]", ex);
		}
	}
	
	@Transactional
	public void lessThanOrEqualToCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) {
		
		try {
			
			Method invokeMethod = CriteriaBuilder.class.getMethod("lessThanOrEqualTo", Expression.class, Comparable.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), DynamicTypeConverter.convert(value));
			predicates.add(predicate);

		} catch (Exception ex) {
			LOGGER.error("[lessThanOrEqualToCriteria]", ex);
		}
	}
	
	
	@Transactional
	public void greaterThanCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) {
		
		try {
			
			Method invokeMethod = CriteriaBuilder.class.getMethod("greaterThan", Expression.class, Comparable.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), DynamicTypeConverter.convert(value));
			predicates.add(predicate);

		} catch (Exception ex) {
			LOGGER.error("[greaterThanCriteria]", ex);
		}
	}
	
	@Transactional
	public void lessThanCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) {
		
		try {
			
			Method invokeMethod = CriteriaBuilder.class.getMethod("lessThan", Expression.class, Comparable.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), DynamicTypeConverter.convert(value));
			predicates.add(predicate);

		} catch (Exception ex) {
			LOGGER.error("[lessThanCriteria]", ex);
		}
	}

	@Transactional
	public void likeCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) {
		
		try {

			Method invokeMethod = CriteriaBuilder.class.getMethod("like", Expression.class, String.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), DynamicTypeConverter.convert(value.replace("*", "%")));
			predicates.add(predicate);

		} catch (Exception ex) {
			LOGGER.error("[likeCriteria]", ex);
		}
	}
	
	@Transactional
	public void notEqualCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) {
		
		try {

			Method invokeMethod = CriteriaBuilder.class.getMethod("notEqual", Expression.class, Object.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), DynamicTypeConverter.convert(value));
			predicates.add(predicate);

		} catch (Exception ex) {
			LOGGER.error("[notEqualCriteria]", ex);
		}
	}
	
	@Transactional
	public void inCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) {
		
		try {

			String[] values = value.split(",");
			Object[] valuesObjects = new Object[values.length];
			
			for (int i = 0; i < values.length; i++) {
				valuesObjects[i] = DynamicTypeConverter.convert(values[i]);
			}
			
			Predicate predicate = root.get(field).in(valuesObjects);
			predicates.add(predicate);

		} catch (Exception ex) {
			LOGGER.error("[inCriteria]", ex);
		}
	}

}
