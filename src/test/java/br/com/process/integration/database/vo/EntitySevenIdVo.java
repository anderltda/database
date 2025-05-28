package br.com.process.integration.database.vo;

public class EntitySevenIdVo {
	
	private Long idEntitySeven;
	private Long idEntitySix;

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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntitySevenIdVo that = (EntitySevenIdVo) o;
		return java.util.Objects.equals(idEntitySeven, that.idEntitySeven)
				&& java.util.Objects.equals(idEntitySix, that.idEntitySix);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(idEntitySeven, idEntitySix);
	}
}
