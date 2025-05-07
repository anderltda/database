package br.com.process.integration.database.generator.metadata;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 
 */
public class ForeignKeyResolver {

	private record ForeignKeyInfo(String fkTable, String fkColumn, String pkTable, String fkName) { }
	
	/**
	 * Resolve foreign keys simples (não compostas), garantindo que uma coluna FK
	 * seja mapeada apenas para uma PK (evita conflitos com chaves compostas).
	 *
	 * @param metaData metadata do banco de dados
	 * @return Map com chave da tabela FK, valor sendo um Map com coluna FK ->
	 *         tabela PK
	 * @throws SQLException
	 */
	public static Map<String, Map<String, String>> resolve(DatabaseMetaData metaData, Set<String> tableNames) throws SQLException {
		
		Map<String, Map<String, String>> foreignKeys = new HashMap<>();
		Map<String, Map<Short, String>> fkGroups = new HashMap<>();

		for (String table : tableNames) {
			try (ResultSet fk = metaData.getImportedKeys(null, null, table)) {
				while (fk.next()) {
					String fkTable = fk.getString("FKTABLE_NAME");
					String fkColumn = fk.getString("FKCOLUMN_NAME");
					String pkTable = fk.getString("PKTABLE_NAME");
					String pkColumn = fk.getString("PKCOLUMN_NAME");
					short keySeq = fk.getShort("KEY_SEQ");
					String fkName = fk.getString("FK_NAME");

					String fkKey = fkTable + "|" + fkName;
					fkGroups.computeIfAbsent(fkKey, k -> new HashMap<>()).put(keySeq, fkColumn + "=>" + pkTable + "." + pkColumn);
				}
			}
		}

		for (Map.Entry<String, Map<Short, String>> entry : fkGroups.entrySet()) {
			Map<Short, String> columns = entry.getValue();
			if (columns.size() == 1) {
				String singleMapping = columns.values().iterator().next();
				String[] parts = singleMapping.split("=>");
				String fkColumn = parts[0];
				String pkTable = parts[1].split("\\.")[0];

				String fkTable = entry.getKey().split("\\|")[0];
				foreignKeys.computeIfAbsent(fkTable, k -> new HashMap<>()).put(fkColumn, pkTable);
			}
		}

		return foreignKeys;
	}

	/**
	 * Resolve foreign keys compostas, agrupando por tabela de origem, destino e
	 * nome da constraint.
	 */
	public static Map<String, Map<String, List<String>>> resolveCompositeForeignKeys(DatabaseMetaData metaData)
			throws SQLException {
		Map<String, Map<String, List<String>>> result = new HashMap<>();

		try (ResultSet fk = metaData.getImportedKeys(null, null, null)) {
			Map<String, List<ForeignKeyInfo>> grouped = new HashMap<>();

			while (fk.next()) {
				String fkTable = fk.getString("FKTABLE_NAME");
				String fkColumn = fk.getString("FKCOLUMN_NAME");
				String pkTable = fk.getString("PKTABLE_NAME");
				String fkName = fk.getString("FK_NAME");

				String key = fkTable + "::" + fkName + "::" + pkTable;
				grouped.computeIfAbsent(key, k -> new java.util.ArrayList<>())
						.add(new ForeignKeyInfo(fkTable, fkColumn, pkTable, fkName));
			}

			for (String key : grouped.keySet()) {
				List<ForeignKeyInfo> infos = grouped.get(key);
				if (infos.size() > 1) {
					String fkTable = infos.get(0).fkTable;
					String pkTable = infos.get(0).pkTable;

					List<String> columns = infos.stream().map(info -> info.fkColumn).collect(Collectors.toList());

					result.computeIfAbsent(fkTable, k -> new HashMap<>()).put(pkTable, columns);
				}
			}
		}
		return result;
	}

}