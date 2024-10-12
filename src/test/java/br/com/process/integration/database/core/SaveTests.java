package br.com.process.integration.database.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import br.com.process.integration.database.core.ui.CrudJpaController;
import br.com.process.integration.database.core.ui.adapter.LocalDateAdapter;
import br.com.process.integration.database.core.ui.adapter.LocalDateTimeAdapter;
import br.com.process.integration.database.core.util.Constants;
import br.com.process.integration.database.domain.model.entity.EntityFive;
import br.com.process.integration.database.domain.model.entity.EntityFour;
import br.com.process.integration.database.domain.model.entity.EntityOne;
import br.com.process.integration.database.domain.model.entity.EntityTree;
import br.com.process.integration.database.domain.model.entity.EntityTwo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SaveTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private CrudJpaController crudJpaController;

	public static Long id;
	public static List<Long> ids;
	
	@BeforeAll
	void setupOnce() {
		
		if(ids == null) {
			ids = new ArrayList<>();
		}
	}

	@BeforeEach
	void setup() { 
		
		if(id == null) {
			id = 0l;
		}
		
	}

	@Test
	@Order(1)
	void contextLoads() {
		assertNotNull(crudJpaController);
	}
	
	@Test
	@Order(2)
	void teste_01() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/EntityOne";

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

		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();

		String json = gson.toJson(entityOne);

		List<String> responses = postJson(url, json);

		EntityOne entity = gson.fromJson(responses.get(1), EntityOne.class);

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

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/flush/EntityOne";

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = "Anderson";
		inteiro[0] = 41;
		dobro[0] = 1.93;
		localDate[0] = "03-29-1983";
		localTime[0] = "2024-02-01T02:52:54";

		text[1] = "Azul";
		inteiro[1] = 2596;
		dobro[1] = 13.25;
		localDate[1] = "03-07-2024";
		localTime[1] = "2024-01-01T16:41:43";

		text[2] = "Avestruz";
		inteiro[2] = 5;
		dobro[2] = 55.6;
		localDate[2] = "10-09-2024";
		localTime[2] = "2024-10-18T13:32:32";

		text[3] = "Laranja";
		inteiro[3] = 11;
		dobro[3] = 21.1;
		localDate[3] = "12-10-2024";
		localTime[3] = "2024-12-09T14:00:21";

		text[4] = "Cadeira";
		dobro[4] = 44.9;
		inteiro[4] = 26;
		localDate[4] = "01-10-2024";
		localTime[4] = "2024-01-10T19:14:10";

		EntityOne entity1 = gerarEntity(text, inteiro, dobro, localDate, localTime);

		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();

		String json = gson.toJson(entity1);

		List<String> responses = postJson(url, json);

		EntityOne entity = gson.fromJson(responses.get(1), EntityOne.class);

		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entity.getId());
		assertNotNull(entity.getEntityTwo().getId());
		assertNotNull(entity.getEntityTwo().getEntityTree().getId());
		assertNotNull(entity.getEntityTwo().getEntityTree().getEntityFour().getId());
		assertNotNull(entity.getEntityTwo().getEntityTree().getEntityFour().getEntityFive().getId());
	}

	@Test
	@Order(4)
	void teste_03() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/all/EntityOne";

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

		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();

		String json = gson.toJson(list);

		List<String> responses = postJson(url, json);

		Type userListType = new TypeToken<List<EntityOne>>(){}.getType();

		List<EntityOne> lists = gson.fromJson(responses.get(1), userListType);

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
	@Order(5)
	void teste_04() {

		String url = "http://localhost:" + port + Constants.API_NAME_REQUEST_MAPPING + "/all/flush/EntityOne";

		List<EntityOne> list = new ArrayList<>();

		String[] text = new String[5];
		Integer[] inteiro = new Integer[5];
		Double[] dobro = new Double[5];
		String[] localDate = new String[5];
		String[] localTime = new String[5];

		text[0] = "Carlos";
		inteiro[0] = 34;
		dobro[0] = 1.70;
		localDate[0] = "08-30-1956";
		localTime[0] = "2024-04-01T08:50:54";

		text[1] = "Preto";
		inteiro[1] = 144;
		dobro[1] = 25.5;
		localDate[1] = "01-07-2024";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Porco";
		inteiro[2] = 72;
		dobro[2] = 20.6;
		localDate[2] = "01-08-2024";
		localTime[2] = "2024-01-08T17:32:32";

		text[3] = "Caju";
		inteiro[3] = 21;
		dobro[3] = 3.1;
		localDate[3] = "01-02-2024";
		localTime[3] = "2024-02-09T18:23:21";

		text[4] = "Porta";
		dobro[4] = 22.0;
		inteiro[4] = 21;
		localDate[4] = "01-10-2024";
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
		localDate[0] = "09-09-1986";
		localTime[0] = "2024-02-01T08:50:54";

		text[1] = "Verde";
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
		inteiro[3] = 19;
		dobro[3] = 3.1;
		localDate[3] = "06-10-2024";
		localTime[3] = "2024-01-09T18:23:21";

		text[4] = "Tennis";
		dobro[4] = 12.9;
		inteiro[4] = 11;
		localDate[4] = "01-10-2024";
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
		localDate[0] = "01-01-1992";
		localTime[0] = "2024-11-01T08:00:00";

		text[1] = "Amarelo";
		inteiro[1] = 524;
		dobro[1] = 25.5;
		localDate[1] = "01-07-2024";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Macado";
		inteiro[2] = 87;
		dobro[2] = 14.6;
		localDate[2] = "01-08-2024";
		localTime[2] = "2024-01-08T17:32:32";

		text[3] = "Mamao";
		inteiro[3] = 1;
		dobro[3] = 29.1;
		localDate[3] = "01-09-2024";
		localTime[3] = "2024-01-09T18:23:21";

		text[4] = "Balde";
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
		
		text[0] = "Ariovaldo";
		inteiro[0] = 22;
		dobro[0] = 1.90;
		localDate[0] = "01-01-1990";
		localTime[0] = "2024-11-01T08:00:00";

		text[1] = "Cinza";
		inteiro[1] = 37890;
		dobro[1] = 21.5;
		localDate[1] = "01-07-2024";
		localTime[1] = "2024-01-07T16:41:43";

		text[2] = "Gato";
		inteiro[2] = 24;
		dobro[2] = 24.6;
		localDate[2] = "01-08-2024";
		localTime[2] = "2024-01-08T17:32:32";

		text[3] = "Banana";
		inteiro[3] = 25;
		dobro[3] = 23.1;
		localDate[3] = "01-09-2024";
		localTime[3] = "2024-01-09T18:23:21";

		text[4] = "Vassoura";
		dobro[4] = 22.9;
		inteiro[4] = 26;
		localDate[4] = "01-10-2024";
		localTime[4] = "2024-01-10T19:14:10";
		
		list.add(gerarEntity(text, inteiro, dobro, localDate, localTime));

		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();

		String json = gson.toJson(list);

		List<String> responses = postJson(url, json);

		Type userListType = new TypeToken<List<EntityOne>>() {}.getType();

		List<EntityOne> lists = gson.fromJson(responses.get(1), userListType);

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
		
	}

	public EntityOne gerarEntity(String[] text, Integer[] inteiro, Double[] dobro, String[] localDate,
			String[] localTime) {

		EntityOne entityOne = new EntityOne();
		entityOne.setName(text[0]);
		entityOne.setAge(inteiro[0]);
		entityOne.setHeight(dobro[0]);
		entityOne.setBirthDate(geraLocalDate(localDate[0]));
		entityOne.setProhibitedDateTime(geraDataLocalTime(localTime[0]));

		EntityTwo entityTwo = new EntityTwo();
		entityTwo.setId(UUID.randomUUID().toString());
		entityTwo.setColor(text[1]);
		entityTwo.setHex(inteiro[1]);
		entityTwo.setCost(dobro[1]);
		entityTwo.setInclusionDate(geraLocalDate(localDate[1]));

		EntityTree entityTree = new EntityTree();
		entityTree.setId(UUID.randomUUID().toString());
		entityTree.setAnimal(text[2]);
		entityTree.setIndicator(inteiro[2]);
		entityTree.setAmount(dobro[2]);
		entityTree.setLocalDate(geraLocalDate(localDate[2]));
		entityTree.setLocalDateTime(geraDataLocalTime(localTime[2]));

		EntityFour entityFour = new EntityFour();
		entityFour.setId(UUID.randomUUID().toString());
		entityFour.setFruit(text[3]);
		entityFour.setAttribute(inteiro[3]);
		entityFour.setInclusionDateTime(geraDataLocalTime(localTime[3]));

		EntityFive entityFive = new EntityFive();
		entityFive.setId(UUID.randomUUID().toString());
		entityFive.setReference(text[4]);
		entityFive.setFactor(inteiro[4]);

		entityOne.setEntityTwo(entityTwo);
		entityTwo.setEntityTree(entityTree);
		entityTree.setEntityFour(entityFour);
		entityFour.setEntityFive(entityFive);

		return entityOne;
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
