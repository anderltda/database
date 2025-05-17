package br.com.process.integration.database.generator.handler.data;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.process.integration.database.generator.util.StringUtils;

/**
 * 
 */
public class XmlGenerator {

    private static final String TARGET_DIR = "src/main/resources/mapper";
    private static final String VERSION_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
    private static final String DOCTYPE = "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n";

    private Map<String, List<String>> mapPrimaryKeys;
    private Map<String, String> mapTables;

    /**
     * @param mapPrimaryKeys
     * @param mapTables
     */
    public XmlGenerator(Map<String, List<String>> mapPrimaryKeys, Map<String, String> mapTables) {
        this.mapPrimaryKeys = mapPrimaryKeys;
        this.mapTables = mapTables;
    }

    /**
     * @param dataClasses
     * @param domain
     * @throws Exception
     */
    public void run(List<Class<?>> dataClasses, String domain) throws Exception {
        for (Class<?> dataClass : dataClasses) {
            generateXmlForClass(dataClass, domain);
        }
    }

    /**
     * @param clazz
     * @param domain
     * @throws Exception
     */
    private void generateXmlForClass(Class<?> clazz, String domain) throws Exception {
        String className = clazz.getSimpleName();
        String baseName = className.replace("Data", "");
        String mapperName = baseName + "DataMapper";
        String namespace = clazz.getPackageName().replace(".dto", ".mapper") + "." + mapperName;
        String tableName = mapTables.get(baseName);

        if (className.endsWith("Id")) return;

        File dir = new File(TARGET_DIR + File.separator + domain);
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, mapperName + ".xml");

        Set<String> generatedMaps = new HashSet<>();
        StringBuilder resultMapBuffer = new StringBuilder();
        StringBuilder nestedMapsBuffer = new StringBuilder();
        List<Class<?>> nestedTypesToGenerate = new ArrayList<>();

        try (FileWriter writer = new FileWriter(file)) {
        	
            writer.write(VERSION_XML);
            writer.write(DOCTYPE);
            writer.write("<mapper namespace=\"" + namespace + "\">\n\n");

            resultMapBuffer.append("    <resultMap id=\"defaultResultMap\" type=\"" + clazz.getName() + "\">\n");
            
            for (Field field : clazz.getDeclaredFields()) {
            	
                Class<?> fieldType = field.getType();
                String propertyName = field.getName();

                if (propertyName.equals("id") && fieldType.getSimpleName().endsWith("Id")) {
                	
                    for (Field idField : fieldType.getDeclaredFields()) {
                        resultMapBuffer.append("        <result column=\"" + StringUtils.camelToSnake(idField.getName()) + "\" property=\"id." + idField.getName() + "\" />\n");
                    }
                    
                } else if (propertyName.equals("id") && isSimple(fieldType)) {
                	
                    resultMapBuffer.append("        <id     column=\"" + getColumnPrimaryKey(baseName) + "\" property=\"" + propertyName + "\"/>\n");
                    
                } else if (isSimple(fieldType)) {
                	
                    resultMapBuffer.append("        <result column=\"" + StringUtils.camelToSnake(propertyName) + "\" property=\"" + propertyName + "\"/>\n");
                    
                } else if (fieldType.getSimpleName().endsWith("Data")) {
                	
                    String nestedMap = fieldType.getSimpleName().replace("Data", "") + "Map";
                    resultMapBuffer.append("        <association property=\"" + propertyName + "\" javaType=\"" + fieldType.getName() + "\" resultMap=\"" + nestedMap + "\" />\n");
                    nestedTypesToGenerate.add(fieldType);
                }
            }
            
            resultMapBuffer.append("    </resultMap>\n\n");

            writer.write(resultMapBuffer.toString());

            for (Class<?> nested : nestedTypesToGenerate) {
                generateNestedResultMap(nestedMapsBuffer, nested, generatedMaps);
            }
            
            writer.write(nestedMapsBuffer.toString());

			Class<?> type = null;
			String fieldName = null;
			
			for (Field field : clazz.getDeclaredFields()) {
				if (field.getName().equals("id")) {
					if (isSimple(field.getType())) {
						type = field.getType();
						fieldName = field.getName();
						break;
					} else if(field.getType().getSimpleName().endsWith("Id")){
						type = field.getType();
						break;
					}
				}
			}

            writer.write("    <select id=\"findById\" resultMap=\"defaultResultMap\" parameterType=\"" + type.getName() + "\">\n");
            writer.write("        SELECT * FROM " + tableName + " WHERE ");
            
            if (isSimple(type)) {
            	
                writer.write(getColumnPrimaryKey(baseName) + " = #{" + fieldName + "}\n");
                
            } else {
            	
                Field[] idFields = Class.forName(type.getName()).getDeclaredFields();
                
                for (int i = 0; i < idFields.length; i++) {
                    Field f = idFields[i];
                    writer.write(StringUtils.camelToSnake(f.getName()) + " = #{id." + f.getName() + "}");
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
     * @param buffer
     * @param type
     * @param generatedMaps
     * @throws Exception
     */
    private void generateNestedResultMap(StringBuilder buffer, Class<?> type, Set<String> generatedMaps) throws Exception {
        String mapId = type.getSimpleName().replace("Data", "") + "Map";
        if (generatedMaps.contains(mapId)) return;
        generatedMaps.add(mapId);

        buffer.append("    <resultMap id=\"" + mapId + "\" type=\"" + type.getName() + "\">\n");
        List<Class<?>> innerNestedTypes = new ArrayList<>();

        String baseName = type.getSimpleName().replace("Data", "");

        for (Field field : type.getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            String propertyName = field.getName();

            if (propertyName.equals("id") && fieldType.getSimpleName().endsWith("Id")) {
            	
                for (Field idField : fieldType.getDeclaredFields()) {
                    buffer.append("        <result column=\"" + StringUtils.camelToSnake(idField.getName()) + "\" property=\"id." + idField.getName() + "\" />\n");
                }
                
            } else if (propertyName.equals("id") && isSimple(fieldType)) {
            	
                buffer.append("        <id     column=\"" + getColumnPrimaryKey(baseName) + "\" property=\"" + propertyName + "\"/>\n");
                
            } else if (isSimple(fieldType)) {
            	
                buffer.append("        <result column=\"" + StringUtils.camelToSnake(propertyName) + "\" property=\"" + propertyName + "\"/>\n");
                
            } else if (fieldType.getSimpleName().endsWith("Data")) {
            	
                String nestedMap = fieldType.getSimpleName().replace("Data", "") + "Map";
                buffer.append("        <association property=\"" + propertyName + "\" javaType=\"" + fieldType.getName() + "\" resultMap=\"" + nestedMap + "\" />\n");
                innerNestedTypes.add(fieldType);
                
            }
        }
        buffer.append("    </resultMap>\n\n");

        for (Class<?> inner : innerNestedTypes) {
            generateNestedResultMap(buffer, inner, generatedMaps);
        }
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
     * @param baseName
     * @return
     */
    private String getColumnPrimaryKey(String baseName) {
        List<String> primaryKeys = mapPrimaryKeys.get(baseName);
        if (primaryKeys == null || primaryKeys.isEmpty()) return null;
        return primaryKeys.getFirst();
    }
}
