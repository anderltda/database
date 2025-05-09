package br.com.process.integration.database.core;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.process.integration.database.generator.handler.view.ViewGenerator;
import br.com.process.integration.database.generator.handler.view.ViewServiceGenerator;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ViewGeneratorTests {
	
	private String packageView = "br.com.process.integration.database.model.view.dto.";
	private String packageService = "br.com.process.integration.database.model.view.service.";
	
	private String domain = "test";
	private String url = "jdbc:h2:mem:testdb";
	private String user = "sa";
	private String pass = "";

	private Set<String> table1s = new LinkedHashSet<>(Arrays.asList("entity_nine", "entity_eight", "entity_seven", "entity_six"));
	
	private Set<String> table2s = new LinkedHashSet<>(Arrays.asList("entity_one", "entity_status", "entity_two", "entity_tree", "entity_four", "entity_five"));

	@Test
	@Order(1)
	void teste_01() throws Exception {
		generateView(table1s);
	}

	@Test
	@Order(2)
	void teste_02() throws Exception {
		generateView(table2s);
	}
	
	
	private void generateView(Set<String> tables) throws Exception {

		/*
		String packages = packageView + domain;

		ViewGenerator viewGenerator = new ViewGenerator(url, user, pass, packages, tables);
		viewGenerator.setTables(tables);

		String classView = viewGenerator.run();

		String service = packageService + domain;

		ViewServiceGenerator.generateServiceClass(classView, packages, service);
		*/
	}

}
