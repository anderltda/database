package br.com.process.integration.database.core.domain;

public interface Entity<I> {

	public I getId();

	public void setId(I id);
}
