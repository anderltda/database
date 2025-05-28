package br.com.process.integration.database.vo;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.Constants;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityTwoVo {
	
	private UUID id;
	private String color;
	private Double cost;
	private Integer hex;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate inclusionDate;
	private EntityStatusVo entityStatus;
	private Long idEntityStatus;
	private EntityTreeVo entityTree;
	private UUID idEntityTree;

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Double getCost() {
		return this.cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Integer getHex() {
		return this.hex;
	}

	public void setHex(Integer hex) {
		this.hex = hex;
	}

	public LocalDate getInclusionDate() {
		return this.inclusionDate;
	}

	public void setInclusionDate(LocalDate inclusionDate) {
		this.inclusionDate = inclusionDate;
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

	public EntityTreeVo getEntityTree() {
		return this.entityTree;
	}

	public void setEntityTree(EntityTreeVo entityTree) {
		this.entityTree = entityTree;
	}

	public UUID getIdEntityTree() {
		return this.idEntityTree;
	}

	public void setIdEntityTree(UUID idEntityTree) {
		this.idEntityTree = idEntityTree;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityTwoVo that = (EntityTwoVo) o;
		return java.util.Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id);
	}

	@Override
	public String toString() {
		return "EntityTwo{" + "id=" + (id != null ? id.toString() : "null") + ", " + "color="
				+ (color != null ? color.toString() : "null") + ", " + "cost="
				+ (cost != null ? cost.toString() : "null") + ", " + "hex=" + (hex != null ? hex.toString() : "null")
				+ ", " + "inclusionDate=" + (inclusionDate != null ? inclusionDate.toString() : "null") + ", "
				+ "entityStatus=" + (entityStatus != null ? entityStatus.toString() : "null") + ", " + "entityStatus="
				+ (idEntityStatus != null ? idEntityStatus.toString() : "null") + ", " + "entityTree="
				+ (entityTree != null ? entityTree.toString() : "null") + ", " + "entityTree="
				+ (idEntityTree != null ? idEntityTree.toString() : "null") + '}';
	}
}
