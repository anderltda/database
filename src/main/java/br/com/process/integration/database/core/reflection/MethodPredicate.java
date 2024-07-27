package br.com.process.integration.database.core.reflection;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.util.DynamicTypeConverter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

@Service
public class MethodPredicate {
	
	private String operatorJoin;
	
	public void setOperatorJoin(String operatorJoin) {
		this.operatorJoin = operatorJoin;
	}

	@Transactional
	public void joinCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) {

		try {

			String[] pathParts = field.split("\\.");
			Join<?, ?> join = null;

			for (int i = 0; i < pathParts.length; i++) {
				String part = pathParts[i];
				if (i == pathParts.length - 1) {
					MethodReflection.executeMethod(this, operatorJoin, criteriaBuilder, predicates, root, part, value);
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
			ex.printStackTrace();
		}
	}
	
	@Transactional
	public void equalCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) {
		
		try {

			Method invokeMethod = CriteriaBuilder.class.getMethod("equal", Expression.class, Object.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), value);
			predicates.add(predicate);

		} catch (Exception ex) {
			ex.printStackTrace();
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
			ex.printStackTrace();
		}
	}
	
	
	@Transactional
	public void greaterThanOrEqualToCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) {
		
		try {
			
			Method invokeMethod = CriteriaBuilder.class.getMethod("greaterThanOrEqualTo", Expression.class, Comparable.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), DynamicTypeConverter.convert(value));
			predicates.add(predicate);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Transactional
	public void lessThanOrEqualToCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) {
		
		try {
			
			Method invokeMethod = CriteriaBuilder.class.getMethod("lessThanOrEqualTo", Expression.class, Comparable.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), DynamicTypeConverter.convert(value));
			predicates.add(predicate);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	@Transactional
	public void greaterThanCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) {
		
		try {
			
			Method invokeMethod = CriteriaBuilder.class.getMethod("greaterThan", Expression.class, Comparable.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), DynamicTypeConverter.convert(value));
			predicates.add(predicate);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Transactional
	public void lessThanCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) {
		
		try {
			
			Method invokeMethod = CriteriaBuilder.class.getMethod("lessThan", Expression.class, Comparable.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), DynamicTypeConverter.convert(value));
			predicates.add(predicate);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Transactional
	public void likeCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) {
		
		try {

			Method invokeMethod = CriteriaBuilder.class.getMethod("like", Expression.class, String.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), value.replace("*", "%"));
			predicates.add(predicate);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Transactional
	public void notEqualCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) {
		
		try {

			Method invokeMethod = CriteriaBuilder.class.getMethod("notEqual", Expression.class, Object.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), value);
			predicates.add(predicate);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Transactional
	public void inCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) {
		
		try {

			String[] values = value.split(",");
			Predicate predicate = root.get(field).in(values);
			predicates.add(predicate);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
