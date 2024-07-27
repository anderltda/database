package br.com.process.integration.database.core.reflection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import br.com.process.integration.database.core.exception.ServiceException;
import br.com.process.integration.database.core.util.Constants;
import br.com.process.integration.database.core.util.DynamicTypeConverter;
import jakarta.persistence.JoinColumn;


@Service
public class MethodReflection {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodReflection.class);

	public static void transformsJsonModel(JsonNode jsonNode, Object object) throws ServiceException {

		try {
			
			List<Field> fields = MethodReflection.getFields(object.getClass(), Object.class);

			for (Field field : fields) {

				Object object_ = null;

				if (jsonNode.has(field.getName())) {

					if (field.getAnnotation(JoinColumn.class) instanceof JoinColumn) {

						object_ = identificarClasse(field.getType().getName());

						transformsJsonModel(jsonNode.get(field.getName()), object_);

						executeMethod(object, setMethod(field.getName()), object_);
						
					} else {

						executeMethod(object, setMethod(field.getName()), DynamicTypeConverter.convert(field, jsonNode));
					}

				}
			}
			
		} catch (Exception ex) {
			LOGGER.error("Ocorreu um erro ao transformar o objeto (jsonNode) em Entity! classe: {} - metodo: {}", "MethodReflection", "transformsJsonModel");
			throw new ServiceException("Ocorreu um erro ao transformar o objeto (jsonNode) em Entity!", ex);
		}
		
	}
	
	public static Class<?> getTypeById(Object object) throws ServiceException {

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

	public static List<Object> transformsJsonIds(JsonNode jsonNode, Object object) throws ServiceException {

		List<Object> list = new ArrayList<Object>();
		
		try {
			
			for (Iterator<JsonNode> iterator = jsonNode.elements(); iterator.hasNext();) {
				list.add(iterator.next().get("id").asText());
			}
						
		} catch (Exception ex) {
			LOGGER.error("Ocorreu um erro ao transformar o objeto (jsonNode) em IDs! classe: {} - metodo: {}", "MethodReflection", "transformsJsonIds");
			throw new ServiceException("Ocorreu um erro ao transformar o objeto (jsonNode) em IDs!", ex);
		}

		return list;
	}

	public static String getNameRepository(String class_) {
		String repository = class_.substring(0, 1).toLowerCase() + class_.substring(1) + "Repository";
		return repository;
	}

	public static String getNameService(String class_) {
		String repository = class_.substring(0, 1).toLowerCase() + class_.substring(1) + "Service";
		return repository;
	}

	public static Object identificarClasse(String instance) {
		try {
			ClassLoader classLoader = MethodReflection.class.getClassLoader();
			Class<?> main = classLoader.loadClass(instance);
			return main.getDeclaredConstructor().newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object executeMethod(Object object, String methodName, Object... paramValue) throws ServiceException {
		Class[] paramTypes = MethodReflection.transformParametersTypes(paramValue);
		Method method = getMethod(object.getClass(), methodName, paramTypes);
		if (method != null) {
			try {
				return method.invoke(object, paramValue);
			} catch (Exception ex) {
				throw new ServiceException(ex);
			}
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
		ArrayList<Field> ret = new ArrayList<Field>();
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
			paramTypes[i] = params[i].getClass();
		}

		return paramTypes;
	}
	
	
	public static Object findDtoUsingClassLoader(String class_) throws ServiceException {
		String packagePath = (Constants.DIRECTORY_APPLICATION + Constants.PACKAGE_NAME_MODEL).replaceAll("[.]", "/");
		return findClassUsingClassLoader(class_, packagePath);
	}
	
	public static Object findEntityUsingClassLoader(String class_) throws ServiceException {
		String packagePath = (Constants.DIRECTORY_APPLICATION + Constants.PACKAGE_NAME_ENTITY).replaceAll("[.]", "/");
		return findClassUsingClassLoader(class_, packagePath);
	}
	

	@SuppressWarnings("resource")
	private static Object findClassUsingClassLoader(String class_, String packagePath) throws ServiceException {

		try {
			
			boolean notFound = true;

			List<Path> dirs = Files.walk(Paths.get(packagePath), 1).filter(Files::isDirectory)
					.collect(Collectors.toList());

			for (Path path : dirs) {

				String packageName = (path.toString().replaceAll("[/]", ".").replace(Constants.DIRECTORY_APPLICATION, ""));

				InputStream stream = ClassLoader.getSystemClassLoader()
						.getResourceAsStream(packageName.replaceAll("[.]", "/"));

				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

				Set<Class<?>> values = reader.lines().filter(line -> line.endsWith(".class"))
						.map(line -> getClass(line, packageName)).collect(Collectors.toSet());

				for (Class<?> classFind : values) {
					if (classFind.getSimpleName().equals(class_)) {
						notFound = false;
						return classFind.getDeclaredConstructor().newInstance();
					}
				}
			}
			
			if(notFound) {
				throw new ServiceException("Entity ou Model nao encontrada, verifique o nome correto da entity ou do model!!");
			}
			
		} catch (Exception ex) {
			LOGGER.error("Erro: {} classe: {} - metodo: {}", ex.getMessage(), "MethodReflection", "findClassUsingClassLoader");
			throw new ServiceException(ex.getMessage(), ex);
		}

		return null;
	}

	private static Class<?> getClass(String className, String packageName) {
		try {
			return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
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
	 * @throws ServiceException
	 */
	public static void queryParams(Object entity, Map<String, Object> params) throws ServiceException {
		try {
			List<Field> fields = MethodReflection.getFields(entity.getClass(), Object.class);
			for (Field field : fields) {
				if (params.get(field.getName()) != null) {
					executeMethod(entity, setMethod(field.getName()), DynamicTypeConverter.convert(field, params.get(field.getName())));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error("Ocorreu um erro ao criar a entity no momento de processar os parametros (params)! classe: {} - metodo: {}", "MethodReflection", "queryParams");
			throw new ServiceException("Ocorreu um erro ao criar a entity no momento de processar os parametros (params)!", ex);
		}
	}
	
	public static Object[] getMethodArgs(Class<?> clazz, String methodName, Map<String, Object> param) throws ServiceException {

		Object[] methodArgs = null;
		
		try {
			
			StringBuilder log = new StringBuilder();
			Method[] methods = clazz.getDeclaredMethods();
			Method targetMethod = null;

			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					targetMethod = method;
					break;
				}
			}

			if (targetMethod == null) {
				throw new ServiceException("Método não encontrado: " + methodName);
			}

			Class<?>[] parameterTypes = targetMethod.getParameterTypes();
			
			if(param.size() != parameterTypes.length) {
				throw new ServiceException("Número de argumentos(param) fornecidos não correspondem ao número de parametros do método: " + methodName);
			}

			methodArgs = new Object[parameterTypes.length];
			int index = 0;

			for (Entry<String, Object> entry : param.entrySet()) {
				Object value = entry.getValue();
				Class<?> class1 = parameterTypes[index];
				log.append(parameterTypes[index].getSimpleName());
				log.append("(");
				log.append(value);
				log.append(")");
				log.append(", ");
				methodArgs[index++] = DynamicTypeConverter.convert(class1, value);
			}
			
			LOGGER.info("Sequencia dos argumentos: {}", log.toString());

		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new ServiceException(ex);
		}
		
		return methodArgs;

	}
}
