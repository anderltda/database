package br.com.process.integration.database.domain.model.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityTreeData implements BeanData<EntityTreeData> {

	private String id;
	private String animal;
	private Integer number;
	private Double value;
	private LocalDate dataLocal;
	private LocalDateTime dataLocalTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAnimal() {
		return animal;
	}

	public void setAnimal(String animal) {
		this.animal = animal;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public LocalDate getDataLocal() {
		return dataLocal;
	}

	public void setDataLocal(LocalDate dataLocal) {
		this.dataLocal = dataLocal;
	}

	public LocalDateTime getDataLocalTime() {
		return dataLocalTime;
	}

	public void setDataLocalTime(LocalDateTime dataLocalTime) {
		this.dataLocalTime = dataLocalTime;
	}

	@Override
	public EntityTreeData getData() {
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(animal, dataLocal, dataLocalTime, id, number, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityTreeData other = (EntityTreeData) obj;
		return Objects.equals(animal, other.animal) && Objects.equals(dataLocal, other.dataLocal)
				&& Objects.equals(dataLocalTime, other.dataLocalTime)
				&& Objects.equals(id, other.id) && Objects.equals(number, other.number)
				&& Objects.equals(value, other.value);
	}

}
