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
@Table(name = "entity_row")
public class EntityRow extends RepresentationModel<EntityRow> implements BeanEntity<Long> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_entity_row")
	private Long id;

	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_entity_ten", referencedColumnName = "id_entity_ten", nullable = false)
	private EntityTen entityTen;

	@Column(name = "id_entity_ten", insertable = false, updatable = false)
	private Long idEntityTen;

	@Column(name = "line_a", nullable = false, length = 200)
	private String lineA;

	@Column(name = "line_b", nullable = false, length = 200)
	private String lineB;

	@Column(name = "line_c", nullable = false, length = 200)
	private String lineC;

	@Column(name = "line_d", nullable = false, length = 200)
	private String lineD;

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

	public String getLineA() {
		return this.lineA;
	}

	public void setLineA(String lineA) {
		this.lineA = lineA;
	}

	public String getLineB() {
		return this.lineB;
	}

	public void setLineB(String lineB) {
		this.lineB = lineB;
	}

	public String getLineC() {
		return this.lineC;
	}

	public void setLineC(String lineC) {
		this.lineC = lineC;
	}

	public String getLineD() {
		return this.lineD;
	}

	public void setLineD(String lineD) {
		this.lineD = lineD;
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
		EntityRow that = (EntityRow) o;
		return java.util.Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id);
	}

	@Override
	public String toString() {
		return "EntityRow{" + "id=" + (id != null ? id.toString() : "null") + ", " + "entityTen="
				+ (entityTen != null ? entityTen.toString() : "null") + ", " + "entityTen="
				+ (idEntityTen != null ? idEntityTen.toString() : "null") + ", " + "lineA="
				+ (lineA != null ? lineA.toString() : "null") + ", " + "lineB="
				+ (lineB != null ? lineB.toString() : "null") + ", " + "lineC="
				+ (lineC != null ? lineC.toString() : "null") + ", " + "lineD="
				+ (lineD != null ? lineD.toString() : "null") + ", " + "dateCreate="
				+ (dateCreate != null ? dateCreate.toString() : "null") + ", " + "dateUpdate="
				+ (dateUpdate != null ? dateUpdate.toString() : "null") + '}';
	}
}
