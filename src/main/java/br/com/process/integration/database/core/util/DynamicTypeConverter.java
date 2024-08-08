package br.com.process.integration.database.core.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.databind.JsonNode;

import br.com.process.integration.database.core.domain.Entity;
import br.com.process.integration.database.core.reflection.MethodReflection;

public class DynamicTypeConverter {

	private DynamicTypeConverter() {}

	public static Object convert(Field field, JsonNode jsonNode) {

		Object object = jsonNode.get(field.getName()).asText();

		boolean integerFound = field.getType().equals(Integer.class);
		boolean doubleFound = field.getType().equals(Double.class);
		boolean floatFound = field.getType().equals(Float.class);
		boolean shortFound = field.getType().equals(Short.class);
		boolean longFound = field.getType().equals(Long.class);
		boolean bigDecimalFound = field.getType().equals(BigDecimal.class);
		boolean bigIntegerFound = field.getType().equals(BigInteger.class);
		boolean booleanFound = field.getType().equals(Boolean.class);
		boolean localDateFound = field.getType().equals(LocalDate.class);
		boolean localDateTimeFound = field.getType().equals(LocalDateTime.class);
		boolean arrayFound = field.getType().equals(String[].class);

		if (integerFound) {
			object = jsonNode.get(field.getName()).asInt();
		} else if (doubleFound) {
			object = jsonNode.get(field.getName()).asDouble();
		} else if (floatFound) {
			object = jsonNode.get(field.getName()).floatValue();
		} else if (shortFound) {
			object = jsonNode.get(field.getName()).shortValue();
		} else if (longFound) {
			object = jsonNode.get(field.getName()).longValue();
		} else if (bigDecimalFound) {
			object = jsonNode.get(field.getName()).decimalValue();
		} else if (bigIntegerFound) {
			object = jsonNode.get(field.getName()).bigIntegerValue();
		} else if (booleanFound) {
			object = jsonNode.get(field.getName()).asBoolean();
		} else if (arrayFound) {
			String objectFound = jsonNode.get(field.getName()).toString().replace("[", "").replace("]", "").replace("\"", "");
			object = objectFound.split(",");
		} else if (localDateFound) {
			LocalDate date = LocalDate.parse(object.toString(), DateTimeFormatter.ISO_LOCAL_DATE);
			object = date;
		} else if (localDateTimeFound) {
			LocalDateTime date = LocalDateTime.parse(object.toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
			object = date;
		}

		return object;
	}

	public static Object convert(Entity<?> model, String value) {

		Class<?> object = MethodReflection.getTypeById(model);

		if (object.getTypeName().equals(Integer.class.getName())) {
			return Integer.parseInt(value);
		} else if (object.getTypeName().equals(Long.class.getName())) {
			return Long.parseLong(value);
		} else if (object.getTypeName().equals(Short.class.getName())) {
			return Short.parseShort(value);
		} else if (object.getTypeName().equals(Double.class.getName())) {
			return Double.parseDouble(value);
		} else if (object.getTypeName().equals(Float.class.getName())) {
			return Float.parseFloat(value);
		} else if (object.getTypeName().equals(Byte.class.getName())) {
			return Byte.parseByte(value);
		} else {
			return value;
		}

	}

	public static Object convert(Field field, Object value) {

		if (field.getType().getClass().isAssignableFrom(Integer.class)) {
			return Integer.parseInt(value.toString());
		} else if (field.getType().getClass().isAssignableFrom(Long.class)) {
			return Long.parseLong(value.toString());
		} else if (field.getType().getClass().isAssignableFrom(Short.class)) {
			return Short.parseShort(value.toString());
		} else if (field.getType().getClass().isAssignableFrom(Double.class)) {
			return Double.parseDouble(value.toString());
		} else if (field.getType().getClass().isAssignableFrom(Float.class)) {
			return Float.parseFloat(value.toString());
		} else if (field.getType().getClass().isAssignableFrom(Byte.class)) {
			return Byte.parseByte(value.toString());
		} else if (field.getType().getClass().isAssignableFrom(PageRequest.class)) {
			return value;
		} else if (field.getType().getClass().isAssignableFrom(LocalDate.class)) {
			return LocalDate.parse(value.toString(), DateTimeFormatter.ISO_LOCAL_DATE);
		} else if (field.getType().getClass().isAssignableFrom(LocalDateTime.class)) {
			return LocalDateTime.parse(value.toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		} else {
			return value.toString();
		}

	}

	public static Object convert(Class<?> clazz, Object value) {

		if (clazz.getTypeName().equals(Integer.class.getName())) {
			return Integer.parseInt(value.toString());
		} else if (clazz.getTypeName().equals(Long.class.getName())) {
			return Long.parseLong(value.toString());
		} else if (clazz.getTypeName().equals(Short.class.getName())) {
			return Short.parseShort(value.toString());
		} else if (clazz.getTypeName().equals(Double.class.getName())) {
			return Double.parseDouble(value.toString());
		} else if (clazz.getTypeName().equals(Float.class.getName())) {
			return Float.parseFloat(value.toString());
		} else if (clazz.getTypeName().equals(Byte.class.getName())) {
			return Byte.parseByte(value.toString());
		} else if (clazz.getTypeName().equals(PageRequest.class.getName())) {
			return value;			
		} else if (clazz.getTypeName().equals(Sort.class.getName())) {
			return value;
		} else if (clazz.getTypeName().equals(LocalDate.class.getName())) {
			return LocalDate.parse(value.toString(), DateTimeFormatter.ISO_LOCAL_DATE);
		} else if (clazz.getTypeName().equals(LocalDateTime.class.getName())) {
			return LocalDateTime.parse(value.toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		} else {
			return value.toString();
		}

	}

	public static Object convert(Object value) {

		try {
			return Double.parseDouble(value.toString());
		} catch (Exception doubleEx) {
			try {
				return Integer.parseInt(value.toString());
			} catch (Exception ix) {
				try {
					return LocalDate.parse(value.toString(), DateTimeFormatter.ISO_LOCAL_DATE);
				} catch (Exception localDateEx) {
					try {
						return LocalDateTime.parse(value.toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
					} catch (Exception localDateTimeEx) {
						return value;
					}
				}
			}
		}
	}
}
