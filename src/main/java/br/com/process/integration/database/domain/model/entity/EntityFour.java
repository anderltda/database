package br.com.process.integration.database.domain.model.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Entity_Four")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityFour extends RepresentationModel<EntityFour> implements BeanEntity<String> {

	@Id
	@Column(name = "id_entity_four")
	private String id;

	@Column(name = "fruit", nullable = false, length = 100)
	private String fruit;

	@Column(name = "attribute", nullable = true)
	private Integer attribute;

	@Column(name = "inclusion_date_time", nullable = false)
	private LocalDateTime inclusionDateTime;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_entity_five", nullable = false, referencedColumnName = "id_entity_five")
	private EntityFive entityFive;

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

	public EntityFive getEntityFive() {
		return entityFive;
	}

	public void setEntityFive(EntityFive entityFive) {
		this.entityFive = entityFive;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(inclusionDateTime, entityFive, fruit, id, attribute);
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
		EntityFour other = (EntityFour) obj;
		return Objects.equals(inclusionDateTime, other.inclusionDateTime)
				&& Objects.equals(entityFive, other.entityFive) && Objects.equals(fruit, other.fruit)
				&& Objects.equals(id, other.id) && Objects.equals(attribute, other.attribute);
	}
}
