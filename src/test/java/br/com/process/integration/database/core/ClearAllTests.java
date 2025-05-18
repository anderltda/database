package br.com.process.integration.database.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.process.integration.database.core.exception.ErrorResponse;
import br.com.process.integration.database.core.ui.CrudJpaController;
import br.com.process.integration.database.util.PackageDeleter;
import br.com.process.integration.database.util.ResourceFolderDeleter;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClearAllTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private CrudJpaController crudJpaController;
	
	private String domain = "test";

	@BeforeAll
	void setupOnce() {
	}

	@Test
	@Order(1)
	void contextLoads() {
		assertNotNull(crudJpaController);
	}

	@Test
	@Order(2)
	void teste_01() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/EntityOne";

		String statusCode = delete(url, new ErrorResponse());

		assertEquals(statusCode, HttpStatus.OK.toString());
	}

	@Test
	@Order(3)
	void teste_02() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/EntityStatus";

		String statusCode = delete(url, new ErrorResponse());

		assertEquals(statusCode, HttpStatus.OK.toString());
	}
	
	@Test
	@Order(4)
	void teste_03() throws IOException {
		PackageDeleter.deletePackage("src/main/java", Constants.MYBATIS_DATA + domain);

	}
	
	@Test
	@Order(5)
	void teste_04() throws IOException {
		PackageDeleter.deletePackage("src/main/java", Constants.MYBATIS_MAPPER + domain);
	}
	
	@Test
	@Order(6)
	void teste_05() throws IOException {
		PackageDeleter.deletePackage("src/main/java", Constants.MYBATIS_SERVICE + domain);
	}
	
	@Test
	@Order(7)
	void teste_06() throws IOException {
		PackageDeleter.deletePackage("src/main/java", Constants.JPA_ENTITY +  domain);
	}
	
	@Test
	@Order(8)
	void teste_07() throws IOException {
		PackageDeleter.deletePackage("src/main/java", Constants.JPA_REPOSITORY + domain);
	}
	
	@Test
	@Order(9)
	void teste_08() throws IOException {
		PackageDeleter.deletePackage("src/main/java", Constants.JPA_SERVICE + domain);
	}	
	
	@Test
	@Order(10)
	void teste_09() throws IOException {
		PackageDeleter.deletePackage("src/main/java", Constants.JDBC_VIEW + domain);
	}	
	
	@Test
	@Order(11)
	void teste_10() throws IOException {
		PackageDeleter.deletePackage("src/main/java", Constants.JDBC_SERVICE + domain);
	}	
	
	@Test
	@Order(12)
	void teste_11() throws IOException {
		ResourceFolderDeleter.deleteResourceFolder("mapper.test");
	}	
	
	@Test
	@Order(13)
	void teste_12() throws IOException {
		ResourceFolderDeleter.deleteResourceFolder("querys.test");
	}

	public String delete(String url, ErrorResponse compare) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			System.out.println("Resource deleted successfully. Response: " + response.getBody());
			return response.getStatusCode().toString();
		} else {
			System.err.println("Failed to delete resource. Status code: " + response.getStatusCode());
			ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertTrue(errorResponse.getMessage().contains(compare.getMessage()));
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: "
					+ errorResponse.getMessage());
		}
	}

	private ErrorResponse convertResponseToErrorResponse(String body) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(body, ErrorResponse.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error parsing ErrorResponse", e);
		}
	}
}
