package br.com.process.integration.database.core.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import br.com.process.integration.database.core.util.Constants;
import br.com.process.integration.database.domain.entity.EntityTest1;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Controller5Tests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private QueryJpaController queryJpaController;

	@Test
	@Order(1)
	void contextLoads() {
		assertNotNull(queryJpaController);
	};

	@Test
	void teste_busca_com_equal_pelo_entityTest2_por_color_por_ordernacao_entityTest2() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/page/EntityTest1?");

		url.append("entityTest2.color=").append("Preto").append("&");
		url.append("entityTest2.color_op=").append(Constants.HTML_IGUAL).append("&");
		
		url.append("page=").append(0).append("&");
		url.append("size=").append(10).append("&");

		url.append("sortList=").append("entityTest2.dateInclusion, entityTest2.hex").append("&");
		url.append("sortOrders=").append("desc,asc");
		
		PagedModel<?> list = getAll(url.toString());

		List<EntityTest1> entitys = new ArrayList<>();

		System.out.println(url.toString());
		
		list.getContent().forEach(object -> {
			
			Gson gson = new GsonBuilder()
					.registerTypeAdapter(LocalDate.class, new LocalDateAdapter(true))
					.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter(true)).create();

	        String jsonString = gson.toJson(object);
	        EntityTest1 entity = gson.fromJson(jsonString, EntityTest1.class);
	        entitys.add(entity);
		});

		assertNotNull(list);
		assertEquals(2, list.getContent().size());
		assertEquals(2, entitys.size());
		assertEquals("Carlos Alberto", entitys.get(0).getName());
		assertEquals("Carlos", entitys.get(1).getName());
		assertEquals(234, entitys.get(0).getEntityTest2().getHex());
		assertEquals(144, entitys.get(1).getEntityTest2().getHex());
	}
	
	@Test
	void teste_busca_com_in_pelo_entityTest3_por_animal() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/page/EntityTest1?");
		
		url.append("entityTest2.entityTest3.animal=").append("Cavalo").append(",").append("Gato").append(",")
				.append("Papagaio").append("&");
		
		url.append("entityTest2.entityTest3.animal_op=").append(Constants.HTML_IN).append("&");
		
		url.append("page=").append(0).append("&");
		url.append("size=").append(10).append("&");
		
		url.append("sortList=").append("entityTest2.color, entityTest2.entityTest3.animal").append("&");
		url.append("sortOrders=").append("desc,asc");

		PagedModel<?> list = getAll(url.toString());
		
		List<EntityTest1> entitys = new ArrayList<>();

		System.out.println(url.toString());
		
		list.getContent().forEach(object -> {
			
			Gson gson = new GsonBuilder()
					.registerTypeAdapter(LocalDate.class, new LocalDateAdapter(true))
					.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter(true)).create();

	        String jsonString = gson.toJson(object);
	        EntityTest1 entity = gson.fromJson(jsonString, EntityTest1.class);
	        entitys.add(entity);
		});
		
		assertNotNull(list);
		assertEquals(5, list.getContent().size());
		assertEquals(5, entitys.size());
		
		assertEquals("Paulo Henrique", entitys.get(0).getName());
		assertEquals("Ricardo", entitys.get(1).getName());
		assertEquals("Paulo", entitys.get(2).getName());
		assertEquals("Renato", entitys.get(3).getName());
		assertEquals("Ariovaldo", entitys.get(4).getName());
		
		assertEquals("Verde", entitys.get(0).getEntityTest2().getColor());
		assertEquals("Verde", entitys.get(1).getEntityTest2().getColor());
		assertEquals("Roxo", entitys.get(2).getEntityTest2().getColor());
		assertEquals("Laranja", entitys.get(3).getEntityTest2().getColor());
		assertEquals("Cinza", entitys.get(4).getEntityTest2().getColor());
		
		assertEquals("Cavalo", entitys.get(0).getEntityTest2().getEntityTest3().getAnimal());
		assertEquals("Gato", entitys.get(1).getEntityTest2().getEntityTest3().getAnimal());
		assertEquals("Cavalo", entitys.get(2).getEntityTest2().getEntityTest3().getAnimal());
		assertEquals("Papagaio", entitys.get(3).getEntityTest2().getEntityTest3().getAnimal());
		assertEquals("Gato", entitys.get(4).getEntityTest2().getEntityTest3().getAnimal());

	}

	@Test
	void teste_busca_com_equal_pelo_entityTest3_por_animal() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/page/EntityTest1?");

		url.append("entityTest2.entityTest3.animal=").append("Capivara").append("&");
		url.append("entityTest2.entityTest3.animal_op=").append(Constants.HTML_IGUAL).append("&");
		
		url.append("page=").append(0).append("&");
		url.append("size=").append(10).append("&");
		
		PagedModel<?> list = getAll(url.toString());

		List<EntityTest1> entitys = new ArrayList<>();
		System.out.println(url.toString());
		
		list.getContent().forEach(object -> {
			
			Gson gson = new GsonBuilder()
					.registerTypeAdapter(LocalDate.class, new LocalDateAdapter(true))
					.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter(true)).create();

	        String jsonString = gson.toJson(object);
	        EntityTest1 entity = gson.fromJson(jsonString, EntityTest1.class);
	        System.out.println(entity.getName());
	        assertEquals("Carlos Alberto", entity.getName());
	        entitys.add(entity);
		});

		assertNotNull(list);
		assertEquals(1, list.getContent().size());
		assertEquals(1, entitys.size());
	}
	
	@Test
	void teste_busca_com_equal_pelo_entityTest4_por_fruit() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/page/EntityTest1?");

		url.append("entityTest2.entityTest3.entityTest4.fruit=").append("Pitanga").append("&");
		url.append("entityTest2.entityTest3.entityTest4.fruit_op=").append(Constants.HTML_IGUAL).append("&");
		
		url.append("page=").append(0).append("&");
		url.append("size=").append(10).append("&");

		PagedModel<?> list = getAll(url.toString());

		List<EntityTest1> entitys = new ArrayList<>();
		System.out.println(url.toString());
		
		list.getContent().forEach(object -> {
			
			Gson gson = new GsonBuilder()
					.registerTypeAdapter(LocalDate.class, new LocalDateAdapter(true))
					.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter(true)).create();

	        String jsonString = gson.toJson(object);
	        EntityTest1 entity = gson.fromJson(jsonString, EntityTest1.class);
	        System.out.println(entity.getName());
	        entitys.add(entity);
		});
		
		assertNotNull(list);
		assertEquals(2, list.getContent().size());
		assertEquals(2, entitys.size());
	}
	
	
	@Test
	void teste_busca_com_equal_pelo_entityTest5_por_fruit() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/page/EntityTest1?");

		url.append("entityTest2.entityTest3.entityTest4.entityTest5.object=").append("Cama").append("&");
		url.append("entityTest2.entityTest3.entityTest4.entityTest5.object_op=").append(Constants.HTML_IGUAL).append("&");
		
		url.append("page=").append(0).append("&");
		url.append("size=").append(10).append("&");

		PagedModel<?> list = getAll(url.toString());

		List<EntityTest1> entitys = new ArrayList<>();
		System.out.println(url.toString());
		
		list.getContent().forEach(object -> {
			Gson gson = new GsonBuilder()
					.registerTypeAdapter(LocalDate.class, new LocalDateAdapter(true))
					.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter(true)).create();

	        String jsonString = gson.toJson(object);
	        EntityTest1 entity = gson.fromJson(jsonString, EntityTest1.class);
	        entitys.add(entity);
			
			System.out.println(entity.getEntityTest2().getEntityTest3().getEntityTest4().getEntityTest5().getObject());
			assertEquals("Cama", entity.getEntityTest2().getEntityTest3().getEntityTest4().getEntityTest5().getObject());
		});

		assertNotNull(list);
		assertEquals(1, list.getContent().size());
		assertEquals(1, entitys.size());
	}
	
	
	@Test
	void teste_busca_com_like_pelo_entityTest5_por_fruit() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/page/EntityTest1?");

		url.append("entityTest2.entityTest3.entityTest4.entityTest5.object=").append("c*").append("&");
		url.append("entityTest2.entityTest3.entityTest4.entityTest5.object_op=").append(Constants.HTML_LIKE).append("&");
		
		url.append("page=").append(0).append("&");
		url.append("size=").append(10).append("&");
		
		url.append("sortList=").append("entityTest2.dateInclusion, entityTest2.hex").append("&");
		url.append("sortOrders=").append("desc,asc");

		PagedModel<?> list = getAll(url.toString());

		List<EntityTest1> entitys = new ArrayList<>();
		System.out.println(url.toString());
		list.getContent().forEach(object -> {
			Gson gson = new GsonBuilder()
					.registerTypeAdapter(LocalDate.class, new LocalDateAdapter(true))
					.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter(true)).create();

	        String jsonString = gson.toJson(object);
	        EntityTest1 entity = gson.fromJson(jsonString, EntityTest1.class);
	        entitys.add(entity);
			System.out.println(entity.getEntityTest2().getEntityTest3().getEntityTest4().getEntityTest5().getObject());
		});

		assertNotNull(list);
		assertEquals(4, list.getContent().size());
		assertEquals(4, entitys.size());
	}

	private PagedModel<?> getAll(String url) {
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
