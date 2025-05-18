package br.com.process.integration.database.model.view.dto.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.Constants;
import br.com.process.integration.database.core.domain.BeanView;

/**
 *
 * // EntityOne
 *
 * // EntityStatus
 *
 * // EntityTwo
 *
 * // EntityTree
 *
 * // EntityFour
 *
 * // EntityFive
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityOneView extends RepresentationModel<EntityOneView> implements BeanView<EntityOneView> {
	
	/**
	 * from entity_one
	 */
	private Long idEntityOne;

	/**
	 * from entity_one
	 */
	private Integer age;

	/**
	 * from entity_one
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate birthDate;

	/**
	 * from entity_one
	 */
	private Boolean code;

	/**
	 * from entity_one
	 */
	private Double height;

	/**
	 * from entity_one
	 */
	private String name;

	/**
	 * from entity_one
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime prohibitedDateTime;

	/**
	 * from entity_one
	 */
	private Long idEntityStatus;

	/**
	 * from entity_one
	 */
	private UUID idEntityTwo;

	/**
	 * from entity_status
	 */
	private Boolean ativo;

	/**
	 * from entity_status
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime startDateTime;

	/**
	 * from entity_status
	 */
	private Integer status;

	/**
	 * from entity_two
	 */
	private String color;

	/**
	 * from entity_two
	 */
	private Double cost;

	/**
	 * from entity_two
	 */
	private Integer hex;

	/**
	 * from entity_two
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate inclusionDate;

	/**
	 * from entity_two
	 */
	private UUID idEntityTree;

	/**
	 * from entity_tree
	 */
	private Double amount;

	/**
	 * from entity_tree
	 */
	private String animal;

	/**
	 * from entity_tree
	 */
	private Integer indicator;

	/**
	 * from entity_tree
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate localDate;

	/**
	 * from entity_tree
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime localDateTime;

	/**
	 * from entity_tree
	 */
	private UUID idEntityFour;

	/**
	 * from entity_four
	 */
	private Integer attribute;

	/**
	 * from entity_four
	 */
	private String fruit;

	/**
	 * from entity_four
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime inclusionDateTime;

	/**
	 * from entity_four
	 */
	private UUID idEntityFive;

	/**
	 * from entity_five
	 */
	private Integer factor;

	/**
	 * from entity_five
	 */
	private String reference;

	public Long getIdEntityOne() {
		return this.idEntityOne;
	}

	public void setIdEntityOne(Long idEntityOne) {
		this.idEntityOne = idEntityOne;
	}

	public Integer getAge() {
		return this.age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public LocalDate getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public Boolean getCode() {
		return this.code;
	}

	public void setCode(Boolean code) {
		this.code = code;
	}

	public Double getHeight() {
		return this.height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getProhibitedDateTime() {
		return this.prohibitedDateTime;
	}

	public void setProhibitedDateTime(LocalDateTime prohibitedDateTime) {
		this.prohibitedDateTime = prohibitedDateTime;
	}

	public Long getIdEntityStatus() {
		return this.idEntityStatus;
	}

	public void setIdEntityStatus(Long idEntityStatus) {
		this.idEntityStatus = idEntityStatus;
	}

	public UUID getIdEntityTwo() {
		return this.idEntityTwo;
	}

	public void setIdEntityTwo(UUID idEntityTwo) {
		this.idEntityTwo = idEntityTwo;
	}

	public Boolean getAtivo() {
		return this.ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public LocalDateTime getStartDateTime() {
		return this.startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public UUID getIdEntityFour() {
		return this.idEntityFour;
	}

	public void setIdEntityFour(UUID idEntityFour) {
		this.idEntityFour = idEntityFour;
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

	public UUID getIdEntityFive() {
		return this.idEntityFive;
	}

	public void setIdEntityFive(UUID idEntityFive) {
		this.idEntityFive = idEntityFive;
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

	@JsonIgnore
	@Override
	public EntityOneView getView() {
		return this;
	}

	@Override
	public String toString() {
		return "EntityOneView{" + "idEntityOne=" + (idEntityOne != null ? idEntityOne.toString() : "null") + ", "
				+ "age=" + (age != null ? age.toString() : "null") + ", " + "birthDate="
				+ (birthDate != null ? birthDate.toString() : "null") + ", " + "code="
				+ (code != null ? code.toString() : "null") + ", " + "height="
				+ (height != null ? height.toString() : "null") + ", " + "name="
				+ (name != null ? name.toString() : "null") + ", " + "prohibitedDateTime="
				+ (prohibitedDateTime != null ? prohibitedDateTime.toString() : "null") + ", " + "idEntityStatus="
				+ (idEntityStatus != null ? idEntityStatus.toString() : "null") + ", " + "idEntityTwo="
				+ (idEntityTwo != null ? idEntityTwo.toString() : "null") + ", " + "ativo="
				+ (ativo != null ? ativo.toString() : "null") + ", " + "startDateTime="
				+ (startDateTime != null ? startDateTime.toString() : "null") + ", " + "status="
				+ (status != null ? status.toString() : "null") + ", " + "color="
				+ (color != null ? color.toString() : "null") + ", " + "cost="
				+ (cost != null ? cost.toString() : "null") + ", " + "hex=" + (hex != null ? hex.toString() : "null")
				+ ", " + "inclusionDate=" + (inclusionDate != null ? inclusionDate.toString() : "null") + ", "
				+ "idEntityTree=" + (idEntityTree != null ? idEntityTree.toString() : "null") + ", " + "amount="
				+ (amount != null ? amount.toString() : "null") + ", " + "animal="
				+ (animal != null ? animal.toString() : "null") + ", " + "indicator="
				+ (indicator != null ? indicator.toString() : "null") + ", " + "localDate="
				+ (localDate != null ? localDate.toString() : "null") + ", " + "localDateTime="
				+ (localDateTime != null ? localDateTime.toString() : "null") + ", " + "idEntityFour="
				+ (idEntityFour != null ? idEntityFour.toString() : "null") + ", " + "attribute="
				+ (attribute != null ? attribute.toString() : "null") + ", " + "fruit="
				+ (fruit != null ? fruit.toString() : "null") + ", " + "inclusionDateTime="
				+ (inclusionDateTime != null ? inclusionDateTime.toString() : "null") + ", " + "idEntityFive="
				+ (idEntityFive != null ? idEntityFive.toString() : "null") + ", " + "factor="
				+ (factor != null ? factor.toString() : "null") + ", " + "reference="
				+ (reference != null ? reference.toString() : "null") + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityOneView that = (EntityOneView) o;
		return java.util.Objects.equals(idEntityOne, that.idEntityOne) && java.util.Objects.equals(age, that.age)
				&& java.util.Objects.equals(birthDate, that.birthDate) && java.util.Objects.equals(code, that.code)
				&& java.util.Objects.equals(height, that.height) && java.util.Objects.equals(name, that.name)
				&& java.util.Objects.equals(prohibitedDateTime, that.prohibitedDateTime)
				&& java.util.Objects.equals(idEntityStatus, that.idEntityStatus)
				&& java.util.Objects.equals(idEntityTwo, that.idEntityTwo)
				&& java.util.Objects.equals(ativo, that.ativo)
				&& java.util.Objects.equals(startDateTime, that.startDateTime)
				&& java.util.Objects.equals(status, that.status) && java.util.Objects.equals(color, that.color)
				&& java.util.Objects.equals(cost, that.cost) && java.util.Objects.equals(hex, that.hex)
				&& java.util.Objects.equals(inclusionDate, that.inclusionDate)
				&& java.util.Objects.equals(idEntityTree, that.idEntityTree)
				&& java.util.Objects.equals(amount, that.amount) && java.util.Objects.equals(animal, that.animal)
				&& java.util.Objects.equals(indicator, that.indicator)
				&& java.util.Objects.equals(localDate, that.localDate)
				&& java.util.Objects.equals(localDateTime, that.localDateTime)
				&& java.util.Objects.equals(idEntityFour, that.idEntityFour)
				&& java.util.Objects.equals(attribute, that.attribute) && java.util.Objects.equals(fruit, that.fruit)
				&& java.util.Objects.equals(inclusionDateTime, that.inclusionDateTime)
				&& java.util.Objects.equals(idEntityFive, that.idEntityFive)
				&& java.util.Objects.equals(factor, that.factor) && java.util.Objects.equals(reference, that.reference);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(idEntityOne, age, birthDate, code, height, name, prohibitedDateTime,
				idEntityStatus, idEntityTwo, ativo, startDateTime, status, color, cost, hex, inclusionDate,
				idEntityTree, amount, animal, indicator, localDate, localDateTime, idEntityFour, attribute, fruit,
				inclusionDateTime, idEntityFive, factor, reference);
	}
}
