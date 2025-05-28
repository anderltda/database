package br.com.process.integration.database.generator.handler.entity;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.element.Modifier;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import br.com.process.integration.database.core.Constants;
import br.com.process.integration.database.core.domain.BeanEntity;
import br.com.process.integration.database.generator.metadata.ClassResolver;
import br.com.process.integration.database.generator.metadata.ForeignKeyResolver;
import br.com.process.integration.database.generator.model.ColumnInfo;
import br.com.process.integration.database.generator.util.StringUtils;
import br.com.process.integration.database.generator.util.TypeMapper;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * 
 */
public class EntityGenerator {

	private final String jdbcUrl;
	private final String username;
	private final String password;
	private final String packageName;
	private boolean isUpperCase;
	private Set<String> tables;

	/**
	 * @param jdbcUrl
	 * @param username
	 * @param password
	 * @param tables
	 */
	public EntityGenerator(String jdbcUrl, String username, String password, String packageName, Set<String> tables) {
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		this.password = password;
		this.packageName = packageName;
		this.isUpperCase = false;
		this.tables = tables.stream().map(String::toLowerCase).collect(Collectors.toSet());
	}
	
	/**
	 * @param jdbcUrl
	 * @param username
	 * @param password
	 * @param packageName
	 */
	public EntityGenerator(String jdbcUrl, String username, String password, String packageName) {
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		this.password = password;
		this.packageName = packageName;
	}

	/**
	 * @param tables
	 */
	public void setTables(Set<String> tables) {
		this.isUpperCase = true;
		this.tables = tables.stream().map(String::toUpperCase).collect(Collectors.toSet());
	}

