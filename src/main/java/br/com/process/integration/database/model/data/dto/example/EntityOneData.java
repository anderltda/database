package br.com.process.integration.database.model.data.dto.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanData;
import br.com.process.integration.database.core.util.Constants;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityOneData extends RepresentationModel<EntityOneData> implements BeanData<EntityOneData> {

	private Long id;
	private String name;
	private Integer age;
	private Boolean code;
	private Double height;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate birthDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime prohibitedDateTime;

	private EntityStatusData entityStatusData;
	private EntityTwoData entityTwoData;

	@Override
	@JsonIgnore
	public EntityOneData getData() {
		return this;
	}

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

	public EntityStatusData getEntityStatusData() {
		return entityStatusData;
	}

	public void setEntityStatusData(EntityStatusData entityStatusData) {
		this.entityStatusData = entityStatusData;
	}

	public EntityTwoData getEntityTwoData() {
		return entityTwoData;
	}

	public void setEntityTwoData(EntityTwoData entityTwoData) {
		this.entityTwoData = entityTwoData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(id);
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
		EntityOneData other = (EntityOneData) obj;
		return Objects.equals(id, other.id);
	}

}
