package entity.subject;

// Generated May 23, 2015 1:59:28 PM by Hibernate Tools 4.3.1

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Enterprise generated by hbm2java
 */
@Entity
@Table(name = "enterprise", schema = "public")
public class Enterprise implements java.io.Serializable {

	private BigDecimal enterprise;
	private String name;
	private String fullName;
	private BigDecimal createdBy;
	private BigDecimal updatedBy;
	private Date created;
	private Date updated;

	public Enterprise() {
	}

	public Enterprise(BigDecimal enterprise) {
		this.enterprise = enterprise;
	}

	public Enterprise(BigDecimal enterprise, String name, String fullName, BigDecimal createdBy, BigDecimal updatedBy, Date created, Date updated) {
		this.enterprise = enterprise;
		this.name = name;
		this.fullName = fullName;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.created = created;
		this.updated = updated;
	}

	@Id
	@Column(name = "enterprise", unique = true, nullable = false, precision = 10, scale = 0)
	public BigDecimal getEnterprise() {
		return this.enterprise;
	}

	public void setEnterprise(BigDecimal enterprise) {
		this.enterprise = enterprise;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "full_name")
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Column(name = "created_by", precision = 10, scale = 0)
	public BigDecimal getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(BigDecimal createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "updated_by", precision = 10, scale = 0)
	public BigDecimal getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(BigDecimal updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", length = 29)
	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated", length = 29)
	public Date getUpdated() {
		return this.updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

}