package br.com.process.integration.database.core.ui;

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

import br.com.process.integration.database.core.ui.adapter.LocalDateAdapter;
import br.com.process.integration.database.core.ui.adapter.LocalDateTimeAdapter;
import br.com.process.integration.database.domain.entity.EntityTest1;
import br.com.process.integration.database.domain.entity.EntityTest2;
import br.com.process.integration.database.domain.entity.EntityTest3;
import br.com.process.integration.database.domain.entity.EntityTest4;
import br.com.process.integration.database.domain.entity.EntityTest5;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QueryJpaControllerSaveTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private CrudJpaController crudJpaController;

	static Long id;
	static List<Long> ids;
	static String endpoint;
	
	@BeforeAll
	void setupOnce() {
		ids = new ArrayList<>();
		endpoint = "http://localhost:" + port + "/v1/api-rest-database/save";
	}

	@BeforeEach
	void setup() {
		id = 0l;
	}

	@Test
	@Order(1)
	void contextLoads() {
		assertNotNull(crudJpaController);
	}
	
	@Test
	@Order(2)
	void testSave() {

		String url = endpoint + "/EntityTest1";

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

		EntityTest1 entityTest1 = gerarEntity(text, inteiro, dobro, localDate, localTime);

		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();

		String json = gson.toJson(entityTest1);

		List<String> responses = postJson(url, json);

		EntityTest1 entity = gson.fromJson(responses.get(1), EntityTest1.class);

		id = entity.getId();
				
		assertEquals(responses.get(0), HttpStatus.OK.toString());
		assertNotNull(entity.getId());
		assertNotNull(entity.getEntityTest2().getId());
		assertNotNull(entity.getEntityTest2().getEntityTest3().getId());
		assertNotNull(entity.getEntityTest2().getEntityTest3().getEntityTest4().getId());
		assertNotNull(entity.getEntityTest2().getEntityTest3().getEntityTest4().getEntityTest5().getId());
	}


	@Test
	@Order(2)
	void testSaveAll() {

		String url = endpoint + "/all/EntityTest1";

		List<EntityTest1> list = new ArrayList<>();

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

		Type userListType = new TypeToken<List<EntityTest1>>(){}.getType();

		// Desserializa o JSON para uma lista de User
		List<EntityTest1> lists = gson.fromJson(responses.get(1), userListType);

		assertEquals(responses.get(0), HttpStatus.OK.toString());

		assertEquals(4, lists.size());

		assertNotNull(lists.get(0).getId());
		assertNotNull(lists.get(1).getId());
		assertNotNull(lists.get(2).getId());
		assertNotNull(lists.get(3).getId());
		
		assertNotNull(lists.get(0).getEntityTest2().getId());
		assertNotNull(lists.get(0).getEntityTest2().getEntityTest3().getId());
		assertNotNull(lists.get(0).getEntityTest2().getEntityTest3().getEntityTest4().getId());
		assertNotNull(lists.get(0).getEntityTest2().getEntityTest3().getEntityTest4().getEntityTest5().getId());

		assertNotNull(lists.get(1).getEntityTest2().getId());
		assertNotNull(lists.get(1).getEntityTest2().getEntityTest3().getId());
		assertNotNull(lists.get(1).getEntityTest2().getEntityTest3().getEntityTest4().getId());
		assertNotNull(lists.get(1).getEntityTest2().getEntityTest3().getEntityTest4().getEntityTest5().getId());

		assertNotNull(lists.get(2).getEntityTest2().getId());
		assertNotNull(lists.get(2).getEntityTest2().getEntityTest3().getId());
		assertNotNull(lists.get(2).getEntityTest2().getEntityTest3().getEntityTest4().getId());
		assertNotNull(lists.get(2).getEntityTest2().getEntityTest3().getEntityTest4().getEntityTest5().getId());
		
		assertNotNull(lists.get(3).getEntityTest2().getId());
		assertNotNull(lists.get(3).getEntityTest2().getEntityTest3().getId());
		assertNotNull(lists.get(3).getEntityTest2().getEntityTest3().getEntityTest4().getId());
		assertNotNull(lists.get(3).getEntityTest2().getEntityTest3().getEntityTest4().getEntityTest5().getId());
		
		ids.add(lists.get(0).getId());
		ids.add(lists.get(1).getId());
		ids.add(lists.get(2).getId());
	}

	public EntityTest1 gerarEntity(String[] text, Integer[] inteiro, Double[] dobro, String[] localDate,
			String[] localTime) {

		EntityTest1 entityTest1 = new EntityTest1();
		entityTest1.setName(text[0]);
		entityTest1.setAge(inteiro[0]);
		entityTest1.setHeight(dobro[0]);
		entityTest1.setBirthDate(geraLocalDate(localDate[0]));
		entityTest1.setProhibited(geraDataLocalTime(localTime[0]));

		EntityTest2 entityTest2 = new EntityTest2();
		entityTest2.setId(UUID.randomUUID().toString());
		entityTest2.setColor(text[1]);
		entityTest2.setHex(inteiro[1]);
		entityTest2.setCost(dobro[1]);
		entityTest2.setDateInclusion(geraLocalDate(localDate[1]));

		EntityTest3 entityTest3 = new EntityTest3();
		entityTest3.setId(UUID.randomUUID().toString());
		entityTest3.setAnimal(text[2]);
		entityTest3.setNumber(inteiro[2]);
		entityTest3.setValue(dobro[2]);
		entityTest3.setDataLocal(geraLocalDate(localDate[2]));
		entityTest3.setDataLocalTime(geraDataLocalTime(localTime[2]));

		EntityTest4 entityTest4 = new EntityTest4();
		entityTest4.setId(UUID.randomUUID().toString());
		entityTest4.setFruit(text[3]);
		entityTest4.setNutritiou(inteiro[3]);
		entityTest4.setDateInclusionTime(geraDataLocalTime(localTime[3]));

		EntityTest5 entityTest5 = new EntityTest5();
		entityTest5.setId(UUID.randomUUID().toString());
		entityTest5.setObject(text[4]);
		entityTest5.setValue(inteiro[4]);

		entityTest1.setEntityTest2(entityTest2);
		entityTest2.setEntityTest3(entityTest3);
		entityTest3.setEntityTest4(entityTest4);
		entityTest4.setEntityTest5(entityTest5);

		return entityTest1;
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

	public LocalDateTime geraDataLocalTime(String dateTimeString) {
		LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
		return dateTime;
	}

	public LocalDate geraLocalDate(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
		LocalDate date = LocalDate.parse(dateString, formatter);
		return date;
	}
}
