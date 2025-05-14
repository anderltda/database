package br.com.process.integration.database.generator.handler.entity;

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

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.generator.util.TypeMapper;

/**
 * 
 */
public class EntityServiceGenerator {

    /**
     * @param entityName
     * @param packageDto
     * @param packageService
     * @param idJavaType
     * @throws IOException
     */
    public static void generateServiceClass(String entityName, String packageDto, String packageService, String idJavaType) throws IOException {

        String serviceName = entityName + Service.class.getSimpleName();

        ClassName serviceAnnotation = ClassName.get(Service.class.getPackageName(), Service.class.getSimpleName());
        ClassName transactional = ClassName.get(Transactional.class.getPackageName(), Transactional.class.getSimpleName());
        ClassName autowired = ClassName.get(Autowired.class.getPackageName(), Autowired.class.getSimpleName());
        ClassName beanUtils = ClassName.get(BeanUtils.class.getPackageName(), BeanUtils.class.getSimpleName());
        ClassName assembler = ClassName.get(PagedResourcesAssembler.class.getPackageName(), PagedResourcesAssembler.class.getSimpleName());
        ClassName abstractService = ClassName.get(AbstractJpaService.class.getPackageName(), AbstractJpaService.class.getSimpleName());
        ClassName controllerClass = ClassName.get(QueryJpaController.class.getPackageName(), QueryJpaController.class.getSimpleName());

        ClassName entityClass = ClassName.get(packageDto, entityName);

        ClassName idClass = null;

		if (idJavaType.contains(".")) {
			int lastDot = idJavaType.lastIndexOf('.');
			String idClassPackage = idJavaType.substring(0, lastDot);
			String idClassName = idJavaType.substring(lastDot + 1);
			idClass = ClassName.get(idClassPackage, idClassName);
		} else {
			idClass = TypeMapper.resolveType(idJavaType);
		}

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(serviceName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(serviceAnnotation)
                .addAnnotation(transactional)
                .superclass(ParameterizedTypeName.get(abstractService, entityClass, entityClass, idClass));

        // Campo com @Autowired
        classBuilder.addField(FieldSpec.builder(ParameterizedTypeName.get(assembler, entityClass), "pagedResourcesAssembler", Modifier.PRIVATE)
                .addAnnotation(autowired)
                .build());

        // Construtor
        classBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addStatement("super($T.class, $T.class)", controllerClass, entityClass)
                .build());

        // Método toModel
        classBuilder.addMethod(MethodSpec.methodBuilder("toModel")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(entityClass)
                .addParameter(entityClass, "entity")
                .addStatement("$T model = new $T()", entityClass, entityClass)
                .addStatement("$T.copyProperties(entity, model)", beanUtils)
                .addStatement("return model")
                .build());

        // Método setId
        classBuilder.addMethod(MethodSpec.methodBuilder("setId")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(idClass, "id")
                .addStatement("this.id = id")
                .build());

        // Método setEntity
        classBuilder.addMethod(MethodSpec.methodBuilder("setEntity")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(entityClass, "entity")
                .addStatement("this.entity = entity")
                .build());

        // Método setPagedModel
        classBuilder.addMethod(MethodSpec.methodBuilder("setPagedModel")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("pagedModel = pagedResourcesAssembler.toModel(pages, this)")
                .build());

        JavaFile javaFile = JavaFile.builder(packageService, classBuilder.build())
                .skipJavaLangImports(true)
                .indent("    ")
                .build();

        javaFile.writeTo(Paths.get("src/main/java"));
    }
}