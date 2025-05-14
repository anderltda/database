package br.com.process.integration.database.model.data.dto.example;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanData;
import br.com.process.integration.database.core.util.Constants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityFourData extends RepresentationModel<EntityFourData> implements BeanData<EntityFourData> {
	
	/**
	 * Coluna: id_entity_four
	 */
	@NotNull
	private UUID idEntityFour;

	/**
	 * Coluna: attribute
	 */
	private Integer attribute;

	/**
	 * Coluna: fruit
	 */
	@NotNull
	@Size(max = 255)
	private String fruit;

	/**
	 * Coluna: inclusion_date_time
	 */
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime inclusionDateTime;

	private EntityFiveData entityFiveData;

	private EntityStatusData entityStatusData;

	public UUID getIdEntityFour() {
		return this.idEntityFour;
	}

	public void setIdEntityFour(UUID idEntityFour) {
		this.idEntityFour = idEntityFour;
	}

	public Integer getAttribute() {
		return this.attribute;
	}

	public void setAttribute(Integer attribute) {
		this.attribute = attribute;
	}

	public String getFruit() {
		return this.fruit;
	}

	public void setFruit(String fruit) {
		this.fruit = fruit;
	}

	public LocalDateTime getInclusionDateTime() {
		return this.inclusionDateTime;
	}

	public void setInclusionDateTime(LocalDateTime inclusionDateTime) {
		this.inclusionDateTime = inclusionDateTime;
	}

	public EntityFiveData getEntityFiveData() {
		return this.entityFiveData;
	}

	public void setEntityFiveData(EntityFiveData entityFiveData) {
		this.entityFiveData = entityFiveData;
	}

	public EntityStatusData getEntityStatusData() {
		return this.entityStatusData;
	}

	public void setEntityStatusData(EntityStatusData entityStatusData) {
		this.entityStatusData = entityStatusData;
	}

	@Override
	@JsonIgnore
	public EntityFourData getData() {
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityFourData other = (EntityFourData) o;
		return java.util.Objects.equals(idEntityFour, other.idEntityFour)
				&& java.util.Objects.equals(attribute, other.attribute) && java.util.Objects.equals(fruit, other.fruit)
				&& java.util.Objects.equals(inclusionDateTime, other.inclusionDateTime)
				&& java.util.Objects.equals(entityFiveData, other.entityFiveData)
				&& java.util.Objects.equals(entityStatusData, other.entityStatusData);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(idEntityFour, attribute, fruit, inclusionDateTime, entityFiveData,
				entityStatusData);
	}

	@Override
	public String toString() {
		return "EntityFourData{" + "idEntityFour=" + (idEntityFour != null ? idEntityFour.toString() : "null") + ", "
				+ "attribute=" + (attribute != null ? attribute.toString() : "null") + ", " + "fruit="
				+ (fruit != null ? fruit.toString() : "null") + ", " + "inclusionDateTime="
				+ (inclusionDateTime != null ? inclusionDateTime.toString() : "null") + ", " + "entityFiveData="
				+ (entityFiveData != null ? entityFiveData.toString() : "null") + ", " + "entityStatusData="
				+ (entityStatusData != null ? entityStatusData.toString() : "null") + '}';
	}
}
