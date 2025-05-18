package br.com.process.integration.database.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import br.com.process.integration.database.model.entity.dto.example.EntityFive;
import br.com.process.integration.database.model.entity.dto.example.EntityFour;
import br.com.process.integration.database.model.entity.dto.example.EntityOne;
import br.com.process.integration.database.model.entity.dto.example.EntityStatus;
import br.com.process.integration.database.model.entity.dto.example.EntityTree;
import br.com.process.integration.database.model.entity.dto.example.EntityTwo;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SaveFlushTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private CrudJpaController crudJpaController;

	@Autowired
	private ObjectMapper objectMapper;

	static Long id;
	static List<Long> ids;
	
	public EntityOne entityOne;
	public EntityStatus entityStatusOne;
	public EntityStatus entityStatusTwo;
	public EntityStatus entityStatusTree;
	public EntityStatus entityStatusFour;
	public EntityStatus entityStatusFive;

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

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/EntityStatus";

		entityStatusOne = gerarEntityStatus("Status One", true, 0);
		String json = objectMapper.writeValueAsString(entityStatusOne);
		List<String> responses = postJson(url, json, null);
		entityStatusOne = objectMapper.readValue(responses.get(1), EntityStatus.class);
		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityStatusOne.getId());

		entityStatusTwo = gerarEntityStatus("Status Two", false, 1);
		json = objectMapper.writeValueAsString(entityStatusTwo);
		responses = postJson(url, json, null);
		entityStatusTwo = objectMapper.readValue(responses.get(1), EntityStatus.class);
		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityStatusTwo.getId());

		entityStatusTree = gerarEntityStatus("Status Tree", true, 0);
		json = objectMapper.writeValueAsString(entityStatusTree);
		responses = postJson(url, json, null);
		entityStatusTree = objectMapper.readValue(responses.get(1), EntityStatus.class);
		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityStatusTree.getId());

		entityStatusFour = gerarEntityStatus("Status Four", false, 1);
		json = objectMapper.writeValueAsString(entityStatusFour);
		responses = postJson(url, json, null);
		entityStatusFour = objectMapper.readValue(responses.get(1), EntityStatus.class);
		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityStatusFour.getId());

		entityStatusFive = gerarEntityStatus("Status Five", false, 0);
		json = objectMapper.writeValueAsString(entityStatusFive);
		responses = postJson(url, json, null);
		entityStatusFive = objectMapper.readValue(responses.get(1), EntityStatus.class);
		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entityStatusFive.getId());
	}

	@Test
	@Order(2)
	void teste_01() throws Exception {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/flush/EntityOne";

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = "Ricardo";
		inteiro[0] = 22;
		dobro[0] = 1.80;
		localDate[0] = "01-01-1990";
		localTime[0] = "2024-11-01T08:00:00";

		text[1] = "Verde";
		inteiro[1] = 12344;
		dobro[1] = 25.5;
		localDate[1] = "01-07-2024";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Gato";
		inteiro[2] = 24;
		dobro[2] = 24.6;
		localDate[2] = "01-08-2024";
		localTime[2] = "2024-01-08T17:32:32";

		text[3] = "Banana";
		inteiro[3] = 45;
		dobro[3] = 103.12;
		localDate[3] = "10-12-2024";
		localTime[3] = "2024-03-29T18:23:21";

		text[4] = "Vassoura";
		dobro[4] = 22.9;
		inteiro[4] = 26;
		localDate[4] = "01-10-2024";
		localTime[4] = "2024-01-10T19:14:10";

		EntityOne entityOne = gerarEntity(text, inteiro, dobro, localDate, localTime);

		String json = objectMapper.writeValueAsString(entityOne);

		List<String> responses = postJson(url, json, new ErrorResponse());

		EntityOne entity = objectMapper.readValue(responses.get(1), EntityOne.class);

		id = entity.getId();

		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entity.getId());
		assertNotNull(entity.getEntityTwo().getId());
		assertNotNull(entity.getEntityTwo().getEntityTree().getId());
		assertNotNull(entity.getEntityTwo().getEntityTree().getEntityFour().getId());
		assertNotNull(entity.getEntityTwo().getEntityTree().getEntityFour().getEntityFive().getId());
	}

	@Test
	@Order(3)
	void teste_02() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/EntityOne/" + id;

		entityOne = getSingleResult(url, new ErrorResponse());

		assertNotNull(entityOne);
		assertEquals("Ricardo", entityOne.getName());
		assertEquals(22, entityOne.getAge());
		assertEquals(1.80, entityOne.getHeight());
	}

	@Test
	@Order(4)
	void teste_03() throws Exception {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/flush/EntityOne";

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = "Manolo";
		inteiro[0] = 70;
		dobro[0] = 1.40;
		localDate[0] = "01-01-1990";
		localTime[0] = "2024-11-01T08:00:00";

		text[1] = "Verde";
		inteiro[1] = 12344;
		dobro[1] = 25.5;
		localDate[1] = "01-07-2024";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Gato";
		inteiro[2] = 24;
		dobro[2] = 24.6;
		localDate[2] = "01-08-2024";
		localTime[2] = "2024-01-08T17:32:32";

		text[3] = "Banana";
		inteiro[3] = 45;
		dobro[3] = 103.12;
		localDate[3] = "10-12-2024";
		localTime[3] = "2024-03-29T18:23:21";

		text[4] = "Vassoura";
		dobro[4] = 22.9;
		inteiro[4] = 26;
		localDate[4] = "01-10-2024";
		localTime[4] = "2024-01-10T19:14:10";

		EntityOne entityOne = gerarEntity(text, inteiro, dobro, localDate, localTime);
		
		entityOne.setId(this.entityOne.getId()); 
		entityOne.getEntityTwo().setId(this.entityOne.getEntityTwo().getId()); 
		entityOne.getEntityTwo().getEntityTree().setId(this.entityOne.getEntityTwo().getEntityTree().getId()); 
		entityOne.getEntityTwo().getEntityTree().getEntityFour().setId(this.entityOne.getEntityTwo().getEntityTree().getEntityFour().getId()); 
		entityOne.getEntityTwo().getEntityTree().getEntityFour().getEntityFive().setId(this.entityOne.getEntityTwo().getEntityTree().getEntityFour().getEntityFive().getId()); 

		String json = objectMapper.writeValueAsString(entityOne);

		List<String> responses = postJson(url, json, new ErrorResponse());

		EntityOne entity = objectMapper.readValue(responses.get(1), EntityOne.class);

		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entity.getId());
		assertNotNull(entity.getEntityTwo().getId());
		assertNotNull(entity.getEntityTwo().getEntityTree().getId());
		assertNotNull(entity.getEntityTwo().getEntityTree().getEntityFour().getId());
		assertNotNull(entity.getEntityTwo().getEntityTree().getEntityFour().getEntityFive().getId());
	}

	@Test
	@Order(5)
	void teste_04() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/EntityOne/" + entityOne.getId();

		entityOne = getSingleResult(url, new ErrorResponse());

		assertNotNull(entityOne);
		assertEquals("Manolo", entityOne.getName());
		assertEquals(70, entityOne.getAge());
		assertEquals(1.40, entityOne.getHeight());
	}

	@Test
	@Order(6)
	void teste_05() throws Exception {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/all/flush/EntityOne";

		List<EntityOne> list = new ArrayList<>();

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = "Carlos Alberto";
		inteiro[0] = 55;
		dobro[0] = 1.77;
		localDate[0] = "09-22-1970";
		localTime[0] = "2024-04-30T23:50:54";

		text[1] = "Preto";
		inteiro[1] = 234;
		dobro[1] = 25.5;
		localDate[1] = "01-10-2024";
		localTime[1] = "2024-10-07T16:40:43";

		text[2] = "Capivara";
		inteiro[2] = 72;
		dobro[2] = 20.6;
		localDate[2] = "01-05-2024";
		localTime[2] = "2024-06-08T17:32:32";

		text[3] = "Damasco";
		inteiro[3] = 13;
		dobro[3] = 33.1;
		localDate[3] = "01-07-2024";
		localTime[3] = "2024-04-09T18:23:21";

		text[4] = "Cama";
		dobro[4] = 22.30;
		inteiro[4] = 55;
		localDate[4] = "02-11-2024";
		localTime[4] = "2024-04-10T19:24:12";

		list.add(gerarEntity(text, inteiro, dobro, localDate, localTime));

		text = new String[5];
		inteiro = new Integer[5];
		dobro = new Double[5];
		localDate = new String[5];
		localTime = new String[5];

		text[0] = "Paulo";
		inteiro[0] = 21;
		dobro[0] = 1.78;
		localDate[0] = "09-09-1990";
		localTime[0] = "2024-10-01T08:50:54";

		text[1] = "Roxo";
		inteiro[1] = 3131;
		dobro[1] = 25.5;
		localDate[1] = "01-07-2024";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Cavalo";
		inteiro[2] = 44;
		dobro[2] = 24.6;
		localDate[2] = "01-08-2024";
		localTime[2] = "2024-01-08T17:32:32";

		text[3] = "Pitanga";
		inteiro[3] = 68;
		dobro[3] = 31.1;
		localDate[3] = "10-10-2024";
		localTime[3] = "2024-02-19T18:30:21";

		text[4] = "Copo";
		dobro[4] = 22.9;
		inteiro[4] = 11;
		localDate[4] = "01-10-2024";
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
		localDate[0] = "01-01-2016";
		localTime[0] = "2024-09-01T08:50:54";

		text[1] = "Amarelo";
		inteiro[1] = 23;
		dobro[1] = 25.5;
		localDate[1] = "01-07-2024";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Baleia";
		inteiro[2] = 87;
		dobro[2] = 14.6;
		localDate[2] = "01-08-2024";
		localTime[2] = "2024-01-08T17:32:32";

		text[3] = "Melao";
		inteiro[3] = 1;
		dobro[3] = 29.1;
		localDate[3] = "04-01-2024";
		localTime[3] = "2024-01-09T23:37:21";

		text[4] = "Caixa";
		dobro[4] = 22.9;
		inteiro[4] = 10;
		localDate[4] = "01-10-2024";
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
		localDate[0] = "08-31-1992";
		localTime[0] = "2024-11-01T08:20:10";

		text[1] = "Laranja";
		inteiro[1] = 8876;
		dobro[1] = 210.52;
		localDate[1] = "12-12-2024";
		localTime[1] = "2024-12-07T16:40:43";

		text[2] = "Papagaio";
		inteiro[2] = 13;
		dobro[2] = 564.61;
		localDate[2] = "12-08-2024";
		localTime[2] = "2024-12-08T17:44:32";

		text[3] = "Goiaba";
		inteiro[3] = 14;
		dobro[3] = 90.21;
		localDate[3] = "12-21-2024";
		localTime[3] = "2024-01-10T19:23:21";

		text[4] = "Raquete";
		dobro[4] = 21.9;
		inteiro[4] = 24;
		localDate[4] = "06-10-2024";
		localTime[4] = "2024-04-10T18:14:10";

		list.add(gerarEntity(text, inteiro, dobro, localDate, localTime));

		String json = objectMapper.writeValueAsString(list);

		List<String> responses = postJson(url, json, new ErrorResponse());

		JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, EntityOne.class);

		List<EntityOne> lists = objectMapper.readValue(responses.get(1), type);

		assertEquals(responses.get(0), HttpStatus.OK.toString());

		assertEquals(4, lists.size());

		assertNotNull(lists.get(0).getId());
		assertNotNull(lists.get(1).getId());
		assertNotNull(lists.get(2).getId());
		assertNotNull(lists.get(3).getId());

		assertNotNull(lists.get(0).getEntityTwo().getId());
		assertNotNull(lists.get(0).getEntityTwo().getEntityTree().getId());
		assertNotNull(lists.get(0).getEntityTwo().getEntityTree().getEntityFour().getId());
		assertNotNull(lists.get(0).getEntityTwo().getEntityTree().getEntityFour().getEntityFive().getId());

		assertNotNull(lists.get(1).getEntityTwo().getId());
		assertNotNull(lists.get(1).getEntityTwo().getEntityTree().getId());
		assertNotNull(lists.get(1).getEntityTwo().getEntityTree().getEntityFour().getId());
		assertNotNull(lists.get(1).getEntityTwo().getEntityTree().getEntityFour().getEntityFive().getId());

		assertNotNull(lists.get(2).getEntityTwo().getId());
		assertNotNull(lists.get(2).getEntityTwo().getEntityTree().getId());
		assertNotNull(lists.get(2).getEntityTwo().getEntityTree().getEntityFour().getId());
		assertNotNull(lists.get(2).getEntityTwo().getEntityTree().getEntityFour().getEntityFive().getId());

		assertNotNull(lists.get(3).getEntityTwo().getId());
		assertNotNull(lists.get(3).getEntityTwo().getEntityTree().getId());
		assertNotNull(lists.get(3).getEntityTwo().getEntityTree().getEntityFour().getId());
		assertNotNull(lists.get(3).getEntityTwo().getEntityTree().getEntityFour().getEntityFive().getId());

		ids.add(lists.get(0).getId());
		ids.add(lists.get(1).getId());
		ids.add(lists.get(2).getId());
	}

	@Test
	@Order(7)
	void teste_06() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/EntityOne/" + ids.get(0);

		EntityOne entity = getSingleResult(url, new ErrorResponse());

		assertEquals("Carlos Alberto", entity.getName());
		assertEquals(55, entity.getAge());
		assertEquals(1.77, entity.getHeight());
		assertEquals(LocalDate.parse("1970-09-22", DateTimeFormatter.ISO_LOCAL_DATE), entity.getBirthDate());
		assertEquals(LocalDateTime.parse("2024-04-30T23:50:54", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
				entity.getProhibitedDateTime());

		assertEquals("Preto", entity.getEntityTwo().getColor());
		assertEquals(234, entity.getEntityTwo().getHex());
		assertEquals(25.5, entity.getEntityTwo().getCost());
		assertEquals(LocalDate.parse("2024-01-10", DateTimeFormatter.ISO_LOCAL_DATE),
				entity.getEntityTwo().getInclusionDate());

		assertEquals("Capivara", entity.getEntityTwo().getEntityTree().getAnimal());
		assertEquals(72, entity.getEntityTwo().getEntityTree().getIndicator());

		assertEquals("Damasco", entity.getEntityTwo().getEntityTree().getEntityFour().getFruit());
		assertEquals(13, entity.getEntityTwo().getEntityTree().getEntityFour().getAttribute());
		assertEquals(LocalDateTime.parse("2024-04-09T18:23:21", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
				entity.getEntityTwo().getEntityTree().getEntityFour().getInclusionDateTime());

		assertEquals("Cama", entity.getEntityTwo().getEntityTree().getEntityFour().getEntityFive().getReference());
		assertEquals(55, entity.getEntityTwo().getEntityTree().getEntityFour().getEntityFive().getFactor());
	}

	@Test
	@Order(8)
	void teste_07() throws Exception {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/flush/EntityOne";

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = null;
		inteiro[0] = 70;
		dobro[0] = 1.40;
		localDate[0] = "01-01-1990";
		localTime[0] = "2024-11-01T08:00:00";

		text[1] = "Verde";
		inteiro[1] = 12344;
		dobro[1] = 25.5;
		localDate[1] = "01-07-2024";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Gato";
		inteiro[2] = 24;
		dobro[2] = 24.6;
		localDate[2] = "01-08-2024";
		localTime[2] = "2024-01-08T17:32:32";

		text[3] = "Banana";
		inteiro[3] = 45;
		dobro[3] = 103.12;
		localDate[3] = "10-12-2024";
		localTime[3] = "2024-03-29T18:23:21";

		text[4] = "Vassoura";
		dobro[4] = 22.9;
		inteiro[4] = 26;
		localDate[4] = "01-10-2024";
		localTime[4] = "2024-01-10T19:14:10";

		EntityOne entityOne = gerarEntity(text, inteiro, dobro, localDate, localTime);

		String json = objectMapper.writeValueAsString(entityOne);

		ErrorResponse errorResponse = new ErrorResponse("NULL not allowed for column", HttpStatus.BAD_REQUEST);

		assertThrows(RuntimeException.class, () -> postJson(url, json, errorResponse));
	}

	@Test
	@Order(9)
	void teste_08() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/single/EntityOne?name=Malaquias&name_op=eq";

		EntityOne entity = getSingleResult(url, new ErrorResponse());

		assertNull(entity);
	}

	@Test
	@Order(10)
	void teste_09() throws Exception {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/flush/EntityOne";

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = "Marcelo";
		inteiro[0] = 70;
		dobro[0] = 1.40;
		localDate[0] = "01-01-1990";
		localTime[0] = "2024-11-01T08:00:00";

		text[1] = "Verde";
		inteiro[1] = 12344;
		dobro[1] = 25.5;
		localDate[1] = "01-07-2024";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Gato";
		inteiro[2] = 24;
		dobro[2] = 24.6;
		localDate[2] = "01-08-2024";
		localTime[2] = "2024-01-08T17:32:32";

		text[3] = "Banana";
		inteiro[3] = 45;
		dobro[3] = 103.12;
		localDate[3] = "10-12-2024";
		localTime[3] = "2024-03-29T18:23:21";

		text[4] = "Vassoura";
		dobro[4] = 22.9;
		inteiro[4] = 26;
		localDate[4] = "01-10-2024";
		localTime[4] = "2024-01-10T19:14:10";

		EntityOne entityOne = gerarEntity(text, inteiro, dobro, localDate, localTime);

		String json = objectMapper.writeValueAsString(entityOne);

		List<String> responses = postJson(url, json, new ErrorResponse());

		EntityOne entity = objectMapper.readValue(responses.get(1), EntityOne.class);

		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entity.getId());
		assertNotNull(entity.getEntityTwo().getId());
		assertNotNull(entity.getEntityTwo().getEntityTree().getId());
		assertNotNull(entity.getEntityTwo().getEntityTree().getEntityFour().getId());
		assertNotNull(entity.getEntityTwo().getEntityTree().getEntityFour().getEntityFive().getId());
	}

	@Test
	@Order(11)
	void teste_10() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/single/EntityOne?name=Maria&name_op=eq";

		EntityOne entity = getSingleResult(url, new ErrorResponse());

		assertNotNull(entity);
	}

	public EntityOne gerarEntity(String[] text, Integer[] inteiro, Double[] dobro, String[] localDate,
			String[] localTime) {

		EntityOne entityOne = new EntityOne();
		entityOne.setCode(true);
		entityOne.setName(text[0]);
		entityOne.setAge(inteiro[0]);
		entityOne.setHeight(dobro[0]);
		entityOne.setBirthDate(geraLocalDate(localDate[0]));
		entityOne.setProhibitedDateTime(geraDataLocalTime(localTime[0]));
		entityOne.setEntityStatus(entityStatusOne);

		EntityTwo entityTwo = new EntityTwo();
		entityTwo.setColor(text[1]);
		entityTwo.setHex(inteiro[1]);
		entityTwo.setCost(dobro[1]);
		entityTwo.setInclusionDate(geraLocalDate(localDate[1]));
		entityTwo.setEntityStatus(entityStatusTwo);

		EntityTree entityTree = new EntityTree();
		entityTree.setAnimal(text[2]);
		entityTree.setIndicator(inteiro[2]);
		entityTree.setAmount(dobro[2]);
		entityTree.setLocalDate(geraLocalDate(localDate[2]));
		entityTree.setLocalDateTime(geraDataLocalTime(localTime[2]));
		entityTree.setEntityStatus(entityStatusTree);

		EntityFour entityFour = new EntityFour();
		entityFour.setFruit(text[3]);
		entityFour.setAttribute(inteiro[3]);
		entityFour.setInclusionDateTime(geraDataLocalTime(localTime[3]));
		entityFour.setEntityStatus(entityStatusFour);

		EntityFive entityFive = new EntityFive();
		entityFive.setReference(text[4]);
		entityFive.setFactor(inteiro[4]);
		entityFive.setEntityStatus(entityStatusFive);

		entityOne.setEntityTwo(entityTwo);
		entityTwo.setEntityTree(entityTree);
		entityTree.setEntityFour(entityFour);
		entityFour.setEntityFive(entityFive);

		return entityOne;
	}

	public EntityStatus gerarEntityStatus(String name, Boolean ativo, Integer status) {

		EntityStatus entityStatus = new EntityStatus();
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

	public List<EntityOne> getAll(String url, ErrorResponse compare) {
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

	public EntityOne getSingleResult(String url, ErrorResponse compare) {
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

	private ObjectMapper createObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.findAndRegisterModules();
		return objectMapper;
	}

	private List<EntityOne> convertResponseToEntityOneList(String body) {
		ObjectMapper objectMapper = createObjectMapper();
		try {
			if (body != null) {
				return objectMapper.readValue(body, new TypeReference<List<EntityOne>>() {
				});
			} else {
				return null;
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error parsing EntityOne list response", e);
		}
	}

	private EntityOne convertResponseToEntityOne(String body) {
		ObjectMapper objectMapper = createObjectMapper();
		try {
			if (body != null) {
				return objectMapper.readValue(body, EntityOne.class);
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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
		LocalDate date = LocalDate.parse(dateString, formatter);
		return date;
	}

	public LocalDateTime geraDataLocalTime(String dateTimeString) {
		LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
		return dateTime;
	}
}
