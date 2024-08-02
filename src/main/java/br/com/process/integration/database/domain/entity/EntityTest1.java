package br.com.process.integration.database.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Entity_Test_1")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityTest1 extends RepresentationModel<EntityTest1>
		implements br.com.process.integration.database.core.domain.Entity<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_entity_test_1")
	private Long id;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "age", nullable = true)
	private Integer age;

	@Column(name = "height", nullable = false, precision = 10, scale = 0)
	private Double height;

	@Column(name = "birth_date", nullable = false)
	private LocalDate birthDate;

	@Column(name = "prohibited", nullable = false)
	private LocalDateTime prohibited;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_entity_test_2", nullable = false, referencedColumnName = "id_entity_test_2")
	private EntityTest2 entityTest2;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDateTime getProhibited() {
		return prohibited;
	}

	public void setProhibited(LocalDateTime prohibited) {
		this.prohibited = prohibited;
	}

	public EntityTest2 getEntityTest2() {
		return entityTest2;
	}

	public void setEntityTest2(EntityTest2 entityTest2) {
		this.entityTest2 = entityTest2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(age, birthDate, entityTest2, height, id, name, prohibited);
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
		EntityTest1 other = (EntityTest1) obj;
		return Objects.equals(age, other.age) && Objects.equals(birthDate, other.birthDate)
				&& Objects.equals(entityTest2, other.entityTest2) && Objects.equals(height, other.height)
				&& Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(prohibited, other.prohibited);
	}

}
