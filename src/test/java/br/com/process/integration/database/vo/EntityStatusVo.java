package br.com.process.integration.database.vo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.Constants;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityStatusVo {
	
	private Long id;
	private Boolean ativo;
	private String name;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime startDateTime;
	private Integer status;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getAtivo() {
		return this.ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getStartDateTime() {
		return this.startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityStatusVo that = (EntityStatusVo) o;
		return java.util.Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id);
	}

	@Override
	public String toString() {
		return "EntityStatus{" + "id=" + (id != null ? id.toString() : "null") + ", " + "ativo="
				+ (ativo != null ? ativo.toString() : "null") + ", " + "name="
				+ (name != null ? name.toString() : "null") + ", " + "startDateTime="
				+ (startDateTime != null ? startDateTime.toString() : "null") + ", " + "status="
				+ (status != null ? status.toString() : "null") + '}';
	}
}
