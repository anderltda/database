package br.com.process.integration.database.model.entity.dto.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.com.process.integration.database.core.Constants;
import br.com.process.integration.database.core.domain.BeanEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
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

	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_entity_status", referencedColumnName = "id_entity_status")
	private EntityStatus entityStatus;

	@Column(name = "id_entity_status", insertable = false, updatable = false)
	private Long idEntityStatus;

	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
	@JoinColumn(name = "id_entity_two", referencedColumnName = "id_entity_two", unique = true)
	private EntityTwo entityTwo;

	@Column(name = "id_entity_two", insertable = false, updatable = false)
	private UUID idEntityTwo;

	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "id_entity_eight", referencedColumnName = "id_entity_eight"),
			@JoinColumn(name = "id_entity_seven", referencedColumnName = "id_entity_seven"),
			@JoinColumn(name = "id_entity_six", referencedColumnName = "id_entity_six") })
	private EntityNine entityNine;

	@Column(name = "id_entity_eight", insertable = false, updatable = false)
	private Long idEntityEight;

	@Column(name = "id_entity_seven", insertable = false, updatable = false)
	private UUID idEntitySeven;

	@Column(name = "id_entity_six", insertable = false, updatable = false)
	private Long idEntitySix;

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

	public Long getIdEntityStatus() {
		return this.idEntityStatus;
	}

	public void setIdEntityStatus(Long idEntityStatus) {
		this.idEntityStatus = idEntityStatus;
	}

	public EntityTwo getEntityTwo() {
		return this.entityTwo;
	}

	public void setEntityTwo(EntityTwo entityTwo) {
		this.entityTwo = entityTwo;
	}

	public UUID getIdEntityTwo() {
		return this.idEntityTwo;
	}

	public void setIdEntityTwo(UUID idEntityTwo) {
		this.idEntityTwo = idEntityTwo;
	}

	public EntityNine getEntityNine() {
		return this.entityNine;
	}

	public void setEntityNine(EntityNine entityNine) {
		this.entityNine = entityNine;
	}

	public Long getIdEntityEight() {
		return this.idEntityEight;
	}

	public void setIdEntityEight(Long idEntityEight) {
		this.idEntityEight = idEntityEight;
	}

	public UUID getIdEntitySeven() {
		return this.idEntitySeven;
	}

	public void setIdEntitySeven(UUID idEntitySeven) {
		this.idEntitySeven = idEntitySeven;
	}

	public Long getIdEntitySix() {
		return this.idEntitySix;
	}

	public void setIdEntitySix(Long idEntitySix) {
		this.idEntitySix = idEntitySix;
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
				+ (entityStatus != null ? entityStatus.toString() : "null") + ", " + "entityStatus="
				+ (idEntityStatus != null ? idEntityStatus.toString() : "null") + ", " + "entityTwo="
				+ (entityTwo != null ? entityTwo.toString() : "null") + ", " + "entityTwo="
				+ (idEntityTwo != null ? idEntityTwo.toString() : "null") + ", " + "entityNine="
				+ (entityNine != null ? entityNine.toString() : "null") + ", " + "entityEight="
				+ (idEntityEight != null ? idEntityEight.toString() : "null") + ", " + "entitySeven="
				+ (idEntitySeven != null ? idEntitySeven.toString() : "null") + ", " + "entitySix="
				+ (idEntitySix != null ? idEntitySix.toString() : "null") + '}';
	}
}
