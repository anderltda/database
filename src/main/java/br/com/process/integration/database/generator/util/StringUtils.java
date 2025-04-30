package br.com.process.integration.database.generator.util;

public class StringUtils {
	public static String camelCase(String input) {
		StringBuilder result = new StringBuilder();
		boolean capitalizeNext = false;
		for (char c : input.toCharArray()) {
			if (c == '_' || c == '-') {
				capitalizeNext = true;
			} else if (capitalizeNext) {
				result.append(Character.toUpperCase(c));
				capitalizeNext = false;
			} else {
				result.append(Character.toLowerCase(c));
			}
		}
		return result.toString();
	}

	public static String capitalize(String input) {
		if (input == null || input.isEmpty())
			return input;
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}
}
