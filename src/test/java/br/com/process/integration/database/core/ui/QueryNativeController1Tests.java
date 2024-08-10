package br.com.process.integration.database.core.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import br.com.process.integration.database.domain.entity.EntityTest1;
import br.com.process.integration.database.domain.view.EntityTest1View;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QueryNativeController1Tests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private QueryNativeController queryNativeController;
	
	@BeforeAll
	void setupOnce() { }
	
	@Test
	@Order(1)
	void contextLoads() {
		assertNotNull(queryNativeController);
	};

	@Test
	void teste_busca_com_equal_pelo_name() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_equal_validar_orderby?name=Anderson&name_op=eq";

		List<EntityTest1View> list = getAll(url);
		
		list.forEach(entity -> {
			assertNotNull(entity.getIdEntityTest1());
			assertEquals("Anderson", entity.getName());
			assertEquals(41, entity.getAge());
			assertEquals(1.93, entity.getHeight());
			assertEquals(LocalDate.parse("1983-03-29", DateTimeFormatter.ISO_LOCAL_DATE), entity.getBirthDate());
			assertEquals(LocalDateTime.parse("2024-02-01T02:52:54", DateTimeFormatter.ISO_LOCAL_DATE_TIME), entity.getProhibited());
			assertNotEquals(0, entity.hashCode()); 
			assertNotNull(entity.getIdEntityTest2());
			assertNotNull(entity.getIdEntityTest3());
			assertNotNull(entity.getIdEntityTest4());
			assertNotNull(entity.getIdEntityTest5());
			
		});
		
		assertNotNull(list);
		assertEquals(1, list.size());
	}

	@Test
	void teste_busca_com_equal_pelo_age_e_birthDate_e_prohibited_ordernacao_name_asc() {
		
		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?"
				+ "age=22&age_op=eq&birthDate=1990-01-01&birthDate_op=eq&prohibited=2024-11-01T08:00:00&prohibited_op=eq&sortList=name,age&sortOrders=asc";

		List<EntityTest1View> list = getAll(url);

		assertNotNull(list);
		assertEquals(2, list.size());
		assertEquals("Ariovaldo", list.get(0).getName());
		assertEquals("Ricardo", list.get(1).getName());
	}

	@Test
	void teste_notEqual_do_teste_busca_com_equal_pelo_age_e_birthDate_e_prohibited_ordernacao_name_asc() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?"
				+ "age=22&age_op=ne&birthDate=1990-01-01&birthDate_op=ne&prohibited=2024-11-01T08:00:00&prohibited_op=ne&sortList=name&sortOrders=asc";
		
		List<EntityTest1View> list = getAll(url);

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
		
		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?"
				+ "name=Carlos&name_op=ne&sortList=name&sortOrders=asc";
		
		testes_single_parameterized_one(url, 9);
	}

	@Test
	void teste_busca_com_equal_pelo_age_e_birthDate_e_height() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?"
				+ "age=22&age_op=eq&birthDate=1990-01-01&birthDate_op=eq&height=1.80&height_op=eq";

		testes_single_parameterized_other(url, "Ricardo", 1);
	}

	@Test
	void teste_busca_com_equal_e_prohibited_e_ordernado_por_name_asc() {
		
		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?"
				+ "prohibited=2024-11-01T08:00:00&prohibited_op=eq";
		
		List<EntityTest1View> list = getAll(url);
		
		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("Ricardo", list.get(0).getName());
		assertEquals("Joana", list.get(1).getName());
		assertEquals("Ariovaldo", list.get(2).getName());
	}
	
	@Test
	void teste_busca_com_like_pelo_name_asterico_esquerda_e_direita_ordernado_por_birthDate_desc_e_name_asc() {
		
		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?"
				+ "name=*ar*&name_op=lk&sortList=birthDate,name&sortOrders=desc,asc";
		
		List<EntityTest1View> list = getAll(url);

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

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?name=ar*&name_op=lk";
		
		testes_single_parameterized_other(url, "Ariovaldo", 1);
	}

	@Test
	void teste_busca_por_in_com_birthDate_ordernado_com_age_asc() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?birthDate=1956-08-30,1986-09-09,1990-09-09&birthDate_op=in&sortList=age&sortOrders=asc";
		
		List<EntityTest1View> list = getAll(url);

		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("Paulo", list.get(0).getName());
		assertEquals("Carlos", list.get(1).getName());
		assertEquals("Paulo Henrique", list.get(2).getName());
	}

	@Test
	void teste_busca_por_in_com_birthDate_ordernado_com_age_desc() {
		
		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?birthDate=1956-08-30,1990-01-01,1990-09-09&birthDate_op=in&sortList=age,height&sortOrders=desc,asc";
		
		List<EntityTest1View> list = getAll(url);

		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("Carlos", list.get(0).getName());
		assertEquals("Ricardo", list.get(1).getName());
		assertEquals("Ariovaldo", list.get(2).getName());
		assertEquals("Paulo", list.get(3).getName());
	}

	@Test
	void teste_busca_por_in_com_age() {
		
		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?age=55,12,22&age_op=in";

		testes_single_parameterized_one(url, 4);
	}

	@Test
	void teste_busca_por_in_com_ids() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?idEntityTest1=" + QueryJpaController1Tests.ids.get(0) +"," + QueryJpaController1Tests.ids.get(1) + "&idEntityTest1_op=in&sortList=idEntityTest_1&sortOrders=asc";

		testes_single_parameterized_one(url, 2);
	}

	@Test
	void teste_busca_por_between_com_height_ordernado_por_height_desc() {
		
		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?height=1.40,1.78&height_op=bt&sortList=height&sortOrders=desc";

		List<EntityTest1View> list = getAll(url);

		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("Paulo", list.get(0).getName());
		assertEquals("Carlos Alberto", list.get(1).getName());
		assertEquals("Carlos", list.get(2).getName());
		assertEquals("Maria", list.get(3).getName());
	}

	@Test
	void teste_busca_por_between_com_prohibited_ordernado_por_birthDate_desc() {
		
		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?prohibited=2024-02-01T08:50:00,2024-10-01T08:50:55&prohibited_op=bt&sortList=birthDate&sortOrders=desc";
		
		List<EntityTest1View> list = getAll(url);

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
		
		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?height=1.86&height_op=ge&sortList=birthDate&sortOrders=desc";

		testes_single_parameterized_one2(url, 3);
	}

	@Test
	void teste_busca_por_greaterThan_com_height() {
		
		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?height=1.87&height_op=gt&sortList=birthDate&sortOrders=desc";

		testes_single_parameterized_one2(url, 2);
	}

	@Test
	void teste_busca_por_greaterThan_com_birthDate() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?birthDate=1990-09-09&birthDate_op=gt&sortList=birthDate&sortOrders=desc";

		testes_single_parameterized_one2(url, 3);
	}

	@Test
	void teste_busca_por_greaterThanOrEqualTo_e_lessThanOrEqualTo_com_birthDate() {
		
		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?birthDate=1990-01-02&birthDate_op=ge&birthDate=2016-01-01&birthDate_op=le&sortList=birthDate&sortOrders=desc";

		testes_single_parameterized_one2(url, 4);
	}

	@Test
	void teste_busca_por_lessThanOrEqualTo_com_birthDate() {
		
		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?birthDate=1990-01-02&birthDate_op=le";

		testes_single_parameterized_one2(url, 6);
	}

	@Test
	void teste_busca_por_lessThan_com_birthDate() {
		
		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?birthDate=1986-09-08&birthDate_op=lt";

		testes_single_parameterized_one2(url, 3);
	}

	@Test
	void teste_busca_por_lessThanOrEqualTo_com_age() {
		
		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?age=21&age_op=le";

		testes_single_parameterized_one2(url, 2);
	}
	
	@Test
	void teste_busca_por_greaterThanOrEqualTo_com_birthDate() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?birthDate=2016-01-01&birthDate_op=ge";

		testes_single_parameterized_other2(url, "Maria", 1);
	}
	
	@Test
	void teste_busca_por_lessThan_com_age() {
		
		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?age=21&age_op=lt";
		
		testes_single_parameterized_other2(url, "Maria", 1);
	}
	
	@Test
	void teste_busca_all_ordernacao_birthDate_asc_name_desc() {
		
		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?sortList=birthDate,name&sortOrders=asc,desc";

		List<EntityTest1>list = getAll2(url);

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
	void teste_busca_all_ordernacao_birthDate_desc_name_asc() {
		
		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?sortList=birthDate,name&sortOrders=desc,asc";

		List<EntityTest1> list = getAll2(url);

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

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?sortList=name,birthDate&sortOrders=asc,desc";

		List<EntityTest1> list = getAll2(url);
		
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

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?sortList=name,birthDate&sortOrders=desc,asc";

		List<EntityTest1> list = getAll2(url);
		
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
	void teste_nenhum_registro_encontrado() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/execute/query/find/all/EntityTest1View/teste_busca_com_condicoes_diversars?name=Silva&name_op=eq";

		List<EntityTest1> list = getAll2(url);
		
		assertNull(list);
	}
	
	@Test
	void teste_busca_por_ids() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/ids/EntityTest1?ids=" + QueryJpaController1Tests.ids.get(0) + "," + QueryJpaController1Tests.ids.get(1) + "," + QueryJpaController1Tests.ids.get(2);

		List<EntityTest1> list = getAll2(url);
		
		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("Carlos Alberto", list.get(0).getName());
		assertEquals("Paulo", list.get(1).getName());
		assertEquals("Maria", list.get(2).getName());
	}
	
	@Test
	void teste_busca_por_ids_nao_encontrado() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/all/ids/EntityTest1?ids=1,2,3";

		List<EntityTest1> list = getAll2(url);
		
		assertNull(list);
	}
	
	@Test
	void teste_busca_por_single_age_name_birthDate() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/single/EntityTest1?age=41&age_op=eq&name=Anderson&name_op=eq&birthDate=1983-03-29&birthDate_op=ge";
		
		EntityTest1 entity = getSingleResult2(url);
		
		assertNotNull(entity);
		assertEquals("Anderson", entity.getName());
	}
	
	@Test
	void teste_busca_por_id() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/EntityTest1/" + QueryJpaController1Tests.ids.get(1);
		
		EntityTest1 entity = getSingleResult2(url);
		
		assertNotNull(entity);
		assertEquals("Paulo", entity.getName());
	}
	
	@Test
	void teste_busca_por_id_nao_encontrado() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/find/EntityTest1/1";
		
		EntityTest1 entity = getSingleResult2(url);
		
		assertNull(entity);
	}
	
	@Test
	void teste_count_maior_prohibited() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/count/EntityTest1?prohibited=2024-11-01T08:00:00&prohibited_op=ge";
		
		Integer count = (Integer) getUniqueResult2(url);
		
		assertEquals(4, count);
	}
	
	@Test
	void teste_existi_o_id() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/exist/EntityTest1/" + QueryJpaController1Tests.ids.get(1);
		
		Boolean value = (Boolean) getUniqueResult2(url);
		
		assertTrue(value);
	}
	
	@Test
	void teste_nao_existi_o_id() {

		String url = "http://localhost:" + port + "/v1/api-rest-database/exist/EntityTest1/1";
		
		Boolean value = (Boolean) getUniqueResult2(url);
		
		assertFalse(value);
	}
	
	void testes_single_parameterized_other(String url, String value, Integer size) {
		
		List<EntityTest1View> list = getAll(url);
		
		assertNotNull(list);
		assertEquals(size, list.size());
		assertEquals(value, list.get(0).getName());
	}
	
	void testes_single_parameterized_one(String url, Integer size) {
		
		List<EntityTest1View> list = getAll(url);
		
		assertNotNull(list);
		assertEquals(size, list.size());
	}
	
	public List<EntityTest1View> getAll(String url) {
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.set("Accept", "application/json");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<List<EntityTest1View>> response = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<EntityTest1View>>() { });

		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		} else {
			throw new RuntimeException("Failed to fetch users. Status code: " + response.getStatusCode());
		}
	}
	
	public EntityTest1View getSingleResult(String url) {
	
		HttpHeaders headers = new HttpHeaders();
		
		headers.set("Accept", "application/json");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<EntityTest1View> response = restTemplate.exchange(url, HttpMethod.GET, entity, EntityTest1View.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		} else {
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode());
		}
	}
	
	/**
	 * remover
	 */
	
	void testes_single_parameterized_other2(String url, String value, Integer size) {
		
		List<EntityTest1> list = getAll2(url);
		
		assertNotNull(list);
		assertEquals(size, list.size());
		assertEquals(value, list.get(0).getName());
	}
	
	void testes_single_parameterized_one2(String url, Integer size) {
		
		List<EntityTest1> list = getAll2(url);
		
		assertNotNull(list);
		assertEquals(size, list.size());
	}
	
	public List<EntityTest1> getAll2(String url) {
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.set("Accept", "application/json");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<List<EntityTest1>> response = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<EntityTest1>>() { });

		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		} else {
			throw new RuntimeException("Failed to fetch users. Status code: " + response.getStatusCode());
		}
	}
	
	public EntityTest1 getSingleResult2(String url) {
	
		HttpHeaders headers = new HttpHeaders();
		
		headers.set("Accept", "application/json");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<EntityTest1> response = restTemplate.exchange(url, HttpMethod.GET, entity, EntityTest1.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		} else {
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode());
		}
	}
	
	private Object getUniqueResult2(String url) {
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.set("Accept", "application/json");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		} else {
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode());
		}
	}
}
