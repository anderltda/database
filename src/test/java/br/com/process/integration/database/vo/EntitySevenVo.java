package br.com.process.integration.database.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntitySevenVo {
	
	private EntitySevenIdVo id;
	private String dado;

	public EntitySevenIdVo getId() {
		return this.id;
	}

	public void setId(EntitySevenIdVo id) {
		this.id = id;
	}

	public String getDado() {
		return this.dado;
	}

	public void setDado(String dado) {
		this.dado = dado;
	}

	@Override
	public String toString() {
		return "EntitySeven{" + "id=" + (id != null ? id.toString() : "null") + ", " + "dado="
				+ (dado != null ? dado.toString() : "null") + '}';
	}
}
