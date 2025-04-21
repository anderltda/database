package br.com.process.integration.database.core.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import br.com.process.integration.database.core.exception.CheckedException;
import br.com.process.integration.database.core.exception.UncheckedException;
import br.com.process.integration.database.core.util.Constants;
import br.com.process.integration.database.core.util.DynamicFoundType;
import jakarta.persistence.JoinColumn;

@Service
public class MethodReflection {

	private MethodReflection() { }

	public static void transformsJsonModel(JsonNode jsonNode, Object object) throws CheckedException {
		try {
			List<Field> fields = MethodReflection.getFields(object.getClass(), Object.class);
			for (Field field : fields) {
				Object objectTemp = null;
				if (jsonNode.has(field.getName())) {
					if (field.getAnnotation(JoinColumn.class) instanceof JoinColumn) {
						objectTemp = identificarClasse(field.getType().getName());
						transformsJsonModel(jsonNode.get(field.getName()), objectTemp);
						executeMethod(object, setMethod(field.getName()), objectTemp);
					} else {
						executeMethod(object, setMethod(field.getName()), DynamicFoundType.getTypeJsonValue(field, jsonNode));
					}
				}
			}
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	public static Class<?> getTypeById(Object object) throws UncheckedException {
		try {
			List<Field> fields = MethodReflection.getFields(object.getClass(), Object.class);
			Class<?> type = null;
			for (Field field : fields) {
				if (field.getName().equals("id")) {
					type = field.getType();
					break;
				}
			}
			return type;
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	public static List<Object> transformsJsonIds(JsonNode jsonNode) throws CheckedException {
		try {
			List<Object> list = new ArrayList<>();
			for (Iterator<JsonNode> iterator = jsonNode.elements(); iterator.hasNext();) {
				list.add(iterator.next().get("id").asText());
			}
			return list;
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
	
	public static String getNameMapper(String className) throws CheckedException {
		try {
			return className.substring(0, 1).toLowerCase() + className.substring(1) + "Mapper";
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	public static String getNameRepository(String className) throws CheckedException {
		try {
			return className.substring(0, 1).toLowerCase() + className.substring(1) + "Repository";
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	public static String getNameService(String className) throws CheckedException {
		try {
			return className.substring(0, 1).toLowerCase() + className.substring(1) + "Service";
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	public static Object findViewUsingClassLoader(String className) throws CheckedException {
		try {
			//String packagePath = (Constants.DIRECTORY_APPLICATION + Constants.PACKAGE_NAME_VIEW).replaceAll("[.]", "/");
			return findClassUsingClassLoader(className, Constants.PACKAGE_NAME_VIEW);
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	public static Object findEntityUsingClassLoader(String className) throws CheckedException {
		try {
			//String packagePath = (Constants.DIRECTORY_APPLICATION + Constants.PACKAGE_NAME_ENTITY).replaceAll("[.]", "/");
			return findClassUsingClassLoader(className, Constants.PACKAGE_NAME_ENTITY);
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
	
	public static Object findDataUsingClassLoader(String className) throws CheckedException {
		try {
			//String packagePath = (Constants.DIRECTORY_APPLICATION + Constants.PACKAGE_NAME_DATA).replaceAll("[.]", "/");
			return findClassUsingClassLoader(className, Constants.PACKAGE_NAME_DATA);
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	public static Object identificarClasse(String instance) throws UncheckedException {
		try {
			ClassLoader classLoader = MethodReflection.class.getClassLoader();
			Class<?> main = classLoader.loadClass(instance);
			return main.getDeclaredConstructor().newInstance();
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object executeMethod(Object object, String methodName, Object... paramValue) throws UncheckedException {
		try {
			Class[] paramTypes = MethodReflection.transformParametersTypes(paramValue);
			Method method = getMethod(object.getClass(), methodName, paramTypes);
			if (method != null) {
				return method.invoke(object, paramValue);
			}
			return null;
		} catch (InvocationTargetException e) {
			Throwable tex = e.getTargetException();
			throw new UncheckedException(tex.getMessage(), tex);			
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> Method getMethod(Class<T> clazz, String methodName, Class<T>... paramClass) {
		Method m = null;
		try {
			m = clazz.getDeclaredMethod(methodName, paramClass);
		} catch (Exception e) {
			try {
				m = clazz.getMethod(methodName, paramClass);
			} catch (Exception e1) {
				Method[] ms = clazz.getMethods();
				for (Method mtmp : ms) {
					if (mtmp.getName().equals(methodName)) {
						m = mtmp;
						break;
					}
				}
			}
		}
		return m;

	}

	private static List<Field> getFields(Class<?> clazz, Class<?> classLimit) throws UncheckedException {
		try {
			ArrayList<Field> ret = new ArrayList<>();
			listFields(clazz, classLimit, ret, ".*");
			return ret;
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	private static void listFields(Class<?> clazz, Class<?> classLimit, List<Field> ret, String pattern) throws UncheckedException {

		try {
			if (!classLimit.equals(clazz) && clazz != null) {

				Field[] fields = clazz.getDeclaredFields();

				for (Field field : fields) {
					if (!ret.contains(field) && field.getName().matches(pattern)) {
						ret.add(field);
					}
				}
				listFields(clazz.getSuperclass(), classLimit, ret, pattern);
			}
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	public static Class<?>[] transformParametersTypes(Object... params) throws UncheckedException {

		try {
			Class<?>[] paramTypes = new Class[params.length];
			for (int i = 0; i < params.length; i++) {
				paramTypes[i] = getParameterType(params[i]);
			}

			return paramTypes;
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	private static Class<?> getParameterType(Object param) {

		if (param instanceof Map) {
			return Map.class;
		}

		if (param instanceof List) {
			return List.class;
		}

		return param.getClass();
	}

	private static Object findClassUsingClassLoader(String className, String packagePath) throws UncheckedException {
		try {
			return PackageScanner.findClassBySimpleName(packagePath, className);
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	private static String setMethod(String value) {
		return "set".concat(firstUpper(value));
	}

	private static String firstUpper(String name) {
		String returnValue = name.substring(0, 1).toUpperCase();
		if (name.length() > 1)
			returnValue += name.substring(1);
		return returnValue;
	}

	public static void queryParams(Object entity, Map<String, Object> params) throws UncheckedException {
		try {
			List<Field> fields = MethodReflection.getFields(entity.getClass(), Object.class);
			for (Field field : fields) {
				if (params.get(field.getName()) != null) {
					executeMethod(entity, setMethod(field.getName()),
							DynamicFoundType.getTypeValue(field, params.get(field.getName())));
				}
			}
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	public static Object[] getMethodArgs(Class<?> clazz, String methodName, Map<String, Object> param) throws UncheckedException {
		try {
			Method targetMethod = findMatchingMethod(clazz, methodName, param);
			if (targetMethod != null) {
				return buildMethodArgs(targetMethod.getParameterTypes(), param);
			}
			return new Object[0];
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	private static Method findMatchingMethod(Class<?> clazz, String methodName, Map<String, Object> param) throws UncheckedException {
		try {
			for (Method method : clazz.getDeclaredMethods()) {
				if (method.getName().equals(methodName) && hasMatchingParameters(method, param)) {
					return method;
				}
			}
			return null;

		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	private static boolean hasMatchingParameters(Method method, Map<String, Object> param) throws UncheckedException {
		try {
			Class<?>[] methodParameterTypes = method.getParameterTypes();
			if (param.size() != methodParameterTypes.length) {
				return false;
			}

			int index = 0;
			for (Object value : param.values()) {
				if (!isCompatibleType(methodParameterTypes[index++], value)) {
					return false;
				}
			}
			return true;

		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	private static Object[] buildMethodArgs(Class<?>[] parameterTypes, Map<String, Object> param) throws UncheckedException {
		try {
			Object[] methodArgs = new Object[parameterTypes.length];
			int index = 0;
			for (Object value : param.values()) {
				methodArgs[index] = DynamicFoundType.getTypeValue(parameterTypes[index], value);
				index++;
			}
			return methodArgs;
		} catch (Exception ex) {
			throw new UncheckedException(ex.getMessage(), ex);
		}
	}

	private static boolean isCompatibleType(Class<?> paramType, Object value) {

		if (paramType.isInstance(value)) {
			return true;
		}

		if (paramType.isPrimitive()) {
			return isPrimitiveMatch(paramType, value);
		}
		
		if (isCompatibleTypeInteger(paramType, value)) {
			return true;
		}

		if (isCompatibleTypeLocalDateTime(paramType, value)) {
			return true;
		}
		
		if (isCompatibleTypeLong(paramType, value)) {
			return true;
		}
		
		if (isCompatibleTypeDouble(paramType, value)) {
			return true;
		}
		
		if (isCompatibleTypeoBigDecimal(paramType, value)) {
			return true;
		}
		
		if (isCompatibleTypeoBigInteger(paramType, value)) {
			return true;
		}
		
		if (isCompatibleTypeFloat(paramType, value)) {
			return true;
		}
		
		if (isCompatibleTypeShort(paramType, value)) {
			return true;
		}
		
		if (isCompatibleTypeBoolean(paramType, value)) {
			return true;
		}
		
		return isCompatibleTypeByte(paramType, value);
	}
	
	public static boolean isCompatibleTypeByte(Class<?> paramType, Object value) {
		if (paramType == Byte.class && value instanceof String string) {
			try {
				Byte.parseByte(string);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
	
	public static boolean isCompatibleTypeoBigInteger(Class<?> paramType, Object value) {
		if (paramType == BigInteger.class && value instanceof String string) {
			try {
				new BigInteger(string);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
	
	public static boolean isCompatibleTypeoBigDecimal(Class<?> paramType, Object value) {
		if (paramType == BigDecimal.class && value instanceof String string) {
			try {
				new BigDecimal(string);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
	
	public static boolean isCompatibleTypeBoolean(Class<?> paramType, Object value) {
		if (paramType == Boolean.class && value instanceof String string) {
			try {
				Boolean.parseBoolean(string);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	public static boolean isCompatibleTypeDouble(Class<?> paramType, Object value) {
		if (paramType == Double.class && value instanceof String string) {
			try {
				Double.parseDouble(string);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
	
	public static boolean isCompatibleTypeShort(Class<?> paramType, Object value) {
		if (paramType == Short.class && value instanceof String string) {
			try {
				Short.parseShort(string);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
	
	public static boolean isCompatibleTypeFloat(Class<?> paramType, Object value) {
		if (paramType == Float.class && value instanceof String string) {
			try {
				Float.parseFloat(string);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	public static boolean isCompatibleTypeLong(Class<?> paramType, Object value) {
		if (paramType == Long.class && value instanceof String string) {
			try {
				Long.parseLong(string);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	public static boolean isCompatibleTypeInteger(Class<?> paramType, Object value) {
		if (paramType == Integer.class && value instanceof String string) {
			try {
				Integer.parseInt(string);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	public static boolean isCompatibleTypeLocalDateTime(Class<?> paramType, Object value) {
		if (paramType == LocalDateTime.class && value instanceof String string) {
			try {
				LocalDateTime.parse(string, DateTimeFormatter.ISO_DATE_TIME);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
	
    public static Class<?> getAttributeType(String classPath, String attributeName, boolean notFound) {
        try {
            Class<?> clazz = Class.forName(classPath);
            Field field = clazz.getDeclaredField(attributeName);
            return field.getType();
        } catch (ClassNotFoundException e) {
            throw new UncheckedException("Classe não encontrada: " + classPath);
        } catch (NoSuchFieldException e) {
        	if(notFound) {
        		return String.class;
        	}
            throw new UncheckedException("Atributo não encontrado: " + attributeName);
        } catch (Exception e) {
            throw new UncheckedException("Erro inesperado ao obter o tipo do atributo.", e);
        }
    }

	private static boolean isPrimitiveMatch(Class<?> paramType, Object value) {

		Map<Class<?>, Class<?>> primitiveToWrapper = Map.of(int.class, Integer.class, long.class, Long.class,
				double.class, Double.class, float.class, Float.class, boolean.class, Boolean.class, char.class,
				Character.class, byte.class, Byte.class, short.class, Short.class);

		return paramType.isPrimitive() && primitiveToWrapper.get(paramType).isInstance(value);
	}
}
