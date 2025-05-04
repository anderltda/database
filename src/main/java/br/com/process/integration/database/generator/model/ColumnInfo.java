package br.com.process.integration.database.generator.model;

public class ColumnInfo {
	public final String name;
	public final int sqlType;
	public final String sqlTypeName;
	public final int size;
	public final boolean nullable;
	public final int decimalDigits;
	public final boolean unique;

	public ColumnInfo(String name, int sqlType, String sqlTypeName, int size, boolean nullable, int decimalDigits, boolean unique) {
	    this.name = name;
	    this.sqlType = sqlType;
	    this.sqlTypeName = sqlTypeName;
	    this.size = size;
	    this.nullable = nullable;
	    this.decimalDigits = decimalDigits;
	    this.unique = unique;
	}
}
