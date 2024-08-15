package br.com.process.integration.database.domain.entity;

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
@Table(name = "Entity_Test_4")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityTest4 extends RepresentationModel<EntityTest4> implements BeanEntity<String> {

	@Id
	@Column(name = "id_entity_test_4")
	private String id;

	@Column(name = "fruit", nullable = false, length = 100)
	private String fruit;

	@Column(name = "nutritiou", nullable = true)
	private Integer nutritiou;

	@Column(name = "date_inclusion_time", nullable = false)
	private LocalDateTime dateInclusionTime;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_entity_test_5", nullable = false, referencedColumnName = "id_entity_test_5")
	private EntityTest5 entityTest5;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFruit() {
		return fruit;
	}

	public void setFruit(String fruit) {
		this.fruit = fruit;
	}

	public Integer getNutritiou() {
		return nutritiou;
	}

	public void setNutritiou(Integer nutritiou) {
		this.nutritiou = nutritiou;
	}

	public LocalDateTime getDateInclusionTime() {
		return dateInclusionTime;
	}

	public void setDateInclusionTime(LocalDateTime dateInclusionTime) {
		this.dateInclusionTime = dateInclusionTime;
	}

	public EntityTest5 getEntityTest5() {
		return entityTest5;
	}

	public void setEntityTest5(EntityTest5 entityTest5) {
		this.entityTest5 = entityTest5;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(dateInclusionTime, entityTest5, fruit, id, nutritiou);
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
		EntityTest4 other = (EntityTest4) obj;
		return Objects.equals(dateInclusionTime, other.dateInclusionTime)
				&& Objects.equals(entityTest5, other.entityTest5) && Objects.equals(fruit, other.fruit)
				&& Objects.equals(id, other.id) && Objects.equals(nutritiou, other.nutritiou);
	}

}
