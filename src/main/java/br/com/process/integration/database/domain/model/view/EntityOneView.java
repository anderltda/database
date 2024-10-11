package br.com.process.integration.database.domain.model.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanView;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityOneView extends RepresentationModel<EntityOneView> implements BeanView<EntityOneView> {

	// EntityOne
	private Long idEntityOne;
	private String name;
	private Integer age;
	private Integer code;
	private Double height;
	private LocalDate birthDate;
	private LocalDateTime prohibitedDateTime;
	private Long count;

	// EntityTwo
	private String idEntityTwo;
	private String color;
	private Integer hex;
	private Double cost;
	private LocalDate dateInclusion;

	// EntityTree
	private String idEntityTree;
	private String animal;
	private Integer number;
	private Double value3;
	private LocalDate dataLocal;
	private LocalDateTime dataLocalTime;

	// EntityFour
	private String idEntityFour;
	private String fruit;
	private Integer nutritiou;
	private LocalDateTime dateInclusionTime;

	// EntityFive
	private String idEntityFive;
	private String object;
	private Integer value5;

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

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
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

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getIdEntityTwo() {
		return idEntityTwo;
	}

	public void setIdEntityTwo(String idEntityTwo) {
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

	public LocalDate getDateInclusion() {
		return dateInclusion;
	}

	public void setDateInclusion(LocalDate dateInclusion) {
		this.dateInclusion = dateInclusion;
	}

	public String getIdEntityTree() {
		return idEntityTree;
	}

	public void setIdEntityTree(String idEntityTree) {
		this.idEntityTree = idEntityTree;
	}

	public String getAnimal() {
		return animal;
	}

	public void setAnimal(String animal) {
		this.animal = animal;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Double getValue3() {
		return value3;
	}

	public void setValue3(Double value3) {
		this.value3 = value3;
	}

	public LocalDate getDataLocal() {
		return dataLocal;
	}

	public void setDataLocal(LocalDate dataLocal) {
		this.dataLocal = dataLocal;
	}

	public LocalDateTime getDataLocalTime() {
		return dataLocalTime;
	}

	public void setDataLocalTime(LocalDateTime dataLocalTime) {
		this.dataLocalTime = dataLocalTime;
	}

	public String getIdEntityFour() {
		return idEntityFour;
	}

	public void setIdEntityFour(String idEntityFour) {
		this.idEntityFour = idEntityFour;
	}

	public String getFruit() {
		return fruit;
	}

	public void setFruit(String fruit) {
		this.fruit = fruit;
	}

	public Integer getNutritiou() {
		return nutritiou;
	}

	public void setNutritiou(Integer nutritiou) {
		this.nutritiou = nutritiou;
	}

	public LocalDateTime getDateInclusionTime() {
		return dateInclusionTime;
	}

	public void setDateInclusionTime(LocalDateTime dateInclusionTime) {
		this.dateInclusionTime = dateInclusionTime;
	}

	public String getIdEntityFive() {
		return idEntityFive;
	}

	public void setIdEntityFive(String idEntityFive) {
		this.idEntityFive = idEntityFive;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public Integer getValue5() {
		return value5;
	}

	public void setValue5(Integer value5) {
		this.value5 = value5;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(age, animal, birthDate, code, color, cost, count, dataLocal,
				dataLocalTime, dateInclusion, dateInclusionTime, fruit, height, hex, idEntityFive, idEntityFour,
				idEntityOne, idEntityTree, idEntityTwo, name, number, nutritiou, object, prohibitedDateTime, value3, value5);
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
		return Objects.equals(age, other.age) && Objects.equals(animal, other.animal)
				&& Objects.equals(birthDate, other.birthDate) && Objects.equals(code, other.code)
				&& Objects.equals(color, other.color) && Objects.equals(cost, other.cost)
				&& Objects.equals(count, other.count) && Objects.equals(dataLocal, other.dataLocal)
				&& Objects.equals(dataLocalTime, other.dataLocalTime)
				&& Objects.equals(dateInclusion, other.dateInclusion)
				&& Objects.equals(dateInclusionTime, other.dateInclusionTime) && Objects.equals(fruit, other.fruit)
				&& Objects.equals(height, other.height) && Objects.equals(hex, other.hex)
				&& Objects.equals(idEntityFive, other.idEntityFive) && Objects.equals(idEntityFour, other.idEntityFour)
				&& Objects.equals(idEntityOne, other.idEntityOne) && Objects.equals(idEntityTree, other.idEntityTree)
				&& Objects.equals(idEntityTwo, other.idEntityTwo) && Objects.equals(name, other.name)
				&& Objects.equals(number, other.number) && Objects.equals(nutritiou, other.nutritiou)
				&& Objects.equals(object, other.object) && Objects.equals(prohibitedDateTime, other.prohibitedDateTime)
				&& Objects.equals(value3, other.value3) && Objects.equals(value5, other.value5);
	}

}
