package br.com.process.integration.database.model.data.dto.example;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public class EntitySevenDataId {
	
	/**
	 * Coluna: id_entity_seven
	 */
	@NotNull
	private UUID idEntitySeven;

	/**
	 * Coluna: id_entity_six
	 */
	@NotNull
	private Long idEntitySix;

	public UUID getIdEntitySeven() {
		return this.idEntitySeven;
	}

	public void setIdEntitySeven(UUID idEntitySeven) {
		this.idEntitySeven = idEntitySeven;
	}

	public Long getIdEntitySix() {
		return this.idEntitySix;
	}

	public void setIdEntitySix(Long idEntitySix) {
		this.idEntitySix = idEntitySix;
	}
}
