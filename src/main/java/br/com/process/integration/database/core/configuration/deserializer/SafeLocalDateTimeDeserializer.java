package br.com.process.integration.database.core.configuration.deserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * 
 */
public class SafeLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

	/**
	 *
	 */
	@Override
	public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		String value = p.getText();
		try {
		    if (value == null || value.trim().isEmpty()) {
		        return null; 
		    }
			return LocalDateTime.parse(value); // ex: 2025-04-16T22:09:13
		} catch (DateTimeParseException e1) {
			try {
				return OffsetDateTime.parse(value).toLocalDateTime(); // ex: 2025-04-16T22:09:13.952Z
			} catch (DateTimeParseException e2) {
				throw new IllegalArgumentException("Data/hora inválida: " + value);
			}
		}
	}
}
