package br.com.process.integration.database.model.entity.dto.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanEntity;
import br.com.process.integration.database.core.util.Constants;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Entity_One")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityOne extends RepresentationModel<EntityOne> implements BeanEntity<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_entity_one")
	private Long id;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "age", nullable = true)
	private Integer age;

	@Column(name = "height", nullable = false, precision = 10, scale = 0)
	private Double height;

	@Column(name = "birth_date", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate birthDate;

	@Column(name = "prohibited_date_time", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime prohibitedDateTime;

	@Column(name = "code", nullable = false)
	private Boolean code = true;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_entity_two", nullable = false, referencedColumnName = "id_entity_two")
	private EntityTwo entityTwo;

	@ManyToOne
	@JoinColumn(name = "id_entity_status", nullable = false, referencedColumnName = "id_entity_status")
	private EntityStatus entityStatus;

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

	public LocalDateTime getProhibitedDateTime() {
		return prohibitedDateTime;
	}

	public void setProhibitedDateTime(LocalDateTime prohibitedDateTime) {
		this.prohibitedDateTime = prohibitedDateTime;
	}

	public Boolean getCode() {
		return code;
	}

	public void setCode(Boolean code) {
		this.code = code;
	}

	public EntityTwo getEntityTwo() {
		return entityTwo;
	}

	public void setEntityTwo(EntityTwo entityTwo) {
		this.entityTwo = entityTwo;
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
				+ Objects.hash(age, birthDate, code, entityStatus, entityTwo, height, id, name, prohibitedDateTime);
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
		EntityOne other = (EntityOne) obj;
		return Objects.equals(age, other.age) && Objects.equals(birthDate, other.birthDate)
				&& Objects.equals(code, other.code) && Objects.equals(entityStatus, other.entityStatus)
				&& Objects.equals(entityTwo, other.entityTwo) && Objects.equals(height, other.height)
				&& Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(prohibitedDateTime, other.prohibitedDateTime);
	}

}
