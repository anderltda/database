package br.com.process.integration.database.generator.util;

import com.squareup.javapoet.ClassName;

/**
 * 
 */
public class TypeMapper {
	
	/**
	 * @param sqlTypeName
	 * @return
	 */
	public static String toJavaType(String sqlTypeName) {
		return switch (sqlTypeName.toUpperCase()) {
		case "INT", "INTEGER", "INT4" -> "Integer";
		case "BIGINT", "BIGSERIAL", "SMALLINT", "INT8", "SMALLSERIAL", "SERIAL" -> "Long";
		case "BOOLEAN", "BIT", "BOOL" -> "Boolean";
		case "DATE" -> "java.time.LocalDate";
		case "TIMESTAMP", "DATETIME" -> "java.time.LocalDateTime";
		case "DOUBLE PRECISION", "DOUBLE", "BIGDECIMAL", "DECIMAL", "NUMERIC", "FLOAT8" -> "Double";
		case "FLOAT" -> "Float";
		case "JSON", "JSONB", "VARCHAR", "TEXT", "CHAR" -> "String";
		case "UUID" -> "UUID";
		default -> "String";
		};
	}

	/**
	 * @param javaTypeName
	 * @return
	 */
	public static ClassName resolveType(String javaTypeName) {
	    return switch (javaTypeName) {
	        case "Integer" -> ClassName.get(Integer.class);
	        case "Long" -> ClassName.get(Long.class);
	        case "Boolean" -> ClassName.get(Boolean.class);
	        case "Double" -> ClassName.get(Double.class);
	        case "Float" -> ClassName.get(Float.class);
	        case "String" -> ClassName.get(String.class);
	        case "UUID" -> ClassName.get("java.util", "UUID");
	        case "java.time.LocalDate" -> ClassName.get("java.time", "LocalDate");
	        case "java.time.LocalDateTime" -> ClassName.get("java.time", "LocalDateTime");
	        default -> ClassName.get(Object.class);
	    };
	}
}
