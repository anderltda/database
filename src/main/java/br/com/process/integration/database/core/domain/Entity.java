package br.com.process.integration.database.core.domain;

public interface Entity<ID extends Object> {

	public ID getId();

	public void setId(ID id);
}
