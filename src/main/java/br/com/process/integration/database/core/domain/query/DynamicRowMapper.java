package br.com.process.integration.database.core.domain.query;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.jdbc.core.RowMapper;

public class DynamicRowMapper<T> implements RowMapper<T> {

	private final Class<T> mappedClass;

	public DynamicRowMapper(Class<T> mappedClass) {
		this.mappedClass = mappedClass;
	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {

		T instance;

		try {
			instance = mappedClass.getDeclaredConstructor().newInstance();
			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
				String columnName = rs.getMetaData().getColumnLabel(i);
				Object columnValue = rs.getObject(i);
				String fieldName = convertToCamelCase(columnName);
				setFieldValue(instance, fieldName, columnValue);
			}
		} catch (Exception e) {
			throw new SQLException("Failed to map row to " + mappedClass.getName(), e);
		}

		return instance;
	}

	private void setFieldValue(T instance, String fieldName, Object columnValue) {

		try {
			Field field = mappedClass.getDeclaredField(fieldName);
			Object convertedValue = convertToFieldType(field, columnValue);

			String setterName = "set" + capitalize(fieldName);
			Method setter = mappedClass.getMethod(setterName, field.getType());

			setter.invoke(instance, convertedValue);

		} catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			// Campo ou método não encontrado, ignorar
		}
	}

	private String capitalize(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	private String convertToCamelCase(String columnName) {
		StringBuilder result = new StringBuilder();
		boolean toUpperCase = false;

		for (char ch : columnName.toCharArray()) {
			if (ch == '_') {
				toUpperCase = true;
			} else if (toUpperCase) {
				result.append(Character.toUpperCase(ch));
				toUpperCase = false;
			} else {
				result.append(Character.toLowerCase(ch));
			}
		}
		return result.toString();
	}

	private Object convertToFieldType(Field field, Object columnValue) {
		
		Class<?> fieldType = field.getType();
		
		if (columnValue == null) {
			return null;
		}

		if (fieldType.equals(LocalDate.class) && columnValue instanceof java.sql.Date date) {
			return date.toLocalDate();
		} else if (fieldType.equals(LocalTime.class) && columnValue instanceof java.sql.Time time) {
			return time.toLocalTime();
		} else if (fieldType.equals(LocalDateTime.class) && columnValue instanceof java.sql.Timestamp timestamp) {
			return timestamp.toLocalDateTime();
		}
		
		return columnValue;
	}
}