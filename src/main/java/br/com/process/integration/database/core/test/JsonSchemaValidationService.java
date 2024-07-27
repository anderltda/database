package br.com.process.integration.database.core.test;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

@Service
public class JsonSchemaValidationService {

	private final ObjectMapper objectMapper;

	public JsonSchemaValidationService(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public Set<ValidationMessage> validateJson(JsonNode jsonNode, JsonNode schemaNode) {
		JsonSchemaFactory schemaFactory = JsonSchemaFactory
				.builder(JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)).objectMapper(objectMapper).build();
		JsonSchema schema = schemaFactory.getSchema(schemaNode);
		return schema.validate(jsonNode);
	}
}
