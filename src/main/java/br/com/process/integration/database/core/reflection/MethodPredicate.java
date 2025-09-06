
package br.com.process.integration.database.core.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.exception.UncheckedException;
import br.com.process.integration.database.core.utils.DynamicFoundTypeUtils;
import br.com.process.integration.database.core.utils.StringsUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.Metamodel;

/**
 * 
 */
@Service
public class MethodPredicate {
	
	private EntityManager entityManager;

	private String operatorJoin;
	
	/**
	 * @param entityManager
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;	
	}
	
	/**
	 /**
	 * @param operatorJoin
	 */
	public void setOperatorJoin(String operatorJoin) {
		this.operatorJoin = operatorJoin;
	}
	
	/**
	 * @param attributePath
	 * @param criteriaBuilder
	 * @param root
	 * @param asc
	 * @return
	 * @throws UncheckedException
	 */
	public Order buildOrder(String attributePath, CriteriaBuilder criteriaBuilder, Path<?> root, boolean asc) throws UncheckedException {
		
		try {

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
			
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	/**
	 * @param criteriaBuilder
	 * @param predicates
	 * @param root
	 * @param field
	 * @param value
	 * @throws UncheckedException
	 */
	@Transactional
	@Deprecated(since = "v1.1.0", forRemoval = true)
	public void joinCriteria_old(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) throws UncheckedException {

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
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}
	
	
	/**
	 * @param criteriaBuilder
	 * @param predicates
	 * @param root
	 * @param field
	 * @param value
	 * @throws UncheckedException
	 */
	@Transactional
	public void joinCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) throws UncheckedException {
		Logger logger = LoggerFactory.getLogger(this.getClass());

		try {
			String[] pathParts = field.split("\\.");
			Path<?> currentPath = root;

			for (int i = 0; i < pathParts.length; i++) {
				String part = pathParts[i];

				// Última parte: aplicar operador no campo
				if (i == pathParts.length - 1) {
					MethodReflection.executeMethod(this, operatorJoin, criteriaBuilder, predicates, currentPath, part, value);
					return;
				}

				// Tenta usar join se possível, senão usa get()
				if (currentPath instanceof From<?, ?> fromPath && isAssociation(entityManager, fromPath, part)) {
					currentPath = fromPath.join(part, JoinType.LEFT);
				} else {
					// Campo simples ou EmbeddedId
					try {
						currentPath = currentPath.get(part);
					} catch (IllegalArgumentException e) {
						logger.warn("Ignorando caminho '{}': não encontrado em {}", part, currentPath.getJavaType().getSimpleName());
						return;
					}
				}
			}

		} catch (Exception ex) {
			throw new UncheckedException("Erro ao construir joins para o campo: " + field, ex);
		}
	}	
	
	/**
	 * @param entityManager
	 * @param fromPath
	 * @param attributeName
	 * @return
	 */
	private boolean isAssociation(EntityManager entityManager, From<?, ?> fromPath, String attributeName) {
		try {
			Class<?> javaType = fromPath.getJavaType();
			Metamodel metamodel = entityManager.getMetamodel();
			ManagedType<?> managedType = metamodel.managedType(javaType);
			Attribute<?, ?> attribute = managedType.getAttribute(attributeName);
			return attribute.isAssociation();
		} catch (Exception e) {
			return false;
		}
	}
	
	
	/**
	 * @param criteriaBuilder
	 * @param predicates
	 * @param root
	 * @param field
	 * @param value
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@Transactional
	public void equalCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		
		try {
			
	        Object object = DynamicFoundTypeUtils.getTypeAttributeName(root.getJavaType().getName(), field, value, false);
			Method invokeMethod = CriteriaBuilder.class.getMethod("equal", Expression.class, Object.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), object);
			predicates.add(predicate);

		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * @param criteriaBuilder
	 * @param predicates
	 * @param root
	 * @param field
	 * @param value
	 * @throws UncheckedException
	 */
	@Transactional
	public void betweenCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) throws UncheckedException {
		
		try {
			
			String[] split = Optional.ofNullable(StringsUtils.processValuesForBetween(value))
                    .filter(str -> !str.isEmpty())
                    .map(str -> str.split(","))
                    .orElse(new String[1]); 
			
	        Object start = DynamicFoundTypeUtils.getTypeAttributeName(root.getJavaType().getName(), field, split[0], false);
	        Object end = DynamicFoundTypeUtils.getTypeAttributeName(root.getJavaType().getName(), field, split[1], false);
	        
			Method invokeMethod = CriteriaBuilder.class.getMethod("between", Expression.class, Comparable.class, Comparable.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), start, end);
			predicates.add(predicate);
			
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * @param criteriaBuilder
	 * @param predicates
	 * @param root
	 * @param field
	 * @param value
	 * @throws UncheckedException
	 */
	@Transactional
	public void greaterThanOrEqualToCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) throws UncheckedException {
		greaterThan("greaterThanOrEqualTo", criteriaBuilder, predicates, root, field, value);
	}
	
