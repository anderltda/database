package br.com.process.integration.database.model.entity.dto.example;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "entity_one")
public class EntityOne extends RepresentationModel<EntityOne> implements BeanEntity<Long> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_entity_one")
	private Long id;

	@Column(name = "age")
	private Integer age;

	@Column(name = "birth_date", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate birthDate;

	@Column(name = "code", nullable = false)
	private Boolean code;

	@Column(name = "height", nullable = false)
	private Double height;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "prohibited_date_time", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime prohibitedDateTime;

	@ManyToOne
	@JoinColumn(name = "id_entity_status", referencedColumnName = "id_entity_status")
	private EntityStatus entityStatus;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_entity_two", referencedColumnName = "id_entity_two", unique = true)
	private EntityTwo entityTwo;

	@Override
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

	public EntityStatus getEntityStatus() {
		return this.entityStatus;
	}

	public void setEntityStatus(EntityStatus entityStatus) {
		this.entityStatus = entityStatus;
	}

	public EntityTwo getEntityTwo() {
		return this.entityTwo;
	}

	public void setEntityTwo(EntityTwo entityTwo) {
		this.entityTwo = entityTwo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityOne that = (EntityOne) o;
		return java.util.Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id);
	}

	@Override
	public String toString() {
		return "EntityOne{" + "id=" + (id != null ? id.toString() : "null") + ", " + "age="
				+ (age != null ? age.toString() : "null") + ", " + "birthDate="
				+ (birthDate != null ? birthDate.toString() : "null") + ", " + "code="
				+ (code != null ? code.toString() : "null") + ", " + "height="
				+ (height != null ? height.toString() : "null") + ", " + "name="
				+ (name != null ? name.toString() : "null") + ", " + "prohibitedDateTime="
				+ (prohibitedDateTime != null ? prohibitedDateTime.toString() : "null") + ", " + "entityStatus="
				+ (entityStatus != null ? entityStatus.toString() : "null") + ", " + "entityTwo="
				+ (entityTwo != null ? entityTwo.toString() : "null") + '}';
	}
}
