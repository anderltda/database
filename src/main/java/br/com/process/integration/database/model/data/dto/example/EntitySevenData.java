package br.com.process.integration.database.model.data.dto.example;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntitySevenData extends RepresentationModel<EntitySevenData> implements BeanData<EntitySevenData> {

	private EntitySevenId id;

	/**
	 * Coluna: dado
	 */
	private String dado;

	private EntitySixData entitySixData;

	public EntitySevenId getId() {
		return this.id;
	}

	public void setId(EntitySevenId id) {
		this.id = id;
	}

	public String getDado() {
		return this.dado;
	}

	public void setDado(String dado) {
		this.dado = dado;
	}

	public EntitySixData getEntitySixData() {
		return this.entitySixData;
	}

	public void setEntitySixData(EntitySixData entitySixData) {
		this.entitySixData = entitySixData;
	}

	@Override
	@JsonIgnore
	public EntitySevenData getData() {
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntitySevenData other = (EntitySevenData) o;
		return java.util.Objects.equals(id, other.id) && java.util.Objects.equals(dado, other.dado)
				&& java.util.Objects.equals(entitySixData, other.entitySixData);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id, dado, entitySixData);
	}

	@Override
	public String toString() {
		return "EntitySevenData{" + "id=" + (id != null ? id.toString() : "null") + ", " + "dado="
				+ (dado != null ? dado.toString() : "null") + ", " + "entitySixData="
				+ (entitySixData != null ? entitySixData.toString() : "null") + '}';
	}
}
