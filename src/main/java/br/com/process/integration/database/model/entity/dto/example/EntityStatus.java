package br.com.process.integration.database.model.entity.dto.example;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanEntity;
import br.com.process.integration.database.core.util.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "entity_status")
public class EntityStatus extends RepresentationModel<EntityStatus> implements BeanEntity<Long> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_entity_status")
	private Long id;

	@Column(name = "ativo")
	private Boolean ativo;

	@Column(name = "name", length = 255)
	private String name;

	@Column(name = "start_date_time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime startDateTime;

	@Column(name = "status")
	private Integer status;

	@Override
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
		EntityStatus that = (EntityStatus) o;
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
