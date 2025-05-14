package br.com.process.integration.database.model.view.dto.example;

import br.com.process.integration.database.core.domain.BeanView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.RepresentationModel;

/**
 *
 * // EntityNine
 */
@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class EntityNineView extends RepresentationModel<EntityNineView> implements BeanView<EntityNineView> {
    /**
     * from entity_nine
     */
    private Long idEntityEight;

    /**
     * from entity_nine
     */
    private Long idEntitySeven;

    /**
     * from entity_nine
     */
    private Long idEntitySix;

    /**
     * from entity_nine
     */
    private String key;

    /**
     * from entity_nine
     */
    private String code;

    /**
     * from entity_nine
     */
    private String variable;

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

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVariable() {
        return this.variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    @JsonIgnore
    @Override
    public EntityNineView getView() {
        return this;
    }

    @Override
    public String toString() {
        return "EntityNineView{" +
        "idEntityEight=" + (idEntityEight != null ? idEntityEight.toString() : "null") + ", " +
        "idEntitySeven=" + (idEntitySeven != null ? idEntitySeven.toString() : "null") + ", " +
        "idEntitySix=" + (idEntitySix != null ? idEntitySix.toString() : "null") + ", " +
        "key=" + (key != null ? key.toString() : "null") + ", " +
        "code=" + (code != null ? code.toString() : "null") + ", " +
        "variable=" + (variable != null ? variable.toString() : "null") + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityNineView that = (EntityNineView) o;
        return java.util.Objects.equals(idEntityEight, that.idEntityEight) &&
        java.util.Objects.equals(idEntitySeven, that.idEntitySeven) &&
        java.util.Objects.equals(idEntitySix, that.idEntitySix) &&
        java.util.Objects.equals(key, that.key) &&
        java.util.Objects.equals(code, that.code) &&
        java.util.Objects.equals(variable, that.variable);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(idEntityEight, idEntitySeven, idEntitySix, key, code, variable);
    }
}
