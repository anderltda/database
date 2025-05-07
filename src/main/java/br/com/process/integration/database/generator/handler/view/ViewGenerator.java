package br.com.process.integration.database.generator.handler.view;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import br.com.process.integration.database.generator.model.ColumnInfo;
import br.com.process.integration.database.generator.util.StringUtils;
import br.com.process.integration.database.generator.util.TypeMapper;

/**
 * 
 */
public class ViewGenerator {

	private final String jdbcUrl;
	private final String username;
	private final String password;
	private final String packageName;
	private final Set<String> tables;

	/**
	 * @param jdbcUrl
	 * @param username
	 * @param password
	 * @param packageName
	 * @param tabelasParaGerar
	 */
	public ViewGenerator(String jdbcUrl, String username, String password, String packageName, Set<String> tabelasParaGerar) {
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		this.password = password;
		this.packageName = packageName;
		this.tables = tabelasParaGerar;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public String run() throws Exception {
		try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
			DatabaseMetaData metaData = conn.getMetaData();
			Map<String, List<ColumnInfo>> tableColumnsMap = new LinkedHashMap<>();

			for (String table : tables) {
				List<ColumnInfo> columns = new ArrayList<>();
				ResultSet rs = metaData.getColumns(null, null, table, null);
				while (rs.next()) {
					columns.add(new ColumnInfo(
							rs.getString("COLUMN_NAME"), 
							rs.getInt("DATA_TYPE"),
							rs.getString("TYPE_NAME"), 
							rs.getInt("COLUMN_SIZE"),
							rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable, 
							rs.getInt("DECIMAL_DIGITS"),
						false));
				}
				tableColumnsMap.put(table, columns);
			}

			return gerarClasseView(tableColumnsMap);
		}
	}

	/**
	 * @param tabelas
	 * @return
	 * @throws IOException
	 */
	private String gerarClasseView(Map<String, List<ColumnInfo>> tabelas) throws IOException {
		
		String firstTable = tabelas.keySet().iterator().next();
		
		String className = StringUtils.capitalize(StringUtils.camelCase(firstTable)) + "View";

		ClassName representationModel = ClassName.get("org.springframework.hateoas", "RepresentationModel");
		ClassName beanView = ClassName.get("br.com.process.integration.database.core.domain", "BeanView");
		ClassName jsonFormat = ClassName.get("com.fasterxml.jackson.annotation", "JsonFormat");
		ClassName constants = ClassName.get("br.com.process.integration.database.core.util", "Constants");
		ClassName jsonIgnore = ClassName.get("com.fasterxml.jackson.annotation", "JsonIgnore");
		ClassName jsonIgnoreProperties = ClassName.get("com.fasterxml.jackson.annotation", "JsonIgnoreProperties");

		TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC)
				.superclass(ParameterizedTypeName.get(representationModel, ClassName.bestGuess(className)))
				.addSuperinterface(ParameterizedTypeName.get(beanView, ClassName.bestGuess(className))).addAnnotation(
					AnnotationSpec.builder(jsonIgnoreProperties).addMember("ignoreUnknown", "$L", true).build());

		Set<String> camposAdicionados = new HashSet<>();
		List<String> fieldNames = new ArrayList<>();

		for (Map.Entry<String, List<ColumnInfo>> entry : tabelas.entrySet()) {
			for (ColumnInfo column : entry.getValue()) {
				String fieldName = StringUtils.camelCase("id_".equals(column.name) ? entry.getKey() : column.name);
				if (!camposAdicionados.add(fieldName))
					continue;

				String javaTypeName = TypeMapper.toJavaType(column.sqlTypeName);
				ClassName typeClass = TypeMapper.resolveType(javaTypeName);

				FieldSpec.Builder fieldBuilder = FieldSpec.builder(typeClass, fieldName, Modifier.PRIVATE);

				if (javaTypeName.contains("LocalDate")) {
					String formatConstant = javaTypeName.endsWith("Time") ? "DATE_TIME_FORMAT" : "DATE_FORMAT";
					fieldBuilder.addAnnotation(AnnotationSpec.builder(jsonFormat)
							.addMember("shape", "$T.STRING", jsonFormat.nestedClass("Shape"))
							.addMember("pattern", "$T.$L", constants, formatConstant).build());
				}

				classBuilder.addField(fieldBuilder.build());

				classBuilder.addMethod(MethodSpec.methodBuilder("get" + StringUtils.capitalize(fieldName))
						.addModifiers(Modifier.PUBLIC).returns(typeClass).addStatement("return this.$N", fieldName)
						.build());

				classBuilder.addMethod(MethodSpec.methodBuilder("set" + StringUtils.capitalize(fieldName))
						.addModifiers(Modifier.PUBLIC).addParameter(typeClass, fieldName)
						.addStatement("this.$N = $N", fieldName, fieldName).build());

				fieldNames.add(fieldName);
			}
		}

		// getView
		classBuilder.addMethod(MethodSpec.methodBuilder("getView").addAnnotation(jsonIgnore)
				.addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(ClassName.bestGuess(className))
				.addStatement("return this").build());

		// toString
		CodeBlock.Builder toStringBuilder = CodeBlock.builder().add("return \"$L{\" +\n", className);
		for (int i = 0; i < fieldNames.size(); i++) {
			String field = fieldNames.get(i);
			String label = field.startsWith("id") && field.length() > 2 && Character.isUpperCase(field.charAt(2))
					? field.substring(2) : field;
			toStringBuilder.add("\"$L=\" + ($L != null ? $L.toString() : \"null\")", label, field, field);
			if (i < fieldNames.size() - 1)
				toStringBuilder.add(" + \", \" +\n");
		}
		toStringBuilder.add(" + '}';");
		classBuilder.addMethod(MethodSpec.methodBuilder("toString").addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC).returns(String.class).addCode(toStringBuilder.build()).build());

		// equals
		CodeBlock.Builder equalsBlock = CodeBlock.builder().add("if (this == o) return true;\n")
				.add("if (o == null || getClass() != o.getClass()) return false;\n")
				.add("$L that = ($L) o;\n", className, className).add("return ");

		for (int i = 0; i < fieldNames.size(); i++) {
			String field = fieldNames.get(i);
			equalsBlock.add("java.util.Objects.equals($L, that.$L)", field, field);
			if (i < fieldNames.size() - 1)
				equalsBlock.add(" &&\n");
		}
		equalsBlock.add(";");
		classBuilder.addMethod(
				MethodSpec.methodBuilder("equals").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
						.returns(boolean.class).addParameter(Object.class, "o").addCode(equalsBlock.build()).build());

		// hashCode
		CodeBlock hashBlock = CodeBlock.of("return java.util.Objects.hash($L);", String.join(", ", fieldNames));
		classBuilder.addMethod(MethodSpec.methodBuilder("hashCode").addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC).returns(int.class).addCode(hashBlock).build());

		JavaFile javaFile = JavaFile.builder(packageName, classBuilder.build()).skipJavaLangImports(true)
				.indent("    ").build();

		javaFile.writeTo(Paths.get("src/main/java"));
		
		return className;
	}
}
