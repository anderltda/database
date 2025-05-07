package br.com.process.integration.database.generator;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
public class MainGenerator {

	public static void main(String[] args) {

		Map<Integer, Set<String>> maps = new HashMap<>();
		maps.put(1, new LinkedHashSet<>(Arrays.asList("entity_six", "entity_seven", "entity_eight", "entity_nine")));
		maps.put(2, new LinkedHashSet<>(Arrays.asList("entity_status", "entity_five", "entity_four", "entity_tree", "entity_two", "entity_one")));

		maps.forEach((key, tables) -> {
			try {
				extractedInit(tables);
			} catch (Exception e) {
				System.err.println("Erro ao gerar entidades: " + e.getMessage());
				e.printStackTrace();
			}
		});
	}

	/**
	 * @param tables
	 * @throws Exception
	 */
	private static void extractedInit(Set<String> tables) throws Exception {

		String domain = "test";
		String url = "jdbc:postgresql://localhost:5432/root-database";
		String user = "anderson";
		String pass = "admin";

		extractedEntity(tables, domain, url, user, pass);
		
		System.out.println("Entitys geradas com sucesso!");
		
		extractedView(tables, domain, url, user, pass);
		
		System.out.println("Views geradas com sucesso!");

		extractedData(tables, domain, url, user, pass);
		
		System.out.println("Data geradas com sucesso!");

	}

	/**
	 * @param tables
	 * @param domain
	 * @param url
	 * @param user
	 * @param pass
	 * @throws Exception
	 */
	private static void extractedEntity(Set<String> tables, String domain, String url, String user, String pass) throws Exception {

		String packageEntity = "br.com.process.integration.database.model.entity.dto." + domain;

		EntityGenerator entityGenerator = new EntityGenerator(url, user, pass, packageEntity, tables);

		List<ClassResolver> classResolverList = entityGenerator.run();

		classResolverList.forEach(resolver -> {

			String packageRepository = "br.com.process.integration.database.model.entity.repository." + domain;

			String packageService = "br.com.process.integration.database.model.entity.service." + domain;

			try {

				EntityRepositoryGenerator.generateRepositoryClass(resolver.getName(), packageEntity, packageRepository, resolver.getTypeId().toString());

				EntityServiceGenerator.generateServiceClass(resolver.getName(), packageEntity, packageService, resolver.getTypeId().toString());

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
	}

	/**
	 * @param tables
	 * @param domain
	 * @param url
	 * @param user
	 * @param pass
	 * @throws Exception
	 */
	private static void extractedView(Set<String> tables, String domain, String url, String user, String pass) throws Exception {

		String packageView = "br.com.process.integration.database.model.view.dto." + domain;

		ViewGenerator viewGenerator = new ViewGenerator(url, user, pass, packageView, tables);

		String classView = viewGenerator.run();

		String packageService = "br.com.process.integration.database.model.view.service." + domain;

		ViewServiceGenerator.generateServiceClass(classView, packageView, packageService);
	}

	/**
	 * @param tables
	 * @param domain
	 * @param url
	 * @param user
	 * @param pass
	 * @throws Exception
	 */
	private static void extractedData(Set<String> tables, String domain, String url, String user, String pass) throws Exception {

		String packageData = "br.com.process.integration.database.model.data.dto." + domain;

		DataGenerator dataGenerator = new DataGenerator(url, user, pass, packageData, tables);

		List<String> classList = dataGenerator.run();

		List<String> qualifiedClassNames = classList.stream().map(clazz -> packageData + "." + clazz).toList();

		List<Class<?>> compiledClasses = JavaCompilerUtil.compileAndLoadClasses(qualifiedClassNames);

		String packageMapper = "br.com.process.integration.database.model.data.mapper." + domain;
		DataMapperGenerator dataMapperGenerator = new DataMapperGenerator(packageMapper, packageData, tables);
		dataMapperGenerator.run();

		String packageService = "br.com.process.integration.database.model.data.service." + domain;
		DataServiceGenerator dataServiceGenerator = new DataServiceGenerator(packageService, packageData, packageMapper, tables);
		dataServiceGenerator.run();

		XmlGenerator xmlGenerator = new XmlGenerator();
		xmlGenerator.run(compiledClasses, domain);
	}
}
