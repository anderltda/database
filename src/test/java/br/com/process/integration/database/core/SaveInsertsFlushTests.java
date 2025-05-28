package br.com.process.integration.database.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.process.integration.database.core.exception.ErrorResponse;
import br.com.process.integration.database.core.ui.CrudJpaController;
import br.com.process.integration.database.vo.EntityFiveVo;
import br.com.process.integration.database.vo.EntityFourVo;
import br.com.process.integration.database.vo.EntityOneVo;
import br.com.process.integration.database.vo.EntityStatusVo;
import br.com.process.integration.database.vo.EntityTreeVo;
import br.com.process.integration.database.vo.EntityTwoVo;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SaveInsertsFlushTests {

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
	void initDatabase() throws Exception {
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
		List<String> responses = postJson(url, json, null);
		entityStatusOne = objectMapper.readValue(responses.get(1), EntityStatusVo.class);
		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityStatusOne.getId());

		entityStatusTwo = gerarEntityStatus("Status Two", false, 1);
		json = objectMapper.writeValueAsString(entityStatusTwo);
		responses = postJson(url, json, null);
		entityStatusTwo = objectMapper.readValue(responses.get(1), EntityStatusVo.class);
		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityStatusTwo.getId());

		entityStatusTree = gerarEntityStatus("Status Tree", true, 0);
		json = objectMapper.writeValueAsString(entityStatusTree);
		responses = postJson(url, json, null);
		entityStatusTree = objectMapper.readValue(responses.get(1), EntityStatusVo.class);
		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityStatusTree.getId());

		entityStatusFour = gerarEntityStatus("Status Four", false, 1);
		json = objectMapper.writeValueAsString(entityStatusFour);
		responses = postJson(url, json, null);
		entityStatusFour = objectMapper.readValue(responses.get(1), EntityStatusVo.class);
		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityStatusFour.getId());

		entityStatusFive = gerarEntityStatus("Status Five", false, 0);
		json = objectMapper.writeValueAsString(entityStatusFive);
		responses = postJson(url, json, null);
		entityStatusFive = objectMapper.readValue(responses.get(1), EntityStatusVo.class);
		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityStatusFive.getId());
	}

	@Test
	@Order(2)
	void teste_01() throws Exception {
		
		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/flush/entityOne";

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = "Manolo";
		inteiro[0] = 70;
		dobro[0] = 1.40;
		localDate[0] = "1990-01-01";
		localTime[0] = "2024-11-01T08:00:00";

		text[1] = "Branco";
		inteiro[1] = 42;
		dobro[1] = 25.5;
		localDate[1] = "2024-01-07";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Abutre";
		inteiro[2] = 13;
		dobro[2] = 24.6;
		localDate[2] = "2024-01-08";
		localTime[2] = "2024-01-08T17:32:32";

		text[3] = "Abacate";
		inteiro[3] = 45;
		dobro[3] = 103.12;
		localDate[3] = "2024-03-29";
		localTime[3] = "2024-03-29T18:23:21";

		text[4] = "Caneca";
		dobro[4] = 22.9;
		inteiro[4] = 26;
		localDate[4] = "2024-01-10";
		localTime[4] = "2024-01-10T19:14:10";

		String json = objectMapper.writeValueAsString(gerarEntity(text, inteiro, dobro, localDate, localTime));

		List<String> responses = postJson(url, json, new ErrorResponse());

		EntityOneVo entityOne = objectMapper.readValue(responses.get(1), EntityOneVo.class);

		id = entityOne.getId();
		
		entityOne = objectMapper.readValue(findById("entityOne", id), EntityOneVo.class);
		
		EntityTwoVo entityTwo = objectMapper.readValue(findById("entityTwo", entityOne.getIdEntityTwo()), EntityTwoVo.class);
		EntityTreeVo entityTree = objectMapper.readValue(findById("entityTree", entityTwo.getIdEntityTree()), EntityTreeVo.class);
		EntityFourVo entityFour = objectMapper.readValue(findById("entityFour", entityTree.getIdEntityFour()), EntityFourVo.class);
		EntityFiveVo entityFive = objectMapper.readValue(findById("entityFive", entityFour.getIdEntityFive()), EntityFiveVo.class);

		assertEquals(responses.get(0), HttpStatus.OK.toString());
		
		assertNotNull(entityOne.getId());
		assertEquals(entityOne.getName(), "Manolo");
		assertEquals(entityOne.getAge(), 70);
		assertEquals(entityOne.getHeight(), 1.40);
		assertEquals(entityOne.getBirthDate(), LocalDate.parse("01-01-1990", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		assertEquals(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").format(entityOne.getProhibitedDateTime()), "2024-11-01T08:00:00");
		
		assertNotNull(entityTwo.getId());
		assertEquals(entityTwo.getColor(), "Branco");
		assertEquals(entityTwo.getHex(), 42);
		assertEquals(entityTwo.getCost(), 25.5);
		assertEquals(entityTwo.getInclusionDate(), LocalDate.parse("07-01-2024", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		
		assertNotNull(entityTree.getId());
		assertEquals(entityTree.getAnimal(), "Abutre");
		assertEquals(entityTree.getIndicator(), 13);
		assertEquals(entityTree.getAmount(), 24.6);
		assertEquals(entityTree.getLocalDate(), LocalDate.parse("08-01-2024", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		assertEquals(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").format(entityTree.getLocalDateTime()), "2024-01-08T17:32:32");
		
		assertNotNull(entityFour.getId());
		assertEquals(entityFour.getFruit(), "Abacate");
		assertEquals(entityFour.getAttribute(), 45);
		assertEquals(DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ss").format(entityFour.getInclusionDateTime()), "29-03-2024T18:23:21");
		
		assertNotNull(entityFive.getId());
		assertEquals(entityFive.getReference(), "Caneca");
		assertEquals(entityFive.getFactor(), 26);
	}

	@Test
	@Order(3)
	void teste_02() throws Exception {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/all/flush/entityOne";

		List<EntityOneVo> list = new ArrayList<>();

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = "Alberto";
		inteiro[0] = 55;
		dobro[0] = 1.77;
		localDate[0] = "1970-09-22";
		localTime[0] = "2024-04-30T23:50:54";

		text[1] = "Preto";
		inteiro[1] = 234;
		dobro[1] = 25.5;
		localDate[1] = "2024-01-10";
		localTime[1] = "2024-10-07T16:40:43";

		text[2] = "Búfalo";
		inteiro[2] = 72;
		dobro[2] = 20.6;
		localDate[2] = "2024-01-05";
		localTime[2] = "2024-06-08T17:32:32";

		text[3] = "Damasco";
		inteiro[3] = 13;
		dobro[3] = 33.1;
		localDate[3] = "2024-01-07";
		localTime[3] = "2024-04-09T18:23:21";

		text[4] = "Cama";
		dobro[4] = 22.30;
		inteiro[4] = 55;
		localDate[4] = "2024-02-11";
		localTime[4] = "2024-04-10T19:24:12";

		list.add(gerarEntity(text, inteiro, dobro, localDate, localTime));

		text = new String[5];
		inteiro = new Integer[5];
		dobro = new Double[5];
		localDate = new String[5];
		localTime = new String[5];

		text[0] = "Pedro";
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

		text[0] = "Josefina";
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

		text[0] = "Carlos Eduardo";
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
		inteiro[2] = 13;
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

		List<String> responses = postJson(url, json, new ErrorResponse());

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
	@Order(4)
	void teste_03() throws Exception {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/all/entityOne";

		List<EntityOneVo> list = new ArrayList<>();

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = "Alberto";
		inteiro[0] = 55;
		dobro[0] = 1.77;
		localDate[0] = "1970-09-22";
		localTime[0] = "2024-04-30T23:50:54";

		text[1] = "Preto";
		inteiro[1] = 234;
		dobro[1] = 25.5;
		localDate[1] = "2024-01-10";
		localTime[1] = "2024-10-07T16:40:43";

		text[2] = "Búfalo";
		inteiro[2] = 72;
		dobro[2] = 20.6;
		localDate[2] = "2024-01-05";
		localTime[2] = "2024-06-08T17:32:32";

		text[3] = "Damasco";
		inteiro[3] = 13;
		dobro[3] = 33.1;
		localDate[3] = "2024-01-07";
		localTime[3] = "2024-04-09T18:23:21";

		text[4] = "Cama";
		dobro[4] = 22.30;
		inteiro[4] = 55;
		localDate[4] = "2024-02-11";
		localTime[4] = "2024-04-10T19:24:12";

		list.add(gerarEntity(text, inteiro, dobro, localDate, localTime));

		text = new String[5];
		inteiro = new Integer[5];
		dobro = new Double[5];
		localDate = new String[5];
		localTime = new String[5];

		text[0] = "Pedro";
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

		text[0] = "Josefina";
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

		text[0] = "Carlos Eduardo";
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
		inteiro[2] = 13;
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

		List<String> responses = postJson(url, json, new ErrorResponse());

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
	void teste_04() throws Exception {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/entityOne";

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = "Andre";
		inteiro[0] = 70;
		dobro[0] = 1.40;
		localDate[0] = "1990-01-01";
		localTime[0] = "2024-11-01T08:00:00";

		text[1] = "Dourado";
		inteiro[1] = 12344;
		dobro[1] = 25.5;
		localDate[1] = "2024-01-07";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Dromedário";
		inteiro[2] = 24;
		dobro[2] = 24.6;
		localDate[2] = "2024-01-08";
		localTime[2] = "2024-01-08T17:32:32";

		text[3] = "Framboesa";
		inteiro[3] = 45;
		dobro[3] = 103.12;
		localDate[3] = "2024-10-12";
		localTime[3] = "2024-03-29T18:23:21";

		text[4] = "Espada";
		dobro[4] = 22.9;
		inteiro[4] = 26;
		localDate[4] = "01-10-2024";
		localTime[4] = "2024-01-10T19:14:10";

		String json = objectMapper.writeValueAsString(gerarEntity(text, inteiro, dobro, localDate, localTime));

		List<String> responses = postJson(url, json, new ErrorResponse());

		EntityOneVo entity = objectMapper.readValue(responses.get(1), EntityOneVo.class);
		
		EntityOneVo entityOne = objectMapper.readValue(findById("entityOne", entity.getId()), EntityOneVo.class);
		EntityTwoVo entityTwo = objectMapper.readValue(findById("entityTwo", entityOne.getIdEntityTwo()), EntityTwoVo.class);
		EntityTreeVo entityTree = objectMapper.readValue(findById("entityTree", entityTwo.getIdEntityTree()), EntityTreeVo.class);
		EntityFourVo entityFour = objectMapper.readValue(findById("entityFour", entityTree.getIdEntityFour()), EntityFourVo.class);
		EntityFiveVo entityFive = objectMapper.readValue(findById("entityFive", entityFour.getIdEntityFive()), EntityFiveVo.class);

		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityOne.getId());
		assertNotNull(entityTwo.getId());
		assertNotNull(entityTree.getId());
		assertNotNull(entityFour.getId());
		assertNotNull(entityFive.getId());
	}

	@Test
	void teste_05() throws Exception {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/flush/entityOne";

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = "Lucas";
		inteiro[0] = 70;
		dobro[0] = 1.40;
		localDate[0] = "1990-01-01";
		localTime[0] = "2024-11-01T08:00:00";

		text[1] = "Mostarda";
		inteiro[1] = 12344;
		dobro[1] = 25.5;
		localDate[1] = "2024-01-07";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Borboleta";
		inteiro[2] = 15;
		dobro[2] = 24.6;
		localDate[2] = "2024-01-08";
		localTime[2] = "2024-03-08T17:32:32";

		text[3] = "Graviola";
		inteiro[3] = 45;
		dobro[3] = 103.12;
		localDate[3] = "2024-10-12";
		localTime[3] = "2024-03-29T18:23:21";

		text[4] = "Ferradura";
		dobro[4] = 22.9;
		inteiro[4] = 26;
		localDate[4] = "2024-01-10";
		localTime[4] = "2024-01-10T19:14:10";

		String json = objectMapper.writeValueAsString(gerarEntity(text, inteiro, dobro, localDate, localTime));

		List<String> responses = postJson(url, json, new ErrorResponse());

		EntityOneVo entity = objectMapper.readValue(responses.get(1), EntityOneVo.class);

		EntityOneVo entityOne = objectMapper.readValue(findById("entityOne", entity.getId()), EntityOneVo.class);
		EntityTwoVo entityTwo = objectMapper.readValue(findById("entityTwo", entityOne.getIdEntityTwo()), EntityTwoVo.class);
		EntityTreeVo entityTree = objectMapper.readValue(findById("entityTree", entityTwo.getIdEntityTree()), EntityTreeVo.class);
		EntityFourVo entityFour = objectMapper.readValue(findById("entityFour", entityTree.getIdEntityFour()), EntityFourVo.class);
		EntityFiveVo entityFive = objectMapper.readValue(findById("entityFive", entityFour.getIdEntityFive()), EntityFiveVo.class);

		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityOne.getId());
		assertNotNull(entityTwo.getId());
		assertNotNull(entityTree.getId());
		assertNotNull(entityFour.getId());
		assertNotNull(entityFive.getId());
	}

	@Test
	void teste_06() throws Exception {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/flush/entityOne";

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = "Gabriela";
		inteiro[0] = 70;
		dobro[0] = 1.40;
		localDate[0] = "1990-01-01";
		localTime[0] = "2024-11-01T08:00:00";

		text[1] = "Prata";
		inteiro[1] = 12344;
		dobro[1] = 25.5;
		localDate[1] = "2024-01-07";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Camarão";
		inteiro[2] = 15;
		dobro[2] = 24.6;
		localDate[2] = "2024-01-08";
		localTime[2] = "2024-03-08T17:32:32";

		text[3] = "Guaraná";
		inteiro[3] = 45;
		dobro[3] = 103.12;
		localDate[3] = "2024-10-12";
		localTime[3] = "2024-03-29T18:23:21";

		text[4] = "Frasco";
		dobro[4] = 22.9;
		inteiro[4] = 26;
		localDate[4] = "2024-01-10";
		localTime[4] = "2024-01-10T19:14:10";

		String json = objectMapper.writeValueAsString(gerarEntity(text, inteiro, dobro, localDate, localTime));

		List<String> responses = postJson(url, json, new ErrorResponse());

		EntityOneVo entity = objectMapper.readValue(responses.get(1), EntityOneVo.class);

		EntityOneVo entityOne = objectMapper.readValue(findById("entityOne", entity.getId()), EntityOneVo.class);
		EntityTwoVo entityTwo = objectMapper.readValue(findById("entityTwo", entityOne.getIdEntityTwo()), EntityTwoVo.class);
		EntityTreeVo entityTree = objectMapper.readValue(findById("entityTree", entityTwo.getIdEntityTree()), EntityTreeVo.class);
		EntityFourVo entityFour = objectMapper.readValue(findById("entityFour", entityTree.getIdEntityFour()), EntityFourVo.class);
		EntityFiveVo entityFive = objectMapper.readValue(findById("entityFive", entityFour.getIdEntityFive()), EntityFiveVo.class);

		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityOne.getId());
		assertNotNull(entityTwo.getId());
		assertNotNull(entityTree.getId());
		assertNotNull(entityFour.getId());
		assertNotNull(entityFive.getId());
	}

	@Test
	void teste_07() throws Exception {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/entityOne";

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = "Maria Bethania";
		inteiro[0] = 366;
		dobro[0] = 1.74;
		localDate[0] = "1950-05-03";
		localTime[0] = "2000-11-01T08:00:00";

		text[1] = "Marfim";
		inteiro[1] = 365;
		dobro[1] = 25.57;
		localDate[1] = "2024-01-07";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Camundongo";
		inteiro[2] = 15;
		dobro[2] = 24.6;
		localDate[2] = "2024-01-08";
		localTime[2] = "2024-03-08T17:32:32";

		text[3] = "Maracujá";
		inteiro[3] = 45;
		dobro[3] = 103.12;
		localDate[3] = "2024-10-12";
		localTime[3] = "2024-03-29T18:23:21";

		text[4] = "Hipômetro";
		dobro[4] = 22.9;
		inteiro[4] = 26;
		localDate[4] = "2024-01-10";
		localTime[4] = "2024-01-10T19:14:10";

		String json = objectMapper.writeValueAsString(gerarEntity(text, inteiro, dobro, localDate, localTime));

		List<String> responses = postJson(url, json, new ErrorResponse());

		EntityOneVo entity = objectMapper.readValue(responses.get(1), EntityOneVo.class);

		EntityOneVo entityOne = objectMapper.readValue(findById("entityOne", entity.getId()), EntityOneVo.class);
		EntityTwoVo entityTwo = objectMapper.readValue(findById("entityTwo", entityOne.getIdEntityTwo()), EntityTwoVo.class);
		EntityTreeVo entityTree = objectMapper.readValue(findById("entityTree", entityTwo.getIdEntityTree()), EntityTreeVo.class);
		EntityFourVo entityFour = objectMapper.readValue(findById("entityFour", entityTree.getIdEntityFour()), EntityFourVo.class);
		EntityFiveVo entityFive = objectMapper.readValue(findById("entityFive", entityFour.getIdEntityFive()), EntityFiveVo.class);

		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityOne.getId());
		assertNotNull(entityTwo.getId());
		assertNotNull(entityTree.getId());
		assertNotNull(entityFour.getId());
		assertNotNull(entityFive.getId());
	}

	@Test
	void teste_08() throws Exception {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/flush/entityOne";

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = "Caetano";
		inteiro[0] = 144;
		dobro[0] = 1.87;
		localDate[0] = "1968-06-11";
		localTime[0] = "2000-11-01T08:00:00";

		text[1] = "Marrom";
		inteiro[1] = 365;
		dobro[1] = 25.57;
		localDate[1] = "2024-01-07";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Égua";
		inteiro[2] = 15;
		dobro[2] = 24.6;
		localDate[2] = "2024-03-08";
		localTime[2] = "2024-03-08T17:32:32";

		text[3] = "Pêssego";
		inteiro[3] = 24;
		dobro[3] = 257.12;
		localDate[3] = "2024-03-29";
		localTime[3] = "2024-03-29T18:23:21";

		text[4] = "Impressora";
		dobro[4] = 22.9;
		inteiro[4] = 26;
		localDate[4] = "2024-01-10";
		localTime[4] = "2024-01-10T19:14:10";

		String json = objectMapper.writeValueAsString(gerarEntity(text, inteiro, dobro, localDate, localTime));

		List<String> responses = postJson(url, json, new ErrorResponse());

		EntityOneVo entity = objectMapper.readValue(responses.get(1), EntityOneVo.class);

		EntityOneVo entityOne = objectMapper.readValue(findById("entityOne", entity.getId()), EntityOneVo.class);
		EntityTwoVo entityTwo = objectMapper.readValue(findById("entityTwo", entityOne.getIdEntityTwo()), EntityTwoVo.class);
		EntityTreeVo entityTree = objectMapper.readValue(findById("entityTree", entityTwo.getIdEntityTree()), EntityTreeVo.class);
		EntityFourVo entityFour = objectMapper.readValue(findById("entityFour", entityTree.getIdEntityFour()), EntityFourVo.class);
		EntityFiveVo entityFive = objectMapper.readValue(findById("entityFive", entityFour.getIdEntityFive()), EntityFiveVo.class);

		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityOne.getId());
		assertNotNull(entityTwo.getId());
		assertNotNull(entityTree.getId());
		assertNotNull(entityFour.getId());
		assertNotNull(entityFive.getId());
	}

	@Test
	void teste_09() throws Exception {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/entityOne";

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = "Valdir";
		inteiro[0] = 198;
		dobro[0] = 1.50;
		localDate[0] = "2000-01-01";
		localTime[0] = "2000-01-01T08:15:00";

		text[1] = "Vermelho";
		inteiro[1] = 99;
		dobro[1] = 314.57;
		localDate[1] = "2024-01-07";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Elefante";
		inteiro[2] = 15;
		dobro[2] = 24.6;
		localDate[2] = "2024-03-08";
		localTime[2] = "2024-03-08T17:32:32";

		text[3] = "Pitanga";
		inteiro[3] = 13;
		dobro[3] = 257.12;
		localDate[3] = "2012-10-12";
		localTime[3] = "2024-03-29T18:23:21";

		text[4] = "Interfone";
		dobro[4] = 22.9;
		inteiro[4] = 26;
		localDate[4] = "2024-01-04";
		localTime[4] = "1992-01-10T19:14:10";

		String json = objectMapper.writeValueAsString(gerarEntity(text, inteiro, dobro, localDate, localTime));

		List<String> responses = postJson(url, json, new ErrorResponse());

		EntityOneVo entity = objectMapper.readValue(responses.get(1), EntityOneVo.class);

		EntityOneVo entityOne = objectMapper.readValue(findById("entityOne", entity.getId()), EntityOneVo.class);
		EntityTwoVo entityTwo = objectMapper.readValue(findById("entityTwo", entityOne.getIdEntityTwo()), EntityTwoVo.class);
		EntityTreeVo entityTree = objectMapper.readValue(findById("entityTree", entityTwo.getIdEntityTree()), EntityTreeVo.class);
		EntityFourVo entityFour = objectMapper.readValue(findById("entityFour", entityTree.getIdEntityFour()), EntityFourVo.class);
		EntityFiveVo entityFive = objectMapper.readValue(findById("entityFive", entityFour.getIdEntityFive()), EntityFiveVo.class);

		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityOne.getId());
		assertNotNull(entityTwo.getId());
		assertNotNull(entityTree.getId());
		assertNotNull(entityFour.getId());
		assertNotNull(entityFive.getId());
	}

	@Test
	void teste_10() throws Exception {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/flush/entityOne";

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = "Bruno";
		inteiro[0] = 6;
		dobro[0] = 1.50;
		localDate[0] = "1995-01-12";
		localTime[0] = "2000-01-01T08:15:00";

		text[1] = "Púrpura";
		inteiro[1] = 11;
		dobro[1] = 314.57;
		localDate[1] = "2024-01-04";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Formiga";
		inteiro[2] = 15;
		dobro[2] = 24.6;
		localDate[2] = "1992-01-04";
		localTime[2] = "2024-03-08T17:32:32";

		text[3] = "Jamelão";
		inteiro[3] = 99;
		dobro[3] = 257.12;
		localDate[3] = "2012-10-12";
		localTime[3] = "2024-03-29T18:23:21";

		text[4] = "PS5";
		dobro[4] = 22.9;
		inteiro[4] = 26;
		localDate[4] = "1992-01-10";
		localTime[4] = "1992-01-10T19:14:10";

		String json = objectMapper.writeValueAsString(gerarEntity(text, inteiro, dobro, localDate, localTime));

		List<String> responses = postJson(url, json, new ErrorResponse());

		EntityOneVo entity = objectMapper.readValue(responses.get(1), EntityOneVo.class);

		EntityOneVo entityOne = objectMapper.readValue(findById("entityOne", entity.getId()), EntityOneVo.class);
		EntityTwoVo entityTwo = objectMapper.readValue(findById("entityTwo", entityOne.getIdEntityTwo()), EntityTwoVo.class);
		EntityTreeVo entityTree = objectMapper.readValue(findById("entityTree", entityTwo.getIdEntityTree()), EntityTreeVo.class);
		EntityFourVo entityFour = objectMapper.readValue(findById("entityFour", entityTree.getIdEntityFour()), EntityFourVo.class);
		EntityFiveVo entityFive = objectMapper.readValue(findById("entityFive", entityFour.getIdEntityFive()), EntityFiveVo.class);

		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityOne.getId());
		assertNotNull(entityTwo.getId());
		assertNotNull(entityTree.getId());
		assertNotNull(entityFour.getId());
		assertNotNull(entityFive.getId());
	}

	@Test
	void teste_11() throws Exception {

		String url = PATH + port + Constants.API_NAME_REQUEST_MAPPING + "/entityOne";

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = "Glacyane";
		inteiro[0] = 6;
		dobro[0] = 1.50;
		localDate[0] = "1986-11-13";
		localTime[0] = "1986-11-13T22:13:11";

		text[1] = "Rosa";
		inteiro[1] = 11;
		dobro[1] = 314.57;
		localDate[1] = "2024-01-07";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Escorpião";
		inteiro[2] = 15;
		dobro[2] = 24.6;
		localDate[2] = "2024-03-08";
		localTime[2] = "2024-03-08T17:32:32";

		text[3] = "Lichia";
		inteiro[3] = 99;
		dobro[3] = 257.12;
		localDate[3] = "2024-03-29";
		localTime[3] = "2024-03-29T18:23:21";

		text[4] = "Jarra";
		dobro[4] = 22.9;
		inteiro[4] = 26;
		localDate[4] = "1992-01-10";
		localTime[4] = "1992-01-10T19:14:10";

		String json = objectMapper.writeValueAsString(gerarEntity(text, inteiro, dobro, localDate, localTime));

		List<String> responses = postJson(url, json, new ErrorResponse());

		EntityOneVo entity = objectMapper.readValue(responses.get(1), EntityOneVo.class);

		EntityOneVo entityOne = objectMapper.readValue(findById("entityOne", entity.getId()), EntityOneVo.class);
		EntityTwoVo entityTwo = objectMapper.readValue(findById("entityTwo", entityOne.getIdEntityTwo()), EntityTwoVo.class);
		EntityTreeVo entityTree = objectMapper.readValue(findById("entityTree", entityTwo.getIdEntityTree()), EntityTreeVo.class);
		EntityFourVo entityFour = objectMapper.readValue(findById("entityFour", entityTree.getIdEntityFour()), EntityFourVo.class);
		EntityFiveVo entityFive = objectMapper.readValue(findById("entityFive", entityFour.getIdEntityFive()), EntityFiveVo.class);

		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityOne.getId());
		assertNotNull(entityTwo.getId());
		assertNotNull(entityTree.getId());
		assertNotNull(entityFour.getId());
		assertNotNull(entityFive.getId());
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

	public List<String> postJson(String url, String json, ErrorResponse compare) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
		List<String> responses = new ArrayList<>();
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			responses.add(response.getStatusCode().toString());
			responses.add(response.getBody());
			return responses;
		} else {
			ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertTrue(errorResponse.getMessage().contains(compare.getMessage()));
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: "
					+ errorResponse.getMessage());
		}
	}

	public List<EntityOneVo> getAll(String url, ErrorResponse compare) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return convertResponseToEntityOneList(response.getBody());
		} else {
			ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertTrue(errorResponse.getMessage().contains(compare.getMessage()));
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: "
					+ errorResponse.getMessage());
		}
	}

	public EntityOneVo getSingleResult(String url, ErrorResponse compare) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return convertResponseToEntityOne(response.getBody());
		} else {
			ErrorResponse errorResponse = convertResponseToErrorResponse(response.getBody());
			assertEquals(compare.getStatus(), errorResponse.getStatus());
			assertTrue(errorResponse.getMessage().contains(compare.getMessage()));
			throw new RuntimeException("Failed to fetch user. Status code: " + response.getStatusCode() + ". Error: "
					+ errorResponse.getMessage());
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

	private ObjectMapper createObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.findAndRegisterModules();
		return objectMapper;
	}

	private List<EntityOneVo> convertResponseToEntityOneList(String body) {
		ObjectMapper objectMapper = createObjectMapper();
		try {
			if (body != null) {
				return objectMapper.readValue(body, new TypeReference<List<EntityOneVo>>() {
				});
			} else {
				return null;
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error parsing EntityOne list response", e);
		}
	}

	private EntityOneVo convertResponseToEntityOne(String body) {
		ObjectMapper objectMapper = createObjectMapper();
		try {
			if (body != null) {
				return objectMapper.readValue(body, EntityOneVo.class);
			} else {
				return null;
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error parsing EntityOne response", e);
		}
	}

	private ErrorResponse convertResponseToErrorResponse(String body) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(body, ErrorResponse.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error parsing ErrorResponse", e);
		}
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
