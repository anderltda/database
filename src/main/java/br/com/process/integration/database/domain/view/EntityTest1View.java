package br.com.process.integration.database.domain.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityTest1View extends RepresentationModel<EntityTest1View> {

	// EntityTest1
	private Long idEntityTest1;
	private String name;
	private Integer age;
	private Double height;
	private LocalDate birthDate;
	private LocalDateTime prohibited;

	// EntityTest2
	private String idEntityTest2;
	private String color;
	private Integer hex;
	private Double cost;
	private LocalDate dateInclusion;

	// EntityTest3
	private String idEntityTest3;
	private String animal;
	private Integer number;
	private Double value3;
	private LocalDate dataLocal;
	private LocalDateTime dataLocalTime;

	// EntityTest4
	private String idEntityTest4;
	private String fruit;
	private Integer nutritiou;
	private LocalDateTime dateInclusionTime;

	// EntityTest5
	private String idEntityTest5;
	private String object;
	private Integer value5;

	public Long getIdEntityTest1() {
		return idEntityTest1;
	}

	public void setIdEntityTest1(Long idEntityTest1) {
		this.idEntityTest1 = idEntityTest1;
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

	public LocalDateTime getProhibited() {
		return prohibited;
	}

	public void setProhibited(LocalDateTime prohibited) {
		this.prohibited = prohibited;
	}

	public String getIdEntityTest2() {
		return idEntityTest2;
	}

	public void setIdEntityTest2(String idEntityTest2) {
		this.idEntityTest2 = idEntityTest2;
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

	public String getIdEntityTest3() {
		return idEntityTest3;
	}

	public void setIdEntityTest3(String idEntityTest3) {
		this.idEntityTest3 = idEntityTest3;
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

	public String getIdEntityTest4() {
		return idEntityTest4;
	}

	public void setIdEntityTest4(String idEntityTest4) {
		this.idEntityTest4 = idEntityTest4;
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

	public String getIdEntityTest5() {
		return idEntityTest5;
	}

	public void setIdEntityTest5(String idEntityTest5) {
		this.idEntityTest5 = idEntityTest5;
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
		result = prime * result + Objects.hash(age, animal, birthDate, color, cost, dataLocal, dataLocalTime,
				dateInclusion, dateInclusionTime, fruit, height, hex, idEntityTest1, idEntityTest2, idEntityTest3,
				idEntityTest4, idEntityTest5, name, number, nutritiou, object, prohibited, value3, value5);
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
		EntityTest1View other = (EntityTest1View) obj;
		return Objects.equals(age, other.age) && Objects.equals(animal, other.animal)
				&& Objects.equals(birthDate, other.birthDate) && Objects.equals(color, other.color)
				&& Objects.equals(cost, other.cost) && Objects.equals(dataLocal, other.dataLocal)
				&& Objects.equals(dataLocalTime, other.dataLocalTime)
				&& Objects.equals(dateInclusion, other.dateInclusion)
				&& Objects.equals(dateInclusionTime, other.dateInclusionTime) && Objects.equals(fruit, other.fruit)
				&& Objects.equals(height, other.height) && Objects.equals(hex, other.hex)
				&& Objects.equals(idEntityTest1, other.idEntityTest1)
				&& Objects.equals(idEntityTest2, other.idEntityTest2)
				&& Objects.equals(idEntityTest3, other.idEntityTest3)
				&& Objects.equals(idEntityTest4, other.idEntityTest4)
				&& Objects.equals(idEntityTest5, other.idEntityTest5) && Objects.equals(name, other.name)
				&& Objects.equals(number, other.number) && Objects.equals(nutritiou, other.nutritiou)
				&& Objects.equals(object, other.object) && Objects.equals(prohibited, other.prohibited)
				&& Objects.equals(value3, other.value3) && Objects.equals(value5, other.value5);
	}

}
