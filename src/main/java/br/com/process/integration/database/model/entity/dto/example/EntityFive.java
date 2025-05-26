package br.com.process.integration.database.model.entity.dto.example;

import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.process.integration.database.core.domain.BeanEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "entity_five")
public class EntityFive extends RepresentationModel<EntityFive> implements BeanEntity<UUID> {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id_entity_five")
	private UUID id;

	@Column(name = "factor", nullable = false)
	private Integer factor;

	@Column(name = "reference", nullable = false, length = 255)
	private String reference;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_entity_status", referencedColumnName = "id_entity_status")
	private EntityStatus entityStatus;
	
	@Column(name = "id_entity_status", insertable = false, updatable = false)
	private Long entityStatusId;

	public Long getEntityStatusId() {
		return entityStatusId;
	}

	@Override
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

	public EntityStatus getEntityStatus() {
		return this.entityStatus;
	}

	public void setEntityStatus(EntityStatus entityStatus) {
		this.entityStatus = entityStatus;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityFive that = (EntityFive) o;
		return java.util.Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id);
	}

	@Override
	public String toString() {
		return "EntityFive{" + "id=" + (id != null ? id.toString() : "null") + ", " + "factor="
				+ (factor != null ? factor.toString() : "null") + ", " + "reference="
				+ (reference != null ? reference.toString() : "null") + ", " + "entityStatus="
				+ (entityStatus != null ? entityStatus.toString() : "null") + '}';
	}
}
