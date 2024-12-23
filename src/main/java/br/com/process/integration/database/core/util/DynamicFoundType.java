package br.com.process.integration.database.core.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.databind.JsonNode;

import br.com.process.integration.database.core.domain.BeanEntity;
import br.com.process.integration.database.core.exception.CheckedException;
import br.com.process.integration.database.core.reflection.MethodReflection;

public class DynamicFoundType {

	private DynamicFoundType() {}

	public static Object getTypeJsonValue(Field field, JsonNode jsonNode) {

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

	public static Object getTypeValue(BeanEntity<?> model, Object value) throws CheckedException {

		try {

			Class<?> clazz = MethodReflection.getTypeById(model);
			
			return typeExtracted(clazz, value);

		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}

	}

	public static Object getTypeValue(Field field, Object value) {
		return typeExtracted(field.getType().getClass(), value);
	}

	public static Object getTypeValue(Class<?> clazz, Object value) {
		return typeExtracted(clazz, value);
	}

	public static Object getTypeAttributeName(String classPath, String attributeName, Object value, Boolean notFound) {
	    Class<?> type = MethodReflection.getAttributeType(classPath, attributeName, notFound);
	    return typeExtracted(type, value);
		
	}
	
	private static Object typeExtracted(Class<?> type, Object value) {
		
		if (type.equals(Integer.class)) {
	    	return Integer.valueOf(value.toString());
	    } else if (type.equals(Short.class)) {
	    	return Short.valueOf(value.toString());
	    } else if (type.equals(Float.class)) {
	    	return Float.valueOf(value.toString());
	    } else if (type.equals(Long.class)) {
	    	return Long.valueOf(value.toString());
	    } else if (type.equals(Double.class)) {
	    	return Double.valueOf(parseStringDouble(value.toString()));
	    } else if (type.equals(Byte.class)) {
	    	return Byte.valueOf(value.toString());    
	    } else if (type.equals(BigInteger.class)) {
	    	return new BigInteger(value.toString());	        
	    } else if (type.equals(BigDecimal.class)) {
	    	return new BigDecimal(parseStringDouble(value.toString()));	
	    } else if (type.equals(Boolean.class)) {
	        return Boolean.valueOf(value.toString());
	    } else if (type.equals(Date.class)) {
	        return new Date();
	    } else if (type.equals(LocalDate.class)) {
	    	DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE; 
	        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	        return LocalDate.parse(value.toString(), value.toString().contains("T") ? dateTimeFormatter :dateFormatter);
	    } else if (type.equals(LocalDateTime.class)) {
	    	return LocalDateTime.parse(value.toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		} else if (type.equals(Sort.class)) {
			return value;
		} else if (type.equals(List.class)) {
			return value;			
		} else if (type.equals(PageRequest.class)) {
			return value;	    	
	    } else {
	        return value.toString();
	    }
	}
	
	private static String parseStringDouble(String value) {
		if(value.contains(",")) {
			return value.toString().replace(".", "").replace(",", ".");
		}
		return value;
	}
	
}
