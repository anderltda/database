package br.com.process.integration.database.core.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

import br.com.process.integration.database.core.util.Constants;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QueryJpaControllerDeleteTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private CrudJpaController crudJpaController;
	
	@BeforeAll
	void setupOnce() { }

	@Test
	@Order(1)
	void contextLoads() {
		assertNotNull(crudJpaController);
	}

	@Test
	@Order(2)
	void testDeleteForAllId() {

		String json = "[";

		for (Long id : QueryJpaControllerSaveTests.ids) {
			json += "{\"id\": " + id + "}";
			json += ",";
		}

		json = json.substring(0, json.length() - 1) + "]";

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/delete/all/id/EntityOne";

		String statusCode = deleteResource(url, json);

		assertEquals(statusCode, HttpStatus.OK.toString());
	}

	@Test
	@Order(3)
	void testDeleteForId() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/delete/id/EntityOne/" + QueryJpaControllerSaveTests.id;

		String statusCode = deleteResource(url);

		assertEquals(statusCode, HttpStatus.OK.toString());
	}

	@Test
	@Order(4)
	void testDeleteForAll() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/delete/all/EntityOne";

		String statusCode = deleteResource(url);

		assertEquals(statusCode, HttpStatus.OK.toString());
	}

	public String deleteResource(String url) {
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.set("Content-Type", "application/json");

		HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			System.out.println("Resource deleted successfully. Response: " + response.getBody());
		} else {
			System.err.println("Failed to delete resource. Status code: " + response.getStatusCode());
		}

		return response.getStatusCode().toString();
	}

	public String deleteResource(String url, String json) {
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.set("Content-Type", "application/json");

		HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			System.out.println("Resource deleted successfully. Response: " + response.getBody());
		} else {
			System.err.println("Failed to delete resource. Status code: " + response.getStatusCode());
		}

		return response.getStatusCode().toString();
	}
}
