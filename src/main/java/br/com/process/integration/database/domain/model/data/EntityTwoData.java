package br.com.process.integration.database.domain.model.data;

import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.process.integration.database.core.domain.BeanData;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class EntityTwoData extends RepresentationModel<EntityTwoData> implements BeanData<EntityTwoData> {

	private String id;
	private String color;
	private Integer hex;
	private Double cost;
	private LocalDate dateInclusion;

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

	public LocalDate getDateInclusion() {
		return dateInclusion;
	}

	public void setDateInclusion(LocalDate dateInclusion) {
		this.dateInclusion = dateInclusion;
	}

	@Override
	public EntityTwoData getData() {
		return this;
	}

}
