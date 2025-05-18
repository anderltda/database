package br.com.process.integration.database.model.data.dto.example;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.Constants;
import br.com.process.integration.database.core.domain.BeanData;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityStatusData extends RepresentationModel<EntityStatusData> implements BeanData<EntityStatusData> {

	@NotNull
	private Long id;

	private Boolean ativo;

	@Size(max = 255)
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
	@JsonIgnore
	public EntityStatusData getData() {
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityStatusData other = (EntityStatusData) o;
		return java.util.Objects.equals(id, other.id) && java.util.Objects.equals(ativo, other.ativo)
				&& java.util.Objects.equals(name, other.name)
				&& java.util.Objects.equals(startDateTime, other.startDateTime)
				&& java.util.Objects.equals(status, other.status);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id, ativo, name, startDateTime, status);
	}

	@Override
	public String toString() {
		return "EntityStatusData{" + "id=" + (id != null ? id.toString() : "null") + ", " + "ativo="
				+ (ativo != null ? ativo.toString() : "null") + ", " + "name="
				+ (name != null ? name.toString() : "null") + ", " + "startDateTime="
				+ (startDateTime != null ? startDateTime.toString() : "null") + ", " + "status="
				+ (status != null ? status.toString() : "null") + '}';
	}
}