	/**
	 * @throws Exception
	 */
	public List<ClassResolver> run() throws Exception {

		List<ClassResolver> classResolverList = new ArrayList<>();

		try {

			Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

			DatabaseMetaData metaData = connection.getMetaData();
			Map<String, Map<String, String>> foreignKeys = ForeignKeyResolver.resolve(metaData, tables);

			ResultSet resultSetTables = metaData.getTables(null, null, "%", new String[] { "TABLE" });

			while (resultSetTables.next()) {
				String tableName = isUpperCase ? resultSetTables.getString("TABLE_NAME").toUpperCase() : resultSetTables.getString("TABLE_NAME").toLowerCase();
				if (tables.isEmpty() || tables.contains(tableName)) {
					ClassResolver classResolver = gerarClasseEntity(metaData, packageName, tableName, foreignKeys);
					classResolverList.add(classResolver);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}

		return classResolverList;
	}

	/**
	 * @param metaData
	 * @param packageName
	 * @param tableName
	 * @param foreignKeys
	 * @return
	 * @throws Exception
	 */
	private ClassResolver gerarClasseEntity(DatabaseMetaData metaData, String packageName, String tableName, Map<String, Map<String, String>> foreignKeys) throws Exception {

		String className = StringUtils.capitalize(StringUtils.camelCase(tableName));

		List<ColumnInfo> columns = new ArrayList<>();

		Set<String> primaryKeys = new HashSet<>();

		Map<String, Map<String, List<String>>> compositeForeignKeys = ForeignKeyResolver.resolveCompositeForeignKeys(metaData, tableName);

		ResultSet pk = metaData.getPrimaryKeys(null, null, tableName);

		while (pk.next()) {
			primaryKeys.add(pk.getString("COLUMN_NAME"));
		}

		ResultSet rs = metaData.getColumns(null, null, tableName, null);
		while (rs.next()) {
			String columnName = rs.getString("COLUMN_NAME");
			boolean isUnique = isColumnUnique(metaData, tableName, columnName);
			columns.add(new ColumnInfo(
					columnName, 
					rs.getInt("DATA_TYPE"), 
					rs.getString("TYPE_NAME"),
					rs.getInt("COLUMN_SIZE"), 
					rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable,
					rs.getInt("DECIMAL_DIGITS"), isUnique));
		}

		boolean isCompositeKey = primaryKeys.size() > 1;
		
		if (isCompositeKey) {
			gerarClasseEmbeddableId(packageName, className + "Id", columns, primaryKeys);
		}

		// Detecta índices únicos compostos
		Map<String, Set<String>> compositeUniqueIndexes = getCompositeUniqueIndexes(metaData, tableName);

		// Cria a annotation @Table dinamicamente
		AnnotationSpec.Builder tableAnnotation = AnnotationSpec.builder(Table.class).addMember("name", "$S", tableName);

		if (!compositeUniqueIndexes.isEmpty()) {
			for (Set<String> columnsSet : compositeUniqueIndexes.values()) {
				if (columnsSet.size() > 1) {
					String columnsFormatted = columnsSet.stream().map(c -> "\"" + c + "\"").collect(Collectors.joining(", "));
					tableAnnotation.addMember("uniqueConstraints", "@$T(columnNames = {$L})", ClassName.get(UniqueConstraint.class.getPackageName(), UniqueConstraint.class.getSimpleName()), columnsFormatted);
				}
			}
		}

		// Importa classes necessárias
		ClassName jsonIgnoreProperties = ClassName.get(JsonIgnoreProperties.class.getPackageName(), JsonIgnoreProperties.class.getSimpleName());
		ClassName representationModel = ClassName.get(RepresentationModel.class.getPackageName(), RepresentationModel.class.getSimpleName());
		ClassName beanEntity = ClassName.get(BeanEntity.class.getPackageName(), BeanEntity.class.getSimpleName());

		// Obtém tipo da chave primária
		String idColumn = primaryKeys.stream().findFirst().orElse(null);
		String javaType = null;

		if (idColumn != null) {
			for (ColumnInfo column : columns) {
				if (column.name.equals(idColumn)) {
					javaType = TypeMapper.toJavaType(column.sqlTypeName);
					System.out.println(" Field Name --->> " + column.name + " - Type Name --->> " + column.sqlTypeName + " - Type Java --->> " + javaType);
					break;
				}
			}
		}

		// Criação da classe com todas as anotações e heranças/interfaces
		TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC)
				.addAnnotation(Entity.class)
				.addAnnotation(AnnotationSpec.builder(jsonIgnoreProperties).addMember("ignoreUnknown", "$L", true).build())
				.addAnnotation(tableAnnotation.build())
				.superclass(ParameterizedTypeName.get(representationModel, ClassName.get(packageName, className)));

		ClassName idTypeClass = null;

		if (isCompositeKey) {
			idTypeClass = ClassName.get(packageName, className + "Id");
		} else if (javaType != null) {
			idTypeClass = ClassName.bestGuess(javaType);
		} else {
			idTypeClass = ClassName.get(Object.class); // fallback
		}

		classBuilder.addSuperinterface(ParameterizedTypeName.get(beanEntity, idTypeClass));

		// Importa Jackson + Constants
		ClassName jsonFormat = ClassName.get(JsonFormat.class.getPackageName(), JsonFormat.class.getSimpleName());
		ClassName constants = ClassName.get(Constants.class.getPackageName(), Constants.class.getSimpleName());

		if (isCompositeKey) {
			ClassName idClass = ClassName.get(packageName, className + "Id");
			classBuilder.addField(FieldSpec.builder(idClass, "id", Modifier.PRIVATE).addAnnotation(EmbeddedId.class).build());
			classBuilder.addMethod(MethodSpec.methodBuilder("getId").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(idClass).addStatement("return this.id").build());
			classBuilder.addMethod(MethodSpec.methodBuilder("setId").addModifiers(Modifier.PUBLIC).addParameter(idClass, "id").addStatement("this.id = id").build());
		}

		for (ColumnInfo column : columns) {

			if (isCompositeKey && primaryKeys.contains(column.name)) {
				continue; // evita gerar novamente campos que já estão no EmbeddedId
			}

			javaType = TypeMapper.toJavaType(column.sqlTypeName);
			
			System.out.println(" Field Name --->> " + column.name + " - Type Name --->> " + column.sqlTypeName + " - Type Java --->> " + javaType);

			ClassName typeClass = ClassName.bestGuess(javaType);

			boolean isPrimaryKey = primaryKeys.contains(column.name);

			boolean isForeignKey = foreignKeys.containsKey(tableName) && foreignKeys.get(tableName).containsKey(column.name);

			String fieldName = isPrimaryKey ? "id" : StringUtils.camelCase(column.name);

			boolean isCompositeFK = compositeForeignKeys.containsKey(tableName) && compositeForeignKeys.get(tableName).values().stream().anyMatch(cols -> cols.contains(column.name));

			if (isCompositeFK && !isForeignKey) {
				continue;
			}

			if (isPrimaryKey && !isCompositeKey) {

				boolean isUUID = "UUID".equalsIgnoreCase(column.sqlTypeName);
				boolean isBigInt = "BIGINT".equalsIgnoreCase(column.sqlTypeName);

				// Define o tipo corretamente (UUID ou Long)
				if (isUUID) {
					typeClass = ClassName.get("java.util", "UUID");
				} else if (isBigInt) {
					typeClass = ClassName.get(Long.class);
				}

				AnnotationSpec.Builder generatedValue = AnnotationSpec.builder(GeneratedValue.class);

				if (isUUID) {
					generatedValue.addMember("strategy", "$T.UUID", GenerationType.class);
				} else {
					generatedValue.addMember("strategy", "$T.IDENTITY", GenerationType.class);
				}

				FieldSpec field = FieldSpec.builder(typeClass, "id", Modifier.PRIVATE).addAnnotation(Id.class).addAnnotation(generatedValue.build())
						.addAnnotation(AnnotationSpec.builder(Column.class).addMember("name", "$S", column.name).build())
						.build();

				classBuilder.addField(field);

				classBuilder.addMethod(MethodSpec.methodBuilder("getId").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(typeClass).addStatement("return this.id").build());

				classBuilder.addMethod(MethodSpec.methodBuilder("setId").addModifiers(Modifier.PUBLIC).addParameter(typeClass, "id").addStatement("this.id = id").build());

			} else if (isForeignKey) {

			    String referencedTable = foreignKeys.get(tableName).get(column.name);
			    String referencedProperty = StringUtils.camelCase(referencedTable);
			    String referencedClass = StringUtils.capitalize(referencedProperty);

			    String referencedColumnName = getReferencedColumnName(metaData, tableName, column.name);

			    AnnotationSpec.Builder joinColumn = AnnotationSpec.builder(JoinColumn.class)
			        .addMember("name", "$S", column.name)
			        .addMember("referencedColumnName", "$S", referencedColumnName);

			    if (!column.nullable) {
			        joinColumn.addMember("nullable", "$L", false);
			    }
			    if (column.unique) {
			        joinColumn.addMember("unique", "$L", true);
			    }

			    ClassName relationAnnotation;
			    AnnotationSpec.Builder relationBuilder;

			    if (column.unique) {
			        relationAnnotation = ClassName.get(OneToOne.class.getPackageName(), OneToOne.class.getSimpleName());
			        ClassName cascadeType = ClassName.get(CascadeType.class.getPackageName(), CascadeType.class.getSimpleName());
			        ClassName fetchType = ClassName.get(FetchType.class.getPackageName(), FetchType.class.getSimpleName());

			        relationBuilder = AnnotationSpec.builder(relationAnnotation)
			                .addMember("fetch", "$T.LAZY", fetchType)
			                .addMember("cascade", "{$T.PERSIST, $T.MERGE, $T.REMOVE}", cascadeType, cascadeType, cascadeType);
			        
			    } else {
			        relationAnnotation = ClassName.get(ManyToOne.class.getPackageName(), ManyToOne.class.getSimpleName());
			        ClassName fetchType = ClassName.get(FetchType.class.getPackageName(), FetchType.class.getSimpleName());
			        relationBuilder = AnnotationSpec.builder(relationAnnotation)
			            .addMember("fetch", "$T.LAZY", fetchType);
			    }

			    ClassName jsonProperty = ClassName.get(JsonProperty.class.getPackageName(), JsonProperty.class.getSimpleName());
			    ClassName accessEnum = ClassName.get(JsonProperty.class.getPackageName() + "." + JsonProperty.class.getSimpleName() , Access.class.getSimpleName());

			    AnnotationSpec writeOnly = AnnotationSpec.builder(jsonProperty)
			        .addMember("access", "$T.WRITE_ONLY", accessEnum)
			        .build();

			    // Campo do tipo objeto (referenciado)
			    FieldSpec relationField = FieldSpec.builder(ClassName.get(packageName, referencedClass), referencedProperty, Modifier.PRIVATE)
			        .addAnnotation(writeOnly)
			        .addAnnotation(relationBuilder.build())
			        .addAnnotation(joinColumn.build())
			        .build();

			    classBuilder.addField(relationField);

			    classBuilder.addMethod(MethodSpec.methodBuilder("get" + referencedClass)
			        .addModifiers(Modifier.PUBLIC)
			        .returns(ClassName.get(packageName, referencedClass))
			        .addStatement("return this.$N", referencedProperty)
			        .build());

			    classBuilder.addMethod(MethodSpec.methodBuilder("set" + referencedClass)
			        .addModifiers(Modifier.PUBLIC)
			        .addParameter(ClassName.get(packageName, referencedClass), referencedProperty)
			        .addStatement("this.$N = $N", referencedProperty, referencedProperty)
			        .build());

			    // Campo primitivo da FK (id)
			    String primitiveFieldName = StringUtils.camelCase(column.name);
			    String javaTypeName = TypeMapper.toJavaType(column.sqlTypeName);
			    ClassName typeFkClass = TypeMapper.resolveType(javaTypeName);

			    AnnotationSpec columnAnnotation = AnnotationSpec.builder(ClassName.get(Column.class.getPackageName(), Column.class.getSimpleName()))
			        .addMember("name", "$S", column.name)
			        .addMember("insertable", "$L", false)
			        .addMember("updatable", "$L", false)
			        .build();

			    FieldSpec fkPrimitiveField = FieldSpec.builder(typeFkClass, primitiveFieldName, Modifier.PRIVATE)
			        .addAnnotation(columnAnnotation)
			        .build();

			    classBuilder.addField(fkPrimitiveField);

			    classBuilder.addMethod(MethodSpec.methodBuilder("get" + StringUtils.capitalize(primitiveFieldName))
			        .addModifiers(Modifier.PUBLIC)
			        .returns(typeFkClass)
			        .addStatement("return this.$N", primitiveFieldName)
			        .build());

			    classBuilder.addMethod(MethodSpec.methodBuilder("set" + StringUtils.capitalize(primitiveFieldName))
			        .addModifiers(Modifier.PUBLIC)
			        .addParameter(typeFkClass, primitiveFieldName)
			        .addStatement("this.$N = $N", primitiveFieldName, primitiveFieldName)
			        .build());

			    continue; // Evita duplicação
			    
			} else {

				AnnotationSpec.Builder columnAnnotation = AnnotationSpec
						.builder(Column.class)
						.addMember("name", "$S",
						column.name);

				if (!column.nullable) {
					columnAnnotation.addMember("nullable", "$L", false);
				}

				String sqlType = column.sqlTypeName.toUpperCase();

				// Adiciona length se for VARCHAR ou CHAR
				if ((sqlType.contains("CHAR") || sqlType.contains("VARCHAR")) && column.size > 0) {
					columnAnnotation.addMember("length", "$L", column.size);
				}

				// Adiciona precision e scale se for numérico
				if ((sqlType.contains("DECIMAL") || sqlType.contains("NUMERIC")) && column.size > 0) {
					columnAnnotation.addMember("precision", "$L", column.size);
					if (column.decimalDigits > 0) {
						columnAnnotation.addMember("scale", "$L", column.decimalDigits);
					}
				}

				if (column.unique) {
					columnAnnotation.addMember("unique", "$L", true);
				}

				FieldSpec.Builder fieldBuilder = FieldSpec.builder(typeClass, fieldName, Modifier.PRIVATE)
						.addAnnotation(columnAnnotation.build());

				// Adiciona @JsonFormat se for LocalDate ou LocalDateTime
				if (javaType.equals(LocalDate.class.getName())) {
					fieldBuilder.addAnnotation(AnnotationSpec.builder(jsonFormat).addMember("shape", "$T.STRING", jsonFormat.nestedClass("Shape")).addMember("pattern", "$T.DATE_FORMAT", constants).build());
				} else if (javaType.equals(LocalDateTime.class.getName())) {
					fieldBuilder.addAnnotation(AnnotationSpec.builder(jsonFormat).addMember("shape", "$T.STRING", jsonFormat.nestedClass("Shape")).addMember("pattern", "$T.DATE_TIME_FORMAT", constants).build());
				}

				FieldSpec field = fieldBuilder.build();
				classBuilder.addField(field);

				// Getter
				classBuilder.addMethod(MethodSpec.methodBuilder("get" + StringUtils.capitalize(fieldName))
						.addModifiers(Modifier.PUBLIC).returns(typeClass).addStatement("return this.$N", fieldName)
						.build());

				// Setter
				classBuilder.addMethod(MethodSpec.methodBuilder("set" + StringUtils.capitalize(fieldName))
						.addModifiers(Modifier.PUBLIC).addParameter(typeClass, fieldName)
						.addStatement("this.$N = $N", fieldName, fieldName).build());
			}
		}

		for (Map.Entry<String, List<String>> entry : compositeForeignKeys.getOrDefault(tableName, Map.of()).entrySet()) {

		    String referencedTable = entry.getKey();
		    List<String> fkColumns = entry.getValue();

		    boolean alreadyMapped = fkColumns.stream().anyMatch(fkCol -> 
		    foreignKeys.getOrDefault(tableName, Map.of()).containsKey(fkCol));

		    if (alreadyMapped)
		        continue;

		    String referencedProperty = StringUtils.camelCase(referencedTable);
		    String referencedClass = StringUtils.capitalize(referencedProperty);

		    List<CodeBlock> joinColumnsBlocks = new ArrayList<>();
		    for (String fkColumn : fkColumns) {
		        String referencedColumnName = getReferencedColumnName(metaData, tableName, fkColumn);
		        CodeBlock join = CodeBlock.of("@$T(name = $S, referencedColumnName = $S)",
		                JoinColumn.class, fkColumn, referencedColumnName);
		        joinColumnsBlocks.add(join);
		    }

		    AnnotationSpec joinColumnsAnnotation = AnnotationSpec.builder(JoinColumns.class)
		            .addMember("value", "{$L}", CodeBlock.join(joinColumnsBlocks, ", "))
		            .build();

		    // @ManyToOne composto
		    ClassName jsonProperty = ClassName.get(JsonProperty.class.getPackageName(), JsonProperty.class.getSimpleName());
		    ClassName accessEnum = ClassName.get(JsonProperty.class.getPackageName() + "." + JsonProperty.class.getSimpleName() , Access.class.getSimpleName());

		    AnnotationSpec writeOnly = AnnotationSpec.builder(jsonProperty)
		    	    .addMember("access", "$T.WRITE_ONLY", accessEnum)
		    	    .build();

		    	FieldSpec field = FieldSpec
		    	        .builder(ClassName.get(packageName, referencedClass), referencedProperty, Modifier.PRIVATE)
		    	        .addAnnotation(writeOnly)
		    	        .addAnnotation(AnnotationSpec.builder(ManyToOne.class)
		    	                .addMember("fetch", "$T.LAZY", ClassName.get(FetchType.class.getPackageName(), FetchType.class.getSimpleName()))
		    	                .build())
		    	        .addAnnotation(joinColumnsAnnotation)
		    	        .build();

		    classBuilder.addField(field);

		    classBuilder.addMethod(MethodSpec.methodBuilder("get" + referencedClass)
		            .addModifiers(Modifier.PUBLIC)
		            .returns(ClassName.get(packageName, referencedClass))
		            .addStatement("return this.$N", referencedProperty)
		            .build());

		    classBuilder.addMethod(MethodSpec.methodBuilder("set" + referencedClass)
		            .addModifiers(Modifier.PUBLIC)
		            .addParameter(ClassName.get(packageName, referencedClass), referencedProperty)
		            .addStatement("this.$N = $N", referencedProperty, referencedProperty)
		            .build());

		    // Campos primitivos da FK composta
		    for (String fkColumn : fkColumns) {
		        String primitiveField = StringUtils.camelCase(fkColumn);
		        String fkType = columns.stream()
		            .filter(c -> c.name.equals(fkColumn))
		            .findFirst()
		            .map(c -> TypeMapper.toJavaType(c.sqlTypeName))
		            .orElse("Long");

		        ClassName typeClass = ClassName.bestGuess(fkType);

		        FieldSpec fkPrimitiveField = FieldSpec.builder(typeClass, primitiveField, Modifier.PRIVATE)
		            .addAnnotation(AnnotationSpec.builder(Column.class)
		                .addMember("name", "$S", fkColumn)
		                .addMember("insertable", "$L", false)
		                .addMember("updatable", "$L", false)
		                .build())
		            .build();

		        classBuilder.addField(fkPrimitiveField);

		        classBuilder.addMethod(MethodSpec.methodBuilder("get" + StringUtils.capitalize(primitiveField))
		            .addModifiers(Modifier.PUBLIC)
		            .returns(typeClass)
		            .addStatement("return this.$N", primitiveField)
		            .build());

		        classBuilder.addMethod(MethodSpec.methodBuilder("set" + StringUtils.capitalize(primitiveField))
		            .addModifiers(Modifier.PUBLIC)
		            .addParameter(typeClass, primitiveField)
		            .addStatement("this.$N = $N", primitiveField, primitiveField)
		            .build());
		    }
		}

		// equals/hashCode
		if (!isCompositeKey && primaryKeys.size() == 1) {
			classBuilder.addMethod(MethodSpec.methodBuilder("equals").addAnnotation(Override.class)
					.addModifiers(Modifier.PUBLIC).returns(boolean.class).addParameter(Object.class, "o").addCode("""
							if (this == o) return true;
							if (o == null || getClass() != o.getClass()) return false;
							$L that = ($L) o;
							return java.util.Objects.equals(id, that.id);
							""", className, className).build());

			classBuilder.addMethod(
					MethodSpec.methodBuilder("hashCode").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
							.returns(int.class).addStatement("return java.util.Objects.hash(id)").build());
		}

		// toString
		List<String> linhas = new ArrayList<>();

		// Gera toString com base em todos os campos realmente gerados
		Set<String> usedFields = new HashSet<>();

		for (FieldSpec field : classBuilder.build().fieldSpecs) {
			String fieldName = field.name;

			if (!usedFields.add(fieldName))
				continue;

			String label = fieldName;
			if (label.startsWith("id") && label.length() > 2 && Character.isUpperCase(label.charAt(2))) {
				label = label.substring(2);
				label = Character.toLowerCase(label.charAt(0)) + label.substring(1);
			}

			linhas.add("\"" + label + "=\" + (" + fieldName + " != null ? " + fieldName + ".toString() : \"null\")");
		}

		classBuilder.addMethod(MethodSpec.methodBuilder("toString").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(String.class)
								.addCode(CodeBlock.builder().add("return $S +\n", className + "{").add("$L", String.join(" + \", \" +\n", linhas)).add(" + '}';\n").build())
								.build());

		// Escreve a classe
		JavaFile javaFile = JavaFile.builder(packageName, classBuilder.build()).skipJavaLangImports(true).indent("    ").build();
		
		javaFile.writeTo(Paths.get("src/main/java"));

		return new ClassResolver(className, idTypeClass);
	}

	/**
	 * @param metaData
	 * @param fkTable
	 * @param fkColumn
	 * @return
	 * @throws SQLException
	 */
	private String getReferencedColumnName(DatabaseMetaData metaData, String fkTable, String fkColumn) throws SQLException {
		ResultSet fkResultSet = metaData.getImportedKeys(null, null, fkTable);
		while (fkResultSet.next()) {
			String fkCol = fkResultSet.getString("FKCOLUMN_NAME");
			if (fkCol.equals(fkColumn)) {
				return fkResultSet.getString("PKCOLUMN_NAME");
			}
		}
		return "id"; // fallback padrão
	}

	/**
	 * @param metaData
	 * @param tableName
	 * @param columnName
	 * @return
	 * @throws SQLException
	 */
	private boolean isColumnUnique(DatabaseMetaData metaData, String tableName, String columnName) throws SQLException {
		ResultSet indexInfo = metaData.getIndexInfo(null, null, tableName, true, false);
		while (indexInfo.next()) {
			String idxColumnName = indexInfo.getString("COLUMN_NAME");
			boolean nonUnique = indexInfo.getBoolean("NON_UNIQUE");
			if (columnName.equals(idxColumnName) && !nonUnique) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param metaData
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	private Map<String, Set<String>> getCompositeUniqueIndexes(DatabaseMetaData metaData, String tableName) throws SQLException {
		
		Map<String, Set<String>> indexMap = new HashMap<>();
		ResultSet indexInfo = metaData.getIndexInfo(null, null, tableName, true, false);

		while (indexInfo.next()) {
			String indexName = indexInfo.getString("INDEX_NAME");
			String columnName = indexInfo.getString("COLUMN_NAME");
			boolean nonUnique = indexInfo.getBoolean("NON_UNIQUE");

			if (indexName != null && columnName != null && !nonUnique) {
				indexMap.computeIfAbsent(indexName, k -> new HashSet<>()).add(columnName);
			}
		}

		return indexMap;
	}

	/**
	 * @param packageName
	 * @param className
	 * @param columns
	 * @param primaryKeys
	 * @throws IOException
	 */
	private void gerarClasseEmbeddableId(String packageName, String className, List<ColumnInfo> columns, Set<String> primaryKeys) throws IOException {

		TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC)
				.addAnnotation(Embeddable.class);

		for (ColumnInfo column : columns) {
			if (!primaryKeys.contains(column.name))
				continue;

			String javaType = TypeMapper.toJavaType(column.sqlTypeName);
			System.out.println(" Field Name --->> " + column.name + " - Type Name --->> " + column.sqlTypeName + " - Type Java --->> " + javaType);
			String fieldName = StringUtils.camelCase(column.name);
			ClassName typeClass = ClassName.bestGuess(javaType);

			FieldSpec field = FieldSpec.builder(typeClass, fieldName, Modifier.PRIVATE)
					.addAnnotation(AnnotationSpec.builder(Column.class).addMember("name", "$S", column.name).build())
					.build();
			classBuilder.addField(field);

			classBuilder.addMethod(
					MethodSpec.methodBuilder("get" + StringUtils.capitalize(fieldName)).addModifiers(Modifier.PUBLIC)
							.returns(typeClass).addStatement("return this.$N", fieldName).build());

			classBuilder.addMethod(MethodSpec.methodBuilder("set" + StringUtils.capitalize(fieldName))
					.addModifiers(Modifier.PUBLIC).addParameter(typeClass, fieldName)
					.addStatement("this.$N = $N", fieldName, fieldName).build());
		}

		// equals/hashCode
		String fieldList = primaryKeys.stream().map(StringUtils::camelCase).reduce((a, b) -> a + ", " + b).orElse("");

		MethodSpec.Builder equalsMethod = MethodSpec.methodBuilder("equals").addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC).returns(boolean.class).addParameter(Object.class, "o").addCode("""
						if (this == o) return true;
						if (o == null || getClass() != o.getClass()) return false;
						$L that = ($L) o;
						""", className, className);

		// Adiciona os campos com &&
		List<String> equalsLines = primaryKeys.stream().map(StringUtils::camelCase)
				.map(field -> " java.util.Objects.equals(" + field + ", that." + field + ")").toList();

		equalsMethod.addStatement("return " + String.join(" &&\n       ", equalsLines));

		classBuilder.addMethod(equalsMethod.build());

		classBuilder.addMethod(
				MethodSpec.methodBuilder("hashCode").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC)
						.returns(int.class).addStatement("return java.util.Objects.hash($L)", fieldList).build());

		JavaFile javaFile = JavaFile.builder(packageName, classBuilder.build()).skipJavaLangImports(true).indent("    ")
				.build();

		javaFile.writeTo(Paths.get("src/main/java"));
	}
}