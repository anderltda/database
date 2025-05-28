package br.com.process.integration.database.vo;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityFiveVo {
	
	private UUID id;
	private Integer factor;
	private String reference;
	private EntityStatusVo entityStatus;
	private Long idEntityStatus;

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Integer getFactor() {
		return this.factor;
	}

	public void setFactor(Integer factor) {
		this.factor = factor;
	}

	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityFiveVo that = (EntityFiveVo) o;
		return java.util.Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id);
	}

	@Override
	public String toString() {
		return "EntityFive{" + "id=" + (id != null ? id.toString() : "null") + ", " + "factor="
				+ (factor != null ? factor.toString() : "null") + ", " + "reference="
				+ (reference != null ? reference.toString() : "null") + ", " + "entityStatus="
				+ (entityStatus != null ? entityStatus.toString() : "null") + ", " + "entityStatus="
				+ (idEntityStatus != null ? idEntityStatus.toString() : "null") + '}';
	}
}
