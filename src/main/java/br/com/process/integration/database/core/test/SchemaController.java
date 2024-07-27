package br.com.process.integration.database.core.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;

@RestController
@RequestMapping("/api/schema")
public class SchemaController {

    private final ObjectMapper objectMapper;

    public SchemaController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @GetMapping("/user")
    public String getUserSchema() throws Exception {
        JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(objectMapper);
        JsonNode jsonSchema = schemaGen.generateJsonSchema(User.class);
        return objectMapper.writeValueAsString(jsonSchema);
    }
}
