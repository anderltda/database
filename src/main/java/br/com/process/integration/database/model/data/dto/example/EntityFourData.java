package br.com.process.integration.database.model.data.dto.example;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanData;
import br.com.process.integration.database.core.util.Constants;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityFourData extends RepresentationModel<EntityFourData> implements BeanData<EntityFourData> {

	private String id;
	private String fruit;
	private Integer attribute;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime inclusionDateTime;

	private EntityStatusData entityStatusData;
	
	private EntityFiveData entityFiveData;

	@Override
	@JsonIgnore
	public EntityFourData getData() {
		return this;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFruit() {
		return fruit;
	}

	public void setFruit(String fruit) {
		this.fruit = fruit;
	}

	public Integer getAttribute() {
		return attribute;
	}

	public void setAttribute(Integer attribute) {
		this.attribute = attribute;
	}

	public LocalDateTime getInclusionDateTime() {
		return inclusionDateTime;
	}

	public void setInclusionDateTime(LocalDateTime inclusionDateTime) {
		this.inclusionDateTime = inclusionDateTime;
	}

	public EntityStatusData getEntityStatusData() {
		return entityStatusData;
	}

	public void setEntityStatusData(EntityStatusData entityStatusData) {
		this.entityStatusData = entityStatusData;
	}

	public EntityFiveData getEntityFiveData() {
		return entityFiveData;
	}

	public void setEntityFiveData(EntityFiveData entityFiveData) {
		this.entityFiveData = entityFiveData;
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
		EntityFourData other = (EntityFourData) obj;
		return Objects.equals(id, other.id);
	}
}
