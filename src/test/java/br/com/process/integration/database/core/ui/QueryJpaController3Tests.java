package br.com.process.integration.database.core.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import br.com.process.integration.database.core.util.Constants;
import br.com.process.integration.database.domain.model.entity.EntityOne;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QueryJpaController3Tests {

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
	void teste_busca_com_equal_pelo_entityTwo_por_color_por_ordernacao_entityTwo() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/find/all/EntityOne?entityTwo.color=Preto&entityTwo.color_op=eq&sortList=entityTwo.dateInclusion, entityTwo.hex&sortOrders=desc,asc";
		
		List<EntityOne> list = getAll(url);
		
		assertNotNull(list);
		assertEquals(2, list.size());
		assertEquals("Carlos Alberto", list.get(0).getName());
		assertEquals("Carlos", list.get(1).getName());
		assertEquals(234, list.get(0).getEntityTwo().getHex());
		assertEquals(144, list.get(1).getEntityTwo().getHex());
	}
	
	@Test
	void teste_busca_com_in_pelo_entityTree_por_animal() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/find/all/EntityOne?entityTwo.entityTree.animal=Cavalo,Gato,Papagaio&entityTwo.entityTree.animal_op=in&page=0&size=10&sortList=entityTwo.color,entityTwo.entityTree.animal&sortOrders=desc,asc";

		List<EntityOne> list = getAll(url);

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

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/find/all/EntityOne?entityTwo.entityTree.animal=Capivara&entityTwo.entityTree.animal_op=eq";

		List<EntityOne> list = getAll(url);

		list.forEach(entity -> {
			assertEquals("Carlos Alberto", entity.getName());
		});

		assertNotNull(list);
		assertEquals(1, list.size());
	}
	
	@Test
	void teste_busca_com_equal_pelo_entityFour_por_fruit() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/find/all/EntityOne?entityTwo.entityTree.entityFour.fruit=Pitanga&entityTwo.entityTree.entityFour.fruit_op=eq";

		List<EntityOne> list = getAll(url);

		assertNotNull(list);
		assertEquals(2, list.size());
	}
	
	
	@Test
	void teste_busca_com_equal_pelo_entityFive_por_fruit() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/find/all/EntityOne?entityTwo.entityTree.entityFour.entityFive.object=Cama&entityTwo.entityTree.entityFour.entityFive.object_op=eq";

		List<EntityOne> list = getAll(url);

		list.forEach(entity -> {
			assertEquals("Cama", entity.getEntityTwo().getEntityTree().getEntityFour().getEntityFive().getObject());
		});

		assertNotNull(list);
		assertEquals(1, list.size());
	}
	
	
	@Test
	void teste_busca_com_like_pelo_entityFive_por_fruit() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/find/all/EntityOne?entityTwo.entityTree.entityFour.entityFive.object=c*&entityTwo.entityTree.entityFour.entityFive.object_op=lk";

		List<EntityOne> list = getAll(url);
	
		assertNotNull(list);
		assertEquals(4, list.size());
	}

	private List<EntityOne> getAll(String url) {
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.set("Accept", "application/json");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<List<EntityOne>> response = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<EntityOne>>() { });

		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		} else {
			throw new RuntimeException("Failed to fetch users. Status code: " + response.getStatusCode());
		}
	}
}
