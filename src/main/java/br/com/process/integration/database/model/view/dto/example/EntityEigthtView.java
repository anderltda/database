package br.com.process.integration.database.model.view.dto.example;

import br.com.process.integration.database.core.domain.BeanView;
import br.com.process.integration.database.core.util.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import org.springframework.hateoas.RepresentationModel;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class EntityEigthtView extends RepresentationModel<EntityEigthtView> implements BeanView<EntityEigthtView> {
    private Long idEntityEigtht;

    private Integer idEntitySeven;

    private Integer idEntitySix;

    private String position;

    private String properties;

    private String dado;

    private String packageName;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = Constants.DATE_FORMAT
    )
    private LocalDate startDate;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = Constants.DATE_FORMAT
    )
    private LocalDate stopDate;

    public Long getIdEntityEigtht() {
        return this.idEntityEigtht;
    }

    public void setIdEntityEigtht(Long idEntityEigtht) {
        this.idEntityEigtht = idEntityEigtht;
    }

    public Integer getIdEntitySeven() {
        return this.idEntitySeven;
    }

    public void setIdEntitySeven(Integer idEntitySeven) {
        this.idEntitySeven = idEntitySeven;
    }

    public Integer getIdEntitySix() {
        return this.idEntitySix;
    }

    public void setIdEntitySix(Integer idEntitySix) {
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
    public EntityEigthtView getView() {
        return this;
    }

    @Override
    public String toString() {
        return "EntityEigthtView{" +
        "EntityEigtht=" + (idEntityEigtht != null ? idEntityEigtht.toString() : "null") + ", " +
        "EntitySeven=" + (idEntitySeven != null ? idEntitySeven.toString() : "null") + ", " +
        "EntitySix=" + (idEntitySix != null ? idEntitySix.toString() : "null") + ", " +
        "position=" + (position != null ? position.toString() : "null") + ", " +
        "properties=" + (properties != null ? properties.toString() : "null") + ", " +
        "dado=" + (dado != null ? dado.toString() : "null") + ", " +
        "packageName=" + (packageName != null ? packageName.toString() : "null") + ", " +
        "startDate=" + (startDate != null ? startDate.toString() : "null") + ", " +
        "stopDate=" + (stopDate != null ? stopDate.toString() : "null") + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityEigthtView that = (EntityEigthtView) o;
        return java.util.Objects.equals(idEntityEigtht, that.idEntityEigtht) &&
        java.util.Objects.equals(idEntitySeven, that.idEntitySeven) &&
        java.util.Objects.equals(idEntitySix, that.idEntitySix) &&
        java.util.Objects.equals(position, that.position) &&
        java.util.Objects.equals(properties, that.properties) &&
        java.util.Objects.equals(dado, that.dado) &&
        java.util.Objects.equals(packageName, that.packageName) &&
        java.util.Objects.equals(startDate, that.startDate) &&
        java.util.Objects.equals(stopDate, that.stopDate);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(idEntityEigtht, idEntitySeven, idEntitySix, position, properties, dado, packageName, startDate, stopDate);
    }
}
