package br.com.process.integration.database.generator;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import br.com.process.integration.database.generator.handler.EntityGenerator;
import br.com.process.integration.database.generator.handler.EntityServiceGenerator;
import br.com.process.integration.database.generator.handler.RepositoryGenerator;
import br.com.process.integration.database.generator.handler.ViewGenerator;
import br.com.process.integration.database.generator.handler.ViewServiceGenerator;
import br.com.process.integration.database.generator.metadata.ClassResolver;

public class MainApplication {

	public static void main(String[] args) {

		try {

			Set<String> tables = new LinkedHashSet<>(Arrays.asList("entity_eigtht", "entity_seven", "entity_six"));
			//Set<String> tables = new LinkedHashSet<>(Arrays.asList("entity_one", "entity_status", "entity_two", "entity_tree", "entity_four", "entity_five"));

			String domain = "example";
			String url = "jdbc:postgresql://localhost:5432/root-database";
			String user = "anderson";
			String pass = "admin";
			
			String packageEntity = "br.com.process.integration.database.model.entity.dto." + domain;
			EntityGenerator entityGenerator = new EntityGenerator(url, user, pass, packageEntity, tables);
			List<ClassResolver> classResolverList = entityGenerator.run();
			
			classResolverList.forEach(resolver -> {
				String packageRepository = "br.com.process.integration.database.model.entity.repository." + domain;
				String packageService = "br.com.process.integration.database.model.entity.service." + domain;
				try {
					RepositoryGenerator.generateRepositoryClass(resolver.getName(), packageEntity, packageRepository, resolver.getTypeId().toString());
					EntityServiceGenerator.generateServiceClass(resolver.getName(), packageEntity, packageService, resolver.getTypeId().toString());
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			});
			
			String packageView = "br.com.process.integration.database.model.view.dto." + domain;
			ViewGenerator viewGenerator = new ViewGenerator(url, user, pass, packageView, tables);
			String classView = viewGenerator.run();
			
			String packageService = "br.com.process.integration.database.model.view.service." + domain;
			ViewServiceGenerator.generateServiceClass(classView, packageView, packageService);

			System.out.println("Entidades geradas com sucesso!");

		} catch (Exception e) {
			System.err.println("Erro ao gerar entidades: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
