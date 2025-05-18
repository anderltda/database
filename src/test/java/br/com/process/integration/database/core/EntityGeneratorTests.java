package br.com.process.integration.database.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.process.integration.database.generator.handler.entity.EntityGenerator;
import br.com.process.integration.database.generator.handler.entity.EntityRepositoryGenerator;
import br.com.process.integration.database.generator.handler.entity.EntityServiceGenerator;
import br.com.process.integration.database.generator.metadata.ClassResolver;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EntityGeneratorTests {

	private String domain = "test";
	private String url = "jdbc:h2:mem:testdb";
	private String user = "sa";
	private String pass = "";

	private Set<String> table1s = new LinkedHashSet<>(Arrays.asList("entity_nine", "entity_eight", "entity_seven", "entity_six"));

	private Set<String> table2s = new LinkedHashSet<>(Arrays.asList("entity_one", "entity_status", "entity_two", "entity_tree", "entity_four", "entity_five"));

	@Test
	@Order(1)
	void teste_01() throws Exception {
		generateEntity(table1s);
	}

	@Test
	@Order(2)
	void teste_02() throws Exception {
		generateEntity(table2s);
	}

	private void generateEntity(Set<String> tables) throws Exception {

		String packages = Constants.JPA_ENTITY + domain;

		EntityGenerator entityGenerator = new EntityGenerator(url, user, pass, packages);
		entityGenerator.setTables(tables);

		List<ClassResolver> classResolverList = entityGenerator.run();

		classResolverList.forEach(resolver -> {

			String repository = Constants.JPA_REPOSITORY + domain;

			String service = Constants.JPA_SERVICE + domain;

			try {

				EntityRepositoryGenerator.generateRepositoryClass(resolver.getName(), packages, repository, resolver.getTypeId().toString());

				EntityServiceGenerator.generateServiceClass(resolver.getName(), packages, service, resolver.getTypeId().toString());

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});

	}

}
