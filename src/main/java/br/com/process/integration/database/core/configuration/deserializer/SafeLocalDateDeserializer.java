package br.com.process.integration.database.core.configuration.deserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * 
 */
public class SafeLocalDateDeserializer extends JsonDeserializer<LocalDate> {

	/**
	 *
	 */
	@Override
	public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		String value = p.getText();
		try {
		    if (value == null || value.trim().isEmpty()) {
		        return null; 
		    }
			return LocalDate.parse(value);
		} catch (DateTimeParseException e1) {
			try {
				return OffsetDateTime.parse(value).toLocalDate();
			} catch (DateTimeParseException e2) {
				throw new IllegalArgumentException("Data inválida: " + value);
			}
		}
	}
}
