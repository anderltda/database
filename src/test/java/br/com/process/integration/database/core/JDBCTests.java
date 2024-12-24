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
import br.com.process.integration.database.core.ui.QueryNativeController;
import br.com.process.integration.database.core.util.Constants;
import br.com.process.integration.database.domain.model.view.EntityOneView;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JDBCTests {

	@LocalServerPort
	private int port;
	
	private static final String PATH = "http://localhost:";

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private QueryNativeController queryNativeController;
	
	@BeforeAll
	void setupOnce() { }
	
	@Test
	@Order(1)
	void contextLoads() {
		assertNotNull(queryNativeController);
	};
	
	@Test
	void teste_01() {
	    
		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/single/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "age=41,38,32&"
		        + "age_op=eq&"
		        + "name=*ar*&"
		        + "name_op=lk&"
		        + "height=1.19&"
		        + "height_op=ne&"
		        + "birthDate=1956-08-30&"
		        + "birthDate_op=ge";
		
	    teste_single_parameterized_one(url, "syntax");
	    
	    //  You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near ', 38, 32 AND t1.birth_date  >=  '1956-08-30' AND t1.height  <>  1.19' at line 1

	}
	
	@Test
	void teste_02() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/single/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "age=41&"
		        + "age_op=eq&"
		        + "name=Anderson&"
		        + "name_op=eq&"
		        + "birthDate=1983-03-29&"
		        + "birthDate_op=ge";
		
		EntityOneView entity = getSingleResult(url, new ErrorResponse());
		
		assertNotNull(entity);
		assertEquals("Anderson", entity.getName());
	}
	
	@Test
	void teste_02_1() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/single/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "age=41&"
		        + "age_op=eq&"
		        + "name=Anderson&"
		        + "name_op=eq&"
		        + "birthDate=1983-03-29&"
		        + "birthDate_op=eq";
		
		EntityOneView entity = getSingleResult(url, new ErrorResponse());
		
		assertNotNull(entity);
		assertEquals("Anderson", entity.getName());
	}
	
	@Test
	void teste_03() {
	    
		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/single/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "name=Paulo&"
		        + "name_op=eq";
		
		EntityOneView entity = getSingleResult(url, new ErrorResponse());
		
		assertNotNull(entity);
		assertEquals("Paulo", entity.getName());
		assertEquals(21, entity.getAge());
	}
	
	@Test
	void teste_04() {
	    
		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/single/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "name=Pablo&"
		        + "name_op=eq";
		
		EntityOneView entity = getSingleResult(url, new ErrorResponse());
		
		assertNull(entity);

	}

	@Test
	void teste_05() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/EntityOneView/teste_busca_com_equal_validar_orderby?"
		        + "name=Anderson&"
		        + "name_op=eq";
		
		List<EntityOneView> list = getAll(url, new ErrorResponse());
		
		list.forEach(entity -> {
			assertNotNull(entity.getIdEntityOne());
			assertEquals("Anderson", entity.getName());
			assertEquals(41, entity.getAge());
			assertEquals(1.93, entity.getHeight());
			assertEquals(LocalDate.parse("1983-03-29", DateTimeFormatter.ISO_LOCAL_DATE), entity.getBirthDate());
			assertEquals(LocalDateTime.parse("2024-02-01T02:52:54", DateTimeFormatter.ISO_LOCAL_DATE_TIME), entity.getProhibitedDateTime());
			assertNotEquals(0, entity.hashCode()); 
			assertNotNull(entity.getIdEntityTwo());
			assertNotNull(entity.getIdEntityTree());
			assertNotNull(entity.getIdEntityFour());
			assertNotNull(entity.getIdEntityFive());
			
		});
		
		assertNotNull(list);
		assertEquals(1, list.size());
	}

	@Test
	void teste_06() {
		
		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "age=22&"
		        + "age_op=eq&"
		        + "birthDate=1990-01-01&"
		        + "birthDate_op=eq&"
		        + "prohibitedDateTime=2024-11-01T08:00:00&"
		        + "prohibitedDateTime_op=eq&"
		        + "sortList=name,age&"
		        + "sortOrders=asc";
		
		List<EntityOneView> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(2, list.size());
		assertEquals("Ariovaldo", list.get(0).getName());
		assertEquals("Ricardo", list.get(1).getName());
	}

	@Test
	void teste_07() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "age=22&"
		        + "age_op=ne&"
		        + "birthDate=1990-01-01&"
		        + "birthDate_op=ne&"
		        + "prohibitedDateTime=2024-11-01T08:00:00&"
		        + "prohibitedDateTime_op=ne&"
		        + "sortList=name&"
		        + "sortOrders=asc";
		
		List<EntityOneView> list = getAll(url, new ErrorResponse());

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
	void teste_08() {
		
		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "name=Carlos&"
		        + "name_op=ne&"
		        + "sortList=name&"
		        + "sortOrders=asc";
		
		testes_single_parameterized_one(url, 9);
	}

	@Test
	void teste_09() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "age=22&"
		        + "age_op=eq&"
		        + "birthDate=1990-01-01&"
		        + "birthDate_op=eq&"
		        + "height=1.80&"
		        + "height_op=eq";
		
		testes_single_parameterized_other(url, "Ricardo", 1);
	}

	@Test
	void teste_10() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "prohibitedDateTime=2024-11-01T08:00:00&"
		        + "prohibitedDateTime_op=eq";
		
		List<EntityOneView> list = getAll(url, new ErrorResponse());
		
		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("Ricardo", list.get(0).getName());
		assertEquals("Joana", list.get(1).getName());
		assertEquals("Ariovaldo", list.get(2).getName());
	}
	
	@Test
	void teste_11() {
		
		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "name=*ar*&"
		        + "name_op=lk&"
		        + "height=1.9&"
		        + "height_op=ne&"
		        + "sortList=birthDate,name&"
		        + "sortOrders=desc,asc";
		
		List<EntityOneView> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("Maria", list.get(0).getName());
		assertEquals("Ricardo", list.get(1).getName());
		assertEquals("Carlos Alberto", list.get(2).getName());
		assertEquals("Carlos", list.get(3).getName());
	}

	@Test
	void teste_12() {
		
		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "name=Ar*&"
		        + "name_op=lk";
		
		testes_single_parameterized_other(url, "Ariovaldo", 1);
	}

	@Test
	void teste_13() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "birthDate=1956-08-30,1986-09-09,1990-09-09&"
		        + "birthDate_op=in&"
		        + "sortList=age&"
		        + "sortOrders=asc";
		
		List<EntityOneView> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("Paulo", list.get(0).getName());
		assertEquals("Carlos", list.get(1).getName());
		assertEquals("Paulo Henrique", list.get(2).getName());
	}
	
	@Test
	void teste_13_1() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "birthDate=1956-08-30&"
		        + "birthDate_op=eq&"
		        + "sortList=age&"
		        + "sortOrders=asc";
		
		List<EntityOneView> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(1, list.size());
		assertEquals("Carlos", list.get(0).getName());
	}

	@Test
	void teste_14() {
		
		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "birthDate=1956-08-30,1990-01-01,1990-09-09&"
		        + "birthDate_op=in&"
		        + "sortList=age,height&"
		        + "sortOrders=desc,asc";
		
		List<EntityOneView> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("Carlos", list.get(0).getName());
		assertEquals("Ricardo", list.get(1).getName());
		assertEquals("Ariovaldo", list.get(2).getName());
		assertEquals("Paulo", list.get(3).getName());
	}

	@Test
	void teste_15() {
		
		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "age=55,12,22&"
		        + "age_op=in";
		
		testes_single_parameterized_one(url, 4);
	}

	@Test
	void teste_16() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "idEntityOne=" + SaveTests.ids.get(0) + "," + SaveTests.ids.get(1) + "&"
		        + "idEntityOne_op=in&"
		        + "sortList=idEntityOne&"
		        + "sortOrders=asc";

		testes_single_parameterized_one(url, 2);
	}

	@Test
	void teste_17() {
		
		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "height=1.40,1.78&"
		        + "height_op=bt&"
		        + "sortList=height&"
		        + "sortOrders=desc";
		
		List<EntityOneView> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("Paulo", list.get(0).getName());
		assertEquals("Carlos Alberto", list.get(1).getName());
		assertEquals("Carlos", list.get(2).getName());
		assertEquals("Maria", list.get(3).getName());
	}

	@Test
	void teste_18() {
		
		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "prohibitedDateTime=2024-02-01T08:50:00,2024-10-01T08:50:55&"
		        + "prohibitedDateTime_op=bt&"
		        + "sortList=birthDate&"
		        + "sortOrders=desc";
		
		List<EntityOneView> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(5, list.size());
		assertEquals("Maria", list.get(0).getName());
		assertEquals("Paulo", list.get(1).getName());
		assertEquals("Paulo Henrique", list.get(2).getName());
		assertEquals("Carlos Alberto", list.get(3).getName());
		assertEquals("Carlos", list.get(4).getName());
	}

	@Test
	void teste_19() {
		
		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "height=1.86&"
		        + "height_op=ge&"
		        + "sortList=birthDate&"
		        + "sortOrders=desc";
		
		testes_single_parameterized_one(url, 3);
	}

	@Test
	void teste_20() {
		
		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "height=1.87&"
		        + "height_op=gt&"
		        + "sortList=birthDate&"
		        + "sortOrders=desc";
		
		testes_single_parameterized_one(url, 2);
	}

	@Test
	void teste_21() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
		        + "birthDate=1990-09-09&"
		        + "birthDate_op=gt&"
		        + "sortList=birthDate&"
		        + "sortOrders=desc";
		
		testes_single_parameterized_one(url, 3);
	}

	@Test
	void teste_22() {
	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
	            + "birthDate=1990-01-02&"
	            + "birthDate_op=ge&"
	            + "birthDate=2016-01-01&"
	            + "birthDate_op=le&"
	            + "sortList=birthDate&"
	            + "sortOrders=desc";
	    
	    testes_single_parameterized_one(url, 4);
	}

	@Test
	void teste_23() {
	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
	            + "birthDate=1990-01-02&"
	            + "birthDate_op=le";
	    
	    testes_single_parameterized_one(url, 6);
	}

	@Test
	void teste_24() {
	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
	            + "birthDate=1986-09-08&"
	            + "birthDate_op=lt";
	    
	    testes_single_parameterized_one(url, 3);
	}

	@Test
	void teste_25() {
	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
	            + "age=21&"
	            + "age_op=le";
	    
	    testes_single_parameterized_one(url, 2);
	}

	@Test
	void teste_26() {
	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
	            + "birthDate=2016-01-01&"
	            + "birthDate_op=ge";
	    
	    testes_single_parameterized_other(url, "Maria", 1);
	}

	@Test
	void teste_27() {
	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
	            + "age=21&"
	            + "age_op=lt";
	    
	    testes_single_parameterized_other(url, "Maria", 1);
	}

	@Test
	void teste_28() {
	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "//query/EntityOneView/teste_utilizando_group_by";
	    
	    List<EntityOneView> list = getAll(url, new ErrorResponse());
	    
	    assertNotNull(list);
	}

	@Test
	void teste_29() {
	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/EntityOneView/teste_utilizando_group_by_erro";
	    
	    teste_single_parameterized_one(url, "PreparedStatementCallback; uncategorized SQLException for SQL");
	}
	
	@Test
	void teste_30() {
	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
	            + "sortList=birthDate,name&"
	            + "sortOrders=asc,desc";

	    List<EntityOneView> list = getAll(url, new ErrorResponse());

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
	void teste_31() {
	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
	            + "sortList=birthDate,name&"
	            + "sortOrders=desc,asc";

	    List<EntityOneView> list = getAll(url, new ErrorResponse());

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
	void teste_32() {
	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
	            + "sortList=name,birthDate&"
	            + "sortOrders=asc,desc";

	    List<EntityOneView> list = getAll(url, new ErrorResponse());

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
	void teste_33() {
	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
	            + "sortList=name,birthDate&"
	            + "sortOrders=desc,asc";

	    List<EntityOneView> list = getAll(url, new ErrorResponse());

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
	void teste_34() {
	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/EntityOneView/teste_busca_com_condicoes_diversars?"
	            + "name=Silva&"
	            + "name_op=eq";
	    
	    List<EntityOneView> list = getAll(url, new ErrorResponse());
	    
	    assertNull(list);
	}

	@Test
	void teste_35() {
	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/EntityOneView/nao_existe_query";
	    
	    teste_single_parameterized_one(url, "Query not found nao_existe_query !");
	}

	@Test
	void teste_36() {
	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/EntityOneView/teste_busca_com_condicoes_diversars";
	    
	    List<EntityOneView> list = getAll(url, new ErrorResponse());
	    
	    assertNotNull(list);
	    assertEquals(10, list.size());
	}

	@Test
	void teste_37() {
	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/EntityOneView/teste_um_exemplo_sem_order_by?"
	            + "prohibitedDateTime=2024-11-01T08:00:00&"
	            + "prohibitedDateTime_op=ge";
	    
	    List<EntityOneView> list = getAll(url, new ErrorResponse());
	    
	    assertNotNull(list);
	    assertEquals(4, list.size());
	}

	@Test
	void teste_38() {
	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/EntityOne/teste_um_exemplo_sem_order_by?"
	            + "prohibitedDateTime-=2024-11-01T08:00:00&"
	            + "prohibitedDateTime_op=ge";
	    
	    ErrorResponse errorResponse = new ErrorResponse("Class not found EntityOne !", HttpStatus.BAD_REQUEST);
	    
	    assertThrows(RuntimeException.class, () -> getAll(url, errorResponse));
	}

	@Test
	void teste_39() {
	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/count/EntityOneView/teste?"
	            + "prohibitedDateTime=2024-11-01T08:00:00&"
	            + "prohibitedDateTime_op=ge";
	    
	    ErrorResponse errorResponse = new ErrorResponse("Query not found teste !", HttpStatus.BAD_REQUEST);
	    
	    assertThrows(RuntimeException.class, () -> getUniqueResult(url, errorResponse));
	}

	@Test
	void teste_40() {
	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/single/EntityOneView/query_123?"
	            + "page=0&"
	            + "size=5&"
	            + "sortList=name&"
	            + "sortOrders=asc";
	    
	    teste_single_parameterized_one(url, "Query not found query_123 !");
	}

	@Test
	void teste_41() {
	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/count/EntityOneView/teste_um_exemplo_count?"
	            + "prohibitedDateTime=2024-11-01T08:00:00&"
	            + "prohibitedDateTime_op=ge";
	    
	    teste_single_parameterized_one(url, "Query not found teste_um_exemplo_count !");
	}

	@Test
	void teste_42() {
	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/count/EntityOneView/teste_busca_com_condicoes_diversars?"
	            + "prohibitedDateTime=2024-11-01T08:00:00&"
	            + "prohibitedDateTime_op=ge";
	    
	    Integer count = Integer.parseInt(getUniqueResult(url, new ErrorResponse()));
	    
	    assertEquals(4, count);
	}
	
	void testes_single_parameterized_other(String url, String value, Integer size) {
		
		List<EntityOneView> list = getAll(url, new ErrorResponse());
		
		assertNotNull(list);
		assertEquals(size, list.size());
		assertEquals(value, list.get(0).getName());
	}
	
	void testes_single_parameterized_one(String url, Integer size) {
		
		List<EntityOneView> list = getAll(url, new ErrorResponse());
		
		assertNotNull(list);
		assertEquals(size, list.size());
	}
	
	public void teste_single_parameterized_one(String url, String message) {
		
		ErrorResponse errorResponse = new ErrorResponse(message, HttpStatus.BAD_REQUEST);
		
	    assertThrows(RuntimeException.class, () -> getSingleResult(url, errorResponse));
	}
	
	public List<EntityOneView> getAll(String url, ErrorResponse compare) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return convertResponseToEntityOneViewList(response.getBody());
		} else {
			ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertTrue(errorResponse.getMessage().contains(compare.getMessage()));
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: " + errorResponse.getMessage());
		}
	}

	public EntityOneView getSingleResult(String url, ErrorResponse compare) {
	    HttpHeaders headers = new HttpHeaders();
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
	    HttpEntity<String> entity = new HttpEntity<>(headers);
	    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
	    if (response.getStatusCode().is2xxSuccessful()) {
	        return convertResponseToEntityOneView(response.getBody());
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

	private List<EntityOneView> convertResponseToEntityOneViewList(String body) {
	    ObjectMapper objectMapper = createObjectMapper();
	    try {
	    	if(body != null) {
	    		return objectMapper.readValue(body, new TypeReference<List<EntityOneView>>(){});
	    	} else {
	    		return null;
	    	}
	    } catch (JsonProcessingException e) {
	        throw new RuntimeException("Error parsing EntityOneView list response", e);
	    }
	}

	private EntityOneView convertResponseToEntityOneView(String body) {
	    ObjectMapper objectMapper = createObjectMapper();
	    try {
	    	if(body != null) {
	    		return objectMapper.readValue(body, EntityOneView.class);
	    	} else {
	    		return null;
	    	}
	    } catch (JsonProcessingException e) {
	        throw new RuntimeException("Error parsing EntityOneView response", e);
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
