package br.com.process.integration.database.model.entity.dto.example;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.process.integration.database.core.Constants;
import br.com.process.integration.database.core.domain.BeanEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "entity_four")
public class EntityFour extends RepresentationModel<EntityFour> implements BeanEntity<UUID> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id_entity_four")
	private UUID id;

	@Column(name = "attribute")
	private Integer attribute;

	@Column(name = "fruit", nullable = false, length = 255)
	private String fruit;

	@Column(name = "inclusion_date_time", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime inclusionDateTime;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_entity_status", referencedColumnName = "id_entity_status")
	private EntityStatus entityStatus;
	
	@Column(name = "id_entity_status", insertable = false, updatable = false)
	private Long entityStatusId;

	public Long getEntityStatusId() {
		return entityStatusId;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "id_entity_five", referencedColumnName = "id_entity_five", unique = true)
	private EntityFive entityFive;

	@Column(name = "id_entity_five", insertable = false, updatable = false)
    private UUID entityFiveId;

	public UUID getEntityFiveId() {
		return entityFiveId;
	}

	@Override
	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Integer getAttribute() {
		return this.attribute;
	}

	public void setAttribute(Integer attribute) {
		this.attribute = attribute;
	}

	public String getFruit() {
		return this.fruit;
	}

	public void setFruit(String fruit) {
		this.fruit = fruit;
	}

	public LocalDateTime getInclusionDateTime() {
		return this.inclusionDateTime;
	}

	public void setInclusionDateTime(LocalDateTime inclusionDateTime) {
		this.inclusionDateTime = inclusionDateTime;
	}

	public EntityStatus getEntityStatus() {
		return this.entityStatus;
	}

	public void setEntityStatus(EntityStatus entityStatus) {
		this.entityStatus = entityStatus;
	}

	public EntityFive getEntityFive() {
		return this.entityFive;
	}

	public void setEntityFive(EntityFive entityFive) {
		this.entityFive = entityFive;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityFour that = (EntityFour) o;
		return java.util.Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id);
	}

	@Override
	public String toString() {
		return "EntityFour{" + "id=" + (id != null ? id.toString() : "null") + ", " + "attribute="
				+ (attribute != null ? attribute.toString() : "null") + ", " + "fruit="
				+ (fruit != null ? fruit.toString() : "null") + ", " + "inclusionDateTime="
				+ (inclusionDateTime != null ? inclusionDateTime.toString() : "null") + ", " + "entityStatus="
				+ (entityStatus != null ? entityStatus.toString() : "null") + ", " + "entityFive="
				+ (entityFive != null ? entityFive.toString() : "null") + '}';
	}
}
