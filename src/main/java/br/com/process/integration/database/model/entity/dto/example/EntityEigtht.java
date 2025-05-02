package br.com.process.integration.database.model.entity.dto.example;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "entity_eigtht")
public class EntityEigtht extends RepresentationModel<EntityEigtht> implements BeanEntity<Long> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_entity_eigtht")
	private Long id;

	@Column(name = "position", nullable = false, length = 100)
	private String position;

	@Column(name = "properties", length = 100)
	private String properties;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "id_entity_seven", referencedColumnName = "id_entity_seven"),
			@JoinColumn(name = "id_entity_six", referencedColumnName = "id_entity_six") })
	private EntitySeven entitySeven;

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getProperties() {
		return this.properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public EntitySeven getEntitySeven() {
		return this.entitySeven;
	}

	public void setEntitySeven(EntitySeven entitySeven) {
		this.entitySeven = entitySeven;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityEigtht that = (EntityEigtht) o;
		return java.util.Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id);
	}

	@Override
	public String toString() {
		return "EntityEigtht{" + "id=" + (id != null ? id.toString() : "null") + ", " + "position="
				+ (position != null ? position.toString() : "null") + ", " + "properties="
				+ (properties != null ? properties.toString() : "null") + ", " + "entitySeven="
				+ (entitySeven != null ? entitySeven.toString() : "null") + '}';
	}
}
