package br.com.process.integration.database.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.process.integration.database.core.exception.ErrorResponse;
import br.com.process.integration.database.core.ui.QueryMyBatisController;
import br.com.process.integration.database.core.util.Constants;
import br.com.process.integration.database.model.data.dto.example.EntityOneData;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MyBatisPaginatorTests {

	@LocalServerPort
	private int port;
	
	private static final String PATH = "http://localhost:";

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private QueryMyBatisController queryMyBatisController;
	
	@BeforeAll
	void setupOnce() { }
	
	@Test
	@Order(1)
	void contextLoads() {
		assertNotNull(queryMyBatisController);
	};

	@Test
	void teste_01() {
		
		String url = PATH 
				+ port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/mapper/paginator/EntityOneData/findEntityOneByAll?"
	            + "code=true&"
	            + "page=0&"
	            + "size=10&"
	            + "sortList=name&"
	            + "sortOrders=desc";

		List<EntityOneData> list = getAll(url, new ErrorResponse());

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
	void teste_02() {

		String url = PATH 
				+ port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/mapper/paginator/EntityOneData/findEntityOneByAll?"
	            + "code=2&"
	            + "page=0&"
	            + "size=2&"
	            + "sortList=name&"
	            + "sortOrders=desc";
		
		List<EntityOneData> list = getAll(url, new ErrorResponse());

		assertNull(list);
	}
	
	
	@Test
	void teste_03() {

		String url = PATH
				+ port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/mapper/paginator/EntityOneData/findEntityOneByAll?"
	            + "page=0&"
	            + "size=0";
		
		ErrorResponse errorResponse = new ErrorResponse("Page size must not be less than one", HttpStatus.BAD_REQUEST);

		assertThrows(RuntimeException.class, () -> getAll(url, errorResponse));
	}
	
	private List<EntityOneData> getAll(String url, ErrorResponse compare) {
		List<EntityOneData> list = null;
		PagedModel<EntityOneData> page = getRestAll(url, compare);
		if(page != null) {
			list = convertToEntityOneDataList(page.getContent());
			assertNotNull(list);
			assertEquals(list.size(), page.getContent().size());
		} else {
			assertNull(page);
		}
		return list;
	}

	private PagedModel<EntityOneData> getRestAll(String url, ErrorResponse compare) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return convertResponseToPagedModel(response.getBody());
		} else {
			ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody().toString());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertTrue(errorResponse.getMessage().contains(compare.getMessage()));
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: "
					+ errorResponse.getMessage());
		}
	}

	private PagedModel<EntityOneData> convertResponseToPagedModel(String body) {
		if(body == null) return null;
		ObjectMapper objectMapper = createObjectMapper();
		try {
			return objectMapper.readValue(body, new TypeReference<PagedModel<EntityOneData>>() {
			});
		} catch (Exception e) {
			throw new RuntimeException("Error parsing PagedModel<EntityOneData> response", e);
		}
	}

	private ErrorResponse convertResponseToErrorResponse(String body) {
		ObjectMapper objectMapper = createObjectMapper();
		try {
			return objectMapper.readValue(body, ErrorResponse.class);
		} catch (Exception e) {
			throw new RuntimeException("Error parsing ErrorResponse", e);
		}
	}

	private ObjectMapper createObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper;
	}

	public List<EntityOneData> convertToEntityOneDataList(Collection<EntityOneData> collection) {
		return collection.stream().map(this::convertToEntityOneData).collect(Collectors.toList());
	}

	private EntityOneData convertToEntityOneData(EntityOneData element) {
		return element;
	}
}
