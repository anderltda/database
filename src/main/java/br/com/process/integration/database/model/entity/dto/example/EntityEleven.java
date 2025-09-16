package br.com.process.integration.database.model.entity.dto.example;

import br.com.process.integration.database.core.Constants;
import br.com.process.integration.database.core.domain.BeanEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.springframework.hateoas.RepresentationModel;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "entity_eleven")
public class EntityEleven extends RepresentationModel<EntityEleven> implements BeanEntity<Long> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_entity_eleven")
	private Long id;

	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_entity_ten", referencedColumnName = "id_entity_ten", nullable = false)
	private EntityTen entityTen;

	@Column(name = "id_entity_ten", insertable = false, updatable = false)
	private Long idEntityTen;

	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_entity_six", referencedColumnName = "id_entity_six", nullable = false)
	private EntitySix entitySix;

	@Column(name = "id_entity_six", insertable = false, updatable = false)
	private Long idEntitySix;

	@Column(name = "amount", nullable = false)
	private Integer amount;

	@Column(name = "value", nullable = false)
	private Double value;

	@Column(name = "date_create", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime dateCreate;

	@Column(name = "date_update")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime dateUpdate;

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EntityTen getEntityTen() {
		return this.entityTen;
	}

	public void setEntityTen(EntityTen entityTen) {
		this.entityTen = entityTen;
	}

	public Long getIdEntityTen() {
		return this.idEntityTen;
	}

	public void setIdEntityTen(Long idEntityTen) {
		this.idEntityTen = idEntityTen;
	}

	public EntitySix getEntitySix() {
		return this.entitySix;
	}

	public void setEntitySix(EntitySix entitySix) {
		this.entitySix = entitySix;
	}

	public Long getIdEntitySix() {
		return this.idEntitySix;
	}

	public void setIdEntitySix(Long idEntitySix) {
		this.idEntitySix = idEntitySix;
	}

	public Integer getAmount() {
		return this.amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Double getValue() {
		return this.value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public LocalDateTime getDateCreate() {
		return this.dateCreate;
	}

	public void setDateCreate(LocalDateTime dateCreate) {
		this.dateCreate = dateCreate;
	}

	public LocalDateTime getDateUpdate() {
		return this.dateUpdate;
	}

	public void setDateUpdate(LocalDateTime dateUpdate) {
		this.dateUpdate = dateUpdate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityEleven that = (EntityEleven) o;
		return java.util.Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id);
	}

	@Override
	public String toString() {
		return "EntityEleven{" + "id=" + (id != null ? id.toString() : "null") + ", " + "entityTen="
				+ (entityTen != null ? entityTen.toString() : "null") + ", " + "entityTen="
				+ (idEntityTen != null ? idEntityTen.toString() : "null") + ", " + "entitySix="
				+ (entitySix != null ? entitySix.toString() : "null") + ", " + "entitySix="
				+ (idEntitySix != null ? idEntitySix.toString() : "null") + ", " + "amount="
				+ (amount != null ? amount.toString() : "null") + ", " + "value="
				+ (value != null ? value.toString() : "null") + ", " + "dateCreate="
				+ (dateCreate != null ? dateCreate.toString() : "null") + ", " + "dateUpdate="
				+ (dateUpdate != null ? dateUpdate.toString() : "null") + '}';
	}
}
