package br.com.process.integration.database.domain.model.data;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityFiveData implements BeanData<EntityFiveData> {

	private String id;
	private String object;
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
	public EntityFiveData getData() {
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, object, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityFiveData other = (EntityFiveData) obj;
		return Objects.equals(id, other.id) && Objects.equals(object, other.object)
				&& Objects.equals(value, other.value);
	}

}
