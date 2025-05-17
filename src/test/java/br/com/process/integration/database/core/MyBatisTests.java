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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.process.integration.database.core.exception.ErrorResponse;
import br.com.process.integration.database.core.ui.QueryMyBatisController;
import br.com.process.integration.database.core.util.Constants;
import br.com.process.integration.database.model.data.dto.example.EntityEightData;
import br.com.process.integration.database.model.data.dto.example.EntityFiveData;
import br.com.process.integration.database.model.data.dto.example.EntityFourData;
import br.com.process.integration.database.model.data.dto.example.EntityNineData;
import br.com.process.integration.database.model.data.dto.example.EntityOneData;
import br.com.process.integration.database.model.data.dto.example.EntitySixData;
import br.com.process.integration.database.model.data.dto.example.EntityStatusData;
import br.com.process.integration.database.model.data.dto.example.EntityTreeData;
import br.com.process.integration.database.model.data.dto.example.EntityTwoData;

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
	private ObjectMapper objectMapper;

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
	@Order(1)
	void teste_01() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING
				+ "/mapper/count/EntityOneData/countEntitiesByName?" + "name=Ariovaldo";

		int count = Integer.parseInt(getUniqueResult(url, new ErrorResponse()));

		assertEquals(1, count);
	}

	@Test
	@Order(2)
	void teste_02() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING
				+ "/mapper/single/EntityOneData/findEntityDataByName?" + "name=Pedro";

		EntityOneData data = getSingleResult(url, new ErrorResponse());

		assertNull(data);

	}

	@Test
	@Order(3)
	void teste_03() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/single/EntityOneData/methodNoMapping?"
				+ "name=Anderson";

		single_parameterized_one_erro(url,
				"Invalid bound statement (not found): br.com.process.integration.database.model.data.mapper.example.EntityOneDataMapper.methodNoMapping");

	}

	@Test
	@Order(4)
	void teste_04() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/count/EntityOneData/methodNoMapping?"
				+ "id=1";

		single_parameterized_one_erro(url,
				"Invalid bound statement (not found): br.com.process.integration.database.model.data.mapper.example.EntityOneDataMapper.methodNoMapping");

	}

	@Test
	@Order(5)
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
	@Order(6)
	void teste_06() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/EntityOneData/findEntityOneByName?"
				+ "name=Pedro&" + "height=1.9&" + "sortList=name&" + "sortOrders=asc";

		List<EntityOneData> list = getAll(url, new ErrorResponse());

		assertNull(list);
	}

	@Test
	@Order(7)
	void teste_07() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/EntityOneData/findEntityOneByName?"
				+ "name=Ariovaldo&" + "height=1.91&" + "sortList=name&" + "sortOrders=asc";

		List<EntityOneData> list = getAll(url, new ErrorResponse());

		assertNotNull(list);

	}

	@Test
	@Order(8)
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
	@Order(9)
	void teste_09() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/EntityOneData/methodNoMapping?"
				+ "name=Pedro";

		single_parameterized_one_erro(url,
				"Invalid bound statement (not found): br.com.process.integration.database.model.data.mapper.example.EntityOneDataMapper.methodNoMapping");
	}

	@Test
	@Order(10)
	void teste_10() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/EntityOneData/findEntityDataByName?"
				+ "name=ar&" + "sortList=name,age&" + "sortOrders=asc,desc";

		single_parameterized_one_erro(url,
				"Erro: Numero de parametros incorretos para o method: 'findEntityDataByName'");

	}

	@Test
	@Order(11)
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
	
	@Test
	@Order(12)
	void teste_12() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/EntityOneData/findAll";

		List<EntityOneData> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(10, list.size());
	}
	
	@Test
	@Order(13)
	void teste_13() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/EntityTwoData/findAll";

		List<EntityTwoData> list = getAllEntityTwoData(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(10, list.size());
	}
	
	@Test
	@Order(14)
	void teste_14() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/EntityTreeData/findAll";

		List<EntityTreeData> list = getAllEntityTreeData(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(10, list.size());
	}
	
	@Test
	@Order(15)
	void teste_15() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/EntityFourData/findAll";

		List<EntityFourData> list = getAllEntityFourData(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(10, list.size());
	}
	
	@Test
	@Order(16)
	void teste_16() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/EntityFiveData/findAll";

		List<EntityFiveData> list = getAllEntityFiveData(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(10, list.size());
	}
	
	@Test
	@Order(17)
	void teste_17() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/EntityStatusData/findAll";

		List<EntityStatusData> list = getAllEntityStatusData(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(5, list.size());
	}
	
	@Test
	@Order(18)
	void teste_18() {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/by-id/entityOneData/" + SaveTests.id;
		
		EntityOneData entityOneData = getSingleResult(url, new ErrorResponse());

		assertNotNull(entityOneData);
	}
	
	@Test
	@Order(19)
	void teste_19() {
		
		String id = null;

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/entityTwoData/findAll";

		List<EntityTwoData> list = getAllEntityTwoData(url, new ErrorResponse());

		assertNotNull(list);
		
		for (EntityTwoData entityTwoData : list) {
			id = entityTwoData.getId().toString();
		}
		
		url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/by-id/entityTwoData/" + id;
		
		EntityTwoData entityTwoData = getSingleResultEntityTwoData(url, new ErrorResponse());

		assertNotNull(entityTwoData);
	}
	
	@Test
	@Order(20)
	void teste_20() {
		
		Long id = null;

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/entityStatusData/findAll";

		List<EntityStatusData> list = getAllEntityStatusData(url, new ErrorResponse());

		assertNotNull(list);
		
		for (EntityStatusData entityStatusData : list) {
			id = entityStatusData.getId();
		}
		
		url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/by-id/entityStatusData/" + id;
		
		EntityStatusData entityStatusData = getSingleResultEntityStatusData(url, new ErrorResponse());

		assertNotNull(entityStatusData);
	}
	
	@Test
	@Order(21)
	void teste_21() throws Exception {
		
		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/by-ids/entityNineData?idEntityEight=1&idEntitySeven=1&idEntitySix=1";
		
		String data = getUniqueResult(url, new ErrorResponse());
		
		EntityNineData entityNineData = objectMapper.readValue(data, EntityNineData.class);

		assertNotNull(data);
		assertNotNull(entityNineData);
		assertEquals(1l, entityNineData.getId().getIdEntityEight());
		assertEquals(1l, entityNineData.getId().getIdEntitySeven());
		assertEquals(1l, entityNineData.getId().getIdEntitySix());
		assertEquals("10", entityNineData.getCode());
	}
	
	@Test
	@Order(22)
	void teste_22() throws Exception {
		
		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/by-id/EntityEightData/2";
		
		String data = getUniqueResult(url, new ErrorResponse());
		
		EntityEightData entityEightData = objectMapper.readValue(data, EntityEightData.class);

		assertNotNull(data);
		assertNotNull(entityEightData);
	}
	
	@Test
	@Order(23)
	void teste_23() throws Exception {
		
		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/mapper/by-id/EntitySixData/1";
		
		String data = getUniqueResult(url, new ErrorResponse());
		
		EntitySixData entitySixData = objectMapper.readValue(data, EntitySixData.class);

		assertNotNull(data);
		assertNotNull(entitySixData);
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
	
	public List<EntityTwoData> getAllEntityTwoData(String url, ErrorResponse compare) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return convertResponseToEntityTwoDataList(response.getBody());
		} else {
			ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertTrue(errorResponse.getMessage().contains(compare.getMessage()));
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: " + errorResponse.getMessage());
		}
	}
	
	public List<EntityTreeData> getAllEntityTreeData(String url, ErrorResponse compare) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return convertResponseToEntityTreeDataList(response.getBody());
		} else {
			ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertTrue(errorResponse.getMessage().contains(compare.getMessage()));
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: "
					+ errorResponse.getMessage());
		}
	}	

	public List<EntityFourData> getAllEntityFourData(String url, ErrorResponse compare) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return convertResponseToEntityFourDataList(response.getBody());
		} else {
			ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertTrue(errorResponse.getMessage().contains(compare.getMessage()));
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: "
					+ errorResponse.getMessage());
		}
	}
	
	public List<EntityFiveData> getAllEntityFiveData(String url, ErrorResponse compare) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return convertResponseToEntityFiveDataList(response.getBody());
		} else {
			ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertTrue(errorResponse.getMessage().contains(compare.getMessage()));
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: "
					+ errorResponse.getMessage());
		}
	}	
	
	public List<EntityStatusData> getAllEntityStatusData(String url, ErrorResponse compare) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return convertResponseToEntityStatusDataList(response.getBody());
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
	
	public EntityTwoData getSingleResultEntityTwoData(String url, ErrorResponse compare) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return convertResponseToEntityTwoData(response.getBody());
		} else {
			ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertTrue(errorResponse.getMessage().contains(compare.getMessage()));
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: "
					+ errorResponse.getMessage());
		}
	}
	
	public EntityTreeData getSingleResultEntityTreeData(String url, ErrorResponse compare) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return convertResponseToEntityTreeData(response.getBody());
		} else {
			ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertTrue(errorResponse.getMessage().contains(compare.getMessage()));
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: "
					+ errorResponse.getMessage());
		}
	}
	
	public EntityFourData getSingleResultEntityFourData(String url, ErrorResponse compare) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return convertResponseToEntityFourData(response.getBody());
		} else {
			ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertTrue(errorResponse.getMessage().contains(compare.getMessage()));
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: "
					+ errorResponse.getMessage());
		}
	}	
	
	public EntityFiveData getSingleResultEntityFiveData(String url, ErrorResponse compare) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return convertResponseToEntityFiveData(response.getBody());
		} else {
			ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertTrue(errorResponse.getMessage().contains(compare.getMessage()));
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: "
					+ errorResponse.getMessage());
		}
	}	
	
	public EntityStatusData getSingleResultEntityStatusData(String url, ErrorResponse compare) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return convertResponseToEntityStatusData(response.getBody());
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

	private List<EntityOneData> convertResponseToEntityOneDataList(String body) {
		try {
			if (body != null) {
				System.out.println("JSON recebido: " + body);
				return objectMapper.readValue(body, new TypeReference<List<EntityOneData>>(){});
			} else {
				return null;
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error parsing EntityOneData list response", e);
		}
	}
	
	private List<EntityTwoData> convertResponseToEntityTwoDataList(String body) {
		try {
			if (body != null) {
				System.out.println("JSON recebido: " + body);
				return objectMapper.readValue(body, new TypeReference<List<EntityTwoData>>(){});
			} else {
				return null;
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error parsing EntityTwoData list response", e);
		}
	}	
	
	private List<EntityTreeData> convertResponseToEntityTreeDataList(String body) {
		try {
			if (body != null) {
				System.out.println("JSON recebido: " + body);
				return objectMapper.readValue(body, new TypeReference<List<EntityTreeData>>(){});
			} else {
				return null;
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error parsing EntityTreeData list response", e);
		}
	}
	
	private List<EntityFourData> convertResponseToEntityFourDataList(String body) {
		try {
			if (body != null) {
				System.out.println("JSON recebido: " + body);
				return objectMapper.readValue(body, new TypeReference<List<EntityFourData>>() {
				});
			} else {
				return null;
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error parsing EntityFourData list response", e);
		}
	}
	
	private List<EntityFiveData> convertResponseToEntityFiveDataList(String body) {
		try {
			if (body != null) {
				System.out.println("JSON recebido: " + body);
				return objectMapper.readValue(body, new TypeReference<List<EntityFiveData>>() {
				});
			} else {
				return null;
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error parsing EntityFiveData list response", e);
		}
	}
	
	private List<EntityStatusData> convertResponseToEntityStatusDataList(String body) {
		try {
			if (body != null) {
				System.out.println("JSON recebido: " + body);
				return objectMapper.readValue(body, new TypeReference<List<EntityStatusData>>() {
				});
			} else {
				return null;
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error parsing EntityFiveData list response", e);
		}
	}

	private EntityOneData convertResponseToEntityOneData(String body) {
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
	
	private EntityTwoData convertResponseToEntityTwoData(String body) {
		try {
			if (body != null) {
				return objectMapper.readValue(body, EntityTwoData.class);
			} else {
				return null;
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error parsing EntityTwoData response", e);
		}
	}
	
	private EntityTreeData convertResponseToEntityTreeData(String body) {
		try {
			if (body != null) {
				return objectMapper.readValue(body, EntityTreeData.class);
			} else {
				return null;
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error parsing EntityTreeData response", e);
		}
	}	
	
	private EntityFourData convertResponseToEntityFourData(String body) {
		try {
			if (body != null) {
				return objectMapper.readValue(body, EntityFourData.class);
			} else {
				return null;
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error parsing EntityFourData response", e);
		}
	}
	
	private EntityFiveData convertResponseToEntityFiveData(String body) {
		try {
			if (body != null) {
				return objectMapper.readValue(body, EntityFiveData.class);
			} else {
				return null;
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error parsing EntityFiveData response", e);
		}
	}	

	private EntityStatusData convertResponseToEntityStatusData(String body) {
		try {
			if (body != null) {
				return objectMapper.readValue(body, EntityStatusData.class);
			} else {
				return null;
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error parsing EntityStatusData response", e);
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
