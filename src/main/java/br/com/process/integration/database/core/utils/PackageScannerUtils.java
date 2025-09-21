package br.com.process.integration.database.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ClassUtils;

import br.com.process.integration.database.core.exception.CheckedException;

/**
 * PACKAGE SCANNER
 */
public class PackageScannerUtils {

	/**
	 * @param basePackage
	 * @param className
	 * @return
	 * @throws CheckedException
	 */
	public static Object findClassBySimpleName(String basePackage, String className) throws CheckedException {
		try {
			Class<?> clazz = findByClass(basePackage, className);
			if (clazz != null) {
				return clazz.getDeclaredConstructor().newInstance();
			}
			throw new CheckedException(String.format("Class not found %s !", className));
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	/**
	 * @param basePackage
	 * @param classNameFound
	 * @return
	 * @throws Exception
	 */
	private static Class<?> findByClass(String basePackage, String classNameFound) throws Exception {
		Class<?> clazz = null;
		try {
			String className = null;
			String path = ClassUtils.convertClassNameToResourcePath(basePackage);
			String classPattern = "classpath*:" + path + "/**/**.class";

			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resolver.getResources(classPattern);

			for (Resource resource : resources) {
				String resourcePath = resource.getURL().toString();
				if (resourcePath.contains("/BOOT-INF/classes/")) {
					className = resourcePath.substring(resourcePath.indexOf("/BOOT-INF/classes/") + 18)
							.replace("/", ".").replace(".class", "");
				} else if (resourcePath.contains("/classes/")) {
					className = resourcePath.substring(resourcePath.indexOf("/classes/") + 9).replace("/", ".")
							.replace(".class", "");
				}
				
				clazz = Class.forName(sanitizeDirectoryPath(className));

				if (clazz.getSimpleName().equals(StringsUtils.firstUpper(classNameFound))) {
					clazz = Class.forName(clazz.getPackageName() + "." + StringsUtils.firstUpper(classNameFound));
					return clazz;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}

		return null;
	}

	/**
	 * @param path
	 * @return
	 */
	private static String sanitizeDirectoryPath(String path) {
		String[] parts = path.split("/");
		// Regex: permite letras, números, underscore e hífen
		String safeRegex = "[^a-zA-Z0-9._-]";
		List<String> sanitizedParts = new ArrayList<>();
		for (String part : parts) {
			// Remove caracteres inválidos de cada "pasta" ou arquivo
			String sanitized = part.replaceAll(safeRegex, "");
			if (!sanitized.isBlank()) {
				sanitizedParts.add(sanitized);
			}
		}
		return String.join("/", sanitizedParts);
	}

}
