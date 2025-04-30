package br.com.process.integration.database.core.domain;


/**
 * Interface base para entidades identificáveis por um ID.
 *
 * @param <T> Tipo da chave primária (ex: Long, UUID, ClasseId)
 */
public interface BeanEntity<I>{

	/**
	 * @return
	 */
	public I getId();
}
