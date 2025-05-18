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
	
	private String domain = "test";
	private String url = "jdbc:h2:mem:testdb";
	private String user = "sa";
	private String pass = "";

	private Set<String> table1s = new LinkedHashSet<>(Arrays.asList("entity_nine", "entity_eight", "entity_seven", "entity_six"));
	
	private Set<String> table2s = new LinkedHashSet<>(Arrays.asList("entity_one", "entity_status", "entity_two", "entity_tree", "entity_four", "entity_five"));

	@Test
	@Order(1)
	void teste_01() throws Exception {
		
		for (String table : table1s) {
			try {
				generateView(table);
			} catch (Exception ex) {
				ex.printStackTrace();
				throw ex;
			}
		}
	}

	@Test
	@Order(2)
	void teste_02() throws Exception {
		for (String table : table2s) {
			try {
				generateView(table);
			} catch (Exception ex) {
				ex.printStackTrace();
				throw ex;
			}
		}
	}
	
	private void generateView(String table) throws Exception {

		String packages = Constants.JDBC_VIEW + domain;

		ViewGenerator viewGenerator = new ViewGenerator(url, user, pass, packages, domain);
		
		String classView = viewGenerator.run(table.toUpperCase());

		viewGenerator.generateQueryDefinition(table.toUpperCase());

		String service = Constants.JDBC_SERVICE + domain;

		ViewServiceGenerator.generateServiceClass(classView, packages, service);

	}

}
