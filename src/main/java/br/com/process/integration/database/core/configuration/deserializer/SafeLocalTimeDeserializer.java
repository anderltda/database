package br.com.process.integration.database.core.configuration.deserializer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * 
 */
public class SafeLocalTimeDeserializer extends JsonDeserializer<LocalTime> {

	/**
	 *
	 */
	@Override
	public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		String value = p.getText();
		try {
		    if (value == null || value.trim().isEmpty()) {
		        return null; 
		    }
			return LocalTime.parse(value); // ex: 22:09:13
		} catch (DateTimeParseException e1) {
			try {
				return OffsetTime.parse(value).toLocalTime(); // ex: 22:09:13.000Z ou +00:00
			} catch (DateTimeParseException e2) {
				throw new IllegalArgumentException("Hora inválida: " + value);
			}
		}
	}
}
