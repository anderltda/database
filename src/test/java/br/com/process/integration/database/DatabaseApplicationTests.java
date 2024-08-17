package br.com.process.integration.database;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.process.integration.database.core.ui.CrudJpaController;
import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.core.ui.QueryNativeController;

@SpringBootTest
class DatabaseApplicationTests {

	@Autowired
	private QueryJpaController queryJpaController;

	@Autowired
	private QueryNativeController queryNativeController;

	@Autowired
	private CrudJpaController crudJpaController;

	@Test
	void contextLoads() {
		assertNotNull(crudJpaController);
		assertNotNull(queryJpaController);
		assertNotNull(queryNativeController);
	}
}
