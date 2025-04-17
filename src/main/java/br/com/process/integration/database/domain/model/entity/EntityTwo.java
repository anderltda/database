package br.com.process.integration.database.domain.model.entity;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanEntity;
import br.com.process.integration.database.core.util.Constants;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Entity_Two")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityTwo extends RepresentationModel<EntityTwo> implements BeanEntity<String> {

	@Id
	@Column(name = "id_entity_two")
	private String id;

	@Column(name = "color", nullable = false, length = 100)
	private String color;

	@Column(name = "hex", nullable = true)
	private Integer hex;

	@Column(name = "cost", nullable = false, precision = 10, scale = 0)
	private Double cost;

	@Column(name = "inclusion_date", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate inclusionDate;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_entity_tree", nullable = false, referencedColumnName = "id_entity_tree")
	private EntityTree entityTree;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_entity_status", nullable = false, referencedColumnName = "id_entity_status")
	private EntityStatus entityStatus;

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

	public EntityTree getEntityTree() {
		return entityTree;
	}

	public void setEntityTree(EntityTree entityTree) {
		this.entityTree = entityTree;
	}

	public EntityStatus getEntityStatus() {
		return entityStatus;
	}

	public void setEntityStatus(EntityStatus entityStatus) {
		this.entityStatus = entityStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(color, cost, entityStatus, entityTree, hex, id, inclusionDate);
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
		EntityTwo other = (EntityTwo) obj;
		return Objects.equals(color, other.color) && Objects.equals(cost, other.cost)
				&& Objects.equals(entityStatus, other.entityStatus) && Objects.equals(entityTree, other.entityTree)
				&& Objects.equals(hex, other.hex) && Objects.equals(id, other.id)
				&& Objects.equals(inclusionDate, other.inclusionDate);
	}

}
