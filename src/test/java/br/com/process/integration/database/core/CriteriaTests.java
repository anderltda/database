package br.com.process.integration.database.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.model.data.dto.example.EntityNineData;
import br.com.process.integration.database.model.entity.dto.example.EntityOne;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CriteriaTests {

	@LocalServerPort
	private int port;
	
	private static final String PATH = "http://localhost:";

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;

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
	void teste_01() {

		String url = PATH 
		            + port 
		            + Constants.API_NAME_REQUEST_MAPPING 
		            + "/EntityOne?" 
		            + "name=Anderson&"
		            + "name_op=lk&"
		            + "sortList=name,age&"
		            + "sortOrders=asc,desc";
		
		List<EntityOne> list = getAll(url, new ErrorResponse());
		
		assertNotNull(list);
		assertEquals(1, list.size());
	}
	
	@Test
	void teste_02() {

		String url = PATH 
		            + port 
		            + Constants.API_NAME_REQUEST_MAPPING 
		            + "/EntityOne?" 
		            + "name=Anderson&"
		            + "name_op=lk&"
		            + "sortList=teste,age&"
		            + "sortOrders=asc,desc";
		
		ErrorResponse errorResponse = new ErrorResponse("Could not resolve attribute 'teste' of 'br.com.process.integration.database.model.entity.dto.example.EntityOne'", HttpStatus.BAD_REQUEST);
		
	    assertThrows(RuntimeException.class, () -> getAll(url, errorResponse));
	}
	
	@Test
	void teste_03() {
		
		String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/count/EntityOne?"
	            + "nome=*ar*&"
	            + "nome_op=lk";
		
		ErrorResponse errorResponse = new ErrorResponse("Atributo não encontrado: nome", HttpStatus.BAD_REQUEST);
		
	    assertThrows(RuntimeException.class, () -> getUniqueResult(url, errorResponse));
	}
	
	@Test
	void teste_04() {

		String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/EntityOne?" 
	            + "name=Anderson&"
	            + "name_op=lk&"
	            + "sortList=name,age";
			
		List<EntityOne> list = getAll(url, new ErrorResponse());
		
		assertNotNull(list);
		assertEquals(1, list.size());
		assertEquals("Anderson", list.get(0).getName());
	}
	
	@Test
	void teste_05() {

		String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/EntityOne?" 
	            + "name=Anderson&"
	            + "name_op=lk&"
	            + "sortList=name,asc&"
	            + "sortOrders=asc,desc";
		
	    testes_single_parameterized_one(url, "Could not resolve attribute 'asc' of 'br.com.process.integration.database.model.entity.dto.example.EntityOne'");
	}

	@Test
	void teste_06() {

		String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/EntityOne?" 
	            + "name=Anderson&"
	            + "name_op=eq";

		List<EntityOne> list = getAll(url, new ErrorResponse());
		
		assertNotNull(list);
		assertEquals(1, list.size());

		list.forEach(entity -> {
			assertNotNull(entity.getId());
			assertEquals("Anderson", entity.getName());
			assertEquals(41, entity.getAge());
			assertEquals(1.93, entity.getHeight());
			assertEquals(LocalDate.parse("1983-03-29", DateTimeFormatter.ISO_LOCAL_DATE), entity.getBirthDate());
			assertEquals(LocalDateTime.parse("2024-02-01T02:52:54", DateTimeFormatter.ISO_LOCAL_DATE_TIME), entity.getProhibitedDateTime());
			assertNotEquals(0, entity.hashCode()); 
			assertNotNull(entity.getIdEntityTwo());
		});
		
	}

	@Test
	void teste_07() {

		String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/EntityOne?" 
	            + "age=22&"
	            + "age_op=eq&"
	            + "birthDate=1990-01-01&"
	            + "birthDate_op=eq&"
	            + "prohibitedDateTime=2024-11-01T08:00:00&"
	            + "prohibitedDateTime_op=eq&"
	            + "sortList=name&"
	            + "sortOrders=asc";
		
		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(2, list.size());
		assertEquals("Ariovaldo", list.get(0).getName());
		assertEquals("Ricardo", list.get(1).getName());
	}

	@Test
	void teste_08() {

		String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/EntityOne?" 
	            + "age=22&"
	            + "age_op=ne&"
	            + "birthDate=1990-01-01&"
	            + "birthDate_op=ne&"
	            + "prohibitedDateTime=2024-11-01T08:00:00&"
	            + "prohibitedDateTime_op=ne&"
	            + "sortList=name&"
	            + "sortOrders=asc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(7, list.size());
		assertEquals("Anderson", list.get(0).getName());
		assertEquals("Carlos", list.get(1).getName());
		assertEquals("Carlos Alberto", list.get(2).getName());
		assertEquals("Maria", list.get(3).getName());
		assertEquals("Paulo", list.get(4).getName());
		assertEquals("Paulo Henrique", list.get(5).getName());
		assertEquals("Renato", list.get(6).getName());
	}
	
	@Test
	void teste_09() {
		
		String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/single/EntityOne?" 
	            + "age=41&"
	            + "age_op=eq&"
	            + "name=Pedro&"
	            + "name_op=eq&"
	            + "birthDate=1983-03-29&"
	            + "birthDate_op=ge";
		
		EntityOne entity = getSingleResult(url, new ErrorResponse());
		
		assertNull(entity);
	}
	
	@Test
	void teste_10() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/single/EntityOne?" 
	                + "age=22,41&" 
	                + "age_op=in";
	    
	    ErrorResponse errorResponse = new ErrorResponse("Query did not return a unique result: 4 results were returned", HttpStatus.BAD_REQUEST);
	    
	    assertThrows(RuntimeException.class, () -> getSingleResult(url, errorResponse));
	}

	@Test
	void teste_11() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "name=Carlos&"
	                + "name_op=ne&"
	                + "sortList=name&"
	                + "sortOrders=asc";
	    
	    testes_single_parameterized_one(url, 9);
	}

	@Test
	void teste_12() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "age=22&"
	                + "age_op=eq&"
	                + "birthDate=1990-01-01&"
	                + "birthDate_op=eq&"
	                + "height=1.8&"
	                + "height_op=eq";
	    
	    testes_single_parameterized_other(url, "Ricardo", 1);
	}

	@Test
	void teste_13() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "prohibitedDateTime=2024-11-01T08:00:00&"
	                + "prohibitedDateTime_op=eq&"
	                + "sortList=name&"
	                + "sortOrders=asc";
	    
	    List<EntityOne> list = getAll(url, new ErrorResponse());
	    
	    assertNotNull(list);
	    assertEquals(3, list.size());
	    assertEquals("Ariovaldo", list.get(0).getName());
	    assertEquals("Joana", list.get(1).getName());
	    assertEquals("Ricardo", list.get(2).getName());
	}

	@Test
	void teste_14() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "prohibitedDateTime=2024-11-01T08:00:00&"
	                + "prohibitedDateTime_op=eq&"
	                + "sortList=name&"
	                + "sortOrder=asc";

	    testes_single_parameterized_one(url, "Atributo não encontrado: sortOrder");
	}

	@Test
	void teste_15() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "name=*ar*&"
	                + "name_op=lk&"
	                + "height=1.9&"
	                + "height_op=ne&"
	                + "sortList=birthDate,name&"
	                + "sortOrders=desc,asc";
	    
	    List<EntityOne> list = getAll(url, new ErrorResponse());

	    assertNotNull(list);
	    assertEquals(4, list.size());
	    assertEquals("Maria", list.get(0).getName());
	    assertEquals("Ricardo", list.get(1).getName());
	    assertEquals("Carlos Alberto", list.get(2).getName());
	    assertEquals("Carlos", list.get(3).getName());
	}

	@Test
	void teste_16() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "name=Ar*&"
	                + "name_op=lk";
	    
	    testes_single_parameterized_other(url, "Ariovaldo", 1);
	}

	@Test
	void teste_17() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "birthDate=1956-08-30,1986-09-09,1990-09-09&"
	                + "birthDate_op=in&"
	                + "sortList=age&"
	                + "sortOrders=asc";
	    
	    List<EntityOne> list = getAll(url, new ErrorResponse());

	    assertNotNull(list);
	    assertEquals(3, list.size());
	    assertEquals("Paulo", list.get(0).getName());
	    assertEquals("Carlos", list.get(1).getName());
	    assertEquals("Paulo Henrique", list.get(2).getName());
	}

	@Test
	void teste_18() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "birthDate=1956-08-30,1990-01-01,1990-09-09&"
	                + "birthDate_op=in&"
	                + "sortList=age,height&"
	                + "sortOrders=desc,asc";
	    
	    List<EntityOne> list = getAll(url, new ErrorResponse());

	    assertNotNull(list);
	    assertEquals(4, list.size());
	    assertEquals("Carlos", list.get(0).getName());
	    assertEquals("Ricardo", list.get(1).getName());
	    assertEquals("Ariovaldo", list.get(2).getName());
	    assertEquals("Paulo", list.get(3).getName());
	}

	@Test
	void teste_19() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "age=55,12,22&"
	                + "age_op=in";

	    testes_single_parameterized_one(url, 3);
	}

	@Test
	void teste_20() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "id=" 
	                + SaveTests.ids.get(0) 
	                + ","
	                + SaveTests.ids.get(1) 
	                + "&"
	                + "id_op=in&"
	                + "sortList=id&"
	                + "sortOrders=asc";

	    testes_single_parameterized_one(url, 2);
	}

	@Test
	void teste_21() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "height=1.40,1.78&"
	                + "height_op=bt&"
	                + "sortList=height&"
	                + "sortOrders=desc";

	    List<EntityOne> list = getAll(url, new ErrorResponse());

	    assertNotNull(list);
	    assertEquals(3, list.size());
	    assertEquals("Paulo", list.get(0).getName());
	    assertEquals("Carlos", list.get(1).getName());
	    assertEquals("Maria", list.get(2).getName());
	}

	@Test
	void teste_22() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "height=1.40,&"
	                + "height_op=bt&"
	                + "sortList=height&"
	                + "sortOrders=desc";

	    testes_single_parameterized_one(url, "Index 1 out of bounds for length 1");
	}

	@Test
	void teste_23() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "prohibitedDateTime=2024-02-01T08:50:00,2024-10-01T08:50:55&"
	                + "prohibitedDateTime_op=bt&"
	                + "sortList=birthDate&"
	                + "sortOrders=desc";

	    List<EntityOne> list = getAll(url, new ErrorResponse());

	    assertNotNull(list);
	    assertEquals(4, list.size());
	    assertEquals("Maria", list.get(0).getName());
	    assertEquals("Paulo", list.get(1).getName());
	    assertEquals("Paulo Henrique", list.get(2).getName());
	    assertEquals("Carlos", list.get(3).getName());
	}

	@Test
	void teste_24() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "height=1.86&"
	                + "height_op=ge";
	    
	    testes_single_parameterized_one(url, 4);
	}

	@Test
	void teste_25() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "height=1.8A6&"
	                + "height_op=ge";

	    testes_single_parameterized_one(url, "For input string: \"1.8A6\"");
	}

	@Test
	void teste_26() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "height=1.87&"
	                + "height_op=gt";

	    testes_single_parameterized_one(url, 3);
	}

	@Test
	void teste_27() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "height=AWs&"
	                + "height_op=gt";

	    testes_single_parameterized_one(url, "For input string: \"AWs\"");
	}

	@Test
	void teste_28() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "birthDate=1990-09-09&"
	                + "birthDate_op=gt";

	    testes_single_parameterized_one(url, 3);
	}

	@Test
	void teste_29() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "birthDate=1990-01-02&"
	                + "birthDate_op=ge&"
	                + "birthDate=2016-01-01&"
	                + "birthDate_op=le";

	    testes_single_parameterized_one(url, 4);
	}

	@Test
	void teste_30() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "birthDate=1990-01-02&"
	                + "birthDate_op=le";

	    testes_single_parameterized_one(url, 6);
	}

	@Test
	void teste_31() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "age=Wre&"
	                + "age_op=le";

	    testes_single_parameterized_one(url, "For input string: \"Wre\"");
	}

	@Test
	void teste_32() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "birthDate=1986-09-08&"
	                + "birthDate_op=lt";

	    testes_single_parameterized_one(url, 3);
	}

	@Test
	void teste_33() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "age=21&"
	                + "age_op=le";

	    testes_single_parameterized_one(url, 2);
	}

	@Test
	void teste_34() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "age=QW&"
	                + "age_op=lt";

	    testes_single_parameterized_one(url, "For input string: \"QW\"");
	}

	@Test
	void teste_35() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "birthDate=2016-01-01&"
	                + "birthDate_op=ge";

	    testes_single_parameterized_other(url, "Maria", 1);
	}

	@Test
	void teste_36() {
		
	    String url = PATH 
	                + port 
	                + Constants.API_NAME_REQUEST_MAPPING 
	                + "/EntityOne?" 
	                + "age=21&"
	                + "age_op=lt";

	    testes_single_parameterized_other(url, "Maria", 1);
	}
	
	@Test
	void teste_37() {
		
		String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/EntityOne?" 
	            + "sortList=birthDate,name&"
	            + "sortOrders=asc,desc";
		
		List<EntityOne>list = getAll(url, new ErrorResponse());

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
	void teste_38() {
		
		String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/EntityOne?" 
	            + "sortList=birthDate,name&"
	            + "sortOrders=desc,asc";

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
	void teste_39() {

		String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/EntityOne?" 
	            + "sortList=name,birthDate&"
	            + "sortOrders=asc,desc";

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
	void teste_40() {

		String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/EntityOne?" 
	            + "sortList=name,birthDate&"
	            + "sortOrders=desc,asc";

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
	void teste_41() {

		String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/EntityOne/ids/" 
	            + SaveTests.ids.get(0) 
	            + "," 
	            + SaveTests.ids.get(1) 
	            + "," 
	            + SaveTests.ids.get(2);
		
		List<EntityOne> list = getAll(url, new ErrorResponse());
		
		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("Carlos Alberto", list.get(0).getName());
		assertEquals("Paulo", list.get(1).getName());
		assertEquals("Maria", list.get(2).getName());
	}
	
	@Test
	void teste_42() {
		
	    testes_result_is_null(PATH 
	        + port 
	        + Constants.API_NAME_REQUEST_MAPPING 
	        + "/EntityOne?name=Pedro&name_op=eq&sortList=name&sortOrders=asc");
	}

	@Test
	void teste_43() {
		
	    testes_result_is_null(PATH 
	        + port 
	        + Constants.API_NAME_REQUEST_MAPPING 
	        + "/EntityOne?name=Silva&name_op=eq");
	}

	@Test
	void teste_44() {
		
	    testes_result_is_null(PATH 
	        + port 
	        + Constants.API_NAME_REQUEST_MAPPING 
	        + "/EntityOne/ids/1,2,3");
	}

	@Test
	void teste_45() {
		
	    String url = PATH 
	        + port 
	        + Constants.API_NAME_REQUEST_MAPPING 
	        + "/single/EntityOne?"
	        + "age=41&"
	        + "age_op=eq&"
	        + "name=Anderson&"
	        + "name_op=eq&"
	        + "birthDate=1983-03-29&"
	        + "birthDate_op=ge";
	    
	    EntityOne entity = getSingleResult(url, new ErrorResponse());
	    
	    assertNotNull(entity);
	    assertEquals("Anderson", entity.getName());
	}

	@Test
	void teste_46() {
		
	    String url = PATH 
	        + port 
	        + Constants.API_NAME_REQUEST_MAPPING 
	        + "/EntityOne/" 
	        + SaveTests.ids.get(1);
	    
	    EntityOne entity = getSingleResult(url, new ErrorResponse());
	    
	    assertNotNull(entity);
	    assertEquals("Paulo", entity.getName());
	}

	@Test
	void teste_47() {
		
	    String url = PATH 
	        + port 
	        + Constants.API_NAME_REQUEST_MAPPING 
	        + "/EntityOne/1";
	    
	    EntityOne entity = getSingleResult(url, new ErrorResponse());
	    
	    assertNull(entity);
	}

	@Test
	void teste_48() {
		
	    String url = PATH 
	        + port 
	        + Constants.API_NAME_REQUEST_MAPPING 
	        + "/count/EntityOne?"
	        + "prohibitedDateTime=2024-11-01T08:00:00&"
	        + "prohibitedDateTime_op=ge";
	    
	    Integer count = Integer.parseInt(getUniqueResult(url, new ErrorResponse()));
	    
	    assertEquals(5, count);
	}

	@Test
	void teste_49() {
		
	    String url = PATH 
	        + port 
	        + Constants.API_NAME_REQUEST_MAPPING 
	        + "/exist/EntityOne/" 
	        + SaveTests.ids.get(1);
	    
	    Boolean value = Boolean.parseBoolean(getUniqueResult(url, new ErrorResponse()));
	    
	    assertTrue(value);
	}

	@Test
	void teste_50() {
		
	    String url = PATH 
	        + port 
	        + Constants.API_NAME_REQUEST_MAPPING 
	        + "/exist/EntityOne/1";
	    
	    Boolean value = Boolean.parseBoolean(getUniqueResult(url, new ErrorResponse()));
	    
	    assertFalse(value);
	}
	
	@Test
	void teste_51() throws Exception {
		
		String entitySevenId = SaveTests.entitySevenIdVo1.getIdEntitySeven().toString();

		String url = PATH 
	        + port 
	        + Constants.API_NAME_REQUEST_MAPPING 
	        + "/entityNine/ids?idEntityEight=1&idEntitySeven="+entitySevenId+"&idEntitySix=1";
	    
		String data = getUniqueResult(url, new ErrorResponse());
		
		EntityNineData entityNineData = objectMapper.readValue(data, EntityNineData.class);
		
		assertNotNull(data);
		assertNotNull(entityNineData);
		assertEquals(1l, entityNineData.getId().getIdEntityEight());
		assertEquals(entitySevenId, entityNineData.getId().getIdEntitySeven().toString());
		assertEquals(1l, entityNineData.getId().getIdEntitySix());
		assertEquals("11", entityNineData.getCode());
	}
	
	@Test
	void teste_52() throws Exception {
		
		String entitySevenId = SaveTests.entitySevenIdVo3.getIdEntitySeven().toString();
		
	    String url = PATH 
	        + port 
	        + Constants.API_NAME_REQUEST_MAPPING 
	        + "/entityNine/ids?idEntityEight=3&idEntitySeven="+entitySevenId+"&idEntitySix=3";
	    
		String data = getUniqueResult(url, new ErrorResponse());
		
		EntityNineData entityNineData = objectMapper.readValue(data, EntityNineData.class);
		
		assertNotNull(data);
		assertNotNull(entityNineData);
		assertEquals(3l, entityNineData.getId().getIdEntityEight());
		assertEquals(entitySevenId, entityNineData.getId().getIdEntitySeven().toString());
		assertEquals(3l, entityNineData.getId().getIdEntitySix());
		assertEquals("22", entityNineData.getCode());
	}
	
	void testes_single_parameterized_other(String url, String value, Integer size) {
		
		List<EntityOne> list = getAll(url, new ErrorResponse());
		
		assertNotNull(list);
		assertEquals(size, list.size());
		assertEquals(value, list.get(0).getName());
	}
	
	void testes_single_parameterized_one(String url, Integer size) {
		
		List<EntityOne> list = getAll(url, new ErrorResponse());
		
		assertNotNull(list);
		assertEquals(size, list.size());
	}
	
	void testes_single_parameterized_one(String url, String message) {
		
		ErrorResponse errorResponse = new ErrorResponse(message, HttpStatus.BAD_REQUEST);
		
	    assertThrows(RuntimeException.class, () -> getAll(url, errorResponse));
	}
	
	void testes_result_is_null(String url) {
		
		List<EntityOne> list = getAll(url, new ErrorResponse());
		
		assertNull(list);
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
	
	private List<EntityOne> convertResponseToEntityOneList(String body) {
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
