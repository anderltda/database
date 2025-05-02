package br.com.process.integration.database.generator.handler.entity;

import java.io.IOException;
import java.nio.file.Paths;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import br.com.process.integration.database.generator.util.TypeMapper;

public class EntityRepositoryGenerator {

	public static void generateRepositoryClass(String entityClassName, String packageEntity, String packageRepository, String idJavaType) throws IOException {

		String repositoryName = entityClassName + "Repository";

		ClassName repositoryAnnotation = ClassName.get("org.springframework.stereotype", "Repository");
		ClassName jpaRepository = ClassName.get("org.springframework.data.jpa.repository", "JpaRepository");
		ClassName jpaSpecExecutor = ClassName.get("org.springframework.data.jpa.repository", "JpaSpecificationExecutor");
		ClassName entityClass = ClassName.get(packageEntity, entityClassName);

		ClassName idClass = null;

		if (idJavaType.contains(".")) {
			int lastDot = idJavaType.lastIndexOf('.');
			String idClassPackage = idJavaType.substring(0, lastDot);
			String idClassName = idJavaType.substring(lastDot + 1);
			idClass = ClassName.get(idClassPackage, idClassName);
		} else {
			idClass = TypeMapper.resolveType(idJavaType);
		}

		TypeSpec repositoryInterface = TypeSpec.interfaceBuilder(repositoryName).addModifiers(Modifier.PUBLIC)
				.addAnnotation(repositoryAnnotation)
				.addSuperinterface(ParameterizedTypeName.get(jpaRepository, entityClass, idClass))
				.addSuperinterface(ParameterizedTypeName.get(jpaSpecExecutor, entityClass)).build();

		JavaFile javaFile = JavaFile.builder(packageRepository, repositoryInterface).skipJavaLangImports(true)
				.indent("    ").build();

		javaFile.writeTo(Paths.get("src/main/java"));
	}
}