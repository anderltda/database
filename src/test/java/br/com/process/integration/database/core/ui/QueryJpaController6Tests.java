package br.com.process.integration.database.core.ui;

import static org.junit.Assert.assertNull;
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
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.process.integration.database.core.exception.ErrorResponse;
import br.com.process.integration.database.domain.entity.EntityTest1;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QueryJpaController6Tests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private QueryJpaController queryJpaController;
	
	@BeforeAll
	void setupOnce() { }

	@Test
	@Order(1)
	void contextLoads() {
		assertNotNull(queryJpaController);
	};

	@Test
	void teste_busca_com_equal_pelo_name_unico_registro() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/single/jpql/EntityTest1/buscaComEqualPeloName?name=Anderson";

		EntityTest1 entity = getSingleResult(url, new ErrorResponse());

		assertNotNull(entity.getId());
		assertEquals("Anderson", entity.getName());
		assertEquals(41, entity.getAge());
		assertEquals(1.93, entity.getHeight());
		assertEquals(LocalDate.parse("1983-03-29", DateTimeFormatter.ISO_LOCAL_DATE), entity.getBirthDate());
		assertEquals(LocalDateTime.parse("2024-02-01T02:52:54", DateTimeFormatter.ISO_LOCAL_DATE_TIME), entity.getProhibited());
		assertNotEquals(0, entity.hashCode());
		assertNotNull(entity.getEntityTest2());

		assertNotNull(entity);
	}
	
	@Test
	void teste_busca_com_equal_pelo_name_unico_registro_trazend_mais_de_um() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/single/jpql/EntityTest1/buscaComLikePeloName?name=Paulo";

		ErrorResponse errorResponse = new ErrorResponse("Query did not return a unique result: 2 results were returned", 400);
		
	    assertThrows(RuntimeException.class, () -> getSingleResult(url, errorResponse));
	}
	
	@Test
	void teste_busca_com_like_pelo_name() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/jpql/EntityTest1/buscaComLikePeloName?name=ar&sortList=birthDate,name&sortOrders=asc,desc";

		List<EntityTest1> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(5, list.size());
		assertEquals("Carlos", list.get(0).getName());
		assertEquals("Carlos Alberto", list.get(1).getName());
		assertEquals("Ricardo", list.get(2).getName());
		assertEquals("Ariovaldo", list.get(3).getName());
		assertEquals("Maria", list.get(4).getName());
	}
	
	@Test
	void teste_busca_all_ordernacao_birthDate_asc_name_desc() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/jpql/EntityTest1/buscaAll?sortList=birthDate,name&sortOrders=asc,desc";

		List<EntityTest1> list = getAll(url, new ErrorResponse());

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
	void teste_busca_all_ordernacao_birthDate_desc_name_asc() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/jpql/EntityTest1/buscaAll?sortList=birthDate,name&sortOrders=desc,asc";

		List<EntityTest1> list = getAll(url, new ErrorResponse());

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
	void teste_busca_all_ordernacao_name_asc_birthDate_desc() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/jpql/EntityTest1/buscaAll?sortList=name,birthDate&sortOrders=asc,desc";

		List<EntityTest1> list = getAll(url, new ErrorResponse());
		
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
	void teste_busca_all_ordernacao_name_desc_birthDate_asc() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/jpql/EntityTest1/buscaAll?sortList=name,birthDate&sortOrders=desc,asc";

		List<EntityTest1> list = getAll(url, new ErrorResponse());
		
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
	void teste_nenhum_registro_encontrado() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/jpql/EntityTest1/buscaComLikePeloName?name=Silva&sortList=name,birthDate&sortOrders=desc,asc";
		
		List<EntityTest1> list = getAll(url, new ErrorResponse());
		
		assertNull(list);
	}
	
	@Test
	void teste_count_maior_prohibited() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/count/jpql/EntityTest1/countMaiorProhibited?prohibited=2024-11-01T08:00:00";
		
		Long count = getCountResult(url);
		
		assertEquals(4, count);
	}
	
	public List<EntityTest1> getAll(String url, ErrorResponse compare) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			return convertResponseToEntityTest1List(response.getBody());
		} else {
			ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertEquals(compare.getMessage(), errorResponse.getMessage());
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: " + errorResponse.getMessage());
		}
	}

	public EntityTest1 getSingleResult(String url, ErrorResponse compare) {
		
	    HttpHeaders headers = new HttpHeaders();
	    
	    headers.set("Accept", "application/json");

	    HttpEntity<String> entity = new HttpEntity<>(headers);

	    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

	    if (response.getStatusCode().is2xxSuccessful()) {
	        return convertResponseToEntityTest1(response.getBody());
	    } else {
	    	ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertEquals(compare.getMessage(), errorResponse.getMessage());
	        throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: " + errorResponse.getMessage());
	    }
	}
	
	private Long getCountResult(String url) {
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.set("Accept", "application/json");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<Long> response = restTemplate.exchange(url, HttpMethod.GET, entity, Long.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		} else {
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode());
		}
	}
	
	private ObjectMapper createObjectMapper() {
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.registerModule(new JavaTimeModule());
	    objectMapper.findAndRegisterModules(); 
	    return objectMapper;
	}

	private List<EntityTest1> convertResponseToEntityTest1List(String body) {
	    ObjectMapper objectMapper = createObjectMapper();
	    try {
	    	if(body != null) {
	    		return objectMapper.readValue(body, new TypeReference<List<EntityTest1>>(){});
	    	} else {
	    		return null;
	    	}
	    } catch (JsonProcessingException e) {
	        throw new RuntimeException("Error parsing EntityTest1 list response", e);
	    }
	}

	private EntityTest1 convertResponseToEntityTest1(String body) {
	    ObjectMapper objectMapper = createObjectMapper();
	    try {
	    	if(body != null) {
	    		return objectMapper.readValue(body, EntityTest1.class);
	    	} else {
	    		return null;
	    	}
	    } catch (JsonProcessingException e) {
	        throw new RuntimeException("Error parsing EntityTest1 response", e);
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
