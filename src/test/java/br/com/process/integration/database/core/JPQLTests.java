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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.process.integration.database.core.exception.ErrorResponse;
import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.core.util.Constants;
import br.com.process.integration.database.domain.model.entity.EntityOne;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JPQLTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private QueryJpaController queryJpaController;

	@BeforeAll
	void setupOnce() {
	}

	@Test
	@Order(1)
	void contextLoads() {
		assertNotNull(queryJpaController);
	};

	@Test
	void teste_01() {

		String url = "http://localhost:" + port
				+ Constants.API_NAME_REQUEST_MAPPING + "/jpql/single/EntityOne/buscaComEqualPeloName?name=Anderson";

		EntityOne entity = getSingleResult(url, new ErrorResponse());

		assertNotNull(entity.getId());
		assertEquals("Anderson", entity.getName());
		assertEquals(41, entity.getAge());
		assertEquals(1.93, entity.getHeight());
		assertEquals(LocalDate.parse("1983-03-29", DateTimeFormatter.ISO_LOCAL_DATE), entity.getBirthDate());
		assertEquals(LocalDateTime.parse("2024-02-01T02:52:54", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
				entity.getProhibitedDateTime());
		assertNotEquals(0, entity.hashCode());
		assertNotNull(entity.getEntityTwo());

		assertNotNull(entity);
	}

	@Test
	void teste_02() {

		String url = "http://localhost:" + port
				+ Constants.API_NAME_REQUEST_MAPPING + "/jpql/single/EntityOne/buscaComLikePeloName?name=Paulo";

		ErrorResponse errorResponse = new ErrorResponse("Query did not return a unique result: 2 results were returned",
				HttpStatus.BAD_REQUEST);

		assertThrows(RuntimeException.class, () -> getSingleResult(url, errorResponse));

	}

	@Test
	void teste_03() {

		String url = "http://localhost:" + port
				+ Constants.API_NAME_REQUEST_MAPPING + "/jpql/EntityOne/buscaComLikePeloName?name=ar&sortList=birthDate,name&sortOrders=asc,desc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(5, list.size());
		assertEquals("Carlos", list.get(0).getName());
		assertEquals("Carlos Alberto", list.get(1).getName());
		assertEquals("Ricardo", list.get(2).getName());
		assertEquals("Ariovaldo", list.get(3).getName());
		assertEquals("Maria", list.get(4).getName());
	}

	@Test
	void teste_04() {

		String url = "http://localhost:" + port
				+ Constants.API_NAME_REQUEST_MAPPING + "/jpql/EntityOne/buscaAll?sortList=birthDate,name&sortOrders=asc,desc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(10, list.size());
		assertEquals("Carlos", list.get(0).getName());
		assertEquals("Carlos Alberto", list.get(1).getName());
		assertEquals("Anderson", list.get(2).getName());
		assertEquals("Paulo Henrique", list.get(3).getName());
		assertEquals("Ricardo", list.get(4).getName());
		assertEquals("Ariovaldo", list.get(5).getName());
		assertEquals("Paulo", list.get(6).getName());
		assertEquals("Joana", list.get(7).getName());
		assertEquals("Renato", list.get(8).getName());
		assertEquals("Maria", list.get(9).getName());
	}

	@Test
	void teste_05() {

		String url = "http://localhost:" + port
				+ Constants.API_NAME_REQUEST_MAPPING + "/jpql/EntityOne/buscaAll?sortList=birthDate,name&sortOrders=desc,asc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(10, list.size());
		assertEquals("Maria", list.get(0).getName());
		assertEquals("Renato", list.get(1).getName());
		assertEquals("Joana", list.get(2).getName());
		assertEquals("Paulo", list.get(3).getName());
		assertEquals("Ariovaldo", list.get(4).getName());
		assertEquals("Ricardo", list.get(5).getName());
		assertEquals("Paulo Henrique", list.get(6).getName());
		assertEquals("Anderson", list.get(7).getName());
		assertEquals("Carlos Alberto", list.get(8).getName());
		assertEquals("Carlos", list.get(9).getName());
	}

	@Test
	void teste_06() {

		String url = "http://localhost:" + port
				+ Constants.API_NAME_REQUEST_MAPPING + "/jpql/EntityOne/buscaAll?sortList=name,birthDate&sortOrders=asc,desc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(10, list.size());
		assertEquals("Anderson", list.get(0).getName());
		assertEquals("Ariovaldo", list.get(1).getName());
		assertEquals("Carlos", list.get(2).getName());
		assertEquals("Carlos Alberto", list.get(3).getName());
		assertEquals("Joana", list.get(4).getName());
		assertEquals("Maria", list.get(5).getName());
		assertEquals("Paulo", list.get(6).getName());
		assertEquals("Paulo Henrique", list.get(7).getName());
		assertEquals("Renato", list.get(8).getName());
		assertEquals("Ricardo", list.get(9).getName());
	}

	@Test
	void teste_07() {

		String url = "http://localhost:" + port
				+ Constants.API_NAME_REQUEST_MAPPING + "/jpql/EntityOne/buscaAll?sortList=name,birthDate&sortOrders=desc,asc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(10, list.size());
		assertEquals("Ricardo", list.get(0).getName());
		assertEquals("Renato", list.get(1).getName());
		assertEquals("Paulo Henrique", list.get(2).getName());
		assertEquals("Paulo", list.get(3).getName());
		assertEquals("Maria", list.get(4).getName());
		assertEquals("Joana", list.get(5).getName());
		assertEquals("Carlos Alberto", list.get(6).getName());
		assertEquals("Carlos", list.get(7).getName());
		assertEquals("Ariovaldo", list.get(8).getName());
		assertEquals("Anderson", list.get(9).getName());
	}

	@Test
	void teste_08() {

		String url = "http://localhost:" + port
				+ Constants.API_NAME_REQUEST_MAPPING + "/jpql/single/EntityOne/buscaComEqualPeloName?name=Pedro";

		EntityOne entity = getSingleResult(url, new ErrorResponse());

		assertNull(entity);
	}

	@Test
	void teste_09() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/jpql/EntityOne/buscaComLikePeloName?"
				+ "name=Silva&"
				+ "sortList=name,birthDate&"
				+ "sortOrders=desc,asc";

		getRestAllNotfound(url, 204);
	}
	
	@Test
	void teste_09_1() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/jpql/EntityOne/buscaComLikePeloName?name=er&sortList=name,birthDate&sortOrders=desc,asc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(2, list.size());
		assertEquals("Carlos Alberto", list.get(0).getName());
		assertEquals("Anderson", list.get(1).getName());
	}

	@Test
	void teste_10() {

		String url = "http://localhost:" + port
				+ Constants.API_NAME_REQUEST_MAPPING + "/jpql/count/EntityOne/countMaiorProhibited?prohibitedDateTime=2024-11-01T08:00:00";

		Long count = Long.parseLong(getUniqueResult(url, new ErrorResponse()));

		assertEquals(4, count);
	}

	@Test
	void teste_11() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/jpql/count/EntityOne/countMaiorAge?age=AQ";

		ErrorResponse errorResponse = new ErrorResponse("Parameter value [AQ] did not match expected type [SqmBasicValuedSimplePath(br.com.process.integration.database.domain.model.entity.EntityOne(e).age)]",
				HttpStatus.BAD_REQUEST);

		assertThrows(RuntimeException.class, () -> getUniqueResult(url, errorResponse));
	}
	
	@Test
	void teste_12() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/jpql/EntityOne/buscaAll?age=22";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(2, list.size());
		assertEquals("Ricardo", list.get(0).getName());
		assertEquals("Ariovaldo", list.get(1).getName());
	}
	
	@Test
	void teste_13() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/jpql/EntityOne/buscaAll?age=Silva&sortList=name,birthDate&sortOrders=desc,asc";

		ErrorResponse errorResponse = new ErrorResponse("Parameter value [Silva] did not match expected type [SqmBasicValuedSimplePath(br.com.process.integration.database.domain.model.entity.EntityOne(e).age)]",
				HttpStatus.BAD_REQUEST);

		assertThrows(RuntimeException.class, () -> getAll(url, errorResponse));
	}

	public List<EntityOne> getAll(String url, ErrorResponse compare) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return convertResponseToEntityOneList(response.getBody());
		} else {
			ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertTrue(errorResponse.getMessage().contains(compare.getMessage()));
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: " + errorResponse.getMessage());
		}
	}
	
	private void getRestAllNotfound(String url, int code) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			assertEquals(response.getStatusCode().value(), HttpStatusCode.valueOf(code).value());
		} 
	}

	public EntityOne getSingleResult(String url, ErrorResponse compare) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Accept", "application/json");
	    HttpEntity<String> entity = new HttpEntity<>(headers);
	    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
	    if (response.getStatusCode().is2xxSuccessful()) {
	        return convertResponseToEntityOne(response.getBody());
	    } else {
	    	ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertTrue(errorResponse.getMessage().contains(compare.getMessage()));
	        throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: " + errorResponse.getMessage());
	    }
	}
	
	private String getUniqueResult(String url, ErrorResponse compare) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
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

	private List<EntityOne> convertResponseToEntityOneList(String body) {
	    ObjectMapper objectMapper = createObjectMapper();
	    try {
	    	return objectMapper.readValue(body, new TypeReference<List<EntityOne>>(){});
	    } catch (JsonProcessingException e) {
	        throw new RuntimeException("Error parsing EntityOne list response", e);
	    }
	}

	private EntityOne convertResponseToEntityOne(String body) {
	    ObjectMapper objectMapper = createObjectMapper();
	    try {
	    	if(body != null) {
	    		return objectMapper.readValue(body, EntityOne.class);
	    	} else {
	    		return null;
	    	}
	    } catch (JsonProcessingException e) {
	        throw new RuntimeException("Error parsing EntityOne response", e);
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
