package br.com.process.integration.database.model.data.dto.example;

import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanData;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityFiveData extends RepresentationModel<EntityFiveData> implements BeanData<EntityFiveData> {

	@NotNull
	private UUID id;

	@NotNull
	private Integer factor;

	@NotNull
	@Size(max = 255)
	private String reference;

	private EntityStatusData entityStatusData;

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Integer getFactor() {
		return this.factor;
	}

	public void setFactor(Integer factor) {
		this.factor = factor;
	}

	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public EntityStatusData getEntityStatusData() {
		return this.entityStatusData;
	}

	public void setEntityStatusData(EntityStatusData entityStatusData) {
		this.entityStatusData = entityStatusData;
	}

	@Override
	@JsonIgnore
	public EntityFiveData getData() {
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityFiveData other = (EntityFiveData) o;
		return java.util.Objects.equals(id, other.id) && java.util.Objects.equals(factor, other.factor)
				&& java.util.Objects.equals(reference, other.reference)
				&& java.util.Objects.equals(entityStatusData, other.entityStatusData);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id, factor, reference, entityStatusData);
	}

	@Override
	public String toString() {
		return "EntityFiveData{" + "id=" + (id != null ? id.toString() : "null") + ", " + "factor="
				+ (factor != null ? factor.toString() : "null") + ", " + "reference="
				+ (reference != null ? reference.toString() : "null") + ", " + "entityStatusData="
				+ (entityStatusData != null ? entityStatusData.toString() : "null") + '}';
	}
}
