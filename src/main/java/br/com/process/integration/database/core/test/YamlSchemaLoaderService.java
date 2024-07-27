package br.com.process.integration.database.core.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class YamlSchemaLoaderService {

    private final ObjectMapper yamlReader;
    private final ObjectMapper jsonWriter;

    public YamlSchemaLoaderService() {
        this.yamlReader = new ObjectMapper(new YAMLFactory());
        this.jsonWriter = new ObjectMapper();
    }

    public JsonNode loadYamlSchema(String resourcePath) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(resourcePath);
        JsonNode yamlNode = yamlReader.readTree(inputStream);
        return jsonWriter.readTree(jsonWriter.writeValueAsString(yamlNode));
    }
}