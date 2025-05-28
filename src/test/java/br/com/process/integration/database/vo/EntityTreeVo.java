package br.com.process.integration.database.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.Constants;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityTreeVo {
	
	private UUID id;
	private Double amount;
	private String animal;
	private Integer indicator;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate localDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime localDateTime;
	private EntityStatusVo entityStatus;
	private Long idEntityStatus;
	private EntityFourVo entityFour;
	private UUID idEntityFour;

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

	public EntityStatusVo getEntityStatus() {
		return this.entityStatus;
	}

	public void setEntityStatus(EntityStatusVo entityStatus) {
		this.entityStatus = entityStatus;
	}

	public Long getIdEntityStatus() {
		return this.idEntityStatus;
	}

	public void setIdEntityStatus(Long idEntityStatus) {
		this.idEntityStatus = idEntityStatus;
	}

	public EntityFourVo getEntityFour() {
		return this.entityFour;
	}

	public void setEntityFour(EntityFourVo entityFour) {
		this.entityFour = entityFour;
	}

	public UUID getIdEntityFour() {
		return this.idEntityFour;
	}

	public void setIdEntityFour(UUID idEntityFour) {
		this.idEntityFour = idEntityFour;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityTreeVo that = (EntityTreeVo) o;
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
				+ (entityStatus != null ? entityStatus.toString() : "null") + ", " + "entityStatus="
				+ (idEntityStatus != null ? idEntityStatus.toString() : "null") + ", " + "entityFour="
				+ (entityFour != null ? entityFour.toString() : "null") + ", " + "entityFour="
				+ (idEntityFour != null ? idEntityFour.toString() : "null") + '}';
	}
}
