package br.com.process.integration.database.domain.entity.test;

import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Entity_Five")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityFive extends RepresentationModel<EntityFive> implements BeanEntity<String> {

	@Id
	@Column(name = "id_entity_five")
	private String id;

	@Column(name = "object", nullable = false, length = 100)
	private String object;

	@Column(name = "value", nullable = true)
	private Integer value;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(id, object, value);
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
		EntityFive other = (EntityFive) obj;
		return Objects.equals(id, other.id) && Objects.equals(object, other.object)
				&& Objects.equals(value, other.value);
	}

}
