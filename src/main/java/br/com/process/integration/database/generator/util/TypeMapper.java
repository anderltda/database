package br.com.process.integration.database.generator.util;

public class TypeMapper {
	public static String toJavaType(String sqlTypeName) {
		return switch (sqlTypeName.toUpperCase()) {
		case "INT", "INTEGER", "INT4" -> "Integer";
		case "BIGINT", "BIGSERIAL", "SMALLINT", "SMALLSERIAL", "SERIAL" -> "Long";
		case "BOOLEAN", "BIT", "BOOL" -> "Boolean";
		case "DATE" -> "java.time.LocalDate";
		case "TIMESTAMP", "DATETIME" -> "java.time.LocalDateTime";
		case "DOUBLE", "BIGDECIMAL", "DECIMAL", "NUMERIC", "FLOAT8" -> "Double";
		case "FLOAT" -> "Float";
		case "JSON", "JSONB", "VARCHAR", "TEXT", "CHAR" -> "String";
		case "UUID" -> "UUID";
		default -> "String";
		};
	}
}
