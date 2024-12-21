package br.com.process.integration.database.domain.model.entity;

import java.time.LocalDate;
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
@Table(name = "Entity_Tree")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityTree extends RepresentationModel<EntityTree> implements BeanEntity<String> {

	@Id
	@Column(name = "id_entity_tree")
	private String id;

	@Column(name = "animal", nullable = false, length = 100)
	private String animal;

	@Column(name = "indicator", nullable = true)
	private Integer indicator;

	@Column(name = "amount", nullable = false, precision = 10, scale = 0)
	private Double amount;

	@Column(name = "local_date", nullable = false)
	private LocalDate localDate;

	@Column(name = "local_date_time", nullable = false)
	private LocalDateTime localDateTime;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_entity_four", nullable = false, referencedColumnName = "id_entity_four")
	private EntityFour entityFour;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_entity_status", nullable = false, referencedColumnName = "id_entity_status")
	private EntityStatus entityStatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAnimal() {
		return animal;
	}

	public void setAnimal(String animal) {
		this.animal = animal;
	}

	public Integer getIndicator() {
		return indicator;
	}

	public void setIndicator(Integer indicator) {
		this.indicator = indicator;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public LocalDate getLocalDate() {
		return localDate;
	}

	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}

	public void setLocalDateTime(LocalDateTime localDateTime) {
		this.localDateTime = localDateTime;
	}

	public EntityFour getEntityFour() {
		return entityFour;
	}

	public void setEntityFour(EntityFour entityFour) {
		this.entityFour = entityFour;
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
		result = prime * result
				+ Objects.hash(amount, animal, entityFour, entityStatus, id, indicator, localDate, localDateTime);
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
		EntityTree other = (EntityTree) obj;
		return Objects.equals(amount, other.amount) && Objects.equals(animal, other.animal)
				&& Objects.equals(entityFour, other.entityFour) && Objects.equals(entityStatus, other.entityStatus)
				&& Objects.equals(id, other.id) && Objects.equals(indicator, other.indicator)
				&& Objects.equals(localDate, other.localDate) && Objects.equals(localDateTime, other.localDateTime);
	}
}
