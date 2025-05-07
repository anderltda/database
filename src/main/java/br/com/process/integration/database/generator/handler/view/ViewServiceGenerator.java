package br.com.process.integration.database.generator.handler.view;

import java.io.IOException;
import java.nio.file.Paths;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

/**
 * 
 */
public class ViewServiceGenerator {

	/**
	 * @param viewClassName
	 * @param packageView
	 * @param packageService
	 * @throws IOException
	 */
	public static void generateServiceClass(String viewClassName, String packageView, String packageService) throws IOException {
		
		String serviceName = viewClassName + "Service";

		ClassName serviceAnnotation = ClassName.get("org.springframework.stereotype", "Service");
		ClassName transactional = ClassName.get("org.springframework.transaction.annotation", "Transactional");
		ClassName autowired = ClassName.get("org.springframework.beans.factory.annotation", "Autowired");
		ClassName beanUtils = ClassName.get("org.springframework.beans", "BeanUtils");
		ClassName assembler = ClassName.get("org.springframework.data.web", "PagedResourcesAssembler");
		ClassName abstractService = ClassName.get("br.com.process.integration.database.core.application", "AbstractJdbcService");
		ClassName controllerClass = ClassName.get("br.com.process.integration.database.core.ui", "QueryNativeController");
		ClassName viewClass = ClassName.get(packageView, viewClassName);

		TypeSpec.Builder classBuilder = TypeSpec.classBuilder(serviceName).addModifiers(Modifier.PUBLIC)
				.addAnnotation(serviceAnnotation).addAnnotation(transactional)
				.superclass(ParameterizedTypeName.get(abstractService, viewClass));

		FieldSpec pagedAssemblerField = FieldSpec
				.builder(ParameterizedTypeName.get(assembler, viewClass), "pagedResourcesAssembler", Modifier.PRIVATE)
				.addAnnotation(autowired)
				.build();

		classBuilder.addField(pagedAssemblerField);

		// Construtor
		classBuilder.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PROTECTED)
				.addStatement("super($T.class, $T.class)", controllerClass, viewClass).build());

		// Método toModel
		classBuilder.addMethod(MethodSpec.methodBuilder("toModel").addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC).returns(viewClass).addParameter(viewClass, "view")
				.addStatement("$T model = new $T()", viewClass, viewClass)
				.addStatement("$T.copyProperties(view, model)", beanUtils).addStatement("return model").build());

		// Método setView
		classBuilder.addMethod(
				MethodSpec.methodBuilder("setView").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
						.addParameter(viewClass, "view").addStatement("this.view = view").build());

		// Método setPagedModel
		classBuilder.addMethod(
				MethodSpec.methodBuilder("setPagedModel").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
						.addStatement("pagedModel = pagedResourcesAssembler.toModel(pages, this)").build());

		JavaFile javaFile = JavaFile.builder(packageService, classBuilder.build()).skipJavaLangImports(true)
				.indent("    ").build();

		javaFile.writeTo(Paths.get("src/main/java"));
	}
}
