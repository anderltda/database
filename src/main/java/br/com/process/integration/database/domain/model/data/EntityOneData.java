package br.com.process.integration.database.domain.model.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityOneData implements BeanData<EntityOneData> {

	private Long id;
	private String name;
	private Integer age;
	private Double height;
	private LocalDate birthDate;
	private LocalDateTime prohibited;

	private EntityTwoData entityTwo;
	private EntityTreeData entityTree;
	private EntityFourData entityFour;
	private EntityFiveData entityFive;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public EntityTwoData getEntityTwo() {
		return entityTwo;
	}

	public void setEntityTwo(EntityTwoData entityTwo) {
		this.entityTwo = entityTwo;
	}

	public EntityTreeData getEntityTree() {
		return entityTree;
	}

	public void setEntityTree(EntityTreeData entityTree) {
		this.entityTree = entityTree;
	}

	public EntityFourData getEntityFour() {
		return entityFour;
	}

	public void setEntityFour(EntityFourData entityFour) {
		this.entityFour = entityFour;
	}

	public EntityFiveData getEntityFive() {
		return entityFive;
	}

	public void setEntityFive(EntityFiveData entityFive) {
		this.entityFive = entityFive;
	}

	@Override
	public EntityOneData getData() {
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(age, birthDate, entityFive, entityFour, entityTree, entityTwo, height, id, name,
				prohibited);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityOneData other = (EntityOneData) obj;
		return Objects.equals(age, other.age) && Objects.equals(birthDate, other.birthDate)
				&& Objects.equals(entityFive, other.entityFive) && Objects.equals(entityFour, other.entityFour)
				&& Objects.equals(entityTree, other.entityTree) && Objects.equals(entityTwo, other.entityTwo)
				&& Objects.equals(height, other.height) && Objects.equals(id, other.id)
				&& Objects.equals(name, other.name) && Objects.equals(prohibited, other.prohibited);
	}
}
