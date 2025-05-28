package br.com.process.integration.database.vo;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.Constants;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityFourVo {
	
	private UUID id;
	private Integer attribute;
	private String fruit;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime inclusionDateTime;
	private EntityStatusVo entityStatus;
	private Long idEntityStatus;
	private EntityFiveVo entityFive;
	private UUID idEntityFive;

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
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

	public EntityFiveVo getEntityFive() {
		return this.entityFive;
	}

	public void setEntityFive(EntityFiveVo entityFive) {
		this.entityFive = entityFive;
	}

	public UUID getIdEntityFive() {
		return this.idEntityFive;
	}

	public void setIdEntityFive(UUID idEntityFive) {
		this.idEntityFive = idEntityFive;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityFourVo that = (EntityFourVo) o;
		return java.util.Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id);
	}

	@Override
	public String toString() {
		return "EntityFour{" + "id=" + (id != null ? id.toString() : "null") + ", " + "attribute="
				+ (attribute != null ? attribute.toString() : "null") + ", " + "fruit="
				+ (fruit != null ? fruit.toString() : "null") + ", " + "inclusionDateTime="
				+ (inclusionDateTime != null ? inclusionDateTime.toString() : "null") + ", " + "entityStatus="
				+ (entityStatus != null ? entityStatus.toString() : "null") + ", " + "entityStatus="
				+ (idEntityStatus != null ? idEntityStatus.toString() : "null") + ", " + "entityFive="
				+ (entityFive != null ? entityFive.toString() : "null") + ", " + "entityFive="
				+ (idEntityFive != null ? idEntityFive.toString() : "null") + '}';
	}
}
