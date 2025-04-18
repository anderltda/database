package br.com.process.integration.database.core;

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
import org.springframework.http.HttpStatusCode;
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
class CriteriaPaginatorJoinTests {

	@LocalServerPort
	private int port;
	
	private static final String PATH = "http://localhost:";

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
	void teste_01() {

		String url = PATH
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name=Anderson&"
		        + "code=true&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=age,height&"
		        + "sortOrders=asc,asc";

		singleParameterizedOne("Anderson", url);
	}

	@Test
	void teste_02() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=eq&"
		        + "name=Anderson&"
		        + "code=true&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=age,height&"
		        + "sortOrders=asc,asc";

		singleParameterizedOne("Anderson", url);
	}

	@Test
	void teste_03() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=ne&"
		        + "name=Anderson&"
		        + "code=true&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=age,height&"
		        + "sortOrders=asc,asc";
		
		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(9, list.size());
		assertEquals("Maria", list.get(0).getName());
		assertEquals("Paulo", list.get(1).getName());
		assertEquals("Ricardo", list.get(2).getName());
		assertEquals("Ariovaldo", list.get(3).getName());
		assertEquals("Joana", list.get(4).getName());
		assertEquals("Carlos", list.get(5).getName());
		assertEquals("Renato", list.get(6).getName());
		assertEquals("Paulo Henrique", list.get(7).getName());
		assertEquals("Carlos Alberto", list.get(8).getName());
	}

	@Test
	void teste_04() {

		String url = PATH 
				+ port 
				+ Constants.API_NAME_REQUEST_MAPPING 
				+ "/paginator/EntityOne?"
				+ "name_op=in&name=Anderson,Paulo,Joana&"
				+ "code=true&"
				+ "page=0&"
				+ "size=10&"
				+ "sortList=age,height,name,birthDate&"
				+ "sortOrders=asc,asc,asc,asc";
		
		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("Paulo", list.get(0).getName());
		assertEquals("Joana", list.get(1).getName());
		assertEquals("Anderson", list.get(2).getName());
	}

	@Test
	void teste_05() {

		String url = PATH 
				+ port 
				+ Constants.API_NAME_REQUEST_MAPPING 
				+ "/paginator/EntityOne?"
				+ "name_op=in&name=Anderson,Paulo,Joana&"
				+ "code=true&"
				+ "entityTwo.color_op=in&"
				+ "entityTwo.color=Roxo,Amarelo,Azul&"
				+ "entityTwo.hex_op=bt&entityTwo.hex=523,3133&"
				+ "page=0&"
				+ "size=10&"
				+ "sortList=age,height,name,birthDate&"
				+ "sortOrders=asc,asc,asc,asc";
		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("Paulo", list.get(0).getName());
		assertEquals("Joana", list.get(1).getName());
		assertEquals("Anderson", list.get(2).getName());
	}

	@Test
	void teste_06() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=in&"
		        + "name=Anderson,Paulo,Joana,Ricardo&"
		        + "code=true&"
		        + "entityTwo.color_op=in&"
		        + "entityTwo.color=Roxo,Amarelo,Verde,Azul,Preto,Laranja&"
		        + "entityTwo.hex_op=ne&"
		        + "entityTwo.hex=0&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=age,height,name,birthDate,code&"
		        + "sortOrders=asc,desc,asc,asc,asc";
		
		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("Paulo", list.get(0).getName());
		assertEquals("Ricardo", list.get(1).getName());
		assertEquals("Joana", list.get(2).getName());
		assertEquals("Anderson", list.get(3).getName());
	}

	@Test
	void teste_07() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=in&"
		        + "name=Anderson,Paulo,Joana,Ricardo&"
		        + "age_op=bt&"
		        + "age=20,45&"
		        + "code=true&"
		        + "entityTwo.color_op=in&"
		        + "entityTwo.color=Roxo,Amarelo,Verde,Azul,Preto,Laranja&"
		        + "entityTwo.hex_op=ne&"
		        + "entityTwo.hex=0&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=code,entityTwo.hex,prohibitedDateTime&"
		        + "sortOrders=asc,desc,desc";
		
		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("Ricardo", list.get(0).getName());
		assertEquals("Paulo", list.get(1).getName());
		assertEquals("Anderson", list.get(2).getName());
		assertEquals("Joana", list.get(3).getName());
	}

	@Test
	void teste_08() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=in&"
		        + "name=Anderson,Paulo,Joana,Ricardo&"
		        + "age_op=bt&"
		        + "age=20,45&"
		        + "code=true&"
		        + "entityTwo.color_op=in&"
		        + "entityTwo.color=Roxo,Amarelo,Verde,Azul,Preto,Laranja&"
		        + "entityTwo.hex_op=bt&"
		        + "entityTwo.hex=523,12345&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=code,entityTwo.hex,prohibitedDateTime&"
		        + "sortOrders=asc,desc,desc";
		
		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("Ricardo", list.get(0).getName());
		assertEquals("Paulo", list.get(1).getName());
		assertEquals("Anderson", list.get(2).getName());
		assertEquals("Joana", list.get(3).getName());
	}

	@Test
	void teste_09() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=in&"
		        + "name=Anderson,Paulo,Joana,Ricardo&"
		        + "age_op=bt&"
		        + "age=20,45&"
		        + "code=true&"
		        + "entityTwo.color_op=in&"
		        + "entityTwo.color=Roxo,Amarelo,Verde,Azul,Preto,Laranja&"
		        + "entityTwo.hex_op=bt&"
		        + "entityTwo.hex=523,12345&"
		        + "entityTwo.cost_op=ge&"
		        + "entityTwo.cost=12,00&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=code,entityTwo.hex,prohibitedDateTime&"
		        + "sortOrders=asc,desc,desc";
		
		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("Ricardo", list.get(0).getName());
		assertEquals("Paulo", list.get(1).getName());
		assertEquals("Anderson", list.get(2).getName());
		assertEquals("Joana", list.get(3).getName());
	}

	@Test
	void teste_10() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=in&"
		        + "name=Anderson,Paulo,Joana,Ricardo&"
		        + "age_op=bt&"
		        + "age=20,45&"
		        + "code=true&"
		        + "entityTwo.color_op=in&"
		        + "entityTwo.color=Roxo,Amarelo,Verde,Azul,Preto,Laranja&"
		        + "entityTwo.hex_op=bt&"
		        + "entityTwo.hex=523,12345&"
		        + "entityTwo.cost_op=ge&"
		        + "entityTwo.cost=12,00&"
		        + "entityTwo.inclusionDate_op=ge&"
		        + "entityTwo.inclusionDate=2024-01-07T00:00:00&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=code,entityTwo.hex,prohibitedDateTime&"
		        + "sortOrders=asc,desc,desc";
		
		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("Ricardo", list.get(0).getName());
		assertEquals("Paulo", list.get(1).getName());
		assertEquals("Anderson", list.get(2).getName());
		assertEquals("Joana", list.get(3).getName());
	}

	@Test
	void teste_11() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=in&"
		        + "name=Anderson,Paulo,Joana,Ricardo&"
		        + "age_op=bt&"
		        + "age=20,45&"
		        + "entityTwo.color_op=in&"
		        + "entityTwo.color=Roxo,Amarelo,Verde,Azul,Preto,Laranja&"
		        + "entityTwo.hex_op=bt&"
		        + "entityTwo.hex=523,12345&"
		        + "entityTwo.cost_op=ge&"
		        + "entityTwo.cost=12,00&"
		        + "entityTwo.inclusionDate_op=ge&"
		        + "entityTwo.inclusionDate=2024-01-07T00:00:00&"
		        + "entityTwo.entityTree.animal_op=in&"
		        + "entityTwo.entityTree.animal=Macado,Avestruz,Gato,Cavalo&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=age&"
		        + "sortOrders=asc";
		
		
		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("Paulo", list.get(0).getName());
		assertEquals("Ricardo", list.get(1).getName());
		assertEquals("Joana", list.get(2).getName());
		assertEquals("Anderson", list.get(3).getName());
	}

	@Test
	void teste_12() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=in&"
		        + "name=Anderson,Paulo,Joana,Ricardo&"
		        + "age_op=bt&"
		        + "age=20,45&"
		        + "entityTwo.color_op=in&"
		        + "entityTwo.color=Roxo,Amarelo,Verde,Azul,Preto,Laranja&"
		        + "entityTwo.hex_op=bt&"
		        + "entityTwo.hex=523,12345&"
		        + "entityTwo.cost_op=ge&"
		        + "entityTwo.cost=12,00&"
		        + "entityTwo.inclusionDate_op=ge&"
		        + "entityTwo.inclusionDate=2024-01-07T00:00:00&"
		        + "entityTwo.entityTree.animal_op=in&"
		        + "entityTwo.entityTree.animal=Macado,Avestruz,Gato,Cavalo&"
		        + "entityTwo.entityTree.indicator_op=ge&"
		        + "entityTwo.entityTree.indicator=11&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=age&"
		        + "sortOrders=asc";
		
		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("Paulo", list.get(0).getName());
		assertEquals("Ricardo", list.get(1).getName());
		assertEquals("Joana", list.get(2).getName());
		assertEquals("Anderson", list.get(3).getName());
	}

	@Test
	void teste_13() {

		String url = PATH 
				+ port 
				+ Constants.API_NAME_REQUEST_MAPPING 
				+ "/paginator/EntityOne?"
				+ "name_op=in&"
				+ "name=Anderson,Paulo,Joana,Ricardo&"
				+ "age_op=bt&age=20,45&"
				+ "entityTwo.color_op=in&"
				+ "entityTwo.color=Roxo,Amarelo,Verde,Azul,Preto,Laranja&"
				+ "entityTwo.hex_op=bt&"
				+ "entityTwo.hex=523,12345&"
				+ "entityTwo.cost_op=ge&"
				+ "entityTwo.cost=12,00&"
				+ "entityTwo.inclusionDate_op=ge&"
				+ "entityTwo.inclusionDate=2024-01-07T00:00:00&"
				+ "entityTwo.entityTree.animal_op=in&"
				+ "entityTwo.entityTree.animal=Macado,Avestruz,Gato,Cavalo&"
				+ "entityTwo.entityTree.indicator_op=ge&"
				+ "entityTwo.entityTree.indicator=11&"
				+ "page=0&"
				+ "size=10&"
				+ "sortList=age&"
				+ "sortOrders=asc";
		
		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("Paulo", list.get(0).getName());
		assertEquals("Ricardo", list.get(1).getName());
		assertEquals("Joana", list.get(2).getName());
		assertEquals("Anderson", list.get(3).getName());
	}
	
	@Test
	void teste_13_1() {

		String url = PATH 
				+ port 
				+ Constants.API_NAME_REQUEST_MAPPING 
				+ "/paginator/EntityOne?"
				+ "name_op=in&"
				+ "name=Anderson,Paulo,Joana,Ricardo&"
				+ "age_op=bt&age=20,45&"
				+ "entityTwo.color_op=in&"
				+ "entityTwo.color=roxo,amarelo,verde,azul,preto,laranja&"
				+ "entityTwo.hex_op=bt&"
				+ "entityTwo.hex=523,12345&"
				+ "entityTwo.cost_op=ge&"
				+ "entityTwo.cost=12,00&"
				+ "entityTwo.inclusionDate_op=ge&"
				+ "entityTwo.inclusionDate=2024-01-07T00:00:00&"
				+ "entityTwo.entityTree.animal_op=in&"
				+ "entityTwo.entityTree.animal=Macado,Avestruz,Gato,Cavalo&"
				+ "entityTwo.entityTree.indicator_op=ge&"
				+ "entityTwo.entityTree.indicator=11&"
				+ "entityTwo.entityTree.amount_op=bt&"
				+ "entityTwo.entityTree.amount=12.00,6.011&"
				+ "page=0&"
				+ "size=10&"
				+ "sortList=age&"
				+ "sortOrders=asc";

		getRestAllNotfound(url, 204);
	}

	@Test
	void teste_14() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "entityTwo.color=Preto&"
		        + "entityTwo.color_op=eq&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=entityTwo.inclusionDate,entityTwo.hex&"
		        + "sortOrders=desc,asc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(2, list.size());
		assertEquals("Carlos Alberto", list.get(0).getName());
		assertEquals("Carlos", list.get(1).getName());
		assertEquals(234, list.get(0).getEntityTwo().getHex());
		assertEquals(144, list.get(1).getEntityTwo().getHex());
	}

	@Test
	void teste_15() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "entityTwo.cost=22,22,2.500,23&"
		        + "entityTwo.cost_op=bt&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=entityTwo.inclusionDate,entityTwo.hex&"
		        + "sortOrders=desc,asc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(8, list.size());
		assertEquals("Renato", list.get(0).getName());
		assertEquals("Carlos Alberto", list.get(1).getName());
		assertEquals(8876, list.get(0).getEntityTwo().getHex());
		assertEquals(234, list.get(1).getEntityTwo().getHex());
	}

	@Test
	void teste_16() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "entityTwo.entityTree.animal=Cavalo,Gato,Papagaio&"
		        + "entityTwo.entityTree.animal_op=in&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=entityTwo.color,entityTwo.entityTree.animal&"
		        + "sortOrders=desc,asc";
		
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
	void teste_17() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "entityTwo.entityTree.animal=Capivara&"
		        + "entityTwo.entityTree.animal_op=eq";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		list.forEach(entity -> {
			assertEquals("Carlos Alberto", entity.getName());
		});

		assertNotNull(list);
		assertEquals(1, list.size());
	}

	@Test
	void teste_18() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "entityTwo.entityTree.entityFour.fruit=Pitanga&"
		        + "entityTwo.entityTree.entityFour.fruit_op=eq&"
		        + "page=0&"
		        + "size=10";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(2, list.size());
	}

	@Test
	void teste_19() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "entityTwo.entityTree.entityFour.entityFive.reference=Cama&"
		        + "entityTwo.entityTree.entityFour.entityFive.reference_op=eq&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=name&"
		        + "sortOrders=desc";
		
		List<EntityOne> list = getAll(url, new ErrorResponse());

		list.forEach(entity -> {
			assertEquals("Cama", entity.getEntityTwo().getEntityTree().getEntityFour().getEntityFive().getReference());
		});

		assertNotNull(list);
		assertEquals(1, list.size());
	}

	@Test
	void teste_20() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "entityTwo.entityTree.entityFour.entityFive.reference=*as*&"
		        + "entityTwo.entityTree.entityFour.entityFive.reference_op=lk&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=entityTwo.inclusionDate,entityTwo.hex&"
		        + "sortOrders=desc,asc";
		
		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(2, list.size());
	}

	@Test
	void teste_21() {

		String url = PATH 
				+ port 
				+ Constants.API_NAME_REQUEST_MAPPING 
				+ "/paginator/EntityOne?"
				+ "name_op=in&"
				+ "name=Anderson,Paulo,Joana,Ricardo&"
				+ "age_op=bt&"
				+ "age=20,45&"
				+ "code_op=eq&"
				+ "code=true&"
	            + "height=1.93&"
	            + "height_op=ne&"
				+ "entityTwo.color_op=in&"
				+ "entityTwo.color=Roxo,Amarelo,Verde,azul,Preto,Laranja&"
				+ "entityTwo.hex_op=bt&"
				+ "entityTwo.hex=523,12345&"
				+ "entityTwo.cost_op=ge&"
				+ "entityTwo.cost=12,00&"
				+ "entityTwo.inclusionDate_op=ge&"
				+ "entityTwo.inclusionDate=2024-01-07T00:00:00&"
				+ "entityTwo.entityTree.animal_op=in&"
				+ "entityTwo.entityTree.animal=Macado,Avestruz,Gato,Cavalo&"
				+ "entityTwo.entityTree.indicator_op=ge&"
				+ "entityTwo.entityTree.indicator=11&"
				+ "entityTwo.entityTree.amount_op=bt&"
				+ "entityTwo.entityTree.amount=12,60&"
				+ "entityTwo.entityTree.localDate_op=ne&"
				+ "entityTwo.entityTree.localDate=2024-10-08T00:00:00&"
				+ "page=0&"
				+ "size=10&"
				+ "sortList=age,height,code,name&"
				+ "sortOrders=asc,desc,asc,desc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("Paulo", list.get(0).getName());
		assertEquals("Ricardo", list.get(1).getName());
		assertEquals("Joana", list.get(2).getName());

	}
	
	@Test
	void teste_21_1() {

		String url = PATH 
				+ port 
				+ Constants.API_NAME_REQUEST_MAPPING 
				+ "/paginator/EntityOne?"
				+ "name_op=in&"
				+ "name=Anderson,Paulo,Joana,Ricardo&"
				+ "age_op=bt&"
				+ "age=20,45&"
				+ "code_op=eq&"
				+ "code=true&"
				+ "entityTwo.color_op=in&"
				+ "entityTwo.color=roxo,amarelo,verde,azul,preto,laranja&entityTwo.hex_op=bt&"
				+ "entityTwo.hex=523,12345&"
				+ "entityTwo.cost_op=ge&"
				+ "entityTwo.cost=12,00&"
				+ "entityTwo.inclusionDate_op=ge&"
				+ "entityTwo.inclusionDate=2024-01-07T00:00:00&"
				+ "entityTwo.entityTree.animal_op=in&"
				+ "entityTwo.entityTree.animal=Macado,Avestruz,Gato,Cavalo&"
				+ "entityTwo.entityTree.indicator_op=ge&"
				+ "entityTwo.entityTree.indicator=44&"
				+ "entityTwo.entityTree.amount_op=bt&"
				+ "entityTwo.entityTree.amount=12,6.011&"
				+ "entityTwo.entityTree.localDate_op=ne&"
				+ "entityTwo.entityTree.localDate=2024-10-08T00:00:00&"
				+ "page=0&"
				+ "size=10&"
				+ "sortList=age,height,code,name&"
				+ "sortOrders=asc,desc,asc,desc";

		
		getRestAllNotfound(url, 204);
	}


	@Test
	void teste_22() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=lk&"
		        + "name=*ar*&"
		        + "code=true&"
	            + "height=1.9&"
	            + "height_op=ne&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=prohibitedDateTime&"
		        + "sortOrders=asc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("Carlos", list.get(0).getName());
		assertEquals("Carlos Alberto", list.get(1).getName());
		assertEquals("Maria", list.get(2).getName());
		assertEquals("Ricardo", list.get(3).getName());

	}

	@Test
	void teste_23() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=lk&"
		        + "name=*ar*&"
		        + "age_op=bt&"
		        + "age=13,55&"
		        + "code=true&"
		        + "height=1.9&"
		        + "height_op=ne&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=name,age&"
		        + "sortOrders=asc,asc";
		
		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("Carlos", list.get(0).getName());
		assertEquals("Carlos Alberto", list.get(1).getName());
		assertEquals("Ricardo", list.get(2).getName());
		
	}

	@Test
	void teste_24() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=lk&"
		        + "name=*ar*&"
		        + "age_op=bt&"
		        + "age=12,55&"
		        + "code=true&"
		        + "height=1.9&"
		        + "height_op=ne&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=name,age&"
		        + "sortOrders=asc,asc";
		
		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("Carlos", list.get(0).getName());
		assertEquals("Carlos Alberto", list.get(1).getName());
		assertEquals("Maria", list.get(2).getName());
		assertEquals("Ricardo", list.get(3).getName());

	}

	@Test
	void teste_25() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=lk&"
		        + "name=*ar*&"
		        + "age_op=bt&"
		        + "age=12,55&"
		        + "code_op=ge&"
		        + "code=true&"
		        + "height_op=ge&"
		        + "height=1.40&"
		        + "birthDate_op=eq&"
		        + "birthDate=1956-08-30T00:00:00&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=name,age&"
		        + "sortOrders=asc,asc";

		singleParameterizedOne("Carlos", url);
	}

	@Test
	void teste_26() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=lk&"
		        + "name=*ar*&"
		        + "age_op=bt&"
		        + "age=12,55&"
		        + "code_op=ge&"
		        + "code=true&"
		        + "height_op=ge&"
		        + "height=1.40&"
		        + "birthDate_op=ge&"
		        + "birthDate=1956-08-30T00:00:00&"
		        + "prohibitedDateTime_op=bt&"
		        + "prohibitedDateTime=2024-04-01T08:50:00,2024-04-30T23:51:00&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=prohibitedDateTime&"
		        + "sortOrders=asc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(2, list.size());
		assertEquals("Carlos", list.get(0).getName());
		assertEquals("Carlos Alberto", list.get(1).getName());
	}

	@Test
	void teste_27() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=lk&"
		        + "name=*ar*&"
		        + "age_op=bt&"
		        + "age=12,55&"
		        + "code_op=ge&"
		        + "code=true&"
		        + "height_op=ge&"
		        + "height=1.40&"
		        + "birthDate_op=ge&"
		        + "birthDate=1956-08-30T00:00:00&"
		        + "prohibitedDateTime_op=bt&"
		        + "prohibitedDateTime=2024-04-01T08:50:00,2024-09-01T08:51:00&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=prohibitedDateTime&"
		        + "sortOrders=asc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("Carlos", list.get(0).getName());
		assertEquals("Carlos Alberto", list.get(1).getName());
		assertEquals("Maria", list.get(2).getName());
	}

	@Test
	void teste_28() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=lk&"
		        + "name=*ar*&"
		        + "age_op=bt&"
		        + "age=12,55&"
		        + "code_op=ge&"
		        + "code=true&"
		        + "height_op=ne&"
		        + "height=1.90&"
		        + "birthDate_op=ge&"
		        + "birthDate=1956-08-30T00:00:00&"
		        + "prohibitedDateTime_op=bt&"
		        + "prohibitedDateTime=2024-04-01T08:50:00,2024-11-01T08:00:00&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=age,height&"
		        + "sortOrders=desc,asc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("Carlos Alberto", list.get(0).getName());
		assertEquals("Carlos", list.get(1).getName());
		assertEquals("Ricardo", list.get(2).getName());
		assertEquals("Maria", list.get(3).getName());

	}

	@Test
	void teste_29() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=lk&"
		        + "name=*ar*&"
		        + "age_op=bt&"
		        + "age=12,55&"
		        + "code_op=ge&"
		        + "code=true&"
		        + "height_op=ge&"
		        + "height=1.40&"
		        + "birthDate_op=ge&"
		        + "birthDate=1956-08-30T00:00:00&"
		        + "prohibitedDateTime_op=bt&"
		        + "prohibitedDateTime=2024-04-01T08:50:00,2024-11-01T08:00:00&"
		        + "entityTwo.color_op=in&"
		        + "entityTwo.color=Preto,Verde,Amarelo&"
		        + "entityTwo.hex_op=bt&"
		        + "entityTwo.hex=23,12344&"
		        + "entityTwo.cost_op=eq&"
		        + "entityTwo.cost=25,50&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=age,height&"
		        + "sortOrders=desc,asc";
		
		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("Carlos Alberto", list.get(0).getName());
		assertEquals("Carlos", list.get(1).getName());
		assertEquals("Ricardo", list.get(2).getName());
		assertEquals("Maria", list.get(3).getName());
	}

	@Test
	void teste_30() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=lk&"
		        + "name=*ar*&"
		        + "age_op=bt&"
		        + "age=12,55&"
		        + "code_op=ge&"
		        + "code=true&"
		        + "height_op=ge&"
		        + "height=1.40&"
		        + "birthDate_op=ge&"
		        + "birthDate=1956-08-30T00:00:00&"
		        + "prohibitedDateTime_op=bt&"
		        + "prohibitedDateTime=2024-04-01T08:50:00,2024-11-01T08:00:00&"
		        + "entityTwo.color_op=in&"
		        + "entityTwo.color=Preto,Verde,Amarelo&"
		        + "entityTwo.hex_op=bt&"
		        + "entityTwo.hex=23,12344&"
		        + "entityTwo.cost_op=eq&"
		        + "entityTwo.cost=25,50&"
		        + "entityTwo.inclusionDate_op=lt&"
		        + "entityTwo.inclusionDate=2024-01-10T00:00:00&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=age,height,entityTwo.inclusionDate&"
		        + "sortOrders=desc,asc,asc";
		
		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("Carlos", list.get(0).getName());
		assertEquals("Ricardo", list.get(1).getName());
		assertEquals("Maria", list.get(2).getName());
	}

	@Test
	void teste_31() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=lk&"
		        + "name=*ar*&"
		        + "age_op=bt&"
		        + "age=12,55&"
		        + "code_op=ge&"
		        + "code=true&"
		        + "height_op=ge&"
		        + "height=1.40&"
		        + "birthDate_op=ge&"
		        + "birthDate=1956-08-30T00:00:00&"
		        + "prohibitedDateTime_op=bt&"
		        + "prohibitedDateTime=2024-04-01T08:50:00,2024-11-01T08:00:00&"
		        + "entityTwo.color_op=in&"
		        + "entityTwo.color=Preto,Verde,Amarelo&"
		        + "entityTwo.hex_op=bt&"
		        + "entityTwo.hex=23,12344&"
		        + "entityTwo.cost_op=eq&"
		        + "entityTwo.cost=25,50&"
		        + "entityTwo.inclusionDate_op=lt&"
		        + "entityTwo.inclusionDate=2024-01-10T00:00:00&"
		        + "entityTwo.entityTree.animal_op=in&"
		        + "entityTwo.entityTree.animal=Porco,Gato,Baleia&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=age,height,entityTwo.inclusionDate&"
		        + "sortOrders=desc,asc,asc";
		
		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("Carlos", list.get(0).getName());
		assertEquals("Ricardo", list.get(1).getName());
		assertEquals("Maria", list.get(2).getName());
	}

	@Test
	void teste_31_1() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=lk&"
		        + "name=*ar*&"
		        + "code_op=eq&"
		        + "code=true&"
		        + "age_op=bt&"
		        + "age=12,55&"
		        + "height_op=ge&"
		        + "height=1.40&"
		        + "birthDate_op=ge&"
		        + "birthDate=1956-08-30T00:00:00&"
		        + "prohibitedDateTime_op=bt&"
		        + "prohibitedDateTime=2024-04-01T08:50:00,2024-11-01T08:00:00&"
		        + "entityTwo.color_op=in&"
		        + "entityTwo.color=Preto,Verde,Amarelo&"
		        + "entityTwo.hex_op=bt&"
		        + "entityTwo.hex=23,12344&"
		        + "entityTwo.cost_op=eq&"
		        + "entityTwo.cost=25,50&"
		        + "entityTwo.inclusionDate_op=lt&"
		        + "entityTwo.inclusionDate=2024-01-10T00:00:00&"
		        + "entityTwo.entityTree.animal_op=in&"
		        + "entityTwo.entityTree.animal=Porco, Gato, Baleia&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=age,height,entityTwo.inclusionDate&"
		        + "sortOrders=desc,asc,asc";
		
		singleParameterizedOne("Carlos", url);
	}

	@Test
	void teste_32() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=lk&"
		        + "name=*ar*&"
		        + "age_op=bt&"
		        + "age=12,55&"
		        + "code_op=ge&"
		        + "code=true&"
		        + "height_op=ge&"
		        + "height=1.40&"
		        + "birthDate_op=ge&"
		        + "birthDate=1956-08-30T00:00:00&"
		        + "prohibitedDateTime_op=bt&"
		        + "prohibitedDateTime=2024-04-01T08:50:00,2024-11-01T08:00:00&"
		        + "entityTwo.color_op=in&"
		        + "entityTwo.color=Preto,Verde,Amarelo&"
		        + "entityTwo.hex_op=bt&"
		        + "entityTwo.hex=23,12344&"
		        + "entityTwo.cost_op=eq&"
		        + "entityTwo.cost=25,50&"
		        + "entityTwo.inclusionDate_op=lt&"
		        + "entityTwo.inclusionDate=2024-01-10T00:00:00&"
		        + "entityTwo.entityTree.animal_op=in&"
		        + "entityTwo.entityTree.animal=Porco,Gato,Baleia&"
		        + "entityTwo.entityTree.indicator_op=gt&"
		        + "entityTwo.entityTree.indicator=22&"
		        + "entityTwo.entityTree.amount_op=bt&"
		        + "entityTwo.entityTree.amount=14,60,24,60&"
		        + "entityTwo.entityTree.localDate_op=eq&"
		        + "entityTwo.entityTree.localDate=2024-01-08T00:00:00&"
		        + "entityTwo.entityTree.localDateTime_op=gt&"
		        + "entityTwo.entityTree.localDateTime=2024-01-08T17:31:00&"
		        + "entityTwo.entityTree.entityFour.fruit_op=in&"
		        + "entityTwo.entityTree.entityFour.fruit=Caju,Banana,Melao&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=age,height,entityTwo.inclusionDate&"
		        + "sortOrders=desc,asc,asc";
		
		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(2, list.size());
		assertEquals("Carlos", list.get(0).getName());
		assertEquals("Maria", list.get(1).getName());
	}

	@Test
	void teste_33() {

		String url = PATH 
		        + port 
		        + Constants.API_NAME_REQUEST_MAPPING 
		        + "/paginator/EntityOne?"
		        + "name_op=lk&"
		        + "name=*ar*&"
		        + "age_op=bt&"
		        + "age=12,55&"
		        + "code_op=ge&"
		        + "code=true&"
		        + "height_op=ge&"
		        + "height=1.40&"
		        + "birthDate_op=ge&"
		        + "birthDate=1956-08-30T00:00:00&"
		        + "prohibitedDateTime_op=bt&"
		        + "prohibitedDateTime=2024-04-01T08:50:00,2024-11-01T08:00:00&"
		        + "entityTwo.color_op=in&"
		        + "entityTwo.color=Preto,Verde,Amarelo&"
		        + "entityTwo.hex_op=bt&"
		        + "entityTwo.hex=23,12344&"
		        + "entityTwo.cost_op=eq&"
		        + "entityTwo.cost=25,50&"
		        + "entityTwo.inclusionDate_op=lt&"
		        + "entityTwo.inclusionDate=2024-01-10T00:00:00&"
		        + "entityTwo.entityTree.animal_op=in&"
		        + "entityTwo.entityTree.animal=Porco,Gato,Baleia&"
		        + "entityTwo.entityTree.indicator_op=eq&"
		        + "entityTwo.entityTree.indicator=11&"
		        + "entityTwo.entityTree.amount_op=bt&"
		        + "entityTwo.entityTree.amount=14,60,24,60&"
		        + "entityTwo.entityTree.localDate_op=eq&"
		        + "entityTwo.entityTree.localDate=2024-01-08T00:00:00&"
		        + "entityTwo.entityTree.localDateTime_op=gt&"
		        + "entityTwo.entityTree.localDateTime=2024-01-08T17:31:00&"
		        + "entityTwo.entityTree.entityFour.fruit_op=in&"
		        + "entityTwo.entityTree.entityFour.fruit=Caju,Banana,Melao&"
		        + "entityTwo.entityTree.entityFour.attribute_op=bt&"
		        + "entityTwo.entityTree.entityFour.attribute=1,45&"
		        + "entityTwo.entityTree.entityFour.inclusionDateTime_op=ne&"
		        + "entityTwo.entityTree.entityFour.inclusionDateTime=2024-10-07T19:03:00&"
		        + "entityTwo.entityTree.entityFour.entityFive.reference_op=lk&"
		        + "entityTwo.entityTree.entityFour.entityFive.reference=*a&"
		        + "entityTwo.entityTree.entityFour.entityFive.factor_op=bt&"
		        + "entityTwo.entityTree.entityFour.entityFive.factor=10,26&"
		        + "page=0&"
		        + "size=10&"
		        + "sortList=age,height,entityTwo.inclusionDate&"
		        + "sortOrders=desc,asc,asc";

		List<EntityOne> list = getAll(url, new ErrorResponse());

		assertNotNull(list);
		assertEquals(1, list.size());
		assertEquals("Ricardo", list.get(0).getName());
	}

	public void singleParameterizedOne(String value, String url) {
		List<EntityOne> list = getAll(url, new ErrorResponse());
		assertNotNull(list);
		assertEquals(1, list.size());
		assertEquals(value, list.get(0).getName());
	}

	private List<EntityOne> getAll(String url, ErrorResponse compare) {
		PagedModel<EntityOne> page = getRestAll(url, compare);
		List<EntityOne> list = convertToEntityOneList(page.getContent());
		assertNotNull(list);
		assertEquals(list.size(), page.getContent().size());
		return list;
	}
	
	private void getRestAllNotfound(String url, int code) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			assertEquals(response.getStatusCode().value(), HttpStatusCode.valueOf(code).value());
		} 
	}

	private PagedModel<EntityOne> getRestAll(String url, ErrorResponse compare) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return convertResponseToPagedModel(response.getBody());
		} else {
			ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody().toString());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertEquals(errorResponse.getMessage(), compare.getMessage());
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
