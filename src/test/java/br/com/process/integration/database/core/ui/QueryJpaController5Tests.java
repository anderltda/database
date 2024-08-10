package br.com.process.integration.database.core.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.process.integration.database.core.ui.adapter.LocalDateAdapter;
import br.com.process.integration.database.core.ui.adapter.LocalDateTimeAdapter;
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
	void setupOnce() { }
	
	@Test
	@Order(1)
	void contextLoads() {
		assertNotNull(queryJpaController);
	};

	@Test
	void teste_busca_com_equal_pelo_entityTest2_por_color_por_ordernacao_entityTest2() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/page/EntityTest1?entityTest2.color=Preto&entityTest2.color_op=eq&page=0&size=10&sortList=entityTest2.dateInclusion,entityTest2.hex&sortOrders=desc,asc";
		
		List<EntityTest1> list = getAll(url.toString());

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

		List<EntityTest1> list = getAll(url.toString());

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
		
		List<EntityTest1> list = getAll(url.toString());

		list.forEach(entity -> {
			assertEquals("Carlos Alberto", entity.getName());
		});

		assertNotNull(list);
		assertEquals(1, list.size());
	}
	
	@Test
	void teste_busca_com_equal_pelo_entityTest4_por_fruit() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/page/EntityTest1?entityTest2.entityTest3.entityTest4.fruit=Pitanga&entityTest2.entityTest3.entityTest4.fruit_op=eq&page=0&size=10";

		List<EntityTest1> list = getAll(url.toString());

		assertNotNull(list);
		assertEquals(2, list.size());
	}
	
	
	@Test
	void teste_busca_com_equal_pelo_entityTest5_por_fruit() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/page/EntityTest1?entityTest2.entityTest3.entityTest4.entityTest5.object=Cama&entityTest2.entityTest3.entityTest4.entityTest5.object_op=eq&page=0&size=10&sortList=name&sortOrders=desc";

		List<EntityTest1> list = getAll(url.toString());

		list.forEach(entity -> {
			assertEquals("Cama", entity.getEntityTest2().getEntityTest3().getEntityTest4().getEntityTest5().getObject());
		});

		assertNotNull(list);
		assertEquals(1, list.size());
	}
	
	
	@Test
	void teste_busca_com_like_pelo_entityTest5_por_fruit() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/page/EntityTest1?entityTest2.entityTest3.entityTest4.entityTest5.object=c*&entityTest2.entityTest3.entityTest4.entityTest5.object_op=lk&page=0&size=10&sortList=entityTest2.dateInclusion,entityTest2.hex&sortOrders=desc,asc";

		List<EntityTest1> list = getAll(url.toString());
	
		assertNotNull(list);
		assertEquals(4, list.size());
	}
	
	private List<EntityTest1> getAll(String url) {

		List<EntityTest1> entitys = new ArrayList<>();
		
		PagedModel<?> list = getRestAll(url);
		
		System.out.println(url);
		
		list.getContent().forEach(object -> {
			Gson gson = new GsonBuilder()
					.registerTypeAdapter(LocalDate.class, new LocalDateAdapter(true))
					.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter(true)).create();

	        String jsonString = gson.toJson(object);
	        entitys.add(gson.fromJson(jsonString, EntityTest1.class));
		});
		
		assertNotNull(list);
		assertEquals(entitys.size(), list.getContent().size());
		
		return entitys;
	}

	private PagedModel<?> getRestAll(String url) {
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.set("Accept", "application/json");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<PagedModel<?>> response = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<PagedModel<?>>() { });

		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		} else {
			throw new RuntimeException("Failed to fetch users. Status code: " + response.getStatusCode());
		}
	}
}
