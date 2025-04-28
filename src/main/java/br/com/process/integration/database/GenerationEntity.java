package br.com.process.integration.database;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GenerationEntity {

	// Configure quais tabelas você quer gerar (se quiser todas, deixe vazio)
	private static final Set<String> TABELAS_PARA_GERAR = Set.of("entity_status", "entity_one", "entity_two", "entity_tree", "entity_four", "entity_five");

	// Configure onde salvar os arquivos gerados
	private static final String OUTPUT_DIR = "src/main/java/br/com/process/integration/database/model/entity/dto/test/";

	public static void main(String[] args) throws Exception {
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/root-database",
				"anderson", "admin")) {

			DatabaseMetaData metaData = connection.getMetaData();
			Map<String, Map<String, String>> foreignKeys = carregarChavesEstrangeiras(metaData);
			Map<String, String> tabelasClasses = new HashMap<>();

			// Mapear tabelas para nomes de classe
			ResultSet tables = metaData.getTables(null, null, "%", new String[] { "TABLE" });
			while (tables.next()) {
				String tableName = tables.getString("TABLE_NAME");
				if (TABELAS_PARA_GERAR.isEmpty() || TABELAS_PARA_GERAR.contains(tableName)) {
					tabelasClasses.put(tableName, capitalize(camelCase(tableName)));
				}
			}

			// Gerar entidades
			tables.beforeFirst();
			while (tables.next()) {
				String tableName = tables.getString("TABLE_NAME");
				if (TABELAS_PARA_GERAR.isEmpty() || TABELAS_PARA_GERAR.contains(tableName)) {
					gerarClasseEntity(metaData, tableName, foreignKeys, tabelasClasses);
				}
			}
		}
	}

	private static void gerarClasseEntity(DatabaseMetaData metaData, String tableName, Map<String, Map<String, String>> foreignKeys, Map<String, String> tabelasClasses)
			throws SQLException, IOException {
		List<ColumnInfo> columns = new ArrayList<>();
		Set<String> primaryKeys = new HashSet<>();

		ResultSet pk = metaData.getPrimaryKeys(null, null, tableName);
		while (pk.next()) {
			primaryKeys.add(pk.getString("COLUMN_NAME"));
		}

		ResultSet rs = metaData.getColumns(null, null, tableName, null);
		while (rs.next()) {
			ColumnInfo column = new ColumnInfo(rs.getString("COLUMN_NAME"), rs.getInt("DATA_TYPE"), 
					rs.getString("TYPE_NAME"), rs.getInt("COLUMN_SIZE"),
					rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
			columns.add(column);
		}

		String className = capitalize(camelCase(tableName));
		StringBuilder sb = new StringBuilder();

		sb.append("package br.com.process.integration.database.model.entity.dto.test; \n\n");
		sb.append("import org.springframework.hateoas.RepresentationModel;\n\n");
		sb.append("import com.fasterxml.jackson.annotation.JsonIgnoreProperties;\n");
		sb.append("import com.fasterxml.jackson.annotation.JsonFormat;\n\n");
		sb.append("import br.com.process.integration.database.core.domain.BeanEntity;\n");
		sb.append("import br.com.process.integration.database.core.util.Constants;\n\n");
		sb.append("import jakarta.persistence.*;\n");
		sb.append("import java.util.Objects;\n\n");

		if (primaryKeys.isEmpty()) {
			sb.append("@Embeddable\n");
		} else {
			sb.append("@Entity\n");
			sb.append("@Table(name = \"").append(tableName).append("\")\n");
			sb.append("@JsonIgnoreProperties(ignoreUnknown = true)\n");
		}
		
		for (ColumnInfo column : columns) {
			String javaType = mapearTipoJava(column.sqlTypeName);
			if (primaryKeys.contains(column.name)) {
				sb.append("public class ").append(className)
				.append(" extends RepresentationModel<").append(className).append(">")
				.append(" implements BeanEntity<").append(javaType).append("> {\n\n");
				break;
			}
		}	
		
		// Gerar campos
		for (ColumnInfo column : columns) {
			String javaType = mapearTipoJava(column.sqlTypeName);
			String fieldName = camelCase(column.name);

			// Forçar o nome do campo @Id como "id"
			if (primaryKeys.contains(column.name)) {
				
				sb.append("    @Id\n");
				sb.append("    @GeneratedValue(strategy = GenerationType.IDENTITY)\n"); // Adicionando a estratégia de geração
				sb.append("    private "+ javaType +" id;\n\n"); // O campo id sempre será "id"
				
			} else if (foreignKeys.containsKey(tableName) && foreignKeys.get(tableName).containsKey(column.name)) {
				
				String referencedTable = foreignKeys.get(tableName).get(column.name);
				String referencedClass = capitalize(camelCase(referencedTable));

				sb.append("    @ManyToOne\n");
				sb.append("    @JoinColumn(name = \"").append(column.name).append("\"");
				if (!column.nullable) {
					sb.append(", nullable = false");
				}
				sb.append(")\n");
				sb.append("    private ").append(referencedClass).append(" ").append(camelCase(referencedTable))
						.append(";\n\n");
			} else {
				sb.append("    @Column(name = \"").append(column.name).append("\"");
				if (!column.nullable) {
					sb.append(", nullable = false");
				}
				sb.append(")\n");
				sb.append("    private ").append(javaType).append(" ").append(fieldName).append(";\n\n");
			}
		}
		
		for (ColumnInfo column : columns) {
			String javaType = mapearTipoJava(column.sqlTypeName);
			String fieldName = camelCase(column.name);
			
			// Forçar o nome do campo @Id como "id"
			if (primaryKeys.contains(column.name)) {
				
				// Gerar get e set
				sb.append("\n    public ").append(javaType).append(" get").append(capitalize("id")).append("() {\n");
				sb.append("        return ").append("id").append(";\n");
				sb.append("    }\n\n");

				sb.append("    public void set").append(capitalize("id")).append("(").append(javaType).append(" ")
						.append("id").append(") {\n");
				sb.append("        this.").append("id").append(" = ").append("id").append(";\n");
				sb.append("    }\n\n");
				
			} else if (foreignKeys.containsKey(tableName) && foreignKeys.get(tableName).containsKey(column.name)) {
				
				String referencedTable = foreignKeys.get(tableName).get(column.name);
				String camelCaseClass = camelCase(referencedTable);
				String referencedClass = capitalize(camelCaseClass);
				
				// Gerar get e set
				sb.append("\n    public ").append(referencedClass).append(" get").append(referencedClass).append("() {\n");
				sb.append("        return ").append(camelCaseClass).append(";\n");
				sb.append("    }\n\n");

				sb.append("    public void set").append(capitalize(referencedClass)).append("(").append(referencedClass).append(" ")
						.append(camelCaseClass).append(") {\n");
				sb.append("        this.").append(camelCaseClass).append(" = ").append(camelCaseClass).append(";\n");
				sb.append("    }\n\n");
				
			} else {
				
				// Gerar get e set
				sb.append("\n    public ").append(javaType).append(" get").append(capitalize(fieldName)).append("() {\n");
				sb.append("        return ").append(fieldName).append(";\n");
				sb.append("    }\n\n");

				sb.append("    public void set").append(capitalize(fieldName)).append("(").append(javaType).append(" ")
						.append(fieldName).append(") {\n");
				sb.append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
				sb.append("    }\n\n");
				
			}


		}

		// equals and hashCode
		if (!primaryKeys.isEmpty()) {
			sb.append("    @Override\n");
			sb.append("    public boolean equals(Object o) {\n");
			sb.append("        if (this == o) return true;\n");
			sb.append("        if (o == null || getClass() != o.getClass()) return false;\n");
			sb.append("        ").append(className).append(" that = (").append(className).append(") o;\n");
			sb.append("        return Objects.equals(id, that.id);\n"); // Usando o campo "id" no equals
			sb.append("    }\n\n");

			sb.append("    @Override\n");
			sb.append("    public int hashCode() {\n");
			sb.append("        return Objects.hash(id);\n"); // Usando o campo "id" no hashCode
			sb.append("    }\n\n");
		}

		// toString
		sb.append("    @Override\n");
		sb.append("    public String toString() {\n");
		sb.append("        return \"").append(className).append("{id=\" + id +\n");

		int idx = 0;
		for (ColumnInfo column : columns) {
		    if (idx++ > 0)
		        sb.append("                \", ");
		    else
		        sb.append("                \"");

			String fieldName = camelCase(column.name);
			
			if (primaryKeys.contains(column.name)) {
				
		        sb.append("id=\" + id +\n");
				
			} else if (foreignKeys.containsKey(tableName) && foreignKeys.get(tableName).containsKey(column.name)) {
				
				String referencedTable = foreignKeys.get(tableName).get(column.name);
				String camelCaseClass = camelCase(referencedTable);
				String referencedClass = capitalize(camelCaseClass);
				
				sb.append(referencedClass).append("=\" + ");
				sb.append(camelCaseClass).append(" != null ? ").append(camelCaseClass).append(".toString() : \"null\" +\n");
				
			} else {
				sb.append(fieldName).append("=\" + ");
	            sb.append(fieldName).append(" != null ? ").append(fieldName).append(".toString() : \"null\" +\n");
				
			}
		  
		}

		sb.append("                '}';\n");
		sb.append("    }\n\n");
		sb.append("}");
		
		// Escrever arquivo
		try (FileWriter writer = new FileWriter(OUTPUT_DIR + className + ".java")) {
			writer.write(sb.toString());
		}
	}
	
    private static void gerarGetterSetter(StringBuilder sb, String fieldName, String javaType) {
        String capitalized = capitalize(fieldName);

        sb.append("    public ").append(javaType).append(" get").append(capitalized).append("() {\n");
        sb.append("        return ").append(fieldName).append(";\n");
        sb.append("    }\n\n");

        sb.append("    public void set").append(capitalized).append("(").append(javaType).append(" ").append(fieldName).append(") {\n");
        sb.append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
        sb.append("    }\n\n");
    }

	private static Map<String, Map<String, String>> carregarChavesEstrangeiras(DatabaseMetaData metaData)
			throws SQLException {
		Map<String, Map<String, String>> foreignKeys = new HashMap<>();

		ResultSet fk = metaData.getImportedKeys(null, null, null);
		while (fk.next()) {
			String fkTable = fk.getString("FKTABLE_NAME");
			String fkColumn = fk.getString("FKCOLUMN_NAME");
			String pkTable = fk.getString("PKTABLE_NAME");

			foreignKeys.computeIfAbsent(fkTable, k -> new HashMap<>()).put(fkColumn, pkTable);
		}
		return foreignKeys;
	}

	private static String camelCase(String input) {
		StringBuilder result = new StringBuilder();
		boolean capitalizeNext = false;
		for (char c : input.toCharArray()) {
			if (c == '_' || c == '-') {
				capitalizeNext = true;
			} else if (capitalizeNext) {
				result.append(Character.toUpperCase(c));
				capitalizeNext = false;
			} else {
				result.append(Character.toLowerCase(c));
			}
		}
		return result.toString();
	}

	private static String capitalize(String input) {
		if (input == null || input.isEmpty())
			return input;
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}

	private static String mapearTipoJava(String sqlTypeName) {
		return switch (sqlTypeName.toUpperCase()) {
		case "VARCHAR", "TEXT", "CHAR" -> "String";
		case "INT", "INTEGER", "INT4" -> "Integer";
		case "BIGINT", "INT8" -> "Long";
		case "BOOLEAN", "BIT" -> "Boolean";
		case "DATE" -> "java.time.LocalDate";
		case "TIMESTAMP", "DATETIME" -> "java.time.LocalDateTime";
		case "DOUBLE", "BIGDECIMAL", "DECIMAL", "NUMERIC", "FLOAT8" -> "Double";
		case "FLOAT" -> "Float";
		default -> "String";
		};
	}

	private static class ColumnInfo {
		String name;
		int sqlType;
		String sqlTypeName;
		int size;
		boolean nullable;

		ColumnInfo(String name, int sqlType, String sqlTypeName, int size, boolean nullable) {
			this.name = name;
			this.sqlType = sqlType;
			this.sqlTypeName = sqlTypeName;
			this.size = size;
			this.nullable = nullable;
		}
	}
}