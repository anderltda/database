package br.com.process.integration.database.core;

import java.io.IOException;

import org.springframework.hateoas.PagedModel;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PagedModelDeserializer<T> extends JsonDeserializer<PagedModel<T>> {

	private final Class<T> contentType;

	public PagedModelDeserializer(Class<T> contentType) {
		this.contentType = contentType;
	}

	@Override
	public PagedModel<T> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		ObjectMapper mapper = (ObjectMapper) p.getCodec();
		return mapper.readValue(p, new TypeReference<PagedModel<T>>() { });
	}
}
