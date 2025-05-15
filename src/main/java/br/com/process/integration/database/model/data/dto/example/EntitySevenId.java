package br.com.process.integration.database.model.data.dto.example;

import jakarta.validation.constraints.NotNull;

public class EntitySevenId {
	
    /**
     * Coluna: id_entity_seven
     */
    @NotNull
    private Long idEntitySeven;

    /**
     * Coluna: id_entity_six
     */
    @NotNull
    private Long idEntitySix;

    public Long getIdEntitySeven() {
        return this.idEntitySeven;
    }

    public void setIdEntitySeven(Long idEntitySeven) {
        this.idEntitySeven = idEntitySeven;
    }

    public Long getIdEntitySix() {
        return this.idEntitySix;
    }

    public void setIdEntitySix(Long idEntitySix) {
        this.idEntitySix = idEntitySix;
    }
}
