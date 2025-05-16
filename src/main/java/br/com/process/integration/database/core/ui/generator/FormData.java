package br.com.process.integration.database.core.ui.generator;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

/**
 * 
 */
public class FormData {

	@NotBlank(message = "O domínio é obrigatório.")
	private String domain;
	
	@NotEmpty(message = "Selecione ao menos uma tabela.")
	private List<String> tables;
	
	@NotEmpty(message = "Selecione ao menos um type p/ geração.")
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