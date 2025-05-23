package br.com.process.integration.database.model.data.dto.example;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanData;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityNineData extends RepresentationModel<EntityNineData> implements BeanData<EntityNineData> {

	private EntityNineDataId id;

	@NotNull
	@Size(max = 100)
	private String keyNine;

	@Size(max = 100)
	private String code;

	@Size(max = 100)
	private String variable;

	private EntityEightData entityEightData;

	private EntitySevenData entitySevenData;

	public EntityNineDataId getId() {
		return this.id;
	}

	public void setId(EntityNineDataId id) {
		this.id = id;
	}

	public String getKeyNine() {
		return this.keyNine;
	}

	public void setKeyNine(String keyNine) {
		this.keyNine = keyNine;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getVariable() {
		return this.variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public EntityEightData getEntityEightData() {
		return this.entityEightData;
	}

	public void setEntityEightData(EntityEightData entityEightData) {
		this.entityEightData = entityEightData;
	}

	public EntitySevenData getEntitySevenData() {
		return this.entitySevenData;
	}

	public void setEntitySevenData(EntitySevenData entitySevenData) {
		this.entitySevenData = entitySevenData;
	}

	@Override
	@JsonIgnore
	public EntityNineData getData() {
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityNineData other = (EntityNineData) o;
		return java.util.Objects.equals(id, other.id) && java.util.Objects.equals(keyNine, other.keyNine)
				&& java.util.Objects.equals(code, other.code) && java.util.Objects.equals(variable, other.variable)
				&& java.util.Objects.equals(entityEightData, other.entityEightData)
				&& java.util.Objects.equals(entitySevenData, other.entitySevenData);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id, keyNine, code, variable, entityEightData, entitySevenData);
	}

	@Override
	public String toString() {
		return "EntityNineData{" + "id=" + (id != null ? id.toString() : "null") + ", " + "keyNine="
				+ (keyNine != null ? keyNine.toString() : "null") + ", " + "code="
				+ (code != null ? code.toString() : "null") + ", " + "variable="
				+ (variable != null ? variable.toString() : "null") + ", " + "entityEightData="
				+ (entityEightData != null ? entityEightData.toString() : "null") + ", " + "entitySevenData="
				+ (entitySevenData != null ? entitySevenData.toString() : "null") + '}';
	}
}
