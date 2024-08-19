package br.com.process.integration.database.core;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.process.integration.database.core.exception.ErrorResponse;
import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.core.util.Constants;
import br.com.process.integration.database.domain.model.entity.EntityOne;

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

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/all/jpql/page/EntityOne/buscaAll?page=0&size=20&sortList=birthDate,name&sortOrders=asc,desc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

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

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/all/jpql/page/EntityOne/buscaComLikePeloName?name=ar&page=0&size=20&sortList=birthDate,name&sortOrders=asc,desc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

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

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/all/jpql/page/EntityOne/buscaComLikePeloName?name=Silva&page=0&size=20&sortList=birthDate,name&sortOrders=asc,desc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(0, list.size());
	}
	
	@Test
	void teste_busca_all_ordernacao_birthDate_desc_name_asc() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/all/jpql/page/EntityOne/buscaAll?page=0&size=20&sortList=birthDate,name&sortOrders=desc,asc";

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
	void teste_busca_all_ordernacao_name_asc_birthDate_desc() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/all/jpql/page/EntityOne/buscaAll?page=0&size=20&sortList=name,birthDate&sortOrders=asc,desc";

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
	void teste_busca_all_ordernacao_name_desc_birthDate_asc() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/all/jpql/page/EntityOne/buscaAll?page=0&size=20&sortList=name,birthDate&sortOrders=desc,asc";

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
	void teste_all_page_erro() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/all/jpql/page/EntityOne/buscaAll?age=Silva&sortList=name,birthDate&sortOrders=desc,asc";

		ErrorResponse errorResponse = new ErrorResponse("Parameter value [Silva] did not match expected type [SqmBasicValuedSimplePath(br.com.process.integration.database.domain.model.entity.EntityOne(e).age)]",
				HttpStatus.BAD_REQUEST);

		assertThrows(RuntimeException.class, () -> getAll(url, errorResponse));
	}
	
	private List<EntityOne> getAll(String url, ErrorResponse compare) {

		PagedModel<EntityOne> page = getRestAll(url, compare);

		List<EntityOne> list = convertToEntityOneList(page.getContent());

		assertNotNull(list);
		assertEquals(list.size(), page.getContent().size());

		return list;
	}

	private PagedModel<EntityOne> getRestAll(String url, ErrorResponse compare) {

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

	private PagedModel<EntityOne> convertResponseToPagedModel(String body) {
		ObjectMapper objectMapper = createObjectMapper();
		try {
			return objectMapper.readValue(body, new TypeReference<PagedModel<EntityOne>>() {
			});
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

	public List<EntityOne> convertToEntityOneList(Collection<EntityOne> collection) {
		return collection.stream().map(this::convertToEntityOne).collect(Collectors.toList());
	}

	private EntityOne convertToEntityOne(EntityOne element) {
		return element;
	}
}
