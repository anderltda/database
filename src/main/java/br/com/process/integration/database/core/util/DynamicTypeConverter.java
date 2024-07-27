package br.com.process.integration.database.core.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.JsonNode;

import br.com.process.integration.database.core.domain.Entity;
import br.com.process.integration.database.core.exception.ServiceException;
import br.com.process.integration.database.core.reflection.MethodReflection;

public class DynamicTypeConverter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicTypeConverter.class);

	public static Object convert(Field field, JsonNode jsonNode) throws ServiceException {

		Object object = jsonNode.get(field.getName()).asText();

		boolean integer_ = field.getType().equals(Integer.class);
		boolean double_ = field.getType().equals(Double.class);
		boolean float_ = field.getType().equals(Float.class);
		boolean short_ = field.getType().equals(Short.class);
		boolean long_ = field.getType().equals(Long.class);
		boolean bigDecimal_ = field.getType().equals(BigDecimal.class);
		boolean bigInteger_ = field.getType().equals(BigInteger.class);
		boolean boolean_ = field.getType().equals(Boolean.class);
		boolean localDate_ = field.getType().equals(LocalDate.class);
		boolean localDateTime_ = field.getType().equals(LocalDateTime.class);
		boolean array_ = field.getType().equals(String[].class);

		if (integer_) {
			object = (Integer) jsonNode.get(field.getName()).asInt();
		} else if (double_) {
			object = (Double) jsonNode.get(field.getName()).asDouble();
		} else if (float_) {
			object = (Float) jsonNode.get(field.getName()).floatValue();
		} else if (short_) {
			object = (Short) jsonNode.get(field.getName()).shortValue();
		} else if (long_) {
			object = (Long) jsonNode.get(field.getName()).longValue();
		} else if (bigDecimal_) {
			object = (BigDecimal) jsonNode.get(field.getName()).decimalValue();
		} else if (bigInteger_) {
			object = (BigInteger) jsonNode.get(field.getName()).bigIntegerValue();
		} else if (boolean_) {
			object = (Boolean) jsonNode.get(field.getName()).asBoolean();
		} else if (array_) {
			String object_ = jsonNode.get(field.getName()).toString().replace("[", "").replace("]", "").replace("\"",
					"");
			object = (String[]) object_.split(",");
		} else if (localDate_) {
			LocalDate date = LocalDate.parse(object.toString(), DateTimeFormatter.ISO_LOCAL_DATE);
			object = (LocalDate) date;
		} else if (localDateTime_) {
			LocalDateTime date = LocalDateTime.parse(object.toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
			object = (LocalDateTime) date;
		}

		return object;
	}

	public static Object convert(Entity<?> model, String value) throws ServiceException {

		try {

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

		} catch (Exception ex) {
			LOGGER.error("Erro ao converte o id {}",  value, ex);
			throw new ServiceException("Erro ao converte o id " + value, ex);
		}
	}
	
	public static Object convert(Field field, Object value) throws ServiceException {

		try {
			
			
			if (field.getType().getName().equals(Integer.class.getName())) {
				return Integer.parseInt(value.toString());
			} else if (field.getType().getName().equals(Long.class.getName())) {
				return Long.parseLong(value.toString());
			} else if (field.getType().getName().equals(Short.class.getName())) {
				return Short.parseShort(value.toString());
			} else if (field.getType().getName().equals(Double.class.getName())) {
				return Double.parseDouble(value.toString());
			} else if (field.getType().getName().equals(Float.class.getName())) {
				return Float.parseFloat(value.toString());
			} else if (field.getType().getName().equals(Byte.class.getName())) {
				return Byte.parseByte(value.toString());
			} else if (field.getType().getName().equals(PageRequest.class.getName())) {
				return (Pageable)value;
			} else if (field.getType().getName().equals(LocalDate.class.getName())) {
				LocalDate date = LocalDate.parse(value.toString(), DateTimeFormatter.ISO_LOCAL_DATE);
				return (LocalDate) date;
			} else if (field.getType().getName().equals(LocalDateTime.class.getName())) {
				LocalDateTime date = LocalDateTime.parse(value.toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
				return (LocalDateTime) date;				
			} else {
				return value.toString();
			}

		} catch (Exception ex) {
			LOGGER.error("Erro ao converte o valor {}",  value, ex);
			throw new ServiceException("Erro ao converte o valor " + value, ex);
		}
	}
	
	public static Object convert(Class<?> clazz, Object value) throws ServiceException {

		try {
			
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
				return (Pageable)value;
			} else if (clazz.getTypeName().equals(LocalDate.class.getName())) {
				LocalDate date = LocalDate.parse(value.toString(), DateTimeFormatter.ISO_LOCAL_DATE);
				return (LocalDate) date;
			} else if (clazz.getTypeName().equals(LocalDateTime.class.getName())) {
				LocalDateTime date = LocalDateTime.parse(value.toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
				return (LocalDateTime) date;				
			} else {
				return value.toString();
			}

		} catch (Exception ex) {
			LOGGER.error("Erro ao converte o valor {}",  value, ex);
			throw new ServiceException("Erro ao converte o valor " + value, ex);
		}
	}
	
	
	public static Object convert(Object value) throws ServiceException {

		try {
			return Integer.parseInt(value.toString());
		} catch (Exception ix) {
			try {
				LocalDate localDate = LocalDate.parse(value.toString(), DateTimeFormatter.ISO_LOCAL_DATE);
				return (LocalDate) localDate;
			} catch (Exception localDateEx) {
				try {
					LocalDateTime localDateTime = LocalDateTime.parse(value.toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
					return (LocalDateTime) localDateTime;
				} catch (Exception localDateTimeEx) {
					return value;
				}
			}
		}
	}
}
