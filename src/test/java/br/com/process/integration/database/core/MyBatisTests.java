package br.com.process.integration.database.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import br.com.process.integration.database.core.exception.ErrorResponse;
import br.com.process.integration.database.core.ui.QueryMyBatisController;
import br.com.process.integration.database.core.util.Constants;
import br.com.process.integration.database.model.data.dto.example.EntityOneData;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MyBatisTests {

	@LocalServerPort
	private int port;

	private static final String PATH = "http://localhost:";

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
	void teste_01() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING
				+ "/mapper/count/EntityOneData/countEntitiesByName?" + "name=Ariovaldo";

		int count = Integer.parseInt(getUniqueResult(url, new ErrorResponse()));

		assertEquals(1, count);
	}

	@Test
	void teste_02() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING
				+ "/mapper/single/EntityOneData/findEntityDataByName?" + "name=Pedro";

		EntityOneData data = getSingleResult(url, new ErrorResponse());

		assertNull(data);

	}

	@Test
	void teste_03() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/single/EntityOneData/methodNoMapping?"
				+ "name=Anderson";

		single_parameterized_one_erro(url,
				"Invalid bound statement (not found): br.com.process.integration.database.model.data.mapper.example.EntityOneDataMapper.methodNoMapping");

	}

	@Test
	void teste_04() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/count/EntityOneData/methodNoMapping?"
				+ "id=1";

		single_parameterized_one_erro(url,
				"Invalid bound statement (not found): br.com.process.integration.database.model.data.mapper.example.EntityOneDataMapper.methodNoMapping");

	}

	@Test
	void teste_05() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING
				+ "/mapper/single/EntityOneData/findEntityDataByName?" + "name=Anderson";

		EntityOneData data = getSingleResult(url, new ErrorResponse());

		assertNotNull(data.getId());
		assertEquals("Anderson", data.getName());
		assertEquals(41, data.getAge());
		assertEquals(1.93, data.getHeight());
		assertEquals(LocalDate.parse("1983-03-29", DateTimeFormatter.ISO_LOCAL_DATE), data.getBirthDate());
		assertEquals(LocalDateTime.parse("2024-02-01T02:52:54", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
				data.getProhibitedDateTime());
		assertNotEquals(0, data.hashCode());
		assertNotNull(data.getEntityTwoData());

		assertNotNull(data);
	}

	@Test
	void teste_06() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/EntityOneData/findEntityOneByName?"
				+ "name=Pedro&" + "height=1.9&" + "sortList=name&" + "sortOrders=asc";

		List<EntityOneData> list = getAll(url, new ErrorResponse());

		assertNull(list);
	}

	@Test
	void teste_07() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/EntityOneData/findEntityOneByName?"
				+ "name=Ariovaldo&" + "height=1.91&" + "sortList=name&" + "sortOrders=asc";

		List<EntityOneData> list = getAll(url, new ErrorResponse());

		assertNotNull(list);

	}

	@Test
	void teste_08() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/EntityOneData/findEntityOneByName?"
				+ "name=ar&" + "height=1.9&" + "sortList=name&" + "sortOrders=asc";

		List<EntityOneData> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("Carlos", list.get(0).getName());
		assertEquals("Carlos Alberto", list.get(1).getName());
		assertEquals("Maria", list.get(2).getName());
		assertEquals("Ricardo", list.get(3).getName());
	}

	@Test
	void teste_09() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/EntityOneData/methodNoMapping?"
				+ "name=Pedro";

		single_parameterized_one_erro(url,
				"Invalid bound statement (not found): br.com.process.integration.database.model.data.mapper.example.EntityOneDataMapper.methodNoMapping");
	}

	@Test
	void teste_10() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/EntityOneData/findEntityDataByName?"
				+ "name=ar&" + "sortList=name,age&" + "sortOrders=asc,desc";

		single_parameterized_one_erro(url,
				"Erro: Numero de parametros incorretos para o method: 'findEntityDataByName'");

	}

	@Test
	void teste_11() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/EntityOneData/findEntityOneByName?"
				+ "name=ar&" + "height=1.9&" + "sortList=name,age&" + "sortOrders=asc,desc";

		List<EntityOneData> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("Carlos", list.get(0).getName());
		assertEquals(34, list.get(0).getAge());
		assertEquals("Carlos Alberto", list.get(1).getName());
		assertEquals(41, list.get(1).getAge());
		assertEquals("Maria", list.get(2).getName());
		assertEquals(12, list.get(2).getAge());
		assertEquals("Ricardo", list.get(3).getName());
		assertEquals(22, list.get(3).getAge());
	}

	public void single_parameterized_one_erro(String url, String message) {

		ErrorResponse errorResponse = new ErrorResponse(message, HttpStatus.BAD_REQUEST);

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
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: "
					+ errorResponse.getMessage());
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
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: "
					+ errorResponse.getMessage());
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
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: "
					+ errorResponse.getMessage());
		}
	}

	private ObjectMapper createObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();

		JavaTimeModule module = new JavaTimeModule();
		module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		module.addSerializer(LocalDateTime.class,
				new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
		objectMapper.registerModule(module);

		objectMapper.registerModule(new Jackson2HalModule()); // ← suporte a RepresentationModel
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

		return objectMapper;
	}

	private List<EntityOneData> convertResponseToEntityOneDataList(String body) {
		ObjectMapper objectMapper = createObjectMapper();
		try {
			if (body != null) {
				System.out.println("JSON recebido: " + body); // ← debug importante
				return objectMapper.readValue(body, new TypeReference<List<EntityOneData>>() {
				});
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
			if (body != null) {
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
