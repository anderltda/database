package br.com.process.integration.database.core;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InitTests {

	@BeforeAll
	void initDatabase() throws Exception {
	    Resource resource = new ClassPathResource("sql/test-schema.sql");
	    String sql = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
	    try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "")) {
	        Statement stmt = conn.createStatement();
	        stmt.execute(sql);
	    }
	}
}
