package br.com.process.integration.database.model.entity.dto.example;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class EntitySevenId {
	@Column(name = "id_entity_seven")
	private Integer idEntitySeven;

	@Column(name = "id_entity_six")
	private Integer idEntitySix;

	public Integer getIdEntitySeven() {
		return this.idEntitySeven;
	}

	public void setIdEntitySeven(Integer idEntitySeven) {
		this.idEntitySeven = idEntitySeven;
	}

	public Integer getIdEntitySix() {
		return this.idEntitySix;
	}

	public void setIdEntitySix(Integer idEntitySix) {
		this.idEntitySix = idEntitySix;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntitySevenId that = (EntitySevenId) o;
		return java.util.Objects.equals(idEntitySeven, that.idEntitySeven)
				&& java.util.Objects.equals(idEntitySix, that.idEntitySix);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(idEntitySeven, idEntitySix);
	}
}
