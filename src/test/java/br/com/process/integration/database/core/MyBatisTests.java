package br.com.process.integration.database.core;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.process.integration.database.core.exception.ErrorResponse;
import br.com.process.integration.database.core.ui.QueryMyBatisController;
import br.com.process.integration.database.core.util.Constants;
import br.com.process.integration.database.domain.model.data.EntityOneData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MyBatisTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private QueryMyBatisController queryMyBatisController;

	@BeforeAll
	void setupOnce() {
	}

	@Test
	@Order(1)
	void contextLoads() {
		assertNotNull(queryMyBatisController);
	};
	
	@Test
	void teste_count_like_by_name() {

		String url = "http://localhost:" + port
				+ Constants.API_NAME_REQUEST_MAPPING + "/mapper/count/EntityOneData/countEntitiesByName?name=ar";

		int count = Integer.parseInt(getUniqueResult(url, new ErrorResponse()));

		assertEquals(5, count);
	}
	
	@Test
	void teste_busca_com_equal_pelo_name_unico_registro_com_nenhum_resultado() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/single/EntityOneData/findEntityDataByName?name=Pedro";

		EntityOneData data = getSingleResult(url, new ErrorResponse());

		assertNull(data);

	}
	
	@Test
	void teste_busca_com_equal_pelo_name_unico_registro_com_erro() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/single/EntityOneData/methodNoMapping?name=Anderson";

		ErrorResponse errorResponse = new ErrorResponse("Invalid bound statement (not found): br.com.process.integration.database.domain.store.mapper.EntityOneDataMapper.methodNoMapping",
				HttpStatus.BAD_REQUEST);

		assertThrows(RuntimeException.class, () -> getSingleResult(url, errorResponse));

	}

	@Test
	void teste_busca_com_equal_pelo_name_unico_registro() {

		String url = "http://localhost:" + port
				+ Constants.API_NAME_REQUEST_MAPPING + "/mapper/single/EntityOneData/findEntityDataByName?name=Anderson";

		EntityOneData data = getSingleResult(url, new ErrorResponse());

		assertNotNull(data.getId());
		assertEquals("Anderson", data.getName());
		assertEquals(41, data.getAge());
		assertEquals(1.93, data.getHeight());
		assertEquals(LocalDate.parse("1983-03-29", DateTimeFormatter.ISO_LOCAL_DATE), data.getBirthDate());
		assertEquals(LocalDateTime.parse("2024-02-01T02:52:54", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
				data.getProhibited());
		assertNotEquals(0, data.hashCode());
		assertNotNull(data.getEntityTwo());

		assertNotNull(data);
	}
	
	@Test
	void teste_busca_com_like_pelo_name_nenhum_registro_encontrado() {

		String url = "http://localhost:" + port
				+ Constants.API_NAME_REQUEST_MAPPING + "/mapper/EntityOneData/findEntityOneByName?name=Pedro";

		List<EntityOneData> list = getAll(url, new ErrorResponse());

		assertNull(list);
	}

	
	@Test
	void teste_busca_com_like_pelo_name() {

		String url = "http://localhost:" + port
				+ Constants.API_NAME_REQUEST_MAPPING + "/mapper/EntityOneData/findEntityOneByName?name=ar";

		List<EntityOneData> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(5, list.size());
		assertEquals("Carlos", list.get(0).getName());
		assertEquals("Carlos Alberto", list.get(1).getName());
		assertEquals("Ricardo", list.get(2).getName());
		assertEquals("Ariovaldo", list.get(3).getName());
		assertEquals("Maria", list.get(4).getName());
	}

	
	@Test
	void teste_all_registro_com_erro() {

		String url = "http://localhost:" + port
				+ Constants.API_NAME_REQUEST_MAPPING + "/mapper/EntityOneData/methodNoMapping?name=Pedro";
		
		ErrorResponse errorResponse = new ErrorResponse("Invalid bound statement (not found): br.com.process.integration.database.domain.store.mapper.EntityOneDataMapper.methodNoMapping",
				HttpStatus.BAD_REQUEST);

		assertThrows(RuntimeException.class, () -> getSingleResult(url, errorResponse));

	}


	public List<EntityOneData> getAll(String url, ErrorResponse compare) {
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.set("Accept", "application/json");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			return convertResponseToEntityOneDataList(response.getBody());
		} else {
			ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertTrue(errorResponse.getMessage().contains(compare.getMessage()));
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: " + errorResponse.getMessage());
		}
	}

	public EntityOneData getSingleResult(String url, ErrorResponse compare) {
		
	    HttpHeaders headers = new HttpHeaders();
	    
	    headers.set("Accept", "application/json");

	    HttpEntity<String> entity = new HttpEntity<>(headers);

	    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

	    if (response.getStatusCode().is2xxSuccessful()) {
	        return convertResponseToEntityOneData(response.getBody());
	    } else {
	    	ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertTrue(errorResponse.getMessage().contains(compare.getMessage()));
	        throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: " + errorResponse.getMessage());
	    }
	}
	
	private String getUniqueResult(String url, ErrorResponse compare) {
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.set("Accept", "application/json");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		} else {
	    	ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertTrue(errorResponse.getMessage().contains(compare.getMessage()));
	        throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: " + errorResponse.getMessage());

		}
	}
	
	private ObjectMapper createObjectMapper() {
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.registerModule(new JavaTimeModule());
	    objectMapper.findAndRegisterModules(); 
	    return objectMapper;
	}

	private List<EntityOneData> convertResponseToEntityOneDataList(String body) {
	    ObjectMapper objectMapper = createObjectMapper();
	    try {
	    	if(body != null) {
	    		return objectMapper.readValue(body, new TypeReference<List<EntityOneData>>(){});
	    	} else {
	    		return null;
	    	}
	    } catch (JsonProcessingException e) {
	        throw new RuntimeException("Error parsing EntityOneData list response", e);
	    }
	}

	private EntityOneData convertResponseToEntityOneData(String body) {
	    ObjectMapper objectMapper = createObjectMapper();
	    try {
	    	if(body != null) {
	    		return objectMapper.readValue(body, EntityOneData.class);
	    	} else {
	    		return null;
	    	}
	    } catch (JsonProcessingException e) {
	        throw new RuntimeException("Error parsing EntityOneData response", e);
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
