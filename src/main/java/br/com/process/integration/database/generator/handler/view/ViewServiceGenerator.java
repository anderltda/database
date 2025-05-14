package br.com.process.integration.database.generator.handler.view;

import java.io.IOException;
import java.nio.file.Paths;

import javax.lang.model.element.Modifier;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import br.com.process.integration.database.core.application.AbstractJdbcService;
import br.com.process.integration.database.core.ui.QueryNativeController;

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
		
		String serviceName = viewClassName + Service.class.getSimpleName();

		ClassName serviceAnnotation = ClassName.get(Service.class.getPackageName(), Service.class.getSimpleName());
		ClassName transactional = ClassName.get(Transactional.class.getPackageName(), Transactional.class.getSimpleName());
		ClassName autowired = ClassName.get(Autowired.class.getPackageName(), Autowired.class.getSimpleName());
		ClassName beanUtils = ClassName.get(BeanUtils.class.getPackageName(), BeanUtils.class.getSimpleName());
		ClassName assembler = ClassName.get(PagedResourcesAssembler.class.getPackageName(), PagedResourcesAssembler.class.getSimpleName());
		ClassName abstractService = ClassName.get(AbstractJdbcService.class.getPackageName(), AbstractJdbcService.class.getSimpleName());
		ClassName controllerClass = ClassName.get(QueryNativeController.class.getPackageName(), QueryNativeController.class.getSimpleName());
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
		classBuilder.addMethod(MethodSpec.methodBuilder("setView").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
						.addParameter(viewClass, "view").addStatement("this.view = view").build());

		// Método setPagedModel
		classBuilder.addMethod(MethodSpec.methodBuilder("setPagedModel").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
						.addStatement("pagedModel = pagedResourcesAssembler.toModel(pages, this)").build());

		JavaFile javaFile = JavaFile.builder(packageService, classBuilder.build()).skipJavaLangImports(true)
				.indent("    ").build();

		javaFile.writeTo(Paths.get("src/main/java"));
	}
}
