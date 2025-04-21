package br.com.process.integration.database.model.data.dto.example;
       
import java.time.LocalDate;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanData;
import br.com.process.integration.database.core.util.Constants;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityTwoData extends RepresentationModel<EntityTwoData> implements BeanData<EntityTwoData> {

	private String id;
	private String color;
	private Integer hex;
	private Double cost;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate inclusionDate;
	
	private EntityStatusData entityStatusData;
	private EntityTreeData entityTreeData;

	@Override
	@JsonIgnore
	public EntityTwoData getData() {
		return this;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getHex() {
		return hex;
	}

	public void setHex(Integer hex) {
		this.hex = hex;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public LocalDate getInclusionDate() {
		return inclusionDate;
	}

	public void setInclusionDate(LocalDate inclusionDate) {
		this.inclusionDate = inclusionDate;
	}

	public EntityStatusData getEntityStatusData() {
		return entityStatusData;
	}

	public void setEntityStatusData(EntityStatusData entityStatusData) {
		this.entityStatusData = entityStatusData;
	}

	public EntityTreeData getEntityTreeData() {
		return entityTreeData;
	}

	public void setEntityTreeData(EntityTreeData entityTreeData) {
		this.entityTreeData = entityTreeData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(id);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityTwoData other = (EntityTwoData) obj;
		return Objects.equals(id, other.id);
	}
}
