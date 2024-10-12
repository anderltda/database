package br.com.process.integration.database.core;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.process.integration.database.core.exception.ErrorResponse;
import br.com.process.integration.database.core.ui.CrudJpaController;
import br.com.process.integration.database.core.util.Constants;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeleteTests {

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
	void teste_01() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/EntityOne?id=" + SaveFlushTests.ids.get(0) + "," + SaveFlushTests.ids.get(1);

		String statusCode = delete(url, new ErrorResponse());

		assertEquals(statusCode, HttpStatus.OK.toString());
	}

	@Test
	@Order(3)
	void teste_02() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/EntityOne/" + SaveFlushTests.id;

		String statusCode = delete(url, new ErrorResponse());

		assertEquals(statusCode, HttpStatus.OK.toString());
	}

	@Test
	@Order(4)
	void teste_03() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/EntityOne";

		String statusCode = delete(url, new ErrorResponse());

		assertEquals(statusCode, HttpStatus.OK.toString());
	}
	
	@Test
	@Order(5)
	void teste_04() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/EntityOne/AQW";

		ErrorResponse errorResponse = new ErrorResponse("For input string: \"AQW\"", HttpStatus.BAD_REQUEST);
		
	    assertThrows(RuntimeException.class, () -> delete(url, errorResponse));
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
	        throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: " + errorResponse.getMessage());
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
