package br.com.process.integration.database.generator.handler.data;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

public class DataServiceGenerator {

	private final String packageName;
	private final String packageDto;
	private final String packageMapper;
	private final Set<String> tables;

	public DataServiceGenerator(String packageName, String packageDto, String packageMapper, Set<String> tables) {
		this.packageName = packageName;
		this.packageDto = packageDto;
		this.packageMapper = packageMapper;
		this.tables = tables;
	}

	public List<String> run() throws IOException {
		List<String> classNames = new ArrayList<>();

		for (String table : tables) {
			String className = generateServiceClass(table);
			classNames.add(className);
		}

		return classNames;
	}

	private String generateServiceClass(String table) throws IOException {
		String baseName = capitalizeCamel(table);
		String dataClassName = baseName + "Data";
		String mapperClassName = dataClassName + "Mapper";
		String serviceClassName = dataClassName + "Service";

		ClassName serviceAnnotation = ClassName.get("org.springframework.stereotype", "Service");
		ClassName transactional = ClassName.get("org.springframework.transaction.annotation", "Transactional");
		ClassName autowired = ClassName.get("org.springframework.beans.factory.annotation", "Autowired");
		ClassName beanUtils = ClassName.get("org.springframework.beans", "BeanUtils");
		ClassName assemblerType = ClassName.get("org.springframework.data.web", "PagedResourcesAssembler");
		ClassName controllerType = ClassName.get("br.com.process.integration.database.core.ui", "QueryMyBatisController");

		ClassName dtoClass = ClassName.get(packageDto, dataClassName);
		ClassName mapperClass = ClassName.get(packageMapper, mapperClassName);
		ClassName abstractService = ClassName.get("br.com.process.integration.database.core.application", "AbstractDataService");

		ParameterizedTypeName superClass = ParameterizedTypeName.get(abstractService, dtoClass);

		FieldSpec assemblerField = FieldSpec
				.builder(ParameterizedTypeName.get(assemblerType, dtoClass), "pagedResourcesAssembler")
				.addAnnotation(autowired).addModifiers(Modifier.PRIVATE).build();

		MethodSpec constructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
				.addParameter(mapperClass, "mapper").addStatement("super($T.class, $T.class)", controllerType, dtoClass)
				.addStatement("this.mapper = mapper").build();

		MethodSpec toModelMethod = MethodSpec.methodBuilder("toModel").addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC).returns(dtoClass).addParameter(dtoClass, "data")
				.addStatement("$T model = new $T()", dtoClass, dtoClass)
				.addStatement("$T.copyProperties(data, model)", beanUtils).addStatement("return model").build();

		MethodSpec setDataMethod = MethodSpec.methodBuilder("setData").addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC).addParameter(dtoClass, "data").addStatement("this.data = data").build();

		MethodSpec setPagedModelMethod = MethodSpec.methodBuilder("setPagedModel").addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC).addStatement("pagedModel = pagedResourcesAssembler.toModel(pages, this)")
				.build();

		TypeSpec serviceClass = TypeSpec.classBuilder(serviceClassName).addModifiers(Modifier.PUBLIC)
				.superclass(superClass).addAnnotation(serviceAnnotation).addAnnotation(transactional)
				.addField(assemblerField).addMethod(constructor).addMethod(toModelMethod).addMethod(setDataMethod)
				.addMethod(setPagedModelMethod).build();

		JavaFile javaFile = JavaFile.builder(packageName, serviceClass).skipJavaLangImports(true).indent("    ")
				.build();

		javaFile.writeTo(Paths.get("src/main/java"));
		return serviceClassName;
	}

	private String capitalizeCamel(String table) {
		String[] parts = table.toLowerCase().split("_");
		StringBuilder sb = new StringBuilder();
		for (String part : parts) {
			sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
		}
		return sb.toString();
	}
}
