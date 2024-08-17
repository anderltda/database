package br.com.process.integration.database.domain.model.entity;

import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import br.com.process.integration.database.core.domain.BeanEntity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class EntityTest extends RepresentationModel<EntityTest> implements BeanEntity<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_entity_test")
	private Long id;

	@Column(name = "teste", nullable = false, length = 100)
	private String teste;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTeste() {
		return teste;
	}

	public void setTeste(String teste) {
		this.teste = teste;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(id, teste);
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
		EntityTest other = (EntityTest) obj;
		return Objects.equals(id, other.id) && Objects.equals(teste, other.teste);
	}

}
