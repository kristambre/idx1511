package entity.subject;

// Generated May 23, 2015 1:59:28 PM by Hibernate Tools 4.3.1

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Employee generated by hbm2java
 */
@Entity
@Table(name = "employee", schema = "public")
public class Employee implements java.io.Serializable {

	private BigDecimal employee;
	private BigDecimal personFk;
	private BigDecimal enterpriseFk;
	private BigDecimal structUnitFk;
	private String active;

	public Employee() {
	}

	public Employee(BigDecimal employee) {
		this.employee = employee;
	}

	public Employee(BigDecimal employee, BigDecimal personFk, BigDecimal enterpriseFk, BigDecimal structUnitFk, String active) {
		this.employee = employee;
		this.personFk = personFk;
		this.enterpriseFk = enterpriseFk;
		this.structUnitFk = structUnitFk;
		this.active = active;
	}

	@Id
	@Column(name = "employee", unique = true, nullable = false, precision = 10, scale = 0)
	public BigDecimal getEmployee() {
		return this.employee;
	}

	public void setEmployee(BigDecimal employee) {
		this.employee = employee;
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

	@Column(name = "struct_unit_fk", precision = 10, scale = 0)
	public BigDecimal getStructUnitFk() {
		return this.structUnitFk;
	}

	public void setStructUnitFk(BigDecimal structUnitFk) {
		this.structUnitFk = structUnitFk;
	}

	@Column(name = "active", length = 1)
	public String getActive() {
		return this.active;
	}

	public void setActive(String active) {
		this.active = active;
	}

}