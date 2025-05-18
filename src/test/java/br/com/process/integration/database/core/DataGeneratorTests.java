package br.com.process.integration.database.core;

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

import br.com.process.integration.database.generator.handler.data.DataGenerator;
import br.com.process.integration.database.generator.handler.data.DataMapperGenerator;
import br.com.process.integration.database.generator.handler.data.DataServiceGenerator;
import br.com.process.integration.database.generator.handler.data.XmlGenerator;
import br.com.process.integration.database.generator.util.JavaCompilerUtil;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DataGeneratorTests {

	private String domain = "test";
	private String url = "jdbc:h2:mem:testdb";
	private String user = "sa";
	private String pass = "";

	private Set<String> table1s = new LinkedHashSet<>(Arrays.asList("entity_nine", "entity_eight", "entity_seven", "entity_six"));
	
	private Set<String> table2s = new LinkedHashSet<>(Arrays.asList("entity_one", "entity_status", "entity_two", "entity_tree", "entity_four", "entity_five"));

	@Test
	@Order(1)
	void teste_01() throws Exception {
		generateData(table1s);
	}

	@Test
	@Order(2)
	void teste_02() throws Exception {
		generateData(table2s);
	}
	
	private void generateData(Set<String> tables) throws Exception {

		String packages = Constants.MYBATIS_DATA + domain;

		DataGenerator dataGenerator = new DataGenerator(url, user, pass, packages);
		dataGenerator.setTables(tables);

		List<String> classList = dataGenerator.run();

		List<String> qualifiedClassNames = classList.stream().map(clazz -> packages + "." + clazz).toList();

		List<Class<?>> compiledClasses = JavaCompilerUtil.compileAndLoadClasses(qualifiedClassNames);

		String mapper = Constants.MYBATIS_MAPPER + domain;
		DataMapperGenerator dataMapperGenerator = new DataMapperGenerator(mapper, packages, tables);
		dataMapperGenerator.run();

		String service = Constants.MYBATIS_SERVICE + domain;
		DataServiceGenerator dataServiceGenerator = new DataServiceGenerator(service, packages, mapper, tables);
		dataServiceGenerator.run();

		XmlGenerator xmlGenerator = new XmlGenerator(dataGenerator.getMapPrimaryKeys(), dataGenerator.getMapTables());
		xmlGenerator.run(compiledClasses, domain);

	}

}