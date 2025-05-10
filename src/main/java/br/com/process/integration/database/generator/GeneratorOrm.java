package br.com.process.integration.database.generator;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.process.integration.database.generator.handler.data.DataGenerator;
import br.com.process.integration.database.generator.handler.data.DataMapperGenerator;
import br.com.process.integration.database.generator.handler.data.DataServiceGenerator;
import br.com.process.integration.database.generator.handler.data.XmlGenerator;
import br.com.process.integration.database.generator.handler.entity.EntityGenerator;
import br.com.process.integration.database.generator.handler.entity.EntityRepositoryGenerator;
import br.com.process.integration.database.generator.handler.entity.EntityServiceGenerator;
import br.com.process.integration.database.generator.handler.view.ViewGenerator;
import br.com.process.integration.database.generator.handler.view.ViewServiceGenerator;
import br.com.process.integration.database.generator.metadata.ClassResolver;
import br.com.process.integration.database.generator.util.JavaCompilerUtil;

/**
 * 
 */
@Component
public class GeneratorOrm {
	
	private static final String JPA_ENTITY      = "br.com.process.integration.database.model.entity.dto.";
	private static final String JPA_REPOSITORY  = "br.com.process.integration.database.model.entity.repository.";
	private static final String JPA_SERVICE     = "br.com.process.integration.database.model.entity.service.";
	private static final String JDBC_VIEW       = "br.com.process.integration.database.model.view.dto.";
	private static final String JDBC_SERVICE    = "br.com.process.integration.database.model.view.service.";
	private static final String MYBATIS_DATA    = "br.com.process.integration.database.model.data.dto.";
	private static final String MYBATIS_MAPPER  = "br.com.process.integration.database.model.data.mapper.";
	private static final String MYBATIS_SERVICE = "br.com.process.integration.database.model.data.service.";

	private String domain;

	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String user;

	@Value("${spring.datasource.password}")
	private String pass;

	public void generateAll(String domain, Set<String> tables, List<String> types) throws Exception {

		this.domain = domain;

		if (types.contains("Jpa")) {
			extractedEntity(tables);
		}
		
		if (types.contains("View")) {
			extractedView(tables);
		}
		
		if (types.contains("Data")) {
			extractedData(tables);
		}
	}

	private void extractedEntity(Set<String> tables) throws Exception {

		String packageEntity = JPA_ENTITY + domain;

		EntityGenerator entityGenerator = new EntityGenerator(url, user, pass, packageEntity, tables);

		List<ClassResolver> classResolverList = entityGenerator.run();

		classResolverList.forEach(resolver -> {

			String packageRepository = JPA_REPOSITORY + domain;

			String packageService = JPA_SERVICE + domain;

			try {

				EntityRepositoryGenerator.generateRepositoryClass(resolver.getName(), packageEntity, packageRepository,
						resolver.getTypeId().toString());

				EntityServiceGenerator.generateServiceClass(resolver.getName(), packageEntity, packageService,
						resolver.getTypeId().toString());

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
	}
	


	private void extractedView(Set<String> tables) throws Exception {

		tables.forEach(table -> {

			try {

				String packageView = JDBC_VIEW + domain;

				ViewGenerator viewGenerator = new ViewGenerator(url, user, pass, packageView, domain);

				String classView = viewGenerator.run(table);

				viewGenerator.generateQueryDefinition(table);

				String packageService = JDBC_SERVICE + domain;

				ViewServiceGenerator.generateServiceClass(classView, packageView, packageService);

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	


	private void extractedData(Set<String> tables) throws Exception {

		String packageData = MYBATIS_DATA + domain;

		DataGenerator dataGenerator = new DataGenerator(url, user, pass, packageData, tables);

		List<String> classList = dataGenerator.run();

		List<String> qualifiedClassNames = classList.stream().map(clazz -> packageData + "." + clazz).toList();

		List<Class<?>> compiledClasses = JavaCompilerUtil.compileAndLoadClasses(qualifiedClassNames);

		String packageMapper = MYBATIS_MAPPER + domain;
		DataMapperGenerator dataMapperGenerator = new DataMapperGenerator(packageMapper, packageData, tables);
		dataMapperGenerator.run();

		String packageService = MYBATIS_SERVICE + domain;
		DataServiceGenerator dataServiceGenerator = new DataServiceGenerator(packageService, packageData, packageMapper,
				tables);
		dataServiceGenerator.run();

		XmlGenerator xmlGenerator = new XmlGenerator(dataGenerator.getMapPrimaryKeys(), dataGenerator.getMapTables());
		xmlGenerator.run(compiledClasses, domain);
	}
}
