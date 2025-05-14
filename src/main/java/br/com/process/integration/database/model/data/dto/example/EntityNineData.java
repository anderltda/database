package br.com.process.integration.database.model.data.dto.example;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanData;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityNineData extends RepresentationModel<EntityNineData> implements BeanData<EntityNineData> {

	private EntityNineId id;

	/**
	 * Coluna: key
	 */
	@NotNull
	@Size(max = 100)
	private String key;

	/**
	 * Coluna: code
	 */
	@Size(max = 100)
	private String code;

	/**
	 * Coluna: variable
	 */
	@Size(max = 100)
	private String variable;

	public EntityNineId getId() {
		return this.id;
	}

	public void setId(EntityNineId id) {
		this.id = id;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
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
		return java.util.Objects.equals(id, other.id) && java.util.Objects.equals(key, other.key)
				&& java.util.Objects.equals(code, other.code) && java.util.Objects.equals(variable, other.variable);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id, key, code, variable);
	}

	@Override
	public String toString() {
		return "EntityNineData{" + "id=" + (id != null ? id.toString() : "null") + ", " + "key="
				+ (key != null ? key.toString() : "null") + ", " + "code=" + (code != null ? code.toString() : "null")
				+ ", " + "variable=" + (variable != null ? variable.toString() : "null") + '}';
	}
}
