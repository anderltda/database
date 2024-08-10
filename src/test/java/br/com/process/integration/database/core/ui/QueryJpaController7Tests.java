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

		List<EntityTest1> list = getAll(url);

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

		List<EntityTest1> list = getAll(url);

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

		List<EntityTest1> list = getAll(url);

		assertNotNull(list);
		assertEquals(0, list.size());
	}
	
	@Test
	void teste_busca_all_ordernacao_birthDate_desc_name_asc() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/jpql/page/EntityTest1/buscaAll?page=0&size=20&sortList=birthDate,name&sortOrders=desc,asc";

		List<EntityTest1> list = getAll(url);

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

		List<EntityTest1> list = getAll(url);
		
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

		List<EntityTest1> list = getAll(url);
		
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
