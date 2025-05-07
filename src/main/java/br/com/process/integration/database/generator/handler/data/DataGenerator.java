package br.com.process.integration.database.generator.handler.data;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.*;

import br.com.process.integration.database.generator.model.ColumnInfo;
import br.com.process.integration.database.generator.util.StringUtils;
import br.com.process.integration.database.generator.util.TypeMapper;

public class DataGenerator {

    private final String jdbcUrl;
    private final String username;
    private final String password;
    private final String packageName;
    private final Set<String> tables;

    public DataGenerator(String jdbcUrl, String username, String password, String packageName, Set<String> tables) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.packageName = packageName;
        this.tables = tables;
    }

    public List<String> run() throws SQLException, IOException {
        List<String> classNames = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
            DatabaseMetaData metaData = conn.getMetaData();
            Map<String, List<ColumnInfo>> tableColumnsMap = new LinkedHashMap<>();
            Map<String, Map<String, String>> foreignKeys = new HashMap<>();
            Map<String, List<String>> primaryKeysMap = new HashMap<>();

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

                Map<String, String> fks = new LinkedHashMap<>();
                ResultSet fkRs = metaData.getImportedKeys(null, null, table);
                while (fkRs.next()) {
                    String fkColumn = fkRs.getString("FKCOLUMN_NAME");
                    String pkTable = fkRs.getString("PKTABLE_NAME");
                    fks.put(fkColumn, pkTable);
                }
                foreignKeys.put(table, fks);

                List<String> pkCols = new ArrayList<>();
                ResultSet pkRs = metaData.getPrimaryKeys(null, null, table);
                while (pkRs.next()) {
                    pkCols.add(pkRs.getString("COLUMN_NAME"));
                }
                primaryKeysMap.put(table, pkCols);
            }

            for (String table : tables) {
                List<String> primaryKeys = primaryKeysMap.getOrDefault(table, List.of());
                if (primaryKeys.size() > 1) {
                    gerarClasseCompostaId(table, tableColumnsMap.get(table), primaryKeys);
                }
                String className = gerarClasseData(table, tableColumnsMap.get(table), foreignKeys.getOrDefault(table, Map.of()), primaryKeys);
                classNames.add(className);
            }
        }
        return classNames;
    }

    private void gerarClasseCompostaId(String tableName, List<ColumnInfo> columns, List<String> primaryKeys) throws IOException {
        String className = StringUtils.capitalize(StringUtils.camelCase(tableName)) + "Id";
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
            .addModifiers(Modifier.PUBLIC);

        for (String pk : primaryKeys) {
            ColumnInfo column = columns.stream().filter(c -> c.name.equalsIgnoreCase(pk)).findFirst().orElse(null);
            if (column == null) continue;

            String fieldName = StringUtils.camelCase(column.name);
            String javaTypeName = TypeMapper.toJavaType(column.sqlTypeName);
            ClassName typeClass = TypeMapper.resolveType(javaTypeName);

            FieldSpec.Builder fieldBuilder = FieldSpec.builder(typeClass, fieldName, Modifier.PRIVATE);
            fieldBuilder.addJavadoc("Coluna: $L\n", column.name);

            if (!column.nullable) {
                fieldBuilder.addAnnotation(ClassName.get("jakarta.validation.constraints", "NotNull"));
            }

            classBuilder.addField(fieldBuilder.build());

            classBuilder.addMethod(MethodSpec.methodBuilder("get" + StringUtils.capitalize(fieldName))
                .addModifiers(Modifier.PUBLIC).returns(typeClass)
                .addStatement("return this.$N", fieldName).build());

            classBuilder.addMethod(MethodSpec.methodBuilder("set" + StringUtils.capitalize(fieldName))
                .addModifiers(Modifier.PUBLIC).addParameter(typeClass, fieldName)
                .addStatement("this.$N = $N", fieldName, fieldName).build());
        }

        JavaFile javaFile = JavaFile.builder(packageName, classBuilder.build())
            .skipJavaLangImports(true).indent("    ").build();

        javaFile.writeTo(Paths.get("src/main/java"));
    }

    private String gerarClasseData(String tableName, List<ColumnInfo> columns, Map<String, String> foreignKeys, List<String> primaryKeys) throws IOException {
    	String className = StringUtils.capitalize(StringUtils.camelCase(tableName)) + "Data";

        ClassName representationModel = ClassName.get("org.springframework.hateoas", "RepresentationModel");
        ClassName beanData = ClassName.get("br.com.process.integration.database.core.domain", "BeanData");
        ClassName jsonFormat = ClassName.get("com.fasterxml.jackson.annotation", "JsonFormat");
        ClassName constants = ClassName.get("br.com.process.integration.database.core.util", "Constants");
        ClassName jsonIgnore = ClassName.get("com.fasterxml.jackson.annotation", "JsonIgnore");
        ClassName jsonIgnoreProperties = ClassName.get("com.fasterxml.jackson.annotation", "JsonIgnoreProperties");

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
            .addModifiers(Modifier.PUBLIC)
            .superclass(ParameterizedTypeName.get(representationModel, ClassName.bestGuess(className)))
            .addSuperinterface(ParameterizedTypeName.get(beanData, ClassName.bestGuess(className)))
            .addAnnotation(AnnotationSpec.builder(jsonIgnoreProperties).addMember("ignoreUnknown", "$L", true).build());

        Set<String> fieldNames = new LinkedHashSet<>();
        Set<String> usedFieldNames = new HashSet<>();
        Set<String> handledForeignTables = new HashSet<>();
        
        if (primaryKeys.size() > 1) {
            String idClassName = StringUtils.capitalize(StringUtils.camelCase(tableName)) + "Id";
            ClassName idClass = ClassName.get(packageName, idClassName);
            classBuilder.addField(FieldSpec.builder(idClass, "id", Modifier.PRIVATE).build());

            classBuilder.addMethod(MethodSpec.methodBuilder("getId")
                .addModifiers(Modifier.PUBLIC).returns(idClass)
                .addStatement("return this.id").build());

            classBuilder.addMethod(MethodSpec.methodBuilder("setId")
                .addModifiers(Modifier.PUBLIC).addParameter(idClass, "id")
                .addStatement("this.id = id").build());

            fieldNames.add("id");
            usedFieldNames.add("id");
        }

        for (ColumnInfo column : columns) {
        	
            if (primaryKeys.size() > 1 && primaryKeys.contains(column.name)) {
                continue;
            }
            if (foreignKeys.containsKey(column.name)) {
                continue;
            }

            String fieldName = StringUtils.camelCase(column.name);
            String javaTypeName = TypeMapper.toJavaType(column.sqlTypeName);
            ClassName typeClass = TypeMapper.resolveType(javaTypeName);

            FieldSpec.Builder fieldBuilder = FieldSpec.builder(typeClass, fieldName, Modifier.PRIVATE);

            // Comentário Javadoc com o nome original da coluna
            fieldBuilder.addJavadoc("Coluna: $L\n", column.name);

            // @NotNull se não for anulável
            if (!column.nullable) {
                fieldBuilder.addAnnotation(ClassName.get("jakarta.validation.constraints", "NotNull"));
            }

            // @Size(max = ...) para VARCHAR, CHAR
            if ((column.sqlTypeName.equalsIgnoreCase("VARCHAR") || column.sqlTypeName.equalsIgnoreCase("CHAR")) && column.size > 0) {
                fieldBuilder.addAnnotation(AnnotationSpec.builder(ClassName.get("jakarta.validation.constraints", "Size"))
                    .addMember("max", "$L", column.size).build());
            }

            // @JsonFormat para datas
            if (javaTypeName.contains("LocalDate")) {
                String formatConstant = javaTypeName.endsWith("Time") ? "DATE_TIME_FORMAT" : "DATE_FORMAT";
                fieldBuilder.addAnnotation(AnnotationSpec.builder(jsonFormat)
                    .addMember("shape", "$T.STRING", jsonFormat.nestedClass("Shape"))
                    .addMember("pattern", "$T.$L", constants, formatConstant).build());
            }

            classBuilder.addField(fieldBuilder.build());

            classBuilder.addMethod(MethodSpec.methodBuilder("get" + StringUtils.capitalize(fieldName))
                .addModifiers(Modifier.PUBLIC).returns(typeClass)
                .addStatement("return this.$N", fieldName).build());

            classBuilder.addMethod(MethodSpec.methodBuilder("set" + StringUtils.capitalize(fieldName))
                .addModifiers(Modifier.PUBLIC).addParameter(typeClass, fieldName)
                .addStatement("this.$N = $N", fieldName, fieldName).build());

            fieldNames.add(fieldName);
            usedFieldNames.add(fieldName);
        }

        for (Map.Entry<String, String> entry : foreignKeys.entrySet()) {
            String referencedTable = entry.getValue();
            if (!handledForeignTables.add(referencedTable)) {
                continue;
            }

            String baseFieldName = StringUtils.camelCase(referencedTable) + "Data";
            String uniqueFieldName = baseFieldName;
            int suffix = 1;
            while (usedFieldNames.contains(uniqueFieldName)) {
                uniqueFieldName = baseFieldName + suffix++;
            }

            String classType = StringUtils.capitalize(StringUtils.camelCase(referencedTable)) + "Data";
            ClassName fkClass = ClassName.get(packageName, classType);

            classBuilder.addField(FieldSpec.builder(fkClass, uniqueFieldName, Modifier.PRIVATE).build());

            classBuilder.addMethod(MethodSpec.methodBuilder("get" + StringUtils.capitalize(uniqueFieldName))
                .addModifiers(Modifier.PUBLIC).returns(fkClass)
                .addStatement("return this.$N", uniqueFieldName).build());

            classBuilder.addMethod(MethodSpec.methodBuilder("set" + StringUtils.capitalize(uniqueFieldName))
                .addModifiers(Modifier.PUBLIC).addParameter(fkClass, uniqueFieldName)
                .addStatement("this.$N = $N", uniqueFieldName, uniqueFieldName).build());

            fieldNames.add(uniqueFieldName);
            usedFieldNames.add(uniqueFieldName);
        }

        classBuilder.addMethod(MethodSpec.methodBuilder("getData")
            .addAnnotation(Override.class)
            .addAnnotation(jsonIgnore)
            .addModifiers(Modifier.PUBLIC)
            .returns(ClassName.bestGuess(className))
            .addStatement("return this").build());

        CodeBlock.Builder equalsBlock = CodeBlock.builder()
            .add("if (this == o) return true;\n")
            .add("if (o == null || getClass() != o.getClass()) return false;\n")
            .add("$L other = ($L) o;\n", className, className)
            .add("return ");

        int index = 0;
        for (String field : fieldNames) {
            equalsBlock.add("java.util.Objects.equals($L, other.$L)", field, field);
            if (++index < fieldNames.size()) equalsBlock.add(" &&\n");
        }
        equalsBlock.add(";");

        classBuilder.addMethod(MethodSpec.methodBuilder("equals")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(boolean.class)
            .addParameter(Object.class, "o")
            .addCode(equalsBlock.build()).build());

        CodeBlock hashBlock = CodeBlock.of("return java.util.Objects.hash($L);", String.join(", ", fieldNames));
        classBuilder.addMethod(MethodSpec.methodBuilder("hashCode")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(int.class)
            .addCode(hashBlock).build());

        CodeBlock.Builder toStringBlock = CodeBlock.builder()
            .add("return \"$L{\" +\n", className);

        int count = 0;
        for (String field : fieldNames) {
            toStringBlock.add("\"$L=\" + ($L != null ? $L.toString() : \"null\")", field, field, field);
            if (++count < fieldNames.size()) toStringBlock.add(" + \", \" +\n");
        }
        toStringBlock.add(" + '}';\n");

        classBuilder.addMethod(MethodSpec.methodBuilder("toString")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(String.class)
            .addCode(toStringBlock.build()).build());

        JavaFile javaFile = JavaFile.builder(packageName, classBuilder.build())
            .skipJavaLangImports(true).indent("    ").build();

        javaFile.writeTo(Paths.get("src/main/java"));

        return className;
    }
} 
