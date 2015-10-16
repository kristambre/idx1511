package entity.subject;

// Generated May 23, 2015 1:59:28 PM by Hibernate Tools 4.3.1

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EnterprisePersonRelation generated by hbm2java
 */
@Entity
@Table(name = "enterprise_person_relation", schema = "public")
public class EnterprisePersonRelation implements java.io.Serializable {

	private BigDecimal enterprisePersonRelation;
	private BigDecimal personFk;
	private BigDecimal enterpriseFk;
	private BigDecimal entPerRelationTypeFk;

	public EnterprisePersonRelation() {
	}

	public EnterprisePersonRelation(BigDecimal enterprisePersonRelation) {
		this.enterprisePersonRelation = enterprisePersonRelation;
	}

	public EnterprisePersonRelation(BigDecimal enterprisePersonRelation, BigDecimal personFk, BigDecimal enterpriseFk, BigDecimal entPerRelationTypeFk) {
		this.enterprisePersonRelation = enterprisePersonRelation;
		this.personFk = personFk;
		this.enterpriseFk = enterpriseFk;
		this.entPerRelationTypeFk = entPerRelationTypeFk;
	}

	@Id
	@Column(name = "enterprise_person_relation", unique = true, nullable = false, precision = 10, scale = 0)
	public BigDecimal getEnterprisePersonRelation() {
		return this.enterprisePersonRelation;
	}

	public void setEnterprisePersonRelation(BigDecimal enterprisePersonRelation) {
		this.enterprisePersonRelation = enterprisePersonRelation;
	}

	@Column(name = "person_fk", precision = 10, scale = 0)
	public BigDecimal getPersonFk() {
		return this.personFk;
	}

	public void setPersonFk(BigDecimal personFk) {
		this.personFk = personFk;
	}

	@Column(name = "enterprise_fk", precision = 10, scale = 0)
	public BigDecimal getEnterpriseFk() {
		return this.enterpriseFk;
	}

	public void setEnterpriseFk(BigDecimal enterpriseFk) {
		this.enterpriseFk = enterpriseFk;
	}

	@Column(name = "ent_per_relation_type_fk", precision = 10, scale = 0)
	public BigDecimal getEntPerRelationTypeFk() {
		return this.entPerRelationTypeFk;
	}

	public void setEntPerRelationTypeFk(BigDecimal entPerRelationTypeFk) {
		this.entPerRelationTypeFk = entPerRelationTypeFk;
	}

}
