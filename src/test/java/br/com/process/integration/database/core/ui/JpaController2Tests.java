package br.com.process.integration.database.core.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import br.com.process.integration.database.core.util.Constants;
import br.com.process.integration.database.domain.entity.EntityTest1;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JpaController2Tests {

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
	void teste_busca_com_equal_pelo_name() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("name=").append("Anderson").append("&");
		url.append("name_op=").append(Constants.HTML_IGUAL).append("&");

		List<EntityTest1> list = getAll(url.toString());

		assertNotNull(list);
		assertEquals(1, list.size());

		System.out.println(url.toString());
		list.forEach(entity -> {
			assertNotNull(entity.getId());
			assertEquals("Anderson", entity.getName());
			assertEquals(41, entity.getAge());
			assertEquals(1.93, entity.getHeight());
			assertEquals(LocalDate.parse("1983-03-29", DateTimeFormatter.ISO_LOCAL_DATE), entity.getBirthDate());
			assertEquals(LocalDateTime.parse("2024-02-01T02:52:54", DateTimeFormatter.ISO_LOCAL_DATE_TIME), entity.getProhibited());
			assertNotEquals(0, entity.hashCode()); 
			assertNotNull(entity.getEntityTest2());
		});

	}

	@Test
	void teste_busca_com_equal_pelo_age_e_birthDate_e_prohibited_ordernacao_name_asc() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("age=").append("22").append("&");
		url.append("age_op=").append(Constants.HTML_IGUAL).append("&");

		url.append("birthDate=").append("1990-01-01").append("&");
		url.append("birthDate_op=").append(Constants.HTML_IGUAL).append("&");

		url.append("prohibited=").append("2024-11-01T08:00:00").append("&");
		url.append("prohibited_op=").append(Constants.HTML_IGUAL).append("&");

		url.append("sortList=").append("name").append("&");
		url.append("sortOrders=").append("asc");
		List<EntityTest1> list = getAll(url.toString());

		System.out.println(url.toString());
		list.forEach(entity -> {
			System.out.println(entity.getName());
		});

		assertNotNull(list);
		assertEquals(2, list.size());
		assertEquals("Ariovaldo", list.get(0).getName());
		assertEquals("Ricardo", list.get(1).getName());
	}
	
	
	@Test
	void teste_notEqual_do_teste_busca_com_equal_pelo_age_e_birthDate_e_prohibited_ordernacao_name_asc() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("age=").append("22").append("&");
		url.append("age_op=").append(Constants.HTML_DIFERENTE).append("&");

		url.append("birthDate=").append("1990-01-01").append("&");
		url.append("birthDate_op=").append(Constants.HTML_DIFERENTE).append("&");

		url.append("prohibited=").append("2024-11-01T08:00:00").append("&");
		url.append("prohibited_op=").append(Constants.HTML_DIFERENTE).append("&");

		url.append("sortList=").append("name").append("&");
		url.append("sortOrders=").append("asc");
		List<EntityTest1> list = getAll(url.toString());

		System.out.println(url.toString());
		list.forEach(entity -> {
			System.out.println(entity.getName());
		});

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
	void teste_notEqual_com_name() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("name=").append("Carlos").append("&");
		url.append("name_op=").append(Constants.HTML_DIFERENTE).append("&");

		url.append("sortList=").append("name").append("&");
		url.append("sortOrders=").append("asc");
		List<EntityTest1> list = getAll(url.toString());

		System.out.println(url.toString());
		list.forEach(entity -> {
			System.out.println(entity.getName());
		});

		assertNotNull(list);
		assertEquals(9, list.size());
	}

	@Test
	void teste_busca_com_equal_pelo_age_e_birthDate_e_height() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("age=").append("22").append("&");
		url.append("age_op=").append(Constants.HTML_IGUAL).append("&");

		url.append("birthDate=").append("1990-01-01").append("&");
		url.append("birthDate_op=").append(Constants.HTML_IGUAL).append("&");

		url.append("height=").append("1.8").append("&");
		url.append("height_op=").append(Constants.HTML_IGUAL).append("&");

		List<EntityTest1> list = getAll(url.toString());

		System.out.println(url.toString());
		assertNotNull(list);
		assertEquals(1, list.size());

		list.forEach(entity -> {
			System.out.println(entity.getName());
			assertEquals("Ricardo", entity.getName());
		});

	}

	@Test
	void teste_busca_com_equal_e_prohibited_e_ordernado_por_name_asc() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("prohibited=").append("2024-11-01T08:00:00").append("&");
		url.append("prohibited_op=").append(Constants.HTML_IGUAL).append("&");

		url.append("sortList=").append("name").append("&");
		url.append("sortOrder=").append("asc");
		List<EntityTest1> list = getAll(url.toString());

		System.out.println(url.toString());
		list.forEach(entity -> {
			System.out.println(entity.getName());
		});

		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("Ricardo", list.get(0).getName());
		assertEquals("Joana", list.get(1).getName());
		assertEquals("Ariovaldo", list.get(2).getName());
	}

	@Test
	void teste_busca_com_like_pelo_name_asterico_esquerda_e_direita_ordernado_por_birthDate_desc_e_name_asc() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("name=").append("*ar*").append("&");
		url.append("name_op=").append(Constants.HTML_LIKE).append("&");

		url.append("sortList=").append("birthDate,name").append("&");
		url.append("sortOrders=").append("desc,asc");
		List<EntityTest1> list = getAll(url.toString());

		System.out.println(url.toString());
		list.forEach(entity -> {
			System.out.println(entity.getName());
		});

		assertNotNull(list);
		assertEquals(5, list.size());
		assertEquals("Maria", list.get(0).getName());
		assertEquals("Ariovaldo", list.get(1).getName());
		assertEquals("Ricardo", list.get(2).getName());
		assertEquals("Carlos Alberto", list.get(3).getName());
		assertEquals("Carlos", list.get(4).getName());
	}

	@Test
	void teste_busca_por_like_pelo_name_asterico_direita() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("name=").append("ar*").append("&");
		url.append("name_op=").append(Constants.HTML_LIKE).append("&");

		List<EntityTest1> list = getAll(url.toString());

		System.out.println(url.toString());
		list.forEach(entity -> {
			System.out.println(entity.getName());
			assertEquals("Ariovaldo", entity.getName());
		});

		assertNotNull(list);
		assertEquals(1, list.size());
	}
	
	@Test
	void teste_busca_por_in_com_birthDate_ordernado_com_age_asc() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("birthDate=").append("1956-08-30").append(",").append("1986-09-09").append(",").append("1990-09-09").append("&");
		url.append("birthDate_op=").append(Constants.HTML_IN).append("&");

		url.append("sortList=").append("age").append("&");
		url.append("sortOrders=").append("asc");
		List<EntityTest1> list = getAll(url.toString());

		System.out.println(url.toString());
		list.forEach(entity -> {
			System.out.println(entity.getName());
		});

		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("Paulo", list.get(0).getName());
		assertEquals("Carlos", list.get(1).getName());
		assertEquals("Paulo Henrique", list.get(2).getName());
	}
	
	@Test
	void teste_busca_por_in_com_birthDate_ordernado_com_age_desc() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("birthDate=").append("1956-08-30").append(",").append("1990-01-01").append(",").append("1990-09-09").append("&");
		url.append("birthDate_op=").append(Constants.HTML_IN).append("&");

		url.append("sortList=").append("age,height").append("&");
		url.append("sortOrders=").append("desc,asc");
		List<EntityTest1> list = getAll(url.toString());
		
		System.out.println(url.toString());
		list.forEach(entity -> {
			System.out.println(entity.getName());
		});

		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("Carlos", list.get(0).getName());
		assertEquals("Ricardo", list.get(1).getName());
		assertEquals("Ariovaldo", list.get(2).getName());
		assertEquals("Paulo", list.get(3).getName());
	}
	
	@Test
	void teste_busca_por_in_com_age() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("age=").append("55").append(",").append("12").append(",").append("22").append("&");
		url.append("age_op=").append(Constants.HTML_IN).append("&");

		List<EntityTest1> list = getAll(url.toString());

		System.out.println(url.toString());
		list.forEach(entity -> {
			System.out.println(entity.getName());
		});

		assertNotNull(list);
		assertEquals(4, list.size());
	}

	@Test
	void teste_busca_por_in_com_ids() {

		List<Long> ids = JpaController1Tests.ids;
		
		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("id=").append(ids.get(0)).append(",").append(ids.get(1)).append("&");
		url.append("id_op=").append(Constants.HTML_IN).append("&");

		url.append("sortList=").append("id").append("&");
		url.append("sortOrders=").append("asc");
		List<EntityTest1> list = getAll(url.toString());

		System.out.println(url.toString());
		list.forEach(entity -> {
			System.out.println(entity.getName());
		});

		assertNotNull(list);
	}
	
	
	@Test
	void teste_busca_por_between_com_height_ordernado_por_height_desc() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("height=").append("1.40").append(",").append("1.78").append("&");
		url.append("height_op=").append(Constants.HTML_BETWEEN).append("&");

		url.append("sortList=").append("height").append("&");
		url.append("sortOrders=").append("desc");
		List<EntityTest1> list = getAll(url.toString());
		
		System.out.println(url.toString());
		list.forEach(entity -> {
			System.out.println(entity.getName());
		});

		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("Paulo", list.get(0).getName());
		assertEquals("Carlos Alberto", list.get(1).getName());
		assertEquals("Carlos", list.get(2).getName());
		assertEquals("Maria", list.get(3).getName());
	}
	
	@Test
	void teste_busca_por_between_com_prohibited_ordernado_por_birthDate_desc() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("prohibited=").append("2024-02-01T08:50:00").append(",").append("2024-10-01T08:50:55").append("&");
		url.append("prohibited_op=").append(Constants.HTML_BETWEEN).append("&");

		url.append("sortList=").append("birthDate").append("&");
		url.append("sortOrders=").append("desc");
		List<EntityTest1> list = getAll(url.toString());
		
		System.out.println(url.toString());
		list.forEach(entity -> {
			System.out.println(entity.getName());
		});

		assertNotNull(list);
		assertEquals(5, list.size());
		assertEquals("Maria", list.get(0).getName());
		assertEquals("Paulo", list.get(1).getName());
		assertEquals("Paulo Henrique", list.get(2).getName());
		assertEquals("Carlos Alberto", list.get(3).getName());
		assertEquals("Carlos", list.get(4).getName());
	}
	
	@Test
	void teste_busca_por_greaterThanOrEqualTo_com_height() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("height=").append("1.86").append("&");
		url.append("height_op=").append(Constants.HTML_MAIOR_OU_IGUAL);

		List<EntityTest1> list = getAll(url.toString());
		
		System.out.println(url.toString());
		list.forEach(entity -> {
			System.out.println(entity.getName());
		});

		assertNotNull(list);
		assertEquals(3, list.size());
	}
	
	
	@Test
	void teste_busca_por_greaterThan_com_height() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("height=").append("1.87").append("&");
		url.append("height_op=").append(Constants.HTML_MAIOR_QUE);

		List<EntityTest1> list = getAll(url.toString());
		
		System.out.println(url.toString());
		list.forEach(entity -> {
			System.out.println(entity.getName());
		});

		assertNotNull(list);
		assertEquals(2, list.size());
	}
	
	@Test
	void teste_busca_por_greaterThanOrEqualTo_com_birthDate() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("birthDate=").append("2016-01-01").append("&");
		url.append("birthDate_op=").append(Constants.HTML_MAIOR_OU_IGUAL);

		List<EntityTest1> list = getAll(url.toString());
		
		System.out.println(url.toString());
		list.forEach(entity -> {
			System.out.println(entity.getName());
			assertEquals("Maria", entity.getName());
		});

		assertNotNull(list);
		assertEquals(1, list.size());
	}
	
	
	@Test
	void teste_busca_por_greaterThan_com_birthDate() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("birthDate=").append("1990-09-09").append("&");
		url.append("birthDate_op=").append(Constants.HTML_MAIOR_QUE);

		List<EntityTest1> list = getAll(url.toString());
		
		System.out.println(url.toString());
		list.forEach(entity -> {
			System.out.println(entity.getName());
		});

		assertNotNull(list);
		assertEquals(3, list.size());
	}

	@Test
	void teste_busca_por_greaterThanOrEqualTo_e_lessThanOrEqualTo_com_birthDate() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("birthDate=").append("1990-01-02").append("&");
		url.append("birthDate_op=").append(Constants.HTML_MAIOR_OU_IGUAL).append("&");
		
		url.append("birthDate=").append("2016-01-01").append("&");
		url.append("birthDate_op=").append(Constants.HTML_MENOR_OU_IGUAL);

		List<EntityTest1> list = getAll(url.toString());
		
		System.out.println(url.toString());
		list.forEach(entity -> {
			System.out.println(entity.getName());
		});

		assertNotNull(list);
		assertEquals(4, list.size());
	}
	
	@Test
	void teste_busca_por_lessThanOrEqualTo_com_birthDate() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("birthDate=").append("1990-01-02").append("&");
		url.append("birthDate_op=").append(Constants.HTML_MENOR_OU_IGUAL);

		List<EntityTest1> list = getAll(url.toString());
		
		System.out.println(url.toString());
		list.forEach(entity -> {
			System.out.println(entity.getName());
		});

		assertNotNull(list);
		assertEquals(6, list.size());
	}
	
	@Test
	void teste_busca_por_lessThan_com_birthDate() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("birthDate=").append("1986-09-08").append("&");
		url.append("birthDate_op=").append(Constants.HTML_MENOR_QUE);

		List<EntityTest1> list = getAll(url.toString());
		
		System.out.println(url.toString());
		list.forEach(entity -> {
			System.out.println(entity.getName());
		});

		assertNotNull(list);
		assertEquals(3, list.size());
	}
	
	@Test
	void teste_busca_por_lessThanOrEqualTo_com_age() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("age=").append("21").append("&");
		url.append("age_op=").append(Constants.HTML_MENOR_OU_IGUAL);

		List<EntityTest1> list = getAll(url.toString());
		
		System.out.println(url.toString());
		list.forEach(entity -> {
			System.out.println(entity.getName());
		});

		assertNotNull(list);
		assertEquals(2, list.size());
	}
	
	
	@Test
	void teste_busca_por_lessThan_com_age() {

		StringBuilder url = new StringBuilder();
		url.append("http://localhost:" + port + "/v1/api-rest-database/find/all/EntityTest1?");

		url.append("age=").append("21").append("&");
		url.append("age_op=").append(Constants.HTML_MENOR_QUE);

		List<EntityTest1> list = getAll(url.toString());
		
		System.out.println(url.toString());
		list.forEach(entity -> {
			System.out.println(entity.getName());
			assertEquals("Maria", entity.getName());
		});

		assertNotNull(list);
		assertEquals(1, list.size());
	}

	private List<EntityTest1> getAll(String url) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<List<EntityTest1>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
				new ParameterizedTypeReference<List<EntityTest1>>() {
				});

		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		} else {
			throw new RuntimeException("Failed to fetch users. Status code: " + response.getStatusCode());
		}
	}
}
