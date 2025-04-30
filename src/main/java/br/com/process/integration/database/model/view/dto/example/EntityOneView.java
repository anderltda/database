package br.com.process.integration.database.model.view.dto.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanView;
import br.com.process.integration.database.core.util.Constants;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityOneView extends RepresentationModel<EntityOneView> implements BeanView<EntityOneView> {

	// EntityOne
	private Long idEntityOne;
	private String name;
	private Integer age;
	private Boolean code;
	private Double height;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate birthDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime prohibitedDateTime;

	// EntityStatus
	private Long idEntityStatus;
	private String nameStatus;
	private Integer status;
	private Boolean ativo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime startDateTime;

	// EntityTwo
	private UUID idEntityTwo;
	private String color;
	private Integer hex;
	private Double cost;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate inclusionDate;

	// EntityTree
	private UUID idEntityTree;
	private String animal;
	private Integer indicator;
	private Double amount;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate localDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime localDateTime;

	// EntityFour
	private UUID idEntityFour;
	private String fruit;
	private Integer attribute;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime inclusionDateTime;

	// EntityFive
	private UUID idEntityFive;
	private String reference;
	private Integer factor;

	@Override
	@JsonIgnore
	public EntityOneView getView() {
		return this;
	}

	public Long getIdEntityOne() {
		return idEntityOne;
	}

	public void setIdEntityOne(Long idEntityOne) {
		this.idEntityOne = idEntityOne;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Boolean getCode() {
		return code;
	}

	public void setCode(Boolean code) {
		this.code = code;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDateTime getProhibitedDateTime() {
		return prohibitedDateTime;
	}

	public void setProhibitedDateTime(LocalDateTime prohibitedDateTime) {
		this.prohibitedDateTime = prohibitedDateTime;
	}

	public Long getIdEntityStatus() {
		return idEntityStatus;
	}

	public void setIdEntityStatus(Long idEntityStatus) {
		this.idEntityStatus = idEntityStatus;
	}

	public String getNameStatus() {
		return nameStatus;
	}

	public void setNameStatus(String nameStatus) {
		this.nameStatus = nameStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public UUID getIdEntityTwo() {
		return idEntityTwo;
	}

	public void setIdEntityTwo(UUID idEntityTwo) {
		this.idEntityTwo = idEntityTwo;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getHex() {
		return hex;
	}

	public void setHex(Integer hex) {
		this.hex = hex;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public LocalDate getInclusionDate() {
		return inclusionDate;
	}

	public void setInclusionDate(LocalDate inclusionDate) {
		this.inclusionDate = inclusionDate;
	}

	public UUID getIdEntityTree() {
		return idEntityTree;
	}

	public void setIdEntityTree(UUID idEntityTree) {
		this.idEntityTree = idEntityTree;
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

	public UUID getIdEntityFour() {
		return idEntityFour;
	}

	public void setIdEntityFour(UUID idEntityFour) {
		this.idEntityFour = idEntityFour;
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

	public UUID getIdEntityFive() {
		return idEntityFive;
	}

	public void setIdEntityFive(UUID idEntityFive) {
		this.idEntityFive = idEntityFive;
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
		result = prime * result + Objects.hash(age, amount, animal, ativo, attribute, birthDate, code, color, cost,
				factor, fruit, height, hex, idEntityFive, idEntityFour, idEntityOne, idEntityStatus, idEntityTree,
				idEntityTwo, inclusionDate, inclusionDateTime, indicator, localDate, localDateTime, name, nameStatus,
				prohibitedDateTime, reference, startDateTime, status);
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
		EntityOneView other = (EntityOneView) obj;
		return Objects.equals(age, other.age) && Objects.equals(amount, other.amount)
				&& Objects.equals(animal, other.animal) && Objects.equals(ativo, other.ativo)
				&& Objects.equals(attribute, other.attribute) && Objects.equals(birthDate, other.birthDate)
				&& Objects.equals(code, other.code) && Objects.equals(color, other.color)
				&& Objects.equals(cost, other.cost) && Objects.equals(factor, other.factor)
				&& Objects.equals(fruit, other.fruit) && Objects.equals(height, other.height)
				&& Objects.equals(hex, other.hex) && Objects.equals(idEntityFive, other.idEntityFive)
				&& Objects.equals(idEntityFour, other.idEntityFour) && Objects.equals(idEntityOne, other.idEntityOne)
				&& Objects.equals(idEntityStatus, other.idEntityStatus)
				&& Objects.equals(idEntityTree, other.idEntityTree) && Objects.equals(idEntityTwo, other.idEntityTwo)
				&& Objects.equals(inclusionDate, other.inclusionDate)
				&& Objects.equals(inclusionDateTime, other.inclusionDateTime)
				&& Objects.equals(indicator, other.indicator) && Objects.equals(localDate, other.localDate)
				&& Objects.equals(localDateTime, other.localDateTime) && Objects.equals(name, other.name)
				&& Objects.equals(nameStatus, other.nameStatus)
				&& Objects.equals(prohibitedDateTime, other.prohibitedDateTime)
				&& Objects.equals(reference, other.reference) && Objects.equals(startDateTime, other.startDateTime)
				&& Objects.equals(status, other.status);
	}
}
