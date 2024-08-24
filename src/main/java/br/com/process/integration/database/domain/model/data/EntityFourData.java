package br.com.process.integration.database.domain.model.data;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.process.integration.database.core.domain.BeanData;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class EntityFourData extends RepresentationModel<EntityFourData> implements BeanData<EntityFourData> {

	private String id;
	private String fruit;
	private Integer nutritiou;
	private LocalDateTime dateInclusionTime;

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

	@Override
	public EntityFourData getData() {
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateInclusionTime, fruit, id, nutritiou);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityFourData other = (EntityFourData) obj;
		return Objects.equals(dateInclusionTime, other.dateInclusionTime) && Objects.equals(fruit, other.fruit)
				&& Objects.equals(id, other.id) && Objects.equals(nutritiou, other.nutritiou);
	}

}
