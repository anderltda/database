package br.com.process.integration.database.core.ui.generator;

import java.util.List;

/**
 * 
 */
public class FormData {

	private String domain;
	private List<String> tables;
	private List<String> types;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public List<String> getTables() {
		return tables;
	}

	public void setTables(List<String> tables) {
		this.tables = tables;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

}