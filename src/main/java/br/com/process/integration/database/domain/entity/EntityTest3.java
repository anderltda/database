package br.com.process.integration.database.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Entity_Test_3")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityTest3 extends RepresentationModel<EntityTest3> implements BeanEntity<String> {

	@Id
	@Column(name = "id_entity_test_3")
	private String id;

	@Column(name = "animal", nullable = false, length = 100)
	private String animal;

	@Column(name = "number", nullable = true)
	private Integer number;

	@Column(name = "value", nullable = false, precision = 10, scale = 0)
	private Double value;

	@Column(name = "data_local", nullable = false)
	private LocalDate dataLocal;

	@Column(name = "data_local_time", nullable = false)
	private LocalDateTime dataLocalTime;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_entity_test_4", nullable = false, referencedColumnName = "id_entity_test_4")
	private EntityTest4 entityTest4;

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

	public EntityTest4 getEntityTest4() {
		return entityTest4;
	}

	public void setEntityTest4(EntityTest4 entityTest4) {
		this.entityTest4 = entityTest4;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(animal, dataLocal, dataLocalTime, entityTest4, id, number, value);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityTest3 other = (EntityTest3) obj;
		return Objects.equals(animal, other.animal) && Objects.equals(dataLocal, other.dataLocal)
				&& Objects.equals(dataLocalTime, other.dataLocalTime) && Objects.equals(entityTest4, other.entityTest4)
				&& Objects.equals(id, other.id) && Objects.equals(number, other.number)
				&& Objects.equals(value, other.value);
	}

}
