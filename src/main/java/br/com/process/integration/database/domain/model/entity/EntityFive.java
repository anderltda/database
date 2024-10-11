package br.com.process.integration.database.domain.model.entity;

import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Entity_Five")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityFive extends RepresentationModel<EntityFive> implements BeanEntity<String> {

	@Id
	@Column(name = "id_entity_five")
	private String id;

	@Column(name = "reference", nullable = false, length = 100)
	private String reference;

	@Column(name = "factor", nullable = true)
	private Integer factor;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Integer getFactor() {
		return factor;
	}

	public void setFactor(Integer factor) {
		this.factor = factor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(id, reference, factor);
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
		EntityFive other = (EntityFive) obj;
		return Objects.equals(id, other.id) && Objects.equals(reference, other.reference)
				&& Objects.equals(factor, other.factor);
	}

}
