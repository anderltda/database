package br.com.process.integration.database.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.Constants;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityOneVo {
	
	private Long id;

	private Integer age;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate birthDate;
	
	private Boolean code;
	
	private Double height;
	
	private String name;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime prohibitedDateTime;
	
	private EntityStatusVo entityStatus;
	private Long idEntityStatus;
	
	private EntityTwoVo entityTwo;
	private UUID idEntityTwo;
	
	private EntityNineVo entityNine;
	private Long idEntityEight;
	private UUID idEntitySeven;
	private Long idEntitySix;

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

	public EntityStatusVo getEntityStatus() {
		return this.entityStatus;
	}

	public void setEntityStatus(EntityStatusVo entityStatus) {
		this.entityStatus = entityStatus;
	}

	public Long getIdEntityStatus() {
		return this.idEntityStatus;
	}

	public void setIdEntityStatus(Long idEntityStatus) {
		this.idEntityStatus = idEntityStatus;
	}

	public EntityTwoVo getEntityTwo() {
		return this.entityTwo;
	}

	public void setEntityTwo(EntityTwoVo entityTwo) {
		this.entityTwo = entityTwo;
	}

	public UUID getIdEntityTwo() {
		return this.idEntityTwo;
	}

	public void setIdEntityTwo(UUID idEntityTwo) {
		this.idEntityTwo = idEntityTwo;
	}
	
	public EntityNineVo getEntityNine() {
		return entityNine;
	}

	public void setEntityNine(EntityNineVo entityNine) {
		this.entityNine = entityNine;
	}

	public Long getIdEntityEight() {
		return idEntityEight;
	}

	public void setIdEntityEight(Long idEntityEight) {
		this.idEntityEight = idEntityEight;
	}

	public UUID getIdEntitySeven() {
		return idEntitySeven;
	}

	public void setIdEntitySeven(UUID idEntitySeven) {
		this.idEntitySeven = idEntitySeven;
	}

	public Long getIdEntitySix() {
		return idEntitySix;
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
		EntityOneVo that = (EntityOneVo) o;
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
				+ (idEntityTwo != null ? idEntityTwo.toString() : "null") + '}';
	}
}
