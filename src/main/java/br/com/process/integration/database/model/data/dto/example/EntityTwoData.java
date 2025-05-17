package br.com.process.integration.database.model.data.dto.example;

import java.time.LocalDate;
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
public class EntityTwoData extends RepresentationModel<EntityTwoData> implements BeanData<EntityTwoData> {

	@NotNull
	private UUID id;

	@NotNull
	@Size(max = 255)
	private String color;

	@NotNull
	private Double cost;

	private Integer hex;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate inclusionDate;

	private EntityStatusData entityStatusData;

	private EntityTreeData entityTreeData;

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Double getCost() {
		return this.cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Integer getHex() {
		return this.hex;
	}

	public void setHex(Integer hex) {
		this.hex = hex;
	}

	public LocalDate getInclusionDate() {
		return this.inclusionDate;
	}

	public void setInclusionDate(LocalDate inclusionDate) {
		this.inclusionDate = inclusionDate;
	}

	public EntityStatusData getEntityStatusData() {
		return this.entityStatusData;
	}

	public void setEntityStatusData(EntityStatusData entityStatusData) {
		this.entityStatusData = entityStatusData;
	}

	public EntityTreeData getEntityTreeData() {
		return this.entityTreeData;
	}

	public void setEntityTreeData(EntityTreeData entityTreeData) {
		this.entityTreeData = entityTreeData;
	}

	@Override
	@JsonIgnore
	public EntityTwoData getData() {
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityTwoData other = (EntityTwoData) o;
		return java.util.Objects.equals(id, other.id) && java.util.Objects.equals(color, other.color)
				&& java.util.Objects.equals(cost, other.cost) && java.util.Objects.equals(hex, other.hex)
				&& java.util.Objects.equals(inclusionDate, other.inclusionDate)
				&& java.util.Objects.equals(entityStatusData, other.entityStatusData)
				&& java.util.Objects.equals(entityTreeData, other.entityTreeData);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id, color, cost, hex, inclusionDate, entityStatusData, entityTreeData);
	}

	@Override
	public String toString() {
		return "EntityTwoData{" + "id=" + (id != null ? id.toString() : "null") + ", " + "color="
				+ (color != null ? color.toString() : "null") + ", " + "cost="
				+ (cost != null ? cost.toString() : "null") + ", " + "hex=" + (hex != null ? hex.toString() : "null")
				+ ", " + "inclusionDate=" + (inclusionDate != null ? inclusionDate.toString() : "null") + ", "
				+ "entityStatusData=" + (entityStatusData != null ? entityStatusData.toString() : "null") + ", "
				+ "entityTreeData=" + (entityTreeData != null ? entityTreeData.toString() : "null") + '}';
	}
}
