package br.com.process.integration.database.core.ui;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import br.com.process.integration.database.domain.entity.EntityTest1;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QueryJpaController7Tests {

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
	void teste_busca_all_ordernacao_birthDate_asc_name_desc() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/jpql/page/EntityTest1/buscaAll?page=0&size=20&sortList=birthDate,name&sortOrders=asc,desc";

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
	void teste_busca_com_like_pelo_name() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/jpql/page/EntityTest1/buscaComLikePeloName?name=ar&page=0&size=20&sortList=birthDate,name&sortOrders=asc,desc";

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
	void teste_busca_com_like_pelo_name_nao_encontrado() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/jpql/page/EntityTest1/buscaComLikePeloName?name=Silva&page=0&size=20&sortList=birthDate,name&sortOrders=asc,desc";

		List<EntityTest1> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(0, list.size());
	}
	
	@Test
	void teste_busca_com_like_pelo_name_nao_encontrado_com_erro() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/jpql/page/EntityTest/buscaComLikePeloName?name=Silva&page=0&size=20&sortList=birthDate,name&sortOrders=asc,desc";

		ErrorResponse errorResponse = new ErrorResponse("Cannot invoke \"org.springframework.data.jpa.repository.JpaRepository.getClass()\" because the return value of \"br.com.process.integration.database.core.application.AbstractJpaService.getRepository()\" is null", 400);

		assertThrows(RuntimeException.class, () -> getAll(url, errorResponse));
	}
	
	@Test
	void teste_busca_all_ordernacao_birthDate_desc_name_asc() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/jpql/page/EntityTest1/buscaAll?page=0&size=20&sortList=birthDate,name&sortOrders=desc,asc";

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

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/jpql/page/EntityTest1/buscaAll?page=0&size=20&sortList=name,birthDate&sortOrders=asc,desc";

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

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/jpql/page/EntityTest1/buscaAll?page=0&size=20&sortList=name,birthDate&sortOrders=desc,asc";

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
	
	private List<EntityTest1> getAll(String url, ErrorResponse compare) {

		PagedModel<EntityTest1> page = getRestAll(url, compare);

		List<EntityTest1> list = convertToEntityTest1List(page.getContent());

		assertNotNull(list);
		assertEquals(list.size(), page.getContent().size());

		return list;
	}

	private PagedModel<EntityTest1> getRestAll(String url, ErrorResponse compare) {

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
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: "
					+ errorResponse.getMessage());
		}
	}

	private PagedModel<EntityTest1> convertResponseToPagedModel(String body) {
		ObjectMapper objectMapper = createObjectMapper();
		try {
			return objectMapper.readValue(body, new TypeReference<PagedModel<EntityTest1>>() {
			});
		} catch (Exception e) {
			throw new RuntimeException("Error parsing PagedModel<EntityTest1View> response", e);
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

	public List<EntityTest1> convertToEntityTest1List(Collection<EntityTest1> collection) {
		return collection.stream().map(this::convertToEntityTest1).collect(Collectors.toList());
	}

	private EntityTest1 convertToEntityTest1(EntityTest1 element) {
		return element;
	}
}
