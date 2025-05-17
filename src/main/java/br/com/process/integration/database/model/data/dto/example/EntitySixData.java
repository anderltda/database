package br.com.process.integration.database.model.data.dto.example;

import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanData;
import br.com.process.integration.database.core.util.Constants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntitySixData extends RepresentationModel<EntitySixData> implements BeanData<EntitySixData> {

	@NotNull
	private Long id;

	@NotNull
	@Size(max = 100)
	private String packageName;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate startDate;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate stopDate;

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
	@JsonIgnore
	public EntitySixData getData() {
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntitySixData other = (EntitySixData) o;
		return java.util.Objects.equals(id, other.id) && java.util.Objects.equals(packageName, other.packageName)
				&& java.util.Objects.equals(startDate, other.startDate)
				&& java.util.Objects.equals(stopDate, other.stopDate);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id, packageName, startDate, stopDate);
	}

	@Override
	public String toString() {
		return "EntitySixData{" + "id=" + (id != null ? id.toString() : "null") + ", " + "packageName="
				+ (packageName != null ? packageName.toString() : "null") + ", " + "startDate="
				+ (startDate != null ? startDate.toString() : "null") + ", " + "stopDate="
				+ (stopDate != null ? stopDate.toString() : "null") + '}';
	}
}
