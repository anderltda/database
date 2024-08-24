package br.com.process.integration.database.domain.model.data;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityTwoData implements BeanData<EntityTwoData> {

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

	@Override
	public int hashCode() {
		return Objects.hash(color, cost, dateInclusion, hex, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityTwoData other = (EntityTwoData) obj;
		return Objects.equals(color, other.color) && Objects.equals(cost, other.cost)
				&& Objects.equals(dateInclusion, other.dateInclusion) && Objects.equals(hex, other.hex)
				&& Objects.equals(id, other.id);
	}

}
