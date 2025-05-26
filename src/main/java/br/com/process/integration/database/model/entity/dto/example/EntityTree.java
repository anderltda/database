package br.com.process.integration.database.model.entity.dto.example;

import java.time.LocalDate;
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
@Table(name = "entity_tree")
public class EntityTree extends RepresentationModel<EntityTree> implements BeanEntity<UUID> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id_entity_tree")
	private UUID id;

	@Column(name = "amount", nullable = false)
	private Double amount;

	@Column(name = "animal", nullable = false, length = 255)
	private String animal;

	@Column(name = "indicator")
	private Integer indicator;

	@Column(name = "local_date", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate localDate;

	@Column(name = "local_date_time", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime localDateTime;

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
	@JoinColumn(name = "id_entity_four", referencedColumnName = "id_entity_four", unique = true)
	private EntityFour entityFour;

	@Column(name = "id_entity_four", insertable = false, updatable = false)
    private UUID entityFourId;

	public UUID getEntityFourId() {
		return entityFourId;
	}

	@Override
	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getAnimal() {
		return this.animal;
	}

	public void setAnimal(String animal) {
		this.animal = animal;
	}

	public Integer getIndicator() {
		return this.indicator;
	}

	public void setIndicator(Integer indicator) {
		this.indicator = indicator;
	}

	public LocalDate getLocalDate() {
		return this.localDate;
	}

	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}

	public LocalDateTime getLocalDateTime() {
		return this.localDateTime;
	}

	public void setLocalDateTime(LocalDateTime localDateTime) {
		this.localDateTime = localDateTime;
	}

	public EntityStatus getEntityStatus() {
		return this.entityStatus;
	}

	public void setEntityStatus(EntityStatus entityStatus) {
		this.entityStatus = entityStatus;
	}

	public EntityFour getEntityFour() {
		return this.entityFour;
	}

	public void setEntityFour(EntityFour entityFour) {
		this.entityFour = entityFour;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityTree that = (EntityTree) o;
		return java.util.Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id);
	}

	@Override
	public String toString() {
		return "EntityTree{" + "id=" + (id != null ? id.toString() : "null") + ", " + "amount="
				+ (amount != null ? amount.toString() : "null") + ", " + "animal="
				+ (animal != null ? animal.toString() : "null") + ", " + "indicator="
				+ (indicator != null ? indicator.toString() : "null") + ", " + "localDate="
				+ (localDate != null ? localDate.toString() : "null") + ", " + "localDateTime="
				+ (localDateTime != null ? localDateTime.toString() : "null") + ", " + "entityStatus="
				+ (entityStatus != null ? entityStatus.toString() : "null") + ", " + "entityFour="
				+ (entityFour != null ? entityFour.toString() : "null") + '}';
	}
}