	/**
	 * @param criteriaBuilder
	 * @param predicates
	 * @param root
	 * @param field
	 * @param value
	 * @throws UncheckedException
	 */
	@Transactional
	public void greaterThanCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) throws UncheckedException {
		greaterThan("greaterThan", criteriaBuilder, predicates, root, field, value);
	}

	/**
	 * @param criteriaBuilder
	 * @param predicates
	 * @param root
	 * @param field
	 * @param value
	 * @throws UncheckedException
	 */
	@Transactional
	public void lessThanOrEqualToCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) throws UncheckedException {
		lessThan("lessThanOrEqualTo", criteriaBuilder, predicates, root, field, value);
	}
	
	/**
	 * @param criteriaBuilder
	 * @param predicates
	 * @param root
	 * @param field
	 * @param value
	 * @throws UncheckedException
	 */
	@Transactional
	public void lessThanCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) throws UncheckedException {
		lessThan("lessThan", criteriaBuilder, predicates, root, field, value);
	}
	
	/**
	 * @param criteriaBuilder
	 * @param predicates
	 * @param root
	 * @param field
	 * @param value
	 * @throws UncheckedException
	 */
	@Transactional
	public void likeCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) throws UncheckedException {
		
		try {

			Object object = DynamicFoundTypeUtils.getTypeAttributeName(root.getJavaType().getName(), field, value.replace("*", "%"), false);
			
			Method invokeMethod = CriteriaBuilder.class.getMethod("like", Expression.class, String.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), object);
			predicates.add(predicate);

		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * @param criteriaBuilder
	 * @param predicates
	 * @param root
	 * @param field
	 * @param value
	 * @throws UncheckedException
	 */
	@Transactional
	public void notEqualCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) throws UncheckedException {
		
		try {

			Object object = DynamicFoundTypeUtils.getTypeAttributeName(root.getJavaType().getName(), field, value, false);
			
			Method invokeMethod = CriteriaBuilder.class.getMethod("notEqual", Expression.class, Object.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), object);
			predicates.add(predicate);

		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * @param criteriaBuilder
	 * @param predicates
	 * @param root
	 * @param field
	 * @param value
	 * @throws UncheckedException
	 */
	@Transactional
	public void inCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) throws UncheckedException {
		
		try {

			String[] values = value.split(",");
			Object[] valuesObjects = new Object[values.length];
	        
			for (int i = 0; i < values.length; i++) {
				valuesObjects[i] = DynamicFoundTypeUtils.getTypeAttributeName(root.getJavaType().getName(), field, values[i], false);
			}
			
			Predicate predicate = root.get(field).in(valuesObjects);
			predicates.add(predicate);

		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * @param method
	 * @param criteriaBuilder
	 * @param predicates
	 * @param root
	 * @param field
	 * @param value
	 * @throws UncheckedException
	 */
	private void greaterThan(String method, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) throws UncheckedException {
		try {
			Object object = DynamicFoundTypeUtils.getTypeAttributeName(root.getJavaType().getName(), field, value, false);
			Method invokeMethod = CriteriaBuilder.class.getMethod(method, Expression.class, Comparable.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), object);
			predicates.add(predicate);
		} catch (InvocationTargetException ex) {
			Throwable tex = ex.getTargetException();
			throw new UncheckedException(tex.getMessage(), tex);
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * @param method
	 * @param criteriaBuilder
	 * @param predicates
	 * @param root
	 * @param field
	 * @param value
	 * @throws UncheckedException
	 */
	public void lessThan(String method, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) throws UncheckedException {
		try {
			Object object = DynamicFoundTypeUtils.getTypeAttributeName(root.getJavaType().getName(), field, value, false);
			Method invokeMethod = CriteriaBuilder.class.getMethod(method, Expression.class, Comparable.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), object);
			predicates.add(predicate);
		} catch (InvocationTargetException ex) {
			Throwable tex = ex.getTargetException();
			throw new UncheckedException(tex.getMessage(), tex);
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}
}
