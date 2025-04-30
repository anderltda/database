package br.com.process.integration.database.generator.metadata;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForeignKeyResolver {

	public static Map<String, Map<String, String>> resolve(DatabaseMetaData metaData) throws SQLException {
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
	
	public static Map<String, Map<String, List<String>>> resolveCompositeForeignKeys(DatabaseMetaData metaData) throws SQLException {
	    Map<String, Map<String, List<String>>> compositeFKs = new HashMap<>();

	    ResultSet fk = metaData.getImportedKeys(null, null, null);
	    while (fk.next()) {
	        String fkTableName = fk.getString("FKTABLE_NAME");
	        String pkTableName = fk.getString("PKTABLE_NAME");
	        String fkColumnName = fk.getString("FKCOLUMN_NAME");

	        compositeFKs
	            .computeIfAbsent(fkTableName, k -> new HashMap<>())
	            .computeIfAbsent(pkTableName, k -> new ArrayList<>())
	            .add(fkColumnName);
	    }
	    return compositeFKs;
	}
}