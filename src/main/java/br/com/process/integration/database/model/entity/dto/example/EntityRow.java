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
import java.time.LocalDate;
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

	@Column(name = "line_string", nullable = false, length = 200)
	private String lineString;

	@Column(name = "line_integer", nullable = false)
	private Integer lineInteger;

	@Column(name = "line_double", nullable = false)
	private Double lineDouble;

	@Column(name = "line_long", nullable = false)
	private Long lineLong;

	@Column(name = "line_boolean", nullable = false)
	private Boolean lineBoolean;

	@Column(name = "line_date", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate lineDate;

	@Column(name = "line_date_time", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
	private LocalDateTime lineDateTime;

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

	public String getLineString() {
		return this.lineString;
	}

	public void setLineString(String lineString) {
		this.lineString = lineString;
	}

	public Integer getLineInteger() {
		return this.lineInteger;
	}

	public void setLineInteger(Integer lineInteger) {
		this.lineInteger = lineInteger;
	}

	public Double getLineDouble() {
		return this.lineDouble;
	}

	public void setLineDouble(Double lineDouble) {
		this.lineDouble = lineDouble;
	}

	public Long getLineLong() {
		return this.lineLong;
	}

	public void setLineLong(Long lineLong) {
		this.lineLong = lineLong;
	}

	public Boolean getLineBoolean() {
		return this.lineBoolean;
	}

	public void setLineBoolean(Boolean lineBoolean) {
		this.lineBoolean = lineBoolean;
	}

	public LocalDate getLineDate() {
		return this.lineDate;
	}

	public void setLineDate(LocalDate lineDate) {
		this.lineDate = lineDate;
	}

	public LocalDateTime getLineDateTime() {
		return this.lineDateTime;
	}

	public void setLineDateTime(LocalDateTime lineDateTime) {
		this.lineDateTime = lineDateTime;
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
				+ (idEntityTen != null ? idEntityTen.toString() : "null") + ", " + "lineString="
				+ (lineString != null ? lineString.toString() : "null") + ", " + "lineInteger="
				+ (lineInteger != null ? lineInteger.toString() : "null") + ", " + "lineDouble="
				+ (lineDouble != null ? lineDouble.toString() : "null") + ", " + "lineLong="
				+ (lineLong != null ? lineLong.toString() : "null") + ", " + "lineBoolean="
				+ (lineBoolean != null ? lineBoolean.toString() : "null") + ", " + "lineDate="
				+ (lineDate != null ? lineDate.toString() : "null") + ", " + "lineDateTime="
				+ (lineDateTime != null ? lineDateTime.toString() : "null") + '}';
	}
}
