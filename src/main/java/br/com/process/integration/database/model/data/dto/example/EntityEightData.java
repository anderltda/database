package br.com.process.integration.database.model.data.dto.example;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanData;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityEightData extends RepresentationModel<EntityEightData> implements BeanData<EntityEightData> {
	
	/**
	 * Coluna: id_entity_eight
	 */
	@NotNull
	private Long idEntityEight;

	/**
	 * Coluna: position
	 */
	@NotNull
	@Size(max = 100)
	private String position;

	/**
	 * Coluna: properties
	 */
	@Size(max = 100)
	private String properties;

	private EntitySevenData entitySevenData;

	public Long getIdEntityEight() {
		return this.idEntityEight;
	}

	public void setIdEntityEight(Long idEntityEight) {
		this.idEntityEight = idEntityEight;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getProperties() {
		return this.properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public EntitySevenData getEntitySevenData() {
		return this.entitySevenData;
	}

	public void setEntitySevenData(EntitySevenData entitySevenData) {
		this.entitySevenData = entitySevenData;
	}

	@Override
	@JsonIgnore
	public EntityEightData getData() {
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityEightData other = (EntityEightData) o;
		return java.util.Objects.equals(idEntityEight, other.idEntityEight)
				&& java.util.Objects.equals(position, other.position)
				&& java.util.Objects.equals(properties, other.properties)
				&& java.util.Objects.equals(entitySevenData, other.entitySevenData);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(idEntityEight, position, properties, entitySevenData);
	}

	@Override
	public String toString() {
		return "EntityEightData{" + "idEntityEight=" + (idEntityEight != null ? idEntityEight.toString() : "null")
				+ ", " + "position=" + (position != null ? position.toString() : "null") + ", " + "properties="
				+ (properties != null ? properties.toString() : "null") + ", " + "entitySevenData="
				+ (entitySevenData != null ? entitySevenData.toString() : "null") + '}';
	}
}
