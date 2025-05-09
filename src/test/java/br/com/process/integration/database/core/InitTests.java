package br.com.process.integration.database.core;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InitTests {

	@Autowired
	private DataSource dataSource;
	
	private Map<String, Boolean> mapTables = new HashMap<>();

	private List<String> tables = Arrays.asList("ENTITY_STATUS", "ENTITY_NINE", "ENTITY_EIGHT", "ENTITY_SEVEN", "ENTITY_SIX", "ENTITY_FIVE", "ENTITY_FOUR", "ENTITY_TREE", "ENTITY_TWO", "ENTITY_ONE");

	@BeforeAll
	void initDatabase() throws Exception {
		
		tables.forEach(table -> {
			mapTables.put(table, false);
		});
		
		Resource resource = new ClassPathResource("sql/test-schema.sql");
		String sql = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
		try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "")) {
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		}
	}

	@Test
	@Order(1)
	void listarTabelas() throws Exception {
		try (Connection conn = dataSource.getConnection()) {
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet tables = metaData.getTables(null, null, "%", new String[] { "TABLE" });
			while (tables.next()) {
				String table = tables.getString("TABLE_NAME");
				System.out.println("TABLE_NAME >>>>> " + table);
				putTrueTable(table);
			}
			
			mapTables.forEach((tableName, exists) -> {
			    assertTrue(exists);
			});
		}
	}

	private void putTrueTable(String table) {

		boolean existe = tables.stream().anyMatch(t -> t.equalsIgnoreCase(table));

		if (existe) {
			mapTables.put(table, true);
		} 
	}
}
