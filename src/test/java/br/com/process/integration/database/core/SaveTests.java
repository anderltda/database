package br.com.process.integration.database.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.process.integration.database.core.ui.CrudJpaController;
import br.com.process.integration.database.vo.EntityEightVo;
import br.com.process.integration.database.vo.EntityFiveVo;
import br.com.process.integration.database.vo.EntityFourVo;
import br.com.process.integration.database.vo.EntityNineIdVo;
import br.com.process.integration.database.vo.EntityNineVo;
import br.com.process.integration.database.vo.EntityOneVo;
import br.com.process.integration.database.vo.EntitySevenIdVo;
import br.com.process.integration.database.vo.EntitySevenVo;
import br.com.process.integration.database.vo.EntitySixVo;
import br.com.process.integration.database.vo.EntityStatusVo;
import br.com.process.integration.database.vo.EntityTreeVo;
import br.com.process.integration.database.vo.EntityTwoVo;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SaveTests {

	@LocalServerPort
	private int port;
	
	private static final String PATH = "http://localhost:";

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private CrudJpaController crudJpaController;
	
	@Autowired
	private ObjectMapper objectMapper;

	public static Long id;
	public static List<Long> ids;

	public EntityStatusVo entityStatusOne;
	public EntityStatusVo entityStatusTwo;
	public EntityStatusVo entityStatusTree;
	public EntityStatusVo entityStatusFour;
	public EntityStatusVo entityStatusFive;

	@BeforeAll
	void setupOnce() {

		if (ids == null) {
			ids = new ArrayList<>();
		}
	}

	@Test
	@Order(1)
	void contextLoads() throws Exception {

		assertNotNull(crudJpaController);

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/entityStatus";

		entityStatusOne = gerarEntityStatus("Status One", true, 0);
		String json = objectMapper.writeValueAsString(entityStatusOne);
		List<String> responses = postJson(url, json);
		entityStatusOne = objectMapper.readValue(responses.get(1), EntityStatusVo.class);
		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityStatusOne.getId());

		entityStatusTwo = gerarEntityStatus("Status Two", false, 1);
		json = objectMapper.writeValueAsString(entityStatusTwo);
		responses = postJson(url, json);
		entityStatusTwo = objectMapper.readValue(responses.get(1), EntityStatusVo.class);
		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityStatusTwo.getId());

		entityStatusTree = gerarEntityStatus("Status Tree", true, 0);
		json = objectMapper.writeValueAsString(entityStatusTree);
		responses = postJson(url, json);
		entityStatusTree = objectMapper.readValue(responses.get(1), EntityStatusVo.class);
		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityStatusTree.getId());

		entityStatusFour = gerarEntityStatus("Status Four", false, 1);
		json = objectMapper.writeValueAsString(entityStatusFour);
		responses = postJson(url, json);
		entityStatusFour = objectMapper.readValue(responses.get(1), EntityStatusVo.class);
		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityStatusFour.getId());

		entityStatusFive = gerarEntityStatus("Status Five", false, 0);
		json = objectMapper.writeValueAsString(entityStatusFive);
		responses = postJson(url, json);
		entityStatusFive = objectMapper.readValue(responses.get(1), EntityStatusVo.class);
		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityStatusFive.getId());
	}

	@Test
	@Order(2)
	void teste_01() throws Exception {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/entityOne";

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = "Ricardo";
		inteiro[0] = 22;
		dobro[0] = 1.80;
		localDate[0] = "1990-01-01";
		localTime[0] = "2024-11-01T08:00:00";

		text[1] = "Verde";
		inteiro[1] = 34;
		dobro[1] = 25.5;
		localDate[1] = "2024-01-07";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Gato";
		inteiro[2] = 11;
		dobro[2] = 24.6;
		localDate[2] = "2024-01-08";
		localTime[2] = "2024-01-08T17:32:32";

		text[3] = "Banana";
		inteiro[3] = 45;
		dobro[3] = 103.12;
		localDate[3] = "2024-10-12";
		localTime[3] = "2024-03-29T18:23:21";

		text[4] = "Vassoura";
		dobro[4] = 22.9;
		inteiro[4] = 26;
		localDate[4] = "2024-01-10";
		localTime[4] = "2024-01-10T19:14:10";

		String json = objectMapper.writeValueAsString(gerarEntity(text, inteiro, dobro, localDate, localTime));

		List<String> responses = postJson(url, json);

		EntityOneVo entity = objectMapper.readValue(responses.get(1), EntityOneVo.class);

		id = entity.getId();
		
		EntityOneVo entityOne = objectMapper.readValue(findById("entityOne", id), EntityOneVo.class);
		EntityTwoVo entityTwo = objectMapper.readValue(findById("entityTwo", entityOne.getIdEntityTwo()), EntityTwoVo.class);
		EntityTreeVo entityTree = objectMapper.readValue(findById("entityTree", entityTwo.getIdEntityTree()), EntityTreeVo.class);
		EntityFourVo entityFour = objectMapper.readValue(findById("entityFour", entityTree.getIdEntityFour()), EntityFourVo.class);
		EntityFiveVo entityFive = objectMapper.readValue(findById("entityFive", entityFour.getIdEntityFive()), EntityFiveVo.class);

		assertEquals(responses.get(0), HttpStatus.OK.toString());
		
		assertNotNull(entityOne.getId());
		assertEquals(entityOne.getName(), "Ricardo");
		assertEquals(entityOne.getAge(), 22);
		assertEquals(entityOne.getHeight(), 1.80);
		assertEquals(entityOne.getBirthDate(), LocalDate.parse("01-01-1990", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		assertEquals(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").format(entityOne.getProhibitedDateTime()), "2024-11-01T08:00:00");
		
		assertNotNull(entityTwo.getId());
		assertEquals(entityTwo.getColor(), "Verde");
		assertEquals(entityTwo.getHex(), 34);
		assertEquals(entityTwo.getCost(), 25.5);
		assertEquals(entityTwo.getInclusionDate(), LocalDate.parse("07-01-2024", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		
		assertNotNull(entityTree.getId());
		assertEquals(entityTree.getAnimal(), "Gato");
		assertEquals(entityTree.getIndicator(), 11);
		assertEquals(entityTree.getAmount(), 24.6);
		assertEquals(entityTree.getLocalDate(), LocalDate.parse("08-01-2024", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		assertEquals(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").format(entityTree.getLocalDateTime()), "2024-01-08T17:32:32");
		
		assertNotNull(entityFour.getId());
		assertEquals(entityFour.getFruit(), "Banana");
		assertEquals(entityFour.getAttribute(), 45);
		assertEquals(DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ss").format(entityFour.getInclusionDateTime()), "29-03-2024T18:23:21");
		
		assertNotNull(entityFive.getId());
		assertEquals(entityFive.getReference(), "Vassoura");
		assertEquals(entityFive.getFactor(), 26);
		
	}

	@Test
	@Order(3)
	void teste_02() throws Exception {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/flush/entityOne";

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = "Anderson";
		inteiro[0] = 41;
		dobro[0] = 1.93;
		localDate[0] = "1983-03-29";
		localTime[0] = "2024-02-01T02:52:54";

		text[1] = "Azul";
		inteiro[1] = 25;
		dobro[1] = 13.25;
		localDate[1] = "2024-03-07";
		localTime[1] = "2024-01-01T16:41:43";

		text[2] = "Avestruz";
		inteiro[2] = 22;
		dobro[2] = 55.6;
		localDate[2] = "2024-10-09";
		localTime[2] = "2024-10-18T13:32:32";

		text[3] = "Laranja";
		inteiro[3] = 11;
		dobro[3] = 21.1;
		localDate[3] = "2024-12-10";
		localTime[3] = "2024-12-09T14:00:21";

		text[4] = "Cadeira";
		dobro[4] = 44.9;
		inteiro[4] = 26;
		localDate[4] = "2024-01-10";
		localTime[4] = "2024-01-10T19:14:10";

		String json = objectMapper.writeValueAsString(gerarEntity(text, inteiro, dobro, localDate, localTime));

		List<String> responses = postJson(url, json);

		EntityOneVo entity = objectMapper.readValue(responses.get(1), EntityOneVo.class);

		EntityOneVo entityOne = objectMapper.readValue(findById("entityOne", entity.getId()), EntityOneVo.class);
		EntityTwoVo entityTwo = objectMapper.readValue(findById("entityTwo", entityOne.getIdEntityTwo()), EntityTwoVo.class);
		EntityTreeVo entityTree = objectMapper.readValue(findById("entityTree", entityTwo.getIdEntityTree()), EntityTreeVo.class);
		EntityFourVo entityFour = objectMapper.readValue(findById("entityFour", entityTree.getIdEntityFour()), EntityFourVo.class);
		EntityFiveVo entityFive = objectMapper.readValue(findById("entityFive", entityFour.getIdEntityFive()), EntityFiveVo.class);

		assertEquals(responses.get(0), HttpStatus.OK.toString());
		
		assertNotNull(entityOne.getId());
		assertEquals(entityOne.getName(), "Anderson");
		assertEquals(entityOne.getAge(), 41);
		assertEquals(entityOne.getHeight(), 1.93);
		assertEquals(entityOne.getBirthDate(), LocalDate.parse("29-03-1983", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		assertEquals(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").format(entityOne.getProhibitedDateTime()), "2024-02-01T02:52:54");
		
		assertNotNull(entityTwo.getId());
		assertEquals(entityTwo.getColor(), "Azul");
		assertEquals(entityTwo.getHex(), 25);
		assertEquals(entityTwo.getCost(), 13.25);
		assertEquals(entityTwo.getInclusionDate(), LocalDate.parse("07-03-2024", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		
		assertNotNull(entityTree.getId());
		assertEquals(entityTree.getAnimal(), "Avestruz");
		assertEquals(entityTree.getIndicator(), 22);
		assertEquals(entityTree.getAmount(), 55.6);
		assertEquals(entityTree.getLocalDate(), LocalDate.parse("09-10-2024", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		assertEquals(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").format(entityTree.getLocalDateTime()), "2024-10-18T13:32:32");
		
		assertNotNull(entityFour.getId());
		assertEquals(entityFour.getFruit(), "Laranja");
		assertEquals(entityFour.getAttribute(), 11);
		assertEquals(DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ss").format(entityFour.getInclusionDateTime()), "09-12-2024T14:00:21");
		
		assertNotNull(entityFive.getId());
		assertEquals(entityFive.getReference(), "Cadeira");
		assertEquals(entityFive.getFactor(), 26);
	}

	@Test
	@Order(4)
	void teste_03() throws Exception {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/all/entityOne";

		List<EntityOneVo> list = new ArrayList<>();

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = "Carlos Alberto";
		inteiro[0] = 41;
		dobro[0] = 1.93;
		localDate[0] = "1983-03-29";
		localTime[0] = "2025-04-19T23:52:24";

		text[1] = "Preto";
		inteiro[1] = 10;
		dobro[1] = 1432.52;
		localDate[1] = "2025-04-17";
		localTime[1] = "2025-04-17T16:40:43";

		text[2] = "Capivara";
		inteiro[2] = 11;
		dobro[2] = 20.6;
		localDate[2] = "2024-01-05";
		localTime[2] = "2024-06-08T17:32:32";

		text[3] = "Damasco";
		inteiro[3] = 11;
		dobro[3] = 33.1;
		localDate[3] = "2024-01-07";
		localTime[3] = "2024-04-09T18:23:21";

		text[4] = "Cama";
		dobro[4] = 22.30;
		inteiro[4] = 55;
		localDate[4] = "2024-02-11";
		localTime[4] = "2024-04-10T19:24:12";

		EntityOneVo entityOne = gerarEntity(text, inteiro, dobro, localDate, localTime);
		
		list.add(entityOne);

		text = new String[5];
		inteiro = new Integer[5];
		dobro = new Double[5];
		localDate = new String[5];
		localTime = new String[5];

		text[0] = "Paulo";
		inteiro[0] = 21;
		dobro[0] = 1.78;
		localDate[0] = "1990-09-09";
		localTime[0] = "2024-10-01T08:50:54";

		text[1] = "Roxo";
		inteiro[1] = 3131;
		dobro[1] = 25.5;
		localDate[1] = "2024-01-07";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Cavalo";
		inteiro[2] = 44;
		dobro[2] = 24.6;
		localDate[2] = "2024-01-08";
		localTime[2] = "2024-01-08T17:32:32";

		text[3] = "Pitanga";
		inteiro[3] = 68;
		dobro[3] = 31.1;
		localDate[3] = "2024-10-10";
		localTime[3] = "2024-02-19T18:30:21";

		text[4] = "Copo";
		dobro[4] = 22.9;
		inteiro[4] = 11;
		localDate[4] = "2024-01-10";
		localTime[4] = "2024-01-10T19:14:10";

		list.add(gerarEntity(text, inteiro, dobro, localDate, localTime));

		text = new String[5];
		inteiro = new Integer[5];
		dobro = new Double[5];
		localDate = new String[5];
		localTime = new String[5];

		text[0] = "Maria";
		inteiro[0] = 12;
		dobro[0] = 1.40;
		localDate[0] = "2016-01-01";
		localTime[0] = "2024-09-01T08:50:54";

		text[1] = "Amarelo";
		inteiro[1] = 23;
		dobro[1] = 25.5;
		localDate[1] = "2024-01-07";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Baleia";
		inteiro[2] = 87;
		dobro[2] = 14.6;
		localDate[2] = "2024-01-08";
		localTime[2] = "2024-01-08T17:32:32";

		text[3] = "Melao";
		inteiro[3] = 1;
		dobro[3] = 29.1;
		localDate[3] = "2024-04-01";
		localTime[3] = "2024-01-09T23:37:21";

		text[4] = "Caixa";
		dobro[4] = 22.9;
		inteiro[4] = 10;
		localDate[4] = "2024-01-10";
		localTime[4] = "2024-01-10T19:14:10";

		list.add(gerarEntity(text, inteiro, dobro, localDate, localTime));

		text = new String[5];
		inteiro = new Integer[5];
		dobro = new Double[5];
		localDate = new String[5];
		localTime = new String[5];

		text[0] = "Renato";
		inteiro[0] = 38;
		dobro[0] = 1.85;
		localDate[0] = "1992-08-31";
		localTime[0] = "2024-11-01T08:20:10";

		text[1] = "Laranja";
		inteiro[1] = 8876;
		dobro[1] = 210.52;
		localDate[1] = "2024-12-12";
		localTime[1] = "2024-12-07T16:40:43";

		text[2] = "Papagaio";
		inteiro[2] = 11;
		dobro[2] = 564.61;
		localDate[2] = "2024-12-08";
		localTime[2] = "2024-12-08T17:44:32";

		text[3] = "Goiaba";
		inteiro[3] = 14;
		dobro[3] = 90.21;
		localDate[3] = "2024-12-21";
		localTime[3] = "2024-01-10T19:23:21";

		text[4] = "Raquete";
		dobro[4] = 21.9;
		inteiro[4] = 24;
		localDate[4] = "2024-06-10";
		localTime[4] = "2024-04-10T18:14:10";

		list.add(gerarEntity(text, inteiro, dobro, localDate, localTime));

		String json = objectMapper.writeValueAsString(list);

		List<String> responses = postJson(url, json);

		JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, EntityOneVo.class);

		List<EntityOneVo> lists = objectMapper.readValue(responses.get(1), type);

		assertEquals(responses.get(0), HttpStatus.OK.toString());

		assertEquals(4, lists.size());
		
		for (int i = 0; i < lists.size(); i++) {
			
			entityOne = objectMapper.readValue(findById("entityOne", lists.get(i).getId()), EntityOneVo.class);
			EntityTwoVo entityTwo = objectMapper.readValue(findById("entityTwo", entityOne.getIdEntityTwo()), EntityTwoVo.class);
			EntityTreeVo entityTree = objectMapper.readValue(findById("entityTree", entityTwo.getIdEntityTree()), EntityTreeVo.class);
			EntityFourVo entityFour = objectMapper.readValue(findById("entityFour", entityTree.getIdEntityFour()), EntityFourVo.class);
			EntityFiveVo entityFive = objectMapper.readValue(findById("entityFive", entityFour.getIdEntityFive()), EntityFiveVo.class);

			ids.add(lists.get(i).getId());
			assertNotNull(entityOne.getId());
			assertNotNull(entityTwo.getId());
			assertNotNull(entityTree.getId());
			assertNotNull(entityFour.getId());
			assertNotNull(entityFive.getId());
		}
	}

	@Test
	@Order(5)
	void teste_04() throws Exception {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/all/flush/entityOne";

		List<EntityOneVo> list = new ArrayList<>();

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = "Carlos";
		inteiro[0] = 34;
		dobro[0] = 1.70;
		localDate[0] = "1956-08-30";
		localTime[0] = "2024-04-01T08:50:54";

		text[1] = "Preto";
		inteiro[1] = 144;
		dobro[1] = 25.5;
		localDate[1] = "2024-01-07";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Porco";
		inteiro[2] = 44;
		dobro[2] = 20.6;
		localDate[2] = "2024-01-08";
		localTime[2] = "2024-01-08T17:32:32";

		text[3] = "Caju";
		inteiro[3] = 21;
		dobro[3] = 3.1;
		localDate[3] = "2024-01-02";
		localTime[3] = "2024-02-09T18:23:21";

		text[4] = "Porta";
		dobro[4] = 22.0;
		inteiro[4] = 21;
		localDate[4] = "2024-01-10";
		localTime[4] = "2024-01-10T19:14:10";

		list.add(gerarEntity(text, inteiro, dobro, localDate, localTime));

		text = new String[5];
		inteiro = new Integer[5];
		dobro = new Double[5];
		localDate = new String[5];
		localTime = new String[5];

		text[0] = "Paulo Henrique";
		inteiro[0] = 38;
		dobro[0] = 1.87;
		localDate[0] = "1986-09-09";
		localTime[0] = "2024-02-01T08:50:54";

		text[1] = "Verde";
		inteiro[1] = 3131;
		dobro[1] = 25.5;
		localDate[1] = "2024-01-07";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Cavalo";
		inteiro[2] = 44;
		dobro[2] = 24.6;
		localDate[2] = "2024-01-08";
		localTime[2] = "2024-01-08T17:32:32";

		text[3] = "Pitanga";
		inteiro[3] = 19;
		dobro[3] = 3.1;
		localDate[3] = "2024-06-10";
		localTime[3] = "2024-01-09T18:23:21";

		text[4] = "Tennis";
		dobro[4] = 12.9;
		inteiro[4] = 11;
		localDate[4] = "2024-01-10";
		localTime[4] = "2024-01-10T19:14:10";

		list.add(gerarEntity(text, inteiro, dobro, localDate, localTime));

		text = new String[5];
		inteiro = new Integer[5];
		dobro = new Double[5];
		localDate = new String[5];
		localTime = new String[5];

		text[0] = "Joana";
		inteiro[0] = 32;
		dobro[0] = 1.80;
		localDate[0] = "1992-01-01";
		localTime[0] = "2024-11-01T08:00:00";

		text[1] = "Amarelo";
		inteiro[1] = 524;
		dobro[1] = 25.5;
		localDate[1] = "2024-01-07";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Macado";
		inteiro[2] = 22;
		dobro[2] = 14.6;
		localDate[2] = "2024-01-08";
		localTime[2] = "2024-01-08T17:32:32";

		text[3] = "Mamao";
		inteiro[3] = 1;
		dobro[3] = 29.1;
		localDate[3] = "2024-01-09";
		localTime[3] = "2024-01-09T18:23:21";

		text[4] = "Balde";
		dobro[4] = 22.9;
		inteiro[4] = 10;
		localDate[4] = "2024-01-10";
		localTime[4] = "2024-01-10T19:14:10";

		list.add(gerarEntity(text, inteiro, dobro, localDate, localTime));

		text = new String[5];
		inteiro = new Integer[5];
		dobro = new Double[5];
		localDate = new String[5];
		localTime = new String[5];

		text[0] = "Ariovaldo";
		inteiro[0] = 22;
		dobro[0] = 1.90;
		localDate[0] = "1990-01-01";
		localTime[0] = "2024-11-01T08:00:00";

		text[1] = "Cinza";
		inteiro[1] = 37890;
		dobro[1] = 21.5;
		localDate[1] = "2024-01-07";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Gato";
		inteiro[2] = 22;
		dobro[2] = 24.6;
		localDate[2] = "2024-01-08";
		localTime[2] = "2024-01-08T17:32:32";

		text[3] = "Banana";
		inteiro[3] = 25;
		dobro[3] = 23.1;
		localDate[3] = "2024-01-09";
		localTime[3] = "2024-01-09T18:23:21";

		text[4] = "Panela";
		dobro[4] = 12.92;
		inteiro[4] = 75;
		localDate[4] = "2024-01-10";
		localTime[4] = "2024-01-10T19:14:10";

		list.add(gerarEntity(text, inteiro, dobro, localDate, localTime));

		String json = objectMapper.writeValueAsString(list);

		List<String> responses = postJson(url, json);

		JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, EntityOneVo.class);

		List<EntityOneVo> lists = objectMapper.readValue(responses.get(1), type);

		assertEquals(responses.get(0), HttpStatus.OK.toString());

		assertEquals(4, lists.size());

		for (int i = 0; i < lists.size(); i++) {
			
			EntityOneVo entityOne = objectMapper.readValue(findById("entityOne", lists.get(i).getId()), EntityOneVo.class);
			EntityTwoVo entityTwo = objectMapper.readValue(findById("entityTwo", entityOne.getIdEntityTwo()), EntityTwoVo.class);
			EntityTreeVo entityTree = objectMapper.readValue(findById("entityTree", entityTwo.getIdEntityTree()), EntityTreeVo.class);
			EntityFourVo entityFour = objectMapper.readValue(findById("entityFour", entityTree.getIdEntityFour()), EntityFourVo.class);
			EntityFiveVo entityFive = objectMapper.readValue(findById("entityFive", entityFour.getIdEntityFive()), EntityFiveVo.class);

			ids.add(lists.get(i).getId());
			assertNotNull(entityOne.getId());
			assertNotNull(entityTwo.getId());
			assertNotNull(entityTree.getId());
			assertNotNull(entityFour.getId());
			assertNotNull(entityFive.getId());
		}

	}
	
	@Test
	@Order(6)
	void teste_05() throws Exception {
		
		String json = null;
		List<String> responses = null;
		String url = null;

		EntitySixVo entitySix = new EntitySixVo();
		entitySix.setPackageName(EntitySixVo.class.getPackageName());
		entitySix.setStartDate(LocalDate.now());
		entitySix.setStopDate(LocalDate.now());

		json = objectMapper.writeValueAsString(entitySix);
		url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/entitySix";
		responses = postJson(url, json);
		entitySix = objectMapper.readValue(responses.get(1), EntitySixVo.class);
		assertNotNull(entitySix.getId());

		EntitySevenVo entitySeven = new EntitySevenVo();
		EntitySevenIdVo entitySevenId = new EntitySevenIdVo();
		entitySevenId.setIdEntitySix(entitySix.getId());
		entitySevenId.setIdEntitySeven(1l);
		entitySeven.setId(entitySevenId);
		entitySeven.setDado(Byte.class.getSimpleName());
		
		json = objectMapper.writeValueAsString(entitySeven);
		url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/entitySeven";
		responses = postJson(url, json);
		entitySeven = objectMapper.readValue(responses.get(1), EntitySevenVo.class);
		assertNotNull(entitySeven.getId());

		EntityEightVo entityEight = new EntityEightVo();
		entityEight.setPosition("Decimo");
		entityEight.setProperties("position");
		entityEight.setEntitySeven(entitySeven);
		
		json = objectMapper.writeValueAsString(entityEight);
		url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/entityEight";
		responses = postJson(url, json);
		entityEight = objectMapper.readValue(responses.get(1), EntityEightVo.class);
		assertNotNull(entityEight.getId());

		EntityNineVo entityNine = new EntityNineVo();
		EntityNineIdVo entityNineId = new EntityNineIdVo();
		entityNineId.setIdEntityEight(entityEight.getId());
		entityNineId.setIdEntitySeven(entitySeven.getId().getIdEntitySeven());
		entityNineId.setIdEntitySix(entitySeven.getId().getIdEntitySix());
		entityNine.setId(entityNineId);
		entityNine.setCode("10");
		entityNine.setKeyNine("key1");
		entityNine.setVariable("new");
		
		json = objectMapper.writeValueAsString(entityNine);
		url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/entityNine";
		responses = postJson(url, json);
		entityNine = objectMapper.readValue(responses.get(1), EntityNineVo.class);
		assertNotNull(entityNine.getId());
	}
	
	@Test
	@Order(7)
	void teste_06() throws Exception {
		
		String json = null;
		List<String> responses = null;
		String url = null;

		EntitySixVo entitySix = new EntitySixVo();
		entitySix.setPackageName(EntitySevenVo.class.getPackageName());
		entitySix.setStartDate(LocalDate.now());
		entitySix.setStopDate(LocalDate.now());

		json = objectMapper.writeValueAsString(entitySix);
		url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/entitySix";
		responses = postJson(url, json);
		entitySix = objectMapper.readValue(responses.get(1), EntitySixVo.class);
		assertNotNull(entitySix.getId());

		EntitySevenVo entitySeven = new EntitySevenVo();
		EntitySevenIdVo entitySevenId = new EntitySevenIdVo();
		entitySevenId.setIdEntitySix(entitySix.getId());
		entitySevenId.setIdEntitySeven(2l);
		entitySeven.setId(entitySevenId);
		entitySeven.setDado(String.class.getSimpleName());
		
		json = objectMapper.writeValueAsString(entitySeven);
		url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/entitySeven";
		responses = postJson(url, json);
		entitySeven = objectMapper.readValue(responses.get(1), EntitySevenVo.class);
		assertNotNull(entitySeven.getId());

		EntityEightVo entityEight = new EntityEightVo();
		entityEight.setPosition("Setimo");
		entityEight.setProperties("mapper");
		entityEight.setEntitySeven(entitySeven);
		
		json = objectMapper.writeValueAsString(entityEight);
		url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/entityEight";
		responses = postJson(url, json);
		entityEight = objectMapper.readValue(responses.get(1), EntityEightVo.class);
		assertNotNull(entityEight.getId());

		EntityNineVo entityNine = new EntityNineVo();
		EntityNineIdVo entityNineId = new EntityNineIdVo();
		entityNineId.setIdEntityEight(entityEight.getId());
		entityNineId.setIdEntitySeven(entitySeven.getId().getIdEntitySeven());
		entityNineId.setIdEntitySix(entitySeven.getId().getIdEntitySix());
		entityNine.setId(entityNineId);
		entityNine.setCode("11");
		entityNine.setKeyNine("key2");
		entityNine.setVariable("test");
		
		json = objectMapper.writeValueAsString(entityNine);
		url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/entityNine";
		responses = postJson(url, json);
		entityNine = objectMapper.readValue(responses.get(1), EntityNineVo.class);
		assertNotNull(entityNine.getId());
	}

	public EntityOneVo gerarEntity(String[] text, Integer[] inteiro, Double[] dobro, String[] localDate, String[] localTime) {

		EntityOneVo entityOne = new EntityOneVo();
		entityOne.setCode(true);
		entityOne.setName(text[0]);
		entityOne.setAge(inteiro[0]);
		entityOne.setHeight(dobro[0]);
		entityOne.setBirthDate(geraLocalDate(localDate[0]));
		entityOne.setProhibitedDateTime(geraDataLocalTime(localTime[0]));
		entityOne.setEntityStatus(entityStatusOne);

		EntityTwoVo entityTwo = new EntityTwoVo();
		entityTwo.setColor(text[1]);
		entityTwo.setHex(inteiro[1]);
		entityTwo.setCost(dobro[1]);
		entityTwo.setInclusionDate(geraLocalDate(localDate[1]));
		entityTwo.setEntityStatus(entityStatusTwo);

		EntityTreeVo entityTree = new EntityTreeVo();
		entityTree.setAnimal(text[2]);
		entityTree.setIndicator(inteiro[2]);
		entityTree.setAmount(dobro[2]);
		entityTree.setLocalDate(geraLocalDate(localDate[2]));
		entityTree.setLocalDateTime(geraDataLocalTime(localTime[2]));
		entityTree.setEntityStatus(entityStatusTree);

		EntityFourVo entityFour = new EntityFourVo();
		entityFour.setFruit(text[3]);
		entityFour.setAttribute(inteiro[3]);
		entityFour.setInclusionDateTime(geraDataLocalTime(localTime[3]));
		entityFour.setEntityStatus(entityStatusFour);

		EntityFiveVo entityFive = new EntityFiveVo();
		entityFive.setReference(text[4]);
		entityFive.setFactor(inteiro[4]);
		entityFive.setEntityStatus(entityStatusFive);

		entityOne.setEntityTwo(entityTwo);
		entityTwo.setEntityTree(entityTree);
		entityTree.setEntityFour(entityFour);
		entityFour.setEntityFive(entityFive);

		return entityOne;
	}

	public EntityStatusVo gerarEntityStatus(String name, Boolean ativo, Integer status) {

		EntityStatusVo entityStatus = new EntityStatusVo();
		entityStatus.setAtivo(ativo);
		entityStatus.setName(name);
		entityStatus.setStartDateTime(LocalDateTime.now());
		entityStatus.setStatus(status);

		return entityStatus;
	}

	public List<String> postJson(String url, String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
		List<String> responses = new ArrayList<>();
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
		if (response.getStatusCode() == HttpStatus.OK) {
			responses.add(response.getStatusCode().toString());
			responses.add(response.getBody());
			return responses;
		} else {
			throw new RuntimeException("Failed to post JSON. Status code: " + response.getStatusCode());
		}
	}
	
	public String findById(String clazz, Object id) {
		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/" + clazz + "/" + id;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		} 
		
		return null;
	}

	public LocalDate geraLocalDate(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
		LocalDate date = LocalDate.parse(dateString, formatter);
		return date;
	}

	public LocalDateTime geraDataLocalTime(String dateTimeString) {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
		LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, fmt);
		return dateTime;
	}
}
