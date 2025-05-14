package br.com.process.integration.database.generator.handler.data;

import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import br.com.process.integration.database.core.domain.DataMapper;

/**
 * 
 */
public class DataMapperGenerator {

	private final String packageName;
	private final String packageDto;
	private final Set<String> tables;

	/**
	 * @param packageName
	 * @param packageDto
	 * @param tables
	 */
	public DataMapperGenerator(String packageName, String packageDto, Set<String> tables) {
		this.packageName = packageName;
		this.packageDto = packageDto;
		this.tables = tables;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public List<String> run() throws Exception {
		
		List<String> classNames = new ArrayList<>();

		for (String table : tables) {
			String className = generateMapperClass(table);
			classNames.add(className);
		}

		return classNames;
	}

	/**
	 * @param table
	 * @return
	 * @throws Exception
	 */
	private String generateMapperClass(String table) throws Exception {
		String dataClassName = capitalizeCamel(table) + "Data";
		String mapperClassName = dataClassName + Mapper.class.getSimpleName();

		ClassName mapperAnnotation = ClassName.get(Mapper.class.getPackageName(), Mapper.class.getSimpleName());
		ClassName dataMapper = ClassName.get(DataMapper.class.getPackageName(), DataMapper.class.getSimpleName());
		ClassName dataClass = ClassName.get(packageDto, dataClassName);

		TypeSpec.Builder interfaceBuilder = TypeSpec.interfaceBuilder(mapperClassName)
				.addModifiers(Modifier.PUBLIC)
				.addAnnotation(mapperAnnotation)
				.addSuperinterface(ParameterizedTypeName.get(dataMapper, dataClass));

		// findAll
		interfaceBuilder.addMethod(MethodSpec.methodBuilder("findAll")
				.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
				.returns(ParameterizedTypeName.get(ClassName.get(List.class), dataClass))
				.build());

		// findById
		try {
			Class<?> dtoClass = Class.forName(packageDto + "." + dataClassName);
			Field idField = dtoClass.getDeclaredFields()[0];
			Class<?> idType = idField.getType();

			ParameterSpec param = ParameterSpec.builder(idType, idField.getName())
				    .addAnnotation(AnnotationSpec.builder(ClassName.get(Param.class.getPackageName(), Param.class.getSimpleName()))
				        .addMember("value", "$S", idField.getName())
				        .build())
				    .build();
			
			interfaceBuilder.addMethod(MethodSpec.methodBuilder("findById")
					.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
					.addParameter(param)
					.returns(dataClass)
					.build());
			
		} catch (Exception e) {
			throw new RuntimeException("Erro ao identificar o tipo da propriedade 'id' da classe " + dataClassName, e);
		}

		JavaFile javaFile = JavaFile.builder(packageName, interfaceBuilder.build())
				.skipJavaLangImports(true)
				.indent("    ")
				.build();

		javaFile.writeTo(Paths.get("src/main/java"));
		
		return mapperClassName;
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