package br.com.process.integration.database.core.reflection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import br.com.process.integration.database.core.util.Constants;
import br.com.process.integration.database.core.util.DynamicTypeConverter;
import jakarta.persistence.JoinColumn;

@Service
public class MethodReflection {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodReflection.class);
	
	private MethodReflection() {}
	
	public static void transformsJsonModel(JsonNode jsonNode, Object object) throws Exception {

		List<Field> fields = MethodReflection.getFields(object.getClass(), Object.class);

		for (Field field : fields) {

			Object objectTemp = null;

			if (jsonNode.has(field.getName())) {

				if (field.getAnnotation(JoinColumn.class) instanceof JoinColumn) {

					objectTemp = identificarClasse(field.getType().getName());

					transformsJsonModel(jsonNode.get(field.getName()), objectTemp);

					executeMethod(object, setMethod(field.getName()), objectTemp);

				} else {

					executeMethod(object, setMethod(field.getName()), DynamicTypeConverter.convert(field, jsonNode));
				}

			}
		}

	}
	
	public static Class<?> getTypeById(Object object) {

		List<Field> fields = MethodReflection.getFields(object.getClass(), Object.class);
		
		Class<?> type = null;

		for (Field field : fields) {
			if(field.getName().equals("id")) {
				type = field.getType();
				break;
			}
		}
		
		return type;
	}

	public static List<Object> transformsJsonIds(JsonNode jsonNode) {

		List<Object> list = new ArrayList<>();

		for (Iterator<JsonNode> iterator = jsonNode.elements(); iterator.hasNext();) {
			list.add(iterator.next().get("id").asText());
		}

		return list;
	}

	public static String getNameRepository(String className) {
		return className.substring(0, 1).toLowerCase() + className.substring(1) + "Repository";
	}

	public static String getNameService(String className) {
		return className.substring(0, 1).toLowerCase() + className.substring(1) + "Service";
	}
	
	public static Object findDtoUsingClassLoader(String className) {
		String packagePath = (Constants.DIRECTORY_APPLICATION + Constants.PACKAGE_NAME_VIEW).replaceAll("[.]", "/");
		return findClassUsingClassLoader(className, packagePath);
	}
	
	public static Object findEntityUsingClassLoader(String className) {
		String packagePath = (Constants.DIRECTORY_APPLICATION + Constants.PACKAGE_NAME_ENTITY).replaceAll("[.]", "/");
		return findClassUsingClassLoader(className, packagePath);
	}

	public static Object identificarClasse(String instance) {
		try {
			ClassLoader classLoader = MethodReflection.class.getClassLoader();
			Class<?> main = classLoader.loadClass(instance);
			return main.getDeclaredConstructor().newInstance();
		} catch (Exception ex) {
			LOGGER.error("[identificarClasse]", ex);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object executeMethod(Object object, String methodName, Object... paramValue) {
		try {
			Class[] paramTypes = MethodReflection.transformParametersTypes(paramValue);
			Method method = getMethod(object.getClass(), methodName, paramTypes);
			if (method != null) {
				return method.invoke(object, paramValue);
			}
		} catch (Exception ex) {
			LOGGER.error("[executeMethod]", ex);
		}
		return null;
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

	private static List<Field> getFields(Class<?> clazz, Class<?> classLimit) {
		ArrayList<Field> ret = new ArrayList<>();
		listFields(clazz, classLimit, ret, ".*");
		return ret;
	}

	private static void listFields(Class<?> clazz, Class<?> classLimit, List<Field> ret, String pattern) {
		if (!classLimit.equals(clazz) && clazz != null) {

			Field[] fields = clazz.getDeclaredFields();

			for (Field field : fields) {
				if (!ret.contains(field) && field.getName().matches(pattern)) {
					ret.add(field);
				}
			}
			listFields(clazz.getSuperclass(), classLimit, ret, pattern);
		}
	}

	public static Class<?>[] transformParametersTypes(Object... params) {

		Class<?>[] paramTypes = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			paramTypes[i] = getParameterType(params[i]);
		}

		return paramTypes;
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
	

	@SuppressWarnings("resource")
	private static Object findClassUsingClassLoader(String className, String packagePath) {

		try {
			
			List<Path> dirs = Files.walk(Paths.get(packagePath), 1)
				    .filter(Files::isDirectory)
				    .toList();

			for (Path path : dirs) {

				String packageName = (path.toString().replace("/", ".").replace(Constants.DIRECTORY_APPLICATION, ""));

				InputStream stream = ClassLoader.getSystemClassLoader()
						.getResourceAsStream(packageName.replaceAll("[.]", "/"));

				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

				Set<Class<?>> values = reader.lines().filter(line -> line.endsWith(".class"))
						.map(line -> getClass(line, packageName)).collect(Collectors.toSet());

				for (Class<?> classFind : values) {
					if (classFind.getSimpleName().equals(className)) {
						return classFind.getDeclaredConstructor().newInstance();
					}
				}
			}
			
		} catch (Exception ex) {
			LOGGER.error("[findClassUsingClassLoader]", ex);
		}

		return null;
	}

	private static Class<?> getClass(String className, String packageName) {
		try {
			return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
		} catch (ClassNotFoundException cnfe) {
			LOGGER.error("[getClass]", cnfe);
		}
		return null;
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
		

	/**
	 * Receber os paramentros e popula a entidade de acordo com as keys
	 * @param entity - Entidade a ser populada
	 * @param params - Paramentros recebidos com key e value
	 */
	public static void queryParams(Object entity, Map<String, Object> params) {
		try {
			List<Field> fields = MethodReflection.getFields(entity.getClass(), Object.class);
			for (Field field : fields) {
				if (params.get(field.getName()) != null) {
					executeMethod(entity, setMethod(field.getName()),
							DynamicTypeConverter.convert(field, params.get(field.getName())));
				}
			}
		} catch (Exception ex) {
			LOGGER.error("[queryParams]", ex);
		}
	}
	
	public static Object[] getMethodArgs(Class<?> clazz, String methodName, Map<String, Object> param) {
	    try {
	        Method targetMethod = findMatchingMethod(clazz, methodName, param);
	        if (targetMethod != null) {
	            return buildMethodArgs(targetMethod.getParameterTypes(), param);
	        }
	    } catch (Exception ex) {
	        LOGGER.error("[getMethodArgs]", ex);
	    }
	    return new Object[0];
	}

	private static Method findMatchingMethod(Class<?> clazz, String methodName, Map<String, Object> param) {
	    for (Method method : clazz.getDeclaredMethods()) {
	        if (method.getName().equals(methodName) && hasMatchingParameters(method, param)) {
	            return method;
	        }
	    }
	    return null;
	}

	private static boolean hasMatchingParameters(Method method, Map<String, Object> param) {
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
	}

	private static Object[] buildMethodArgs(Class<?>[] parameterTypes, Map<String, Object> param) {
	    Object[] methodArgs = new Object[parameterTypes.length];
	    int index = 0;
	    for (Object value : param.values()) {
	        methodArgs[index] = DynamicTypeConverter.convert(parameterTypes[index], value);
	        index++;
	    }
	    return methodArgs;
	}
	
	private static boolean isCompatibleType(Class<?> paramType, Object value) {

		if (paramType.isInstance(value)) {
			return true;
		}

		if (paramType.isPrimitive()) {
			return isPrimitiveMatch(paramType, value);
		}
		
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
	
	private static boolean isPrimitiveMatch(Class<?> paramType, Object value) {

		Map<Class<?>, Class<?>> primitiveToWrapper = Map.of(
				int.class, Integer.class, 
				long.class, Long.class,
				double.class, Double.class, 
				float.class, Float.class, 
				boolean.class, Boolean.class, 
				char.class, Character.class, 
				byte.class, Byte.class, 
				short.class, Short.class);

		return paramType.isPrimitive() && primitiveToWrapper.get(paramType).isInstance(value);
	}
	
}
