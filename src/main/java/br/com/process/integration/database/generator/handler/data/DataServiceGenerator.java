package br.com.process.integration.database.generator.handler.data;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;

import org.apache.ibatis.annotations.Mapper;
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

import br.com.process.integration.database.core.application.AbstractDataService;
import br.com.process.integration.database.core.ui.QueryMyBatisController;

/**
 * 
 */
public class DataServiceGenerator {

	private final String packageName;
	private final String packageDto;
	private final String packageMapper;
	private final Set<String> tables;

	/**
	 * @param packageName
	 * @param packageDto
	 * @param packageMapper
	 * @param tables
	 */
	public DataServiceGenerator(String packageName, String packageDto, String packageMapper, Set<String> tables) {
		this.packageName = packageName;
		this.packageDto = packageDto;
		this.packageMapper = packageMapper;
		this.tables = tables;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public List<String> run() throws Exception {
		
		List<String> classNames = new ArrayList<>();

		for (String table : tables) {
			String className = generateServiceClass(table);
			classNames.add(className);
		}

		return classNames;
	}

	/**
	 * @param table
	 * @return
	 * @throws Exception
	 */
	private String generateServiceClass(String table) throws Exception {
		
		String baseName = capitalizeCamel(table);
		String dataClassName = baseName + "Data";
		String mapperClassName = dataClassName + Mapper.class.getSimpleName();
		String serviceClassName = dataClassName + Service.class.getSimpleName();

		ClassName serviceAnnotation = ClassName.get(Service.class.getPackageName(), Service.class.getSimpleName());
		ClassName transactional = ClassName.get(Transactional.class.getPackageName(), Transactional.class.getSimpleName());
		ClassName autowired = ClassName.get(Autowired.class.getPackageName(), Autowired.class.getSimpleName());
		ClassName beanUtils = ClassName.get(BeanUtils.class.getPackageName(), BeanUtils.class.getSimpleName());
		ClassName assemblerType = ClassName.get(PagedResourcesAssembler.class.getPackageName(), PagedResourcesAssembler.class.getSimpleName());
		ClassName controllerType = ClassName.get(QueryMyBatisController.class.getPackageName(), QueryMyBatisController.class.getSimpleName());

		ClassName dtoClass = ClassName.get(packageDto, dataClassName);
		ClassName mapperClass = ClassName.get(packageMapper, mapperClassName);
		ClassName abstractService = ClassName.get(AbstractDataService.class.getPackageName(), AbstractDataService.class.getSimpleName());

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

	/**
	 * @param table
	 * @return
	 */
	private String capitalizeCamel(String table) {
		
		String[] parts = table.toLowerCase().split("_");
		
		StringBuilder sb = new StringBuilder();
		
		for (String part : parts) {
			sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
		}
		
		return sb.toString();
	}
}
