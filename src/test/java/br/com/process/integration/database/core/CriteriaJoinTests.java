package br.com.process.integration.database.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.process.integration.database.core.exception.ErrorResponse;
import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.model.entity.dto.example.EntityFive;
import br.com.process.integration.database.model.entity.dto.example.EntityFour;
import br.com.process.integration.database.model.entity.dto.example.EntityOne;
import br.com.process.integration.database.model.entity.dto.example.EntityTree;
import br.com.process.integration.database.model.entity.dto.example.EntityTwo;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CriteriaJoinTests {

	@LocalServerPort
	private int port;
	
	private static final String PATH = "http://localhost:";

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private QueryJpaController queryJpaController;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@BeforeAll
	void setupOnce() { }

	@Test
	@Order(1)
	void contextLoads() {
		assertNotNull(queryJpaController);
	};

	@Test
	void teste_01() throws Exception {

	    String url = PATH
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/EntityOne?"
	            + "entityTwo.color=Preto&"
	            + "entityTwo.color_op=eq&"
	            + "sortList=entityTwo.inclusionDate, entityTwo.hex&"
	            + "sortOrders=desc,asc";
	    
	    List<EntityOne> list = getAll(url, new ErrorResponse());
	    
	    assertNotNull(list);
	    assertEquals(2, list.size());
	    assertEquals("Carlos Alberto", list.get(0).getName());
	    assertEquals("Carlos", list.get(1).getName());
	    
	    EntityTwo entityTwo = objectMapper.readValue(findById("entityTwo", list.get(0).getIdEntityTwo()), EntityTwo.class);
	    assertEquals(10, entityTwo.getHex());
	    entityTwo = objectMapper.readValue(findById("entityTwo", list.get(1).getIdEntityTwo()), EntityTwo.class);
	    assertEquals(144, entityTwo.getHex());
	}

	@Test
	void teste_02() throws Exception {

	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/EntityOne?"
	            + "entityTwo.entityTree.animal=Cavalo,Gato,Papagaio&"
	            + "entityTwo.entityTree.animal_op=in&"
	            + "page=0&"
	            + "size=10&"
	            + "sortList=entityTwo.color,entityTwo.entityTree.animal&"
	            + "sortOrders=desc,asc";

	    List<EntityOne> list = getAll(url, new ErrorResponse());
	    
	    EntityTwo entityTwo = null;
		EntityTree entityTree = null;

	    assertNotNull(list);
	    assertEquals(5, list.size());
	    
	    assertEquals("Paulo Henrique", list.get(0).getName());
	    assertEquals("Ricardo", list.get(1).getName());
	    assertEquals("Paulo", list.get(2).getName());
	    assertEquals("Renato", list.get(3).getName());
	    assertEquals("Ariovaldo", list.get(4).getName());
	    
	    entityTwo = objectMapper.readValue(findById("entityTwo", list.get(0).getIdEntityTwo()), EntityTwo.class);
	    assertEquals("Verde", entityTwo.getColor());
	    entityTree = objectMapper.readValue(findById("entityTree", entityTwo.getIdEntityTree()), EntityTree.class);
	    assertEquals("Cavalo", entityTree.getAnimal());

	    entityTwo = objectMapper.readValue(findById("entityTwo", list.get(1).getIdEntityTwo()), EntityTwo.class);
	    assertEquals("Verde", entityTwo.getColor());
	    entityTree = objectMapper.readValue(findById("entityTree", entityTwo.getIdEntityTree()), EntityTree.class);
	    assertEquals("Gato", entityTree.getAnimal());
	    
	    entityTwo = objectMapper.readValue(findById("entityTwo", list.get(2).getIdEntityTwo()), EntityTwo.class);
	    assertEquals("Roxo", entityTwo.getColor());
	    entityTree = objectMapper.readValue(findById("entityTree", entityTwo.getIdEntityTree()), EntityTree.class);
	    assertEquals("Cavalo", entityTree.getAnimal());
	    
	    entityTwo = objectMapper.readValue(findById("entityTwo", list.get(3).getIdEntityTwo()), EntityTwo.class);
	    assertEquals("Laranja", entityTwo.getColor());
	    entityTree = objectMapper.readValue(findById("entityTree", entityTwo.getIdEntityTree()), EntityTree.class);
	    assertEquals("Papagaio", entityTree.getAnimal());
	    
	    entityTwo = objectMapper.readValue(findById("entityTwo", list.get(4).getIdEntityTwo()), EntityTwo.class);
	    assertEquals("Cinza", entityTwo.getColor());
		entityTree = objectMapper.readValue(findById("entityTree", entityTwo.getIdEntityTree()), EntityTree.class);
	    assertEquals("Gato", entityTree.getAnimal());
	}

	@Test
	void teste_03() {

	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/EntityOne?"
	            + "entityTwo.entityTree.animal=Capivara&"
	            + "entityTwo.entityTree.animal_op=eq";

	    List<EntityOne> list = getAll(url, new ErrorResponse());

	    list.forEach(entity -> {
	        assertEquals("Carlos Alberto", entity.getName());
	    });

	    assertNotNull(list);
	    assertEquals(1, list.size());
	}

	@Test
	void teste_04() {

	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/EntityOne?"
	            + "entityTwo.entityTree.entityFour.fruit=Pitanga&"
	            + "entityTwo.entityTree.entityFour.fruit_op=eq";

	    List<EntityOne> list = getAll(url, new ErrorResponse());

	    assertNotNull(list);
	    assertEquals(2, list.size());
	}

	@Test
	void teste_05() throws Exception {

	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/EntityOne?"
	            + "entityTwo.entityTree.entityFour.entityFive.reference=Cama&"
	            + "entityTwo.entityTree.entityFour.entityFive.reference_op=eq";

	    List<EntityOne> list = getAll(url, new ErrorResponse());

	    assertNotNull(list);
	    assertEquals(1, list.size());

	    for (EntityOne entity : list) {
			EntityTwo entityTwo = objectMapper.readValue(findById("entityTwo", entity.getIdEntityTwo()), EntityTwo.class);
			EntityTree entityTree = objectMapper.readValue(findById("entityTree", entityTwo.getIdEntityTree()), EntityTree.class);
			EntityFour entityFour = objectMapper.readValue(findById("entityFour", entityTree.getIdEntityFour()), EntityFour.class);
			EntityFive entityFive = objectMapper.readValue(findById("entityFive", entityFour.getIdEntityFive()), EntityFive.class);
	        assertEquals("Cama", entityFive.getReference());
	    };

	}

	@Test
	void teste_06() {

	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/EntityOne?"
	            + "entityTwo.entityTree.entityFour.entityFive.reference=C*&"
	            + "entityTwo.entityTree.entityFour.entityFive.reference_op=lk";

	    List<EntityOne> list = getAll(url, new ErrorResponse());

	    assertNotNull(list);
	    assertEquals(4, list.size());
	}

	public List<EntityOne> getAll(String url, ErrorResponse compare) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
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

	public EntityOne getSingleResult(String url, ErrorResponse compare) {
	    HttpHeaders headers = new HttpHeaders();
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
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
	
	private ObjectMapper createObjectMapper() {
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.registerModule(new JavaTimeModule());
	    objectMapper.findAndRegisterModules(); 
	    return objectMapper;
	}

	private List<EntityOne> convertResponseToEntityOneList(String body) {
	    ObjectMapper objectMapper = createObjectMapper();
	    try {
	    	if(body != null) {
	    		return objectMapper.readValue(body, new TypeReference<List<EntityOne>>(){});
	    	} else {
	    		return null;
	    	}
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
	
	public String findById(String clazz, Object id) {
		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/" + clazz + "/" + id;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		} 
		return null;
	}
}
