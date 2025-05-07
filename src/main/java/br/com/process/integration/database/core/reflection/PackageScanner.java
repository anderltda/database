package br.com.process.integration.database.core.reflection;

import java.util.HashSet;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ClassUtils;

import br.com.process.integration.database.core.exception.CheckedException;

/**
 * PACKAGE SCANNER
 */
public class PackageScanner {

	/**
	 * @param basePackage
	 * @param className
	 * @return
	 * @throws CheckedException
	 */
	public static Object findClassBySimpleName(String basePackage, String className) throws CheckedException {
		try {
			Class<?> clazz = findAllClasses(basePackage).stream().filter(c -> c.getSimpleName().equalsIgnoreCase(className)).findFirst().orElse(null);
			if(clazz != null) {
				return clazz.getDeclaredConstructor().newInstance();
			}
			throw new CheckedException(String.format("Class not found %s !", className));
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	/**
	 * @param basePackage
	 * @param className
	 * @return
	 * @throws Exception
	 */
	public static String findClassPackageByName(String basePackage, String className) throws Exception {
		Set<Class<?>> classes = findAllClasses(basePackage);
		for (Class<?> clazz : classes) {
			if (clazz.getSimpleName().equalsIgnoreCase(className)) {
				return clazz.getPackageName();
			}
		}
		return null;
	}

	/**
	 * Lista todas as classes em tempo de execução dentro de um pacote.
	 *
	 * @param basePackage Ex: "br.com.enginer.domain"
	 * @return Set<Class<?>> com todas as classes encontradas
	 * @throws Exception
	 */
	private static Set<Class<?>> findAllClasses(String basePackage) throws Exception {
		Set<Class<?>> classes = new HashSet<>();
		try {
			String className = null;
			String path = ClassUtils.convertClassNameToResourcePath(basePackage);
			String classPattern = "classpath*:" + path + "/**/*.class";

			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resolver.getResources(classPattern);

			for (Resource resource : resources) {
				String resourcePath = resource.getURL().toString();
				if (resourcePath.contains("/BOOT-INF/classes/")) {
					className = resourcePath
							.substring(resourcePath.indexOf("/BOOT-INF/classes/") + 18)
							.replace("/", ".")
							.replace(".class", "");
				} else if (resourcePath.contains("/classes/")) {
					className = resourcePath
							.substring(resourcePath.indexOf("/classes/") + 18)
							.replace("/", ".")
							.replace(".class", "");
				}
				Class<?> clazz = Class.forName(className);
				classes.add(clazz);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return classes;
	}
}
