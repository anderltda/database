package br.com.process.integration.database.model.view.dto.example;

import br.com.process.integration.database.core.domain.BeanView;
import br.com.process.integration.database.core.util.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import org.springframework.hateoas.RepresentationModel;

/**
 *
 * // EntityEight
 *
 * // EntitySeven
 *
 * // EntitySix
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityEightView extends RepresentationModel<EntityEightView> implements BeanView<EntityEightView> {
	
	/**
	 * from entity_eight
	 */
	private Long idEntityEight;

	/**
	 * from entity_eight
	 */
	private Long idEntitySeven;

	/**
	 * from entity_eight
	 */
	private Long idEntitySix;

	/**
	 * from entity_eight
	 */
	private String position;

	/**
	 * from entity_eight
	 */
	private String properties;

	/**
	 * from entity_seven
	 */
	private String dado;

	/**
	 * from entity_six
	 */
	private String packageName;

	/**
	 * from entity_six
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate startDate;

	/**
	 * from entity_six
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private LocalDate stopDate;

	public Long getIdEntityEight() {
		return this.idEntityEight;
	}

	public void setIdEntityEight(Long idEntityEight) {
		this.idEntityEight = idEntityEight;
	}

	public Long getIdEntitySeven() {
		return this.idEntitySeven;
	}

	public void setIdEntitySeven(Long idEntitySeven) {
		this.idEntitySeven = idEntitySeven;
	}

	public Long getIdEntitySix() {
		return this.idEntitySix;
	}

	public void setIdEntitySix(Long idEntitySix) {
		this.idEntitySix = idEntitySix;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getProperties() {
		return this.properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getDado() {
		return this.dado;
	}

	public void setDado(String dado) {
		this.dado = dado;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public LocalDate getStartDate() {
		return this.startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getStopDate() {
		return this.stopDate;
	}

	public void setStopDate(LocalDate stopDate) {
		this.stopDate = stopDate;
	}

	@JsonIgnore
	@Override
	public EntityEightView getView() {
		return this;
	}

	@Override
	public String toString() {
		return "EntityEightView{" + "idEntityEight=" + (idEntityEight != null ? idEntityEight.toString() : "null")
				+ ", " + "idEntitySeven=" + (idEntitySeven != null ? idEntitySeven.toString() : "null") + ", "
				+ "idEntitySix=" + (idEntitySix != null ? idEntitySix.toString() : "null") + ", " + "position="
				+ (position != null ? position.toString() : "null") + ", " + "properties="
				+ (properties != null ? properties.toString() : "null") + ", " + "dado="
				+ (dado != null ? dado.toString() : "null") + ", " + "packageName="
				+ (packageName != null ? packageName.toString() : "null") + ", " + "startDate="
				+ (startDate != null ? startDate.toString() : "null") + ", " + "stopDate="
				+ (stopDate != null ? stopDate.toString() : "null") + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityEightView that = (EntityEightView) o;
		return java.util.Objects.equals(idEntityEight, that.idEntityEight)
				&& java.util.Objects.equals(idEntitySeven, that.idEntitySeven)
				&& java.util.Objects.equals(idEntitySix, that.idEntitySix)
				&& java.util.Objects.equals(position, that.position)
				&& java.util.Objects.equals(properties, that.properties) && java.util.Objects.equals(dado, that.dado)
				&& java.util.Objects.equals(packageName, that.packageName)
				&& java.util.Objects.equals(startDate, that.startDate)
				&& java.util.Objects.equals(stopDate, that.stopDate);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(idEntityEight, idEntitySeven, idEntitySix, position, properties, dado,
				packageName, startDate, stopDate);
	}
}
