package br.com.process.integration.database.model.data.dto.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanData;
import br.com.process.integration.database.core.util.Constants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityTreeData extends RepresentationModel<EntityTreeData> implements BeanData<EntityTreeData> {

	/**
	 * Coluna: id_entity_tree
	 */
	@NotNull
	private UUID idEntityTree;

	/**
	 * Coluna: amount
	 */
	@NotNull
	private Double amount;

	/**
	 * Coluna: animal
	 */
	@NotNull
	@Size(max = 255)
	private String animal;

	/**
	 * Coluna: indicator
	 */
	private Integer indicator;

	/**
	 * Coluna: local_date
	 */
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate localDate;

	/**
	 * Coluna: local_date_time
	 */
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime localDateTime;

	private EntityFourData entityFourData;

	private EntityStatusData entityStatusData;

	public UUID getIdEntityTree() {
		return this.idEntityTree;
	}

	public void setIdEntityTree(UUID idEntityTree) {
		this.idEntityTree = idEntityTree;
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

	public EntityFourData getEntityFourData() {
		return this.entityFourData;
	}

	public void setEntityFourData(EntityFourData entityFourData) {
		this.entityFourData = entityFourData;
	}

	public EntityStatusData getEntityStatusData() {
		return this.entityStatusData;
	}

	public void setEntityStatusData(EntityStatusData entityStatusData) {
		this.entityStatusData = entityStatusData;
	}

	@Override
	@JsonIgnore
	public EntityTreeData getData() {
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityTreeData other = (EntityTreeData) o;
		return java.util.Objects.equals(idEntityTree, other.idEntityTree)
				&& java.util.Objects.equals(amount, other.amount) && java.util.Objects.equals(animal, other.animal)
				&& java.util.Objects.equals(indicator, other.indicator)
				&& java.util.Objects.equals(localDate, other.localDate)
				&& java.util.Objects.equals(localDateTime, other.localDateTime)
				&& java.util.Objects.equals(entityFourData, other.entityFourData)
				&& java.util.Objects.equals(entityStatusData, other.entityStatusData);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(idEntityTree, amount, animal, indicator, localDate, localDateTime, entityFourData,
				entityStatusData);
	}

	@Override
	public String toString() {
		return "EntityTreeData{" + "idEntityTree=" + (idEntityTree != null ? idEntityTree.toString() : "null") + ", "
				+ "amount=" + (amount != null ? amount.toString() : "null") + ", " + "animal="
				+ (animal != null ? animal.toString() : "null") + ", " + "indicator="
				+ (indicator != null ? indicator.toString() : "null") + ", " + "localDate="
				+ (localDate != null ? localDate.toString() : "null") + ", " + "localDateTime="
				+ (localDateTime != null ? localDateTime.toString() : "null") + ", " + "entityFourData="
				+ (entityFourData != null ? entityFourData.toString() : "null") + ", " + "entityStatusData="
				+ (entityStatusData != null ? entityStatusData.toString() : "null") + '}';
	}
}
