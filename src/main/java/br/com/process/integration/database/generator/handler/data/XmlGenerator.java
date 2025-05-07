package br.com.process.integration.database.generator.handler.data;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 */
public class XmlGenerator {

    private static final String TARGET_DIR = "src/main/resources/mapper";

    private static final String VERSION_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";

    private static final String DOCTYPE = "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n";

    /**
     * @param dataClasses
     * @param domain
     * @throws Exception
     */
    public void run(List<Class<?>> dataClasses, String domain) throws Exception {
        for (Class<?> dataClass : dataClasses) {
            generateXmlForClass(dataClass, domain, new HashSet<>());
        }
    }

    /**
     * @param clazz
     * @param domain
     * @param generatedMaps
     * @throws Exception
     */
    private void generateXmlForClass(Class<?> clazz, String domain, Set<String> generatedMaps) throws Exception {
        String className = clazz.getSimpleName();
        String baseName = className.replace("Data", "");
        String mapperName = baseName + "DataMapper";
        String namespace = clazz.getPackageName().replace(".dto", ".mapper") + "." + mapperName;
        String tableName = camelToSnake(baseName);

        boolean classeCompostaId = (className.substring(className.length() - 2).endsWith("Id"));
        if (classeCompostaId) return;

        File dir = new File(TARGET_DIR + File.separator + domain);
        if (!dir.exists())
            dir.mkdirs();

        File file = new File(dir, mapperName + ".xml");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(VERSION_XML);
            writer.write(DOCTYPE);
            writer.write("<mapper namespace=\"" + namespace + "\">\n\n");

            writer.write("    <resultMap id=\"defaultResultMap\" type=\"" + clazz.getName() + "\">\n");
            for (Field field : clazz.getDeclaredFields()) {
                Class<?> fieldType = field.getType();
                String propertyName = field.getName();

                if (propertyName.equals("id") && fieldType.getSimpleName().endsWith("Id")) {
                    writer.write("        <association property=\"id\" javaType=\"" + fieldType.getName() + "\">\n");
                    for (Field idField : fieldType.getDeclaredFields()) {
                        writer.write("            <id property=\"" + idField.getName() + "\" column=\"" + camelToSnake(idField.getName()) + "\" />\n");
                    }
                    writer.write("        </association>\n");
                } else if (isSimple(fieldType)) {
                    writer.write("        <result column=\"" + camelToSnake(propertyName) + "\" property=\"" + propertyName + "\"/>\n");
                } else if (fieldType.getSimpleName().endsWith("Data")) {
                    String nestedMap = fieldType.getSimpleName().replace("Data", "") + "Map";
                    writer.write("        <association property=\"" + propertyName + "\" javaType=\""
                            + fieldType.getName() + "\" resultMap=\"" + nestedMap + "\" />\n");
                    // Gera recursivamente os ResultMaps aninhados
                    generateNestedResultMap(writer, fieldType, generatedMaps);
                }
            }
            writer.write("    </resultMap>\n\n");

            String idType = "java.lang.Long";
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getName().equals("id") && field.getType().getSimpleName().endsWith("Id")) {
                    idType = field.getType().getName();
                    break;
                }
            }

            writer.write("    <select id=\"findById\" resultMap=\"defaultResultMap\" parameterType=\"" + idType + "\">\n");
            writer.write("        SELECT * FROM " + tableName + " WHERE ");

            if (idType.equals("java.lang.Long")) {
                writer.write("id = #{id}\n");
            } else {
                Field[] idFields = Class.forName(idType).getDeclaredFields();
                for (int i = 0; i < idFields.length; i++) {
                    Field f = idFields[i];
                    writer.write(camelToSnake(f.getName()) + " = #{id." + f.getName() + "}");
                    if (i < idFields.length - 1) writer.write(" AND ");
                }
                writer.write("\n");
            }
            writer.write("    </select>\n\n");

            writer.write("    <select id=\"findAll\" resultMap=\"defaultResultMap\">\n");
            writer.write("        SELECT * FROM " + tableName + "\n");
            writer.write("    </select>\n\n");

            writer.write("</mapper>\n");
        }
    }

    /**
     * @param writer
     * @param type
     * @param generatedMaps
     * @throws Exception
     */
    private void generateNestedResultMap(FileWriter writer, Class<?> type, Set<String> generatedMaps) throws Exception {
    	
        String mapId = type.getSimpleName().replace("Data", "") + "Map";
        
        if (generatedMaps.contains(mapId)) return;
        
        generatedMaps.add(mapId);

        writer.write("    <resultMap id=\"" + mapId + "\" type=\"" + type.getName() + "\">\n");
        
        for (Field field : type.getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            String propertyName = field.getName();

            if (propertyName.equals("id") && fieldType.getSimpleName().endsWith("Id")) {
                writer.write("        <association property=\"id\" javaType=\"" + fieldType.getName() + "\">\n");
                for (Field idField : fieldType.getDeclaredFields()) {
                    writer.write("            <id property=\"" + idField.getName() + "\" column=\"" + camelToSnake(idField.getName()) + "\" />\n");
                }
                writer.write("        </association>\n");
            } else if (isSimple(fieldType)) {
                writer.write("        <result column=\"" + camelToSnake(propertyName) + "\" property=\"" + propertyName + "\"/>\n");
            } else if (fieldType.getSimpleName().endsWith("Data")) {
                String nestedMap = fieldType.getSimpleName().replace("Data", "") + "Map";
                writer.write("        <association property=\"" + propertyName + "\" javaType=\""
                        + fieldType.getName() + "\" resultMap=\"" + nestedMap + "\" />\n");
                generateNestedResultMap(writer, fieldType, generatedMaps);
            }
        }
        writer.write("    </resultMap>\n\n");
    }

    /**
     * @param type
     * @return
     */
    private boolean isSimple(Class<?> type) {
        return type.isPrimitive() || type == String.class || type == Integer.class || type == Long.class
                || type == Double.class || type == Float.class || type == Boolean.class
                || type == java.time.LocalDate.class || type == java.time.LocalDateTime.class
                || type == java.util.UUID.class;
    }

    /**
     * @param str
     * @return
     */
    private String camelToSnake(String str) {
        return str.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }
} 
