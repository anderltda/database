package br.com.process.integration.database.core.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.exception.UncheckedException;
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
	
	private String operatorJoin;
	
	public void setOperatorJoin(String operatorJoin) {
		this.operatorJoin = operatorJoin;
	}
	
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

	@Transactional
	public void joinCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) throws UncheckedException {

		try {

			String[] pathParts = field.split("\\.");
			Join<?, ?> join = null;

			for (int i = 0; i < pathParts.length; i++) {
				String part = pathParts[i];
				if (i == pathParts.length - 1) {
					MethodReflection.executeMethod(this, operatorJoin, criteriaBuilder, predicates, root, part, DynamicTypeConverter.convertCascade(value));
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
	
	@Transactional
	public void equalCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		
		try {

			Method invokeMethod = CriteriaBuilder.class.getMethod("equal", Expression.class, Object.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), DynamicTypeConverter.convertCascade(value));
			predicates.add(predicate);

		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}
	
	@Transactional
	public void betweenCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) throws UncheckedException {
		
		try {
			
			String[] split = processValuesForBetween(value.toString()).replaceAll("[\\[\\]]", "").split(",");
			Method invokeMethod = CriteriaBuilder.class.getMethod("between", Expression.class, Comparable.class, Comparable.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), DynamicTypeConverter.convertCascade(split[0]), DynamicTypeConverter.convertCascade(split[1]));
			predicates.add(predicate);
			
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}
	
	
	@Transactional
	public void greaterThanOrEqualToCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) throws UncheckedException {
		greaterThan("greaterThanOrEqualTo", criteriaBuilder, predicates, root, field, value);
	}
	
	@Transactional
	public void greaterThanCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) throws UncheckedException {
		greaterThan("greaterThan", criteriaBuilder, predicates, root, field, value);
	}

	@Transactional
	public void lessThanOrEqualToCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) throws UncheckedException {
		lessThan("lessThanOrEqualTo", criteriaBuilder, predicates, root, field, value);
	}
	
	@Transactional
	public void lessThanCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) throws UncheckedException {
		lessThan("lessThan", criteriaBuilder, predicates, root, field, value);
	}
	
	@Transactional
	public void likeCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) throws UncheckedException {
		
		try {

			Method invokeMethod = CriteriaBuilder.class.getMethod("like", Expression.class, String.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), DynamicTypeConverter.convertCascade(value.replace("*", "%")));
			predicates.add(predicate);

		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}
	
	@Transactional
	public void notEqualCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) throws UncheckedException {
		
		try {

			Method invokeMethod = CriteriaBuilder.class.getMethod("notEqual", Expression.class, Object.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), DynamicTypeConverter.convertCascade(value));
			predicates.add(predicate);

		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}
	
	@Transactional
	public void inCriteria(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, String value) throws UncheckedException {
		
		try {

			String[] values = value.split(",");
			Object[] valuesObjects = new Object[values.length];
			
			for (int i = 0; i < values.length; i++) {
				valuesObjects[i] = DynamicTypeConverter.convertCascade(values[i]);
			}
			
			Predicate predicate = root.get(field).in(valuesObjects);
			predicates.add(predicate);

		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}
	
	private void greaterThan(String method, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) throws UncheckedException {
		try {
			Method invokeMethod = CriteriaBuilder.class.getMethod(method, Expression.class, Comparable.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), DynamicTypeConverter.convertCascade(value));
			predicates.add(predicate);
		} catch (InvocationTargetException ex) {
			Throwable tex = ex.getTargetException();
			throw new UncheckedException(tex.getMessage(), tex);
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}
	
	public void lessThan(String method, CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Path<?> root, String field, Object value) throws UncheckedException {
		try {
			Method invokeMethod = CriteriaBuilder.class.getMethod(method, Expression.class, Comparable.class);
			Predicate predicate = (Predicate) invokeMethod.invoke(criteriaBuilder, root.get(field), DynamicTypeConverter.convertCascade(value));
			predicates.add(predicate);
		} catch (InvocationTargetException ex) {
			Throwable tex = ex.getTargetException();
			throw new UncheckedException(tex.getMessage(), tex);
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	// Método para separar e converter os valores numéricos
	public String processValuesForBetween(String value) {

		long count = value != null && !value.isEmpty() ? value.chars().filter(ch -> ch == ',').count() : 0;
		
		if(count == 0) {
			value = value.replace(".", ",");
		}
		
		if(count >= 3) {
			
			StringBuilder builder = new StringBuilder();
			// Substituindo a vírgula decimal por um marcador temporário
			value = value.replaceAll("(\\d+),(\\d{2})", "$1#$2");
			
			// Separando os valores com base na vírgula e espaço
			String[] valoresArray = value.split(",\\s*");
			
			// Criando um array para armazenar os números convertidos
			double[] numeros = new double[valoresArray.length];
			
			// Processando cada valor
			for (int i = 0; i < valoresArray.length; i++) {
				// Restaurando a vírgula decimal
				String valorFormatado = valoresArray[i].replace("#", ",").replace(".", "").replace(",", ".");
				
				// Convertendo para número
				try {
					numeros[i] = Double.parseDouble(valorFormatado);
				} catch (NumberFormatException e) {
					System.err.println("Erro ao converter valor: " + valoresArray[i]);
				}
			}
			
			for (double numero : numeros) {
				builder.append(numero);
				builder.append(",");
			}
			
			builder.deleteCharAt(builder.length() - 1);
			
			// Retornando o array de números
			return builder.toString();
		}

		return value;
	}
}
