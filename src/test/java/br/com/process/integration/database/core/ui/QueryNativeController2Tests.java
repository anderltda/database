package br.com.process.integration.database.core.ui;

import static org.junit.Assert.assertTrue;
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
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.process.integration.database.core.exception.ErrorResponse;
import br.com.process.integration.database.domain.view.EntityOneView;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QueryNativeController2Tests {

	@LocalServerPort
	private int port;

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
	void teste_paginacao_com_varios_parmetros_page_nulo() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/page/EntityOneView/teste_busca_com_condicoes_diversars?name=Anderson&name_op=eq&age=41&age_op=eq&size=10&sortList=age,name&sortOrders=asc,desc";

		PagedModel<EntityOneView> page = getAll(url, new ErrorResponse());
		
		List<EntityOneView> list = convertToEntityOneViewList(page.getContent());

		assertNotNull(list);
		assertEquals(1, list.size());
		assertEquals(1, page.getContent().size());
	}
	
	@Test
	void teste_paginacao_com_varios_parmetros() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/page/EntityOneView/teste_busca_com_condicoes_diversars?name=Anderson&name_op=in&age=23&age_op=eq&birthDate=1983-03-29&birthDate_op=eq&height=1.7&height_op=gt&prohibited=2024-02-01T02:52:54&prohibited_op=gt&page=0&size=10&sortList=name&sortOrders=asc";

		PagedModel<EntityOneView> page = getAll(url, new ErrorResponse());
		
		List<EntityOneView> list = convertToEntityOneViewList(page.getContent());

		assertNotNull(list);
		assertEquals(0, list.size());
		assertEquals(0, page.getContent().size());
	}
	
	@Test
	void teste_gera_um_erro_paginacao() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/page/EntityOneView/teste_busca_com_condicoes_diversars?page=ee&size=10&sortList=name&sortOrders=asc";

		teste_single_parameterized_one(url, "For input string: \"ee\"");
	}

	@Test
	void teste_gera_um_erro_single_query_nao_encontrada() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/single/EntityOneView/query_123?page=0&size=5&sortList=name&sortOrders=asc";

		teste_single_parameterized_one(url, "Query not found query_123 !");
	}
	
	@Test
	void teste_gera_um_erro_paginacao_query_nao_encontrada() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/page/EntityOneView/query_123?page=0&size=5&sortList=name&sortOrders=asc";

		teste_single_parameterized_one(url, "Query not found query_123 !");
	}
	
	@Test
	void teste_a_query_teste_busca_com_condicoes_diversars_paginada() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/page/EntityOneView/teste_busca_com_condicoes_diversars?page=0&size=10&sortList=name&sortOrders=asc";

		PagedModel<EntityOneView> page = getAll(url, new ErrorResponse());
		
		assertEquals(10, page.getContent().size());

	}
	
	@Test
	void teste_a_query_teste_busca_com_condicoes_diversars_paginada_com_size_2() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/page/EntityOneView/teste_busca_com_condicoes_diversars?page=0&size=2&sortList=name&sortOrders=asc";

		PagedModel<EntityOneView> page = getAll(url, new ErrorResponse());
		
		assertEquals(2, page.getContent().size());
		
	}
	
	@Test
	void teste_a_query_teste_busca_com_condicoes_diversars_paginada_com_id() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/page/EntityOneView/teste_busca_com_condicoes_diversars?idEntityOne="+ QueryJpaController1Tests.ids.get(0) +"&idEntityOne_op=eq&page=0&size=2&sortList=name&sortOrders=asc";

		PagedModel<EntityOneView> page = getAll(url, new ErrorResponse());
		
		List<EntityOneView> list = convertToEntityOneViewList(page.getContent());

		assertEquals(1, page.getContent().size());
		assertEquals("Carlos Alberto", list.get(0).getName());
		assertEquals(55, list.get(0).getAge());
	}
	
	
	@Test
	void teste_busca_com_equal_pelo_name() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/page/EntityOneView/teste_busca_com_condicoes_diversars?name=Anderson&name_op=eq&page=0&size=10&sortList=name&sortOrders=asc";

		PagedModel<EntityOneView> list = getAll(url, new ErrorResponse());
		
		list.getContent().forEach(entity -> {
			assertEquals("Anderson", entity.getName());
			assertEquals(41, entity.getAge());
			assertEquals(1.93, entity.getHeight());
			assertEquals(LocalDate.parse("1983-03-29", DateTimeFormatter.ISO_LOCAL_DATE), entity.getBirthDate());
			assertEquals(LocalDateTime.parse("2024-02-01T02:52:54", DateTimeFormatter.ISO_LOCAL_DATE_TIME), entity.getProhibited());
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
	void teste_busca_com_equal_pelo_age_e_birthDate_e_prohibited_ordernacao_name_asc() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/page/EntityOneView/teste_busca_com_condicoes_diversars?age=22&age_op=eq&birthDate=1990-01-01&birthDate_op=eq&prohibited=2024-11-01T08:00:00&prohibited_op=eq&page=0&size=10&sortList=name&sortOrders=asc";

		PagedModel<EntityOneView> page = getAll(url, new ErrorResponse());
		
		List<EntityOneView> list = convertToEntityOneViewList(page.getContent());

		assertNotNull(list);
		assertEquals(2, page.getContent().size());
		assertEquals("Ariovaldo", list.get(0).getName());
		assertEquals("Ricardo", list.get(1).getName());
	}
	
	@Test
	void teste_notEqual_do_teste_busca_com_equal_pelo_age_e_birthDate_e_prohibited_ordernacao_name_asc() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/page/EntityOneView/teste_busca_com_condicoes_diversars?age=22&age_op=ne&birthDate=1990-01-01&birthDate_op=ne&prohibited=2024-11-01T08:00:00&prohibited_op=ne&page=0&size=10&sortList=name&sortOrders=asc";

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
	
	@Test
	void teste_utilizando_group_by() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/page/EntityOneView/teste_utilizando_group_by?page=0&size=10";

		PagedModel<EntityOneView> page = getAll(url, new ErrorResponse());
		
		List<EntityOneView> list = convertToEntityOneViewList(page.getContent());

		assertNotNull(list);
		assertEquals(8, list.size());

	}
	
	@Test
	void teste_utilizando_group_by_erro() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/page/EntityOne/teste_utilizando_group_by_erro?page=0&size=10";
		
		ErrorResponse errorResponse = new ErrorResponse("Class not found EntityOne !", 400);
		
	    assertThrows(RuntimeException.class, () -> getAll(url, errorResponse));

	}
	
	@Test
	void teste_busca_com_equal_pelo_age_e_birthDate_e_prohibited_ordernacao_name_desc() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/page/EntityOneView/teste_busca_com_condicoes_diversars?age=22&age_op=ne&birthDate=1990-01-01&birthDate_op=ne&prohibited=2024-11-01T08:00:00&prohibited_op=ne&page=0&size=10&sortList=name&sortOrders=desc";

		PagedModel<EntityOneView> page = getAll(url, new ErrorResponse());
		
		List<EntityOneView> list = convertToEntityOneViewList(page.getContent());

		assertEquals("Renato", list.get(0).getName());
		assertEquals("Paulo Henrique", list.get(1).getName());
		assertEquals("Paulo", list.get(2).getName());
		assertEquals("Maria", list.get(3).getName());
		assertEquals("Carlos Alberto", list.get(4).getName());
		assertEquals("Carlos", list.get(5).getName());
		assertEquals("Anderson", list.get(6).getName());
	}

	public void teste_single_parameterized_one(String url, String message) {
		ErrorResponse errorResponse = new ErrorResponse(message, 400);
	    assertThrows(RuntimeException.class, () -> getAll(url, errorResponse));
	}
	
	private PagedModel<EntityOneView> getAll(String url, ErrorResponse compare) {
		
        HttpHeaders headers = new HttpHeaders();
      
        headers.set("Accept", "application/json");

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
        ObjectMapper objectMapper = createObjectMapper();
        try {
            // Converte a string JSON para PagedModel<EntityOneView>
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

        // Ignorar propriedades desconhecidas
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
