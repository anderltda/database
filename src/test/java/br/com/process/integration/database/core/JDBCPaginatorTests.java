package br.com.process.integration.database.core;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.process.integration.database.core.exception.ErrorResponse;
import br.com.process.integration.database.core.ui.QueryNativeController;
import br.com.process.integration.database.core.util.Constants;
import br.com.process.integration.database.model.view.dto.example.EntityOneView;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JDBCPaginatorTests {

	@LocalServerPort
	private int port;
	
	private static final String PATH = "http://localhost:";

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private QueryNativeController queryNativeController;
	
	@BeforeAll
	void setupOnce() {}
	
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
	            + "/query/paginator/EntityOneView/teste_busca_com_condicoes_diversars?"
	            + "name=Anderson&"
	            + "name_op=eq&"
	            + "age=41&"
	            + "age_op=eq&"
	            + "size=10&"
	            + "sortList=age,name&"
	            + "sortOrders=asc,desc";
		
		PagedModel<EntityOneView> page = getAll(url, new ErrorResponse());
		
		List<EntityOneView> list = convertToEntityOneViewList(page.getContent());

		assertNotNull(list);
		assertEquals(1, list.size());
		assertEquals(1, page.getContent().size());
	}
	
	@Test
	void teste_02() {

	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/paginator/EntityOneView/teste_busca_com_condicoes_diversars?"
	            + "name=Anderson&"
	            + "name_op=in&"
	            + "age=23&"
	            + "age_op=eq&"
	            + "birthDate=1983-03-29&"
	            + "birthDate_op=eq&"
	            + "height=1.7&"
	            + "height_op=gt&"
	            + "prohibitedDateTime=2024-02-01T02:52:54&"
	            + "prohibitedDateTime_op=gt&"
	            + "page=0&"
	            + "size=10&"
	            + "sortList=name&"
	            + "sortOrders=asc";

	    PagedModel<EntityOneView> page = getAll(url, new ErrorResponse());

	    assertNull(page);
	}

	@Test
	void teste_03() {

	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/paginator/EntityOneView/teste_busca_com_condicoes_diversars?"
	            + "page=ee&"
	            + "size=10&"
	            + "sortList=name&"
	            + "sortOrders=asc";

	    teste_single_parameterized_one(url, "For input string: \"ee\"");
	}

	@Test
	void teste_04() {

	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/paginator/EntityOneView/query_123?"
	            + "page=0&"
	            + "size=5&"
	            + "sortList=name&"
	            + "sortOrders=asc";

	    teste_single_parameterized_one(url, "Query not found query_123 !");
	}

	@Test
	void teste_05() {

	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/paginator/EntityOneView/teste_busca_com_condicoes_diversars?"
	            + "page=0&"
	            + "size=10&"
	            + "sortList=name&"
	            + "sortOrders=asc";

	    PagedModel<EntityOneView> page = getAll(url, new ErrorResponse());

	    assertEquals(10, page.getContent().size());
	}

	@Test
	void teste_06() {

	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/paginator/EntityOneView/teste_busca_com_condicoes_diversars?"
	            + "page=0&"
	            + "size=2&"
	            + "sortList=name&"
	            + "sortOrders=asc";

	    PagedModel<EntityOneView> page = getAll(url, new ErrorResponse());

	    assertEquals(2, page.getContent().size());
	}

	@Test
	void teste_07() {

	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/paginator/EntityOneView/teste_busca_com_condicoes_diversars?"
	            + "idEntityOne=" + SaveTests.ids.get(0) + "&"
	            + "idEntityOne_op=eq&"
	            + "page=0&"
	            + "size=2&"
	            + "sortList=name&"
	            + "sortOrders=asc";

	    PagedModel<EntityOneView> page = getAll(url, new ErrorResponse());

	    List<EntityOneView> list = convertToEntityOneViewList(page.getContent());

	    assertEquals(1, page.getContent().size());
	    assertEquals("Carlos Alberto", list.get(0).getName());
	    assertEquals(41, list.get(0).getAge());
	}

	@Test
	void teste_08() {

	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/paginator/EntityOneView/teste_busca_com_condicoes_diversars?"
	            + "name=Anderson&"
	            + "name_op=eq&"
	            + "page=0&"
	            + "size=10&"
	            + "sortList=name&"
	            + "sortOrders=asc";

	    PagedModel<EntityOneView> list = getAll(url, new ErrorResponse());

	    list.getContent().forEach(entity -> {
	        assertEquals("Anderson", entity.getName());
	        assertEquals(41, entity.getAge());
	        assertEquals(1.93, entity.getHeight());
	        assertEquals(LocalDate.parse("1983-03-29", DateTimeFormatter.ISO_LOCAL_DATE), entity.getBirthDate());
	        assertEquals(LocalDateTime.parse("2024-02-01T02:52:54", DateTimeFormatter.ISO_LOCAL_DATE_TIME), entity.getProhibitedDateTime());
	        assertNotEquals(0, entity.hashCode());
	        assertNotNull(entity.getIdEntityOne());
	        assertNotNull(entity.getIdEntityTwo());
	        assertNotNull(entity.getIdEntityTree());
	        assertNotNull(entity.getIdEntityFour());
	        assertNotNull(entity.getIdEntityFive());
	    });

	    assertNotNull(list);
	    assertEquals(1, list.getContent().size());
	}

	@Test
	void teste_09() {

	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/paginator/EntityOneView/teste_busca_com_condicoes_diversars?"
	            + "age=22&"
	            + "age_op=eq&"
	            + "birthDate=1990-01-01&"
	            + "birthDate_op=eq&"
	            + "prohibitedDateTime=2024-11-01T08:00:00&"
	            + "prohibitedDateTime_op=eq&"
	            + "page=0&"
	            + "size=10&"
	            + "sortList=name&"
	            + "sortOrders=asc";

	    PagedModel<EntityOneView> page = getAll(url, new ErrorResponse());

	    List<EntityOneView> list = convertToEntityOneViewList(page.getContent());

	    assertNotNull(list);
	    assertEquals(2, page.getContent().size());
	    assertEquals("Ariovaldo", list.get(0).getName());
	    assertEquals("Ricardo", list.get(1).getName());
	}

	@Test
	void teste_10() {

	    String url = PATH 
	            + port 
	            + Constants.API_NAME_REQUEST_MAPPING 
	            + "/query/paginator/EntityOneView/teste_busca_com_condicoes_diversars?"
	            + "age=22&"
	            + "age_op=ne&"
	            + "birthDate=1990-01-01&"
	            + "birthDate_op=ne&"
	            + "prohibitedDateTime=2024-11-01T08:00:00&"
	            + "prohibitedDateTime_op=ne&"
	            + "page=0&"
	            + "size=10&"
	            + "sortList=name&"
	            + "sortOrders=asc";

	    PagedModel<EntityOneView> page = getAll(url, new ErrorResponse());

	    List<EntityOneView> list = convertToEntityOneViewList(page.getContent());

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

	public void teste_single_parameterized_one(String url, String message) {
		ErrorResponse errorResponse = new ErrorResponse(message, HttpStatus.BAD_REQUEST);
	    assertThrows(RuntimeException.class, () -> getAll(url, errorResponse));
	}
	
	private PagedModel<EntityOneView> getAll(String url, ErrorResponse compare) {
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
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: " + errorResponse.getMessage());
        }
    }

    private PagedModel<EntityOneView> convertResponseToPagedModel(String body) {
    	if(body == null) return null;
        ObjectMapper objectMapper = createObjectMapper();
        try {
            return objectMapper.readValue(body, new TypeReference<PagedModel<EntityOneView>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error parsing PagedModel<EntityOneView> response", e);
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
    
    public List<EntityOneView> convertToEntityOneViewList(Collection<EntityOneView> collection) {
        return collection.stream()
                         .map(this::convertToEntityOneView)
                         .collect(Collectors.toList());
    }

    private EntityOneView convertToEntityOneView(EntityOneView element) {
        return element;
    }
}
