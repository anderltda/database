package br.com.process.integration.database.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityNineVo {
	
	private EntityNineIdVo id;
	private String keyNine;
	private String code;
	private String variable;

	public EntityNineIdVo getId() {
		return this.id;
	}

	public void setId(EntityNineIdVo id) {
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

	@Override
	public String toString() {
		return "EntityNine{" + "id=" + (id != null ? id.toString() : "null") + ", " + "keyNine="
				+ (keyNine != null ? keyNine.toString() : "null") + ", " + "code="
				+ (code != null ? code.toString() : "null") + ", " + "variable="
				+ (variable != null ? variable.toString() : "null") + '}';
	}
}
