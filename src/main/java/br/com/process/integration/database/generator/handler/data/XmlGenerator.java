package br.com.process.integration.database.generator.handler.data;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.List;

public class XmlGenerator {

	private static final String TARGET_DIR = "src/main/resources/mapper";
	
	private static final String VERSION_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
	
	private static final String DOCTYPE = "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n";

	public void run(List<Class<?>> dataClasses, String domain) throws Exception {
		for (Class<?> dataClass : dataClasses) {
			generateXmlForClass(dataClass, domain);
		}
	}

	private void generateXmlForClass(Class<?> clazz, String domain) throws Exception {
		String className = clazz.getSimpleName();
		String baseName = className.replace("Data", "");
		String mapperName = baseName + "DataMapper";
		String namespace = clazz.getPackageName().replace(".dto", ".mapper") + "." + mapperName;
		String tableName = camelToSnake(baseName);

		File dir = new File(TARGET_DIR + File.separator + domain);
		if (!dir.exists())
			dir.mkdirs();

		File file = new File(dir, mapperName + ".xml");

		try (FileWriter writer = new FileWriter(file)) {
			writer.write(VERSION_XML);
			writer.write(DOCTYPE);
			writer.write("<mapper namespace=\"" + namespace + "\">\n\n");

			// ResultMap
			writer.write("    <resultMap id=\"defaultResultMap\" type=\"" + clazz.getName() + "\">\n");
			for (Field field : clazz.getDeclaredFields()) {
				Class<?> fieldType = field.getType();
				String propertyName = field.getName();

				if (isSimple(fieldType)) {
					String columnName = camelToSnake(propertyName);
					writer.write("        <result column=\"" + columnName + "\" property=\"" + propertyName + "\"/>\n");
				} else if (fieldType.getSimpleName().endsWith("Data")) {
					String nestedMap = fieldType.getSimpleName().replace("Data", "") + "Map";
					writer.write("        <association property=\"" + propertyName + "\" javaType=\""
							+ fieldType.getName() + "\" resultMap=\"" + nestedMap + "\" />\n");
				}
			}
			writer.write("    </resultMap>\n\n");

			// Nested resultMaps
			for (Field field : clazz.getDeclaredFields()) {
				Class<?> fieldType = field.getType();
				if (!isSimple(fieldType) && fieldType.getSimpleName().endsWith("Data")) {
					String nestedMap = fieldType.getSimpleName().replace("Data", "") + "Map";
					writer.write("    <resultMap id=\"" + nestedMap + "\" type=\"" + fieldType.getName() + "\">\n");
					for (Field nestedField : fieldType.getDeclaredFields()) {
						if (isSimple(nestedField.getType())) {
							String nestedName = nestedField.getName();
							String columnName = camelToSnake(nestedName);
							writer.write("        <result column=\"" + columnName + "\" property=\"" + nestedName
									+ "\"/>\n");
						}
					}
					writer.write("    </resultMap>\n\n");
				}
			}

			// SELECT: findById
			writer.write(
					"    <select id=\"findById\" resultMap=\"defaultResultMap\" parameterType=\"java.lang.Long\">\n");
			writer.write("        SELECT * FROM " + tableName + " WHERE id = #{id}\n");
			writer.write("    </select>\n\n");

			// SELECT: findAll
			writer.write("    <select id=\"findAll\" resultMap=\"defaultResultMap\">\n");
			writer.write("        SELECT * FROM " + tableName + "\n");
			writer.write("    </select>\n\n");

			writer.write("</mapper>\n");
		}
	}

	private boolean isSimple(Class<?> type) {
		return type.isPrimitive() || type == String.class || type == Integer.class || type == Long.class
				|| type == Double.class || type == Float.class || type == Boolean.class
				|| type == java.time.LocalDate.class || type == java.time.LocalDateTime.class
				|| type == java.util.UUID.class;
	}

	private String camelToSnake(String str) {
		return str.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
	}
}