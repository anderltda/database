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
	void teste_busca_com_equal_pelo_entityTest2_por_color_por_ordernacao_entityTest2() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/page/EntityTest1?entityTest2.color=Preto&entityTest2.color_op=eq&page=0&size=10&sortList=entityTest2.dateInclusion,entityTest2.hex&sortOrders=desc,asc";

		List<EntityTest1> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(2, list.size());
		assertEquals("Carlos Alberto", list.get(0).getName());
		assertEquals("Carlos", list.get(1).getName());
		assertEquals(234, list.get(0).getEntityTest2().getHex());
		assertEquals(144, list.get(1).getEntityTest2().getHex());
	}

	@Test
	void teste_busca_com_in_pelo_entityTest3_por_animal() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/page/EntityTest1?entityTest2.entityTest3.animal=Cavalo,Gato,Papagaio&entityTest2.entityTest3.animal_op=in&page=0&size=10&sortList=entityTest2.color,entityTest2.entityTest3.animal&sortOrders=desc,asc";

		List<EntityTest1> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(5, list.size());

		assertEquals("Paulo Henrique", list.get(0).getName());
		assertEquals("Ricardo", list.get(1).getName());
		assertEquals("Paulo", list.get(2).getName());
		assertEquals("Renato", list.get(3).getName());
		assertEquals("Ariovaldo", list.get(4).getName());

		assertEquals("Verde", list.get(0).getEntityTest2().getColor());
		assertEquals("Verde", list.get(1).getEntityTest2().getColor());
		assertEquals("Roxo", list.get(2).getEntityTest2().getColor());
		assertEquals("Laranja", list.get(3).getEntityTest2().getColor());
		assertEquals("Cinza", list.get(4).getEntityTest2().getColor());

		assertEquals("Cavalo", list.get(0).getEntityTest2().getEntityTest3().getAnimal());
		assertEquals("Gato", list.get(1).getEntityTest2().getEntityTest3().getAnimal());
		assertEquals("Cavalo", list.get(2).getEntityTest2().getEntityTest3().getAnimal());
		assertEquals("Papagaio", list.get(3).getEntityTest2().getEntityTest3().getAnimal());
		assertEquals("Gato", list.get(4).getEntityTest2().getEntityTest3().getAnimal());
	}

	@Test
	void teste_busca_com_equal_pelo_entityTest3_por_animal() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/page/EntityTest1?entityTest2.entityTest3.animal=Capivara&entityTest2.entityTest3.animal_op=eq";

		List<EntityTest1> list = getAll(url, new ErrorResponse());

		list.forEach(entity -> {
			assertEquals("Carlos Alberto", entity.getName());
		});

		assertNotNull(list);
		assertEquals(1, list.size());
	}

	@Test
	void teste_busca_com_equal_pelo_entityTest4_por_fruit() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/page/EntityTest1?entityTest2.entityTest3.entityTest4.fruit=Pitanga&entityTest2.entityTest3.entityTest4.fruit_op=eq&page=0&size=10";

		List<EntityTest1> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(2, list.size());
	}

	@Test
	void teste_busca_com_equal_pelo_entityTest5_por_fruit() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/page/EntityTest1?entityTest2.entityTest3.entityTest4.entityTest5.object=Cama&entityTest2.entityTest3.entityTest4.entityTest5.object_op=eq&page=0&size=10&sortList=name&sortOrders=desc";

		List<EntityTest1> list = getAll(url, new ErrorResponse());

		list.forEach(entity -> {
			assertEquals("Cama",
					entity.getEntityTest2().getEntityTest3().getEntityTest4().getEntityTest5().getObject());
		});

		assertNotNull(list);
		assertEquals(1, list.size());
	}

	@Test
	void teste_busca_com_like_pelo_entityTest5_por_fruit() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/page/EntityTest1?entityTest2.entityTest3.entityTest4.entityTest5.object=c*&entityTest2.entityTest3.entityTest4.entityTest5.object_op=lk&page=0&size=10&sortList=entityTest2.dateInclusion,entityTest2.hex&sortOrders=desc,asc";

		List<EntityTest1> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(4, list.size());
	}
	
	@Test
	void teste_findAll_paginacao_erro_repository() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/jpql/page/EntityTest/buscaComLikePeloName?name=Silva&sortList=name,birthDate&sortOrders=desc,asc";

		ErrorResponse errorResponse = new ErrorResponse("Cannot invoke \"org.springframework.data.jpa.repository.JpaRepository.getClass()\" because the return value of \"br.com.process.integration.database.core.application.AbstractJpaService.getRepository()\" is null", 400);

		assertThrows(RuntimeException.class, () -> getAll(url, errorResponse));

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
