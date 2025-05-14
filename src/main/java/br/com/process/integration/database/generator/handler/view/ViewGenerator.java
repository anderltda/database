package br.com.process.integration.database.generator.handler.view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

import javax.lang.model.element.Modifier;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.*;

import br.com.process.integration.database.core.domain.BeanView;
import br.com.process.integration.database.core.util.Constants;
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
    private final String domain;

    /**
     * @param jdbcUrl
     * @param username
     * @param password
     * @param packageName
     */
    public ViewGenerator(String jdbcUrl, String username, String password, String packageName, String domain) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.packageName = packageName;
        this.domain = domain;
    }

    /**
     * @param rootTable
     * @return
     * @throws Exception
     */
    public String run(String rootTable) throws Exception {
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
            DatabaseMetaData metaData = conn.getMetaData();
            Map<String, List<ColumnInfo>> resolved = resolveRecursively(metaData, rootTable, new HashSet<>());
            return generateClass(rootTable, resolved);
        }
    }

    /**
     * @param metaData
     * @param table
     * @param visited
     * @return
     * @throws SQLException
     */
    private Map<String, List<ColumnInfo>> resolveRecursively(DatabaseMetaData metaData, String table, Set<String> visited) throws SQLException {
    	
        if (!visited.add(table)) return new LinkedHashMap<>();

        Map<String, List<ColumnInfo>> result = new LinkedHashMap<>();

        List<ColumnInfo> columns = new ArrayList<>();
        try (ResultSet rs = metaData.getColumns(null, null, table, null)) {
            while (rs.next()) {
                columns.add(new ColumnInfo(
                    rs.getString("COLUMN_NAME"),
                    rs.getInt("DATA_TYPE"),
                    rs.getString("TYPE_NAME"),
                    rs.getInt("COLUMN_SIZE"),
                    rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable,
                    rs.getInt("DECIMAL_DIGITS"),
                    false
                ));
            }
        }
        result.put(table, columns);

        try (ResultSet fks = metaData.getImportedKeys(null, null, table)) {
            while (fks.next()) {
                String pkTable = fks.getString("PKTABLE_NAME");
                result.putAll(resolveRecursively(metaData, pkTable, visited));
            }
        }

        return result;
    }


    /**
     * @param rootTable
     * @throws Exception
     */
    public void generateQueryDefinition(String rootTable) throws Exception {
    	try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
    		DatabaseMetaData metaData = conn.getMetaData();
    		Set<String> visited = new HashSet<>();
    		Map<String, List<ColumnInfo>> resolved = resolveRecursively(metaData, rootTable, visited);

    		List<String> joins = new ArrayList<>();
    		List<String> wheres = new ArrayList<>();
    		StringBuilder select = new StringBuilder("SELECT ");
    		String viewClassName = StringUtils.capitalize(StringUtils.camelCase(rootTable)) + "ViewService";
    		String rootAlias = "t1";
    		int aliasCounter = 1;

    		Map<String, String> tableAliasMap = new LinkedHashMap<>();
    		Map<String, String> columnToAliasMap = new HashMap<>();
    		tableAliasMap.put(rootTable, rootAlias);

    		List<String> aliasList = new ArrayList<>();
    		aliasList.add(rootAlias);

    		String from = " FROM " + rootTable + " " + rootAlias;

    		Queue<String> queue = new LinkedList<>();
    		queue.add(rootTable);

    		while (!queue.isEmpty()) {
    			String currentTable = queue.poll();
    			String currentAlias = tableAliasMap.get(currentTable);

    			try (ResultSet fks = metaData.getImportedKeys(null, null, currentTable)) {
    				while (fks.next()) {
    					String pkTable = fks.getString("PKTABLE_NAME");
    					String pkColumn = fks.getString("PKCOLUMN_NAME");
    					String fkColumn = fks.getString("FKCOLUMN_NAME");

    					String joinKey = currentTable + "." + fkColumn + "->" + pkTable + "." + pkColumn;
    					if (columnToAliasMap.containsKey(joinKey)) continue;

    					aliasCounter++;
    					String alias = "t" + aliasCounter;
    					tableAliasMap.put(pkTable, alias);
    					columnToAliasMap.put(joinKey, alias);
    					aliasList.add(alias);

    					joins.add("INNER JOIN " + pkTable + " " + alias + " ON " +
    						alias + "." + pkColumn + " = " + currentAlias + "." + fkColumn);

    					queue.add(pkTable);
    				}
    			}
    		}

    		// SELECT
    		for (int i = 0; i < aliasList.size(); i++) {
    			select.append(aliasList.get(i)).append(".*");
    			if (i < aliasList.size() - 1) select.append(", ");
    		}
    		select.append(from);

    		// WHERE
    		Set<String> whereKeys = new HashSet<>();
    		for (Map.Entry<String, List<ColumnInfo>> entry : resolved.entrySet()) {
    			String table = entry.getKey();
    			String alias = tableAliasMap.getOrDefault(table, "");

    			for (ColumnInfo col : entry.getValue()) {
    				String column = col.name;
    				String key = alias + "." + column;
    				if (whereKeys.add(key)) {
    					String camel = StringUtils.camelCase(column);
    					wheres.add("AND " + key + " {" + camel + "_op} :" + camel);
    				}
    			}
    		}

    		// Monta JSON
    		Map<String, Object> json = new LinkedHashMap<>();
    		json.put("name", "busca_" + rootTable + "_com_joins");
    		json.put("select", select.toString());
    		json.put("join", joins);
    		json.put("where", wheres);
    		json.put("orderby", ":sortList :sortOrders");

    		ObjectMapper mapper = new ObjectMapper();
    		File dir = new File("src/main/resources/querys/" + domain);
    		if (!dir.exists()) dir.mkdirs();

    		try (FileWriter writer = new FileWriter(dir.getPath() + "/" + viewClassName + ".json")) {
    			mapper.writerWithDefaultPrettyPrinter().writeValue(writer, json);
    		}
    	}
    }

    /**
     * @param rootTable
     * @param tables
     * @return
     * @throws IOException
     */
    private String generateClass(String rootTable, Map<String, List<ColumnInfo>> tables) throws IOException {

		String className = StringUtils.capitalize(StringUtils.camelCase(rootTable)) + "View";

		ClassName representationModel = ClassName.get(RepresentationModel.class.getPackageName(), RepresentationModel.class.getSimpleName());
		ClassName beanView = ClassName.get(BeanView.class.getPackageName(), BeanView.class.getSimpleName());
		ClassName jsonFormat = ClassName.get(JsonFormat.class.getPackageName(), JsonFormat.class.getSimpleName());
		ClassName constants = ClassName.get(Constants.class.getPackageName(), Constants.class.getSimpleName());
		ClassName jsonIgnore = ClassName.get(JsonIgnore.class.getPackageName(), JsonIgnore.class.getSimpleName());
		ClassName jsonIgnoreProperties = ClassName.get(JsonIgnoreProperties.class.getPackageName(), JsonIgnoreProperties.class.getSimpleName());

		TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC)
				.superclass(ParameterizedTypeName.get(representationModel, ClassName.bestGuess(className)))
				.addSuperinterface(ParameterizedTypeName.get(beanView, ClassName.bestGuess(className))).addAnnotation(
						AnnotationSpec.builder(jsonIgnoreProperties).addMember("ignoreUnknown", "$L", true).build());

		Set<String> addedFields = new HashSet<>();
		List<String> fieldNames = new ArrayList<>();

		for (Map.Entry<String, List<ColumnInfo>> entry : tables.entrySet()) {
			String table = entry.getKey();
			classBuilder.addJavadoc("\n// $L\n", StringUtils.capitalize(StringUtils.camelCase(table)));

			for (ColumnInfo column : entry.getValue()) {
				String fieldName = StringUtils.camelCase(column.name);
				if (!addedFields.add(fieldName))
					continue;

				String javaType = TypeMapper.toJavaType(column.sqlTypeName);
				ClassName typeClass = TypeMapper.resolveType(javaType);

				FieldSpec.Builder fieldBuilder = FieldSpec.builder(typeClass, fieldName, Modifier.PRIVATE)
						.addJavadoc("from $L\n", table);

				if (javaType.contains("LocalDate")) {
					String format = javaType.endsWith("Time") ? "DATE_TIME_FORMAT" : "DATE_FORMAT";
					fieldBuilder.addAnnotation(AnnotationSpec.builder(jsonFormat)
							.addMember("shape", "$T.STRING", jsonFormat.nestedClass("Shape"))
							.addMember("pattern", "$T.$L", constants, format).build());
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

		classBuilder.addMethod(MethodSpec.methodBuilder("getView").addAnnotation(jsonIgnore)
				.addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(ClassName.bestGuess(className))
				.addStatement("return this").build());

		CodeBlock.Builder toStringBuilder = CodeBlock.builder().add("return $S +\n", className + "{");

		for (int i = 0; i < fieldNames.size(); i++) {
			String f = fieldNames.get(i);
			toStringBuilder.add("$S + ($N != null ? $N.toString() : \"null\")", f + "=", f, f);
			if (i < fieldNames.size() - 1) {
				toStringBuilder.add(" + \", \" +\n");
			}
		}
		toStringBuilder.add(" + '}';");
		classBuilder.addMethod(MethodSpec.methodBuilder("toString").addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC).returns(String.class).addCode(toStringBuilder.build()).build());

		CodeBlock.Builder equalsBuilder = CodeBlock.builder().add("if (this == o) return true;\n")
				.add("if (o == null || getClass() != o.getClass()) return false;\n")
				.add("$L that = ($L) o;\n", className, className).add("return ");

		for (int i = 0; i < fieldNames.size(); i++) {
			String f = fieldNames.get(i);
			equalsBuilder.add("java.util.Objects.equals($L, that.$L)", f, f);
			if (i < fieldNames.size() - 1) {
				equalsBuilder.add(" &&\n");
			}
		}
		equalsBuilder.add(";");
		classBuilder.addMethod(
				MethodSpec.methodBuilder("equals").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
						.returns(boolean.class).addParameter(Object.class, "o").addCode(equalsBuilder.build()).build());

		CodeBlock hashBlock = CodeBlock.of("return java.util.Objects.hash($L);", String.join(", ", fieldNames));
		classBuilder.addMethod(MethodSpec.methodBuilder("hashCode").addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC).returns(int.class).addCode(hashBlock).build());

		JavaFile javaFile = JavaFile.builder(packageName, classBuilder.build()).skipJavaLangImports(true).indent("    ")
				.build();

		javaFile.writeTo(Paths.get("src/main/java"));
		return className;
	}
}
