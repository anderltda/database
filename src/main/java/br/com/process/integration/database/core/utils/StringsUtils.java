package br.com.process.integration.database.core.utils;

import java.util.Optional;

import br.com.process.integration.database.core.exception.CheckedException;
import br.com.process.integration.database.core.exception.UncheckedException;

/**
 * 
 */
public class StringsUtils {

	/**
	 * @param value
	 * @return
	 */
	public static String setMethod(String value) {
		return "set".concat(firstUpper(value));
	}

	/**
	 * @param value
	 * @return
	 */
	public static String getMethod(String value) {
		return "get".concat(firstUpper(value));
	}

	/**
	 * @param name
	 * @return
	 */
	public static String firstUpper(String name) {
		String returnValue = name.substring(0, 1).toUpperCase();
		if (name.length() > 1)
			returnValue += name.substring(1);
		return returnValue;
	}
	
	/**
	 * @param name
	 * @return
	 */
	public static String firstLower(String name) {
		String returnValue = name.substring(0, 1).toLowerCase();
		if (name.length() > 1)
			returnValue += name.substring(1);
		return returnValue;
	}	
	
	/**
	 * @param className
	 * @return
	 * @throws CheckedException
	 */
	public static String getNameMapper(String className) throws CheckedException {
		try {
			return className.substring(0, 1).toLowerCase() + className.substring(1) + "Mapper";
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	/**
	 * @param className
	 * @return
	 * @throws CheckedException
	 */
	public static String getNameRepository(String className) throws CheckedException {
		try {
			return className.substring(0, 1).toLowerCase() + className.substring(1) + "Repository";
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	/**
	 * @param className
	 * @return
	 * @throws CheckedException
	 */
	public static String getNameService(String className) throws CheckedException {
		try {
			return className.substring(0, 1).toLowerCase() + className.substring(1) + "Service";
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * @param object
	 * @return
	 */
	public static String processValuesForBetween(Object object) {
		if (object == null) {
			return "";
		}

		String value = formatObjectToString(object);

		if (value.isEmpty()) {
			return value;
		}

		long count = countCommas(value);

		if (count == 0) {
			return replaceDotsWithCommas(value);
		} else if (count >= 3) {
			return processMultipleValues(value);
		}

		return value;
	}

	/**
	 * @param object
	 * @return
	 */
	private static String formatObjectToString(Object object) {
		return Optional.ofNullable(object).map(obj -> obj.toString().replaceAll("[\\[\\]]", "")).orElse("");
	}

	/**
	 * @param value
	 * @return
	 */
	private static String replaceDotsWithCommas(String value) {
		return value.replace(".", ",");
	}

	/**
	 * @param value
	 * @return
	 */
	private static String processMultipleValues(String value) {
		value = value.replaceAll("(\\d{1,10}),(\\d{2})", "$1#$2");
		String[] valoresArray = value.split(",\\s*");

		double[] numeros = parseValues(valoresArray);

		StringBuilder builder = new StringBuilder();
		for (double numero : numeros) {
			builder.append(numero).append(",");
		}

		if (builder.length() > 0) {
			builder.deleteCharAt(builder.length() - 1);
		}

		return builder.toString();
	}
	
	/**
	 * @param value
	 * @return
	 */
	private static String formatNumber(String value) {
		return value.replace("#", ",").replace(".", "").replace(",", ".");
	}
	
	/**
	 * @param value
	 * @return
	 */
	private static long countCommas(String value) {
		return value.chars().filter(ch -> ch == ',').count();
	}
	
	/**
	 * @param valoresArray
	 * @return
	 */
	private static double[] parseValues(String[] valoresArray) {
		double[] numeros = new double[valoresArray.length];
		for (int i = 0; i < valoresArray.length; i++) {
			String valorFormatado = formatNumber(valoresArray[i]);
			try {
				numeros[i] = Double.parseDouble(valorFormatado);
			} catch (NumberFormatException ex) {
				throw new UncheckedException("Invalid number format: " + valorFormatado, ex);
			}
		}
		return numeros;
	}

	/**
	 * Formatar atributo para uma formacao ex: Prohibited Date Time ou
	 * ProhibitedDateTime para isso prohibitedDateTime
	 * 
	 * @param input - valor a ser formatado
	 * @return
	 */
	public static String normalizeToCamelCaseFromPascalCase(String input) {
	    if (input == null || input.isEmpty()) return input;

	    return input.substring(0, 1).toLowerCase() + input.substring(1);
	}

	/**
	 * Formatar atributo para uma formacao ex: prohibitedDateTime para isso
	 * Prohibited Date Time
	 * 
	 * @param input - valor a ser formatado
	 * @return
	 */
	public static String normalizeLabelToLowercaseCamelization(String input) {
		if (input == null || input.isEmpty())
			return input;

		String spaced = input.replaceAll("([a-z])([A-Z])", "$1 $2");
		String[] words = spaced.split(" ");
		StringBuilder builder = new StringBuilder();
		for (String word : words) {
			if (!word.isEmpty()) {
				builder.append(Character.toUpperCase(word.charAt(0)));
				builder.append(word.substring(1));
				builder.append(" ");
			}
		}
		return builder.toString().trim();
	}

	/**
	 * @param value
	 * @return
	 */
	public static String parseStringDouble(String value) {
		if(value.contains(",")) {
			return value.toString().replace(".", "").replace(",", ".");
		}
		return value;
	}
}
