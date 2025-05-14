package br.com.process.integration.database.model.entity.dto.example;

import java.time.LocalDate;

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
@Table(name = "entity_six")
public class EntitySix extends RepresentationModel<EntitySix> implements BeanEntity<Long> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_entity_six")
	private Long id;

	@Column(name = "package_name", nullable = false, length = 100)
	private String packageName;

	@Column(name = "start_date", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate startDate;

	@Column(name = "stop_date", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate stopDate;

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public LocalDate getStartDate() {
		return this.startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getStopDate() {
		return this.stopDate;
	}

	public void setStopDate(LocalDate stopDate) {
		this.stopDate = stopDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntitySix that = (EntitySix) o;
		return java.util.Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id);
	}

	@Override
	public String toString() {
		return "EntitySix{" + "id=" + (id != null ? id.toString() : "null") + ", " + "packageName="
				+ (packageName != null ? packageName.toString() : "null") + ", " + "startDate="
				+ (startDate != null ? startDate.toString() : "null") + ", " + "stopDate="
				+ (stopDate != null ? stopDate.toString() : "null") + '}';
	}
}
