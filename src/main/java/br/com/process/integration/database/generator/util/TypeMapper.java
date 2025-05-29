package br.com.process.integration.database.generator.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.squareup.javapoet.ClassName;

/**
 * 
 */
public class TypeMapper {
	
	/**
	 * De -> Para: Identifica o tipo no banco e retorna o tipo utilizado em Java
	 * @param sqlTypeName - Tipo de como está no banco
	 * @return
	 */
	public static String toJavaType(String sqlTypeName) {
		return switch (sqlTypeName.toUpperCase()) {
		case "INT", "INTEGER", "INT4" -> "Integer";
		case "BIGINT", "BIGSERIAL", "SMALLINT", "INT8", "SMALLSERIAL", "SERIAL" -> "Long";
		case "BOOLEAN", "BIT", "BOOL" -> "Boolean";
		case "DATE" -> "java.time.LocalDate";
		case "TIMESTAMP", "DATETIME" -> "java.time.LocalDateTime";
		case "BIGDECIMAL", "DECIMAL", "NUMERIC", "FLOAT8" -> "Double";
		case "DOUBLE PRECISION", "DOUBLE" -> "Double";
		case "REAL", "FLOAT" -> "Float";
		case "JSON", "JSONB", "VARCHAR", "TEXT", "CHAR" -> "String";
		case "UUID" -> "UUID";
		default -> "String";
		};
	}

	/**
	 * Converte o nome da classe e, ClassName
	 * @param javaTypeName - Nome da classe
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
	        case "UUID" -> ClassName.get(UUID.class);
	        case "BigDecimal" -> ClassName.get(BigDecimal.class);
	        case "java.time.LocalDate" -> ClassName.get(LocalDate.class);
	        case "java.time.LocalDateTime" -> ClassName.get(LocalDateTime.class);
	        default -> ClassName.get(Object.class);
	    };
	}
}
