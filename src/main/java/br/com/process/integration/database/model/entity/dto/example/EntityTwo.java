package br.com.process.integration.database.model.entity.dto.example;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

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
@Table(name = "entity_two")
public class EntityTwo extends RepresentationModel<EntityTwo> implements BeanEntity<UUID> {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id_entity_two")
	private UUID id;

	@Column(name = "color", nullable = false, length = 255)
	private String color;

	@Column(name = "cost", nullable = false)
	private Double cost;

	@Column(name = "hex")
	private Integer hex;

	@Column(name = "inclusion_date", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate inclusionDate;

	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_entity_status", referencedColumnName = "id_entity_status")
	private EntityStatus entityStatus;

	@Column(name = "id_entity_status", insertable = false, updatable = false)
	private Long idEntityStatus;

	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
	@JoinColumn(name = "id_entity_tree", referencedColumnName = "id_entity_tree", unique = true)
	private EntityTree entityTree;

	@Column(name = "id_entity_tree", insertable = false, updatable = false)
	private UUID idEntityTree;

	@Override
	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Double getCost() {
		return this.cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Integer getHex() {
		return this.hex;
	}

	public void setHex(Integer hex) {
		this.hex = hex;
	}

	public LocalDate getInclusionDate() {
		return this.inclusionDate;
	}

	public void setInclusionDate(LocalDate inclusionDate) {
		this.inclusionDate = inclusionDate;
	}

	public EntityStatus getEntityStatus() {
		return this.entityStatus;
	}

	public void setEntityStatus(EntityStatus entityStatus) {
		this.entityStatus = entityStatus;
	}

	public Long getIdEntityStatus() {
		return this.idEntityStatus;
	}

	public void setIdEntityStatus(Long idEntityStatus) {
		this.idEntityStatus = idEntityStatus;
	}

	public EntityTree getEntityTree() {
		return this.entityTree;
	}

	public void setEntityTree(EntityTree entityTree) {
		this.entityTree = entityTree;
	}

	public UUID getIdEntityTree() {
		return this.idEntityTree;
	}

	public void setIdEntityTree(UUID idEntityTree) {
		this.idEntityTree = idEntityTree;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityTwo that = (EntityTwo) o;
		return java.util.Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id);
	}

	@Override
	public String toString() {
		return "EntityTwo{" + "id=" + (id != null ? id.toString() : "null") + ", " + "color="
				+ (color != null ? color.toString() : "null") + ", " + "cost="
				+ (cost != null ? cost.toString() : "null") + ", " + "hex=" + (hex != null ? hex.toString() : "null")
				+ ", " + "inclusionDate=" + (inclusionDate != null ? inclusionDate.toString() : "null") + ", "
				+ "entityStatus=" + (entityStatus != null ? entityStatus.toString() : "null") + ", " + "entityStatus="
				+ (idEntityStatus != null ? idEntityStatus.toString() : "null") + ", " + "entityTree="
				+ (entityTree != null ? entityTree.toString() : "null") + ", " + "entityTree="
				+ (idEntityTree != null ? idEntityTree.toString() : "null") + '}';
	}
}
