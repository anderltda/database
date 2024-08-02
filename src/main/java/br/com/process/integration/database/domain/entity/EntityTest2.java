package br.com.process.integration.database.domain.entity;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Entity_Test_2")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityTest2 extends RepresentationModel<EntityTest2>
		implements br.com.process.integration.database.core.domain.Entity<String> {

	@Id
	@Column(name = "id_entity_test_2")
	private String id;

	@Column(name = "color", nullable = false, length = 100)
	private String color;

	@Column(name = "hex", nullable = true)
	private Integer hex;

	@Column(name = "cost", nullable = false, precision = 10, scale = 0)
	private Double cost;

	@Column(name = "date_inclusion", nullable = false)
	private LocalDate dateInclusion;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_entity_test_3", nullable = false, referencedColumnName = "id_entity_test_3")
	private EntityTest3 entityTest3;

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

	public EntityTest3 getEntityTest3() {
		return entityTest3;
	}

	public void setEntityTest3(EntityTest3 entityTest3) {
		this.entityTest3 = entityTest3;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(color, cost, dateInclusion, entityTest3, hex, id);
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
		EntityTest2 other = (EntityTest2) obj;
		return Objects.equals(color, other.color) && Objects.equals(cost, other.cost)
				&& Objects.equals(dateInclusion, other.dateInclusion) && Objects.equals(entityTest3, other.entityTest3)
				&& Objects.equals(hex, other.hex) && Objects.equals(id, other.id);
	}

}
