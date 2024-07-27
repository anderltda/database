package br.com.process.integration.database.core.test;

import java.io.IOException;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.ValidationMessage;

@RestController
@RequestMapping("/api/yaml/users")
public class UserYamlController {

    private final ObjectMapper objectMapper;
    private final JsonSchemaValidationService validationService;
    private final JsonNode userSchema;

    public UserYamlController(ObjectMapper objectMapper, JsonSchemaValidationService validationService, YamlSchemaLoaderService schemaLoaderService) throws IOException {
        this.objectMapper = objectMapper;
        this.validationService = validationService;
        this.userSchema = schemaLoaderService.loadYamlSchema("/user-schema.yml");
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody JsonNode userJson) {
        Set<ValidationMessage> validationMessages = validationService.validateJson(userJson, userSchema);

        if (!validationMessages.isEmpty()) {
            StringBuilder errors = new StringBuilder();
            for (ValidationMessage message : validationMessages) {
                errors.append(message.getMessage()).append("\n");
            }
            return new ResponseEntity<>(errors.toString(), HttpStatus.BAD_REQUEST);
        }

        // Suponha que o objeto User é validado e pode ser salvo no banco de dados
        // User user = objectMapper.convertValue(userJson, User.class);

        // Salve o user no banco de dados (omissão do código de persistência para brevidade)

        return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    }
}