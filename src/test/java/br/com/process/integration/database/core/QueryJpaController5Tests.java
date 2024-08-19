package br.com.process.integration.database.core;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.core.util.Constants;
import br.com.process.integration.database.domain.model.entity.EntityOne;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QueryJpaController5Tests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private QueryJpaController queryJpaController;

	@BeforeAll
	void setupOnce() {
	}

	@Test
	@Order(1)
	void contextLoads() {
		assertNotNull(queryJpaController);
	};

	@Test
	void teste_busca_com_equal_pelo_entityTwo_por_color_por_ordernacao_entityTwo() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/paginator/EntityOne?entityTwo.color=Preto&entityTwo.color_op=eq&page=0&size=10&sortList=entityTwo.dateInclusion,entityTwo.hex&sortOrders=desc,asc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(2, list.size());
		assertEquals("Carlos Alberto", list.get(0).getName());
		assertEquals("Carlos", list.get(1).getName());
		assertEquals(234, list.get(0).getEntityTwo().getHex());
		assertEquals(144, list.get(1).getEntityTwo().getHex());
	}

	@Test
	void teste_busca_com_in_pelo_entityTree_por_animal() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/paginator/EntityOne?entityTwo.entityTree.animal=Cavalo,Gato,Papagaio&entityTwo.entityTree.animal_op=in&page=0&size=10&sortList=entityTwo.color,entityTwo.entityTree.animal&sortOrders=desc,asc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(5, list.size());

		assertEquals("Paulo Henrique", list.get(0).getName());
		assertEquals("Ricardo", list.get(1).getName());
		assertEquals("Paulo", list.get(2).getName());
		assertEquals("Renato", list.get(3).getName());
		assertEquals("Ariovaldo", list.get(4).getName());

		assertEquals("Verde", list.get(0).getEntityTwo().getColor());
		assertEquals("Verde", list.get(1).getEntityTwo().getColor());
		assertEquals("Roxo", list.get(2).getEntityTwo().getColor());
		assertEquals("Laranja", list.get(3).getEntityTwo().getColor());
		assertEquals("Cinza", list.get(4).getEntityTwo().getColor());

		assertEquals("Cavalo", list.get(0).getEntityTwo().getEntityTree().getAnimal());
		assertEquals("Gato", list.get(1).getEntityTwo().getEntityTree().getAnimal());
		assertEquals("Cavalo", list.get(2).getEntityTwo().getEntityTree().getAnimal());
		assertEquals("Papagaio", list.get(3).getEntityTwo().getEntityTree().getAnimal());
		assertEquals("Gato", list.get(4).getEntityTwo().getEntityTree().getAnimal());
	}

	@Test
	void teste_busca_com_equal_pelo_entityTree_por_animal() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/paginator/EntityOne?entityTwo.entityTree.animal=Capivara&entityTwo.entityTree.animal_op=eq";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		list.forEach(entity -> {
			assertEquals("Carlos Alberto", entity.getName());
		});

		assertNotNull(list);
		assertEquals(1, list.size());
	}

	@Test
	void teste_busca_com_equal_pelo_entityFour_por_fruit() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/paginator/EntityOne?entityTwo.entityTree.entityFour.fruit=Pitanga&entityTwo.entityTree.entityFour.fruit_op=eq&page=0&size=10";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(2, list.size());
	}

	@Test
	void teste_busca_com_equal_pelo_entityFive_por_fruit() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/paginator/EntityOne?entityTwo.entityTree.entityFour.entityFive.object=Cama&entityTwo.entityTree.entityFour.entityFive.object_op=eq&page=0&size=10&sortList=name&sortOrders=desc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		list.forEach(entity -> {
			assertEquals("Cama",
					entity.getEntityTwo().getEntityTree().getEntityFour().getEntityFive().getObject());
		});

		assertNotNull(list);
		assertEquals(1, list.size());
	}

	@Test
	void teste_busca_com_like_pelo_entityFive_por_fruit() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/paginator/EntityOne?entityTwo.entityTree.entityFour.entityFive.object=c*&entityTwo.entityTree.entityFour.entityFive.object_op=lk&page=0&size=10&sortList=entityTwo.dateInclusion,entityTwo.hex&sortOrders=desc,asc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(4, list.size());
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
