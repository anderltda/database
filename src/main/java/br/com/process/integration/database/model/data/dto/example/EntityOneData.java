package br.com.process.integration.database.model.data.dto.example;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.Constants;
import br.com.process.integration.database.core.domain.BeanData;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityOneData extends RepresentationModel<EntityOneData> implements BeanData<EntityOneData> {

	@NotNull
	private Long id;

	private Integer age;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate birthDate;

	@NotNull
	private Boolean code;

	@NotNull
	private Double height;

	@NotNull
	@Size(max = 255)
	private String name;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime prohibitedDateTime;

	private EntityStatusData entityStatusData;

	private EntityTwoData entityTwoData;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public EntityStatusData getEntityStatusData() {
		return this.entityStatusData;
	}

	public void setEntityStatusData(EntityStatusData entityStatusData) {
		this.entityStatusData = entityStatusData;
	}

	public EntityTwoData getEntityTwoData() {
		return this.entityTwoData;
	}

	public void setEntityTwoData(EntityTwoData entityTwoData) {
		this.entityTwoData = entityTwoData;
	}

	@Override
	@JsonIgnore
	public EntityOneData getData() {
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityOneData other = (EntityOneData) o;
		return java.util.Objects.equals(id, other.id) && java.util.Objects.equals(age, other.age)
				&& java.util.Objects.equals(birthDate, other.birthDate) && java.util.Objects.equals(code, other.code)
				&& java.util.Objects.equals(height, other.height) && java.util.Objects.equals(name, other.name)
				&& java.util.Objects.equals(prohibitedDateTime, other.prohibitedDateTime)
				&& java.util.Objects.equals(entityStatusData, other.entityStatusData)
				&& java.util.Objects.equals(entityTwoData, other.entityTwoData);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id, age, birthDate, code, height, name, prohibitedDateTime, entityStatusData,
				entityTwoData);
	}

	@Override
	public String toString() {
		return "EntityOneData{" + "id=" + (id != null ? id.toString() : "null") + ", " + "age="
				+ (age != null ? age.toString() : "null") + ", " + "birthDate="
				+ (birthDate != null ? birthDate.toString() : "null") + ", " + "code="
				+ (code != null ? code.toString() : "null") + ", " + "height="
				+ (height != null ? height.toString() : "null") + ", " + "name="
				+ (name != null ? name.toString() : "null") + ", " + "prohibitedDateTime="
				+ (prohibitedDateTime != null ? prohibitedDateTime.toString() : "null") + ", " + "entityStatusData="
				+ (entityStatusData != null ? entityStatusData.toString() : "null") + ", " + "entityTwoData="
				+ (entityTwoData != null ? entityTwoData.toString() : "null") + '}';
	}
}
