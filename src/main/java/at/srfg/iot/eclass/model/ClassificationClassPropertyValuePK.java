package at.srfg.iot.eclass.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the classification_class_property_value database table.
 * 
 */
@Embeddable
public class ClassificationClassPropertyValuePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="irdicc", insertable=false, updatable=false)
	private String irdiCC;

	@Column(name="irdipr", insertable=false, updatable=false)
	private String irdiPR;

	@Column(name="irdiva", insertable=false, updatable=false)
	private String iridVA;

	public ClassificationClassPropertyValuePK() {
	}
	public String getIrdiCC() {
		return this.irdiCC;
	}
	public void setIrdiCC(String irdicc) {
		this.irdiCC = irdicc;
	}
	public String getIrdiPR() {
		return this.irdiPR;
	}
	public void setIrdiPR(String irdipr) {
		this.irdiPR = irdipr;
	}
	public String getIridVA() {
		return this.iridVA;
	}
	public void setIridVA(String irdiva) {
		this.iridVA = irdiva;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ClassificationClassPropertyValuePK)) {
			return false;
		}
		ClassificationClassPropertyValuePK castOther = (ClassificationClassPropertyValuePK)other;
		return 
			this.irdiCC.equals(castOther.irdiCC)
			&& this.irdiPR.equals(castOther.irdiPR)
			&& this.iridVA.equals(castOther.iridVA);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.irdiCC.hashCode();
		hash = hash * prime + this.irdiPR.hashCode();
		hash = hash * prime + this.iridVA.hashCode();
		
		return hash;
	}
}