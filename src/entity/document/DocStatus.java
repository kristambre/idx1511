package entity.document;

// Generated May 23, 2015 1:59:28 PM by Hibernate Tools 4.3.1

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * DocStatus generated by hbm2java
 */
@Entity
@Table(name = "doc_status", schema = "public")
public class DocStatus implements java.io.Serializable {

	private BigDecimal docStatus;
	private BigDecimal documentFk;
	private BigDecimal docStatusTypeFk;
	private Date statusBegin;
	private Date statusEnd;
	private BigDecimal createdBy;

	public DocStatus() {
	}

	public DocStatus(BigDecimal docStatus) {
		this.docStatus = docStatus;
	}

	public DocStatus(BigDecimal docStatus, BigDecimal documentFk, BigDecimal docStatusTypeFk, Date statusBegin, Date statusEnd, BigDecimal createdBy) {
		this.docStatus = docStatus;
		this.documentFk = documentFk;
		this.docStatusTypeFk = docStatusTypeFk;
		this.statusBegin = statusBegin;
		this.statusEnd = statusEnd;
		this.createdBy = createdBy;
	}

	@Id
	@Column(name = "doc_status", unique = true, nullable = false, precision = 10, scale = 0)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public BigDecimal getDocStatus() {
		return this.docStatus;
	}

	public void setDocStatus(BigDecimal docStatus) {
		this.docStatus = docStatus;
	}

	@Column(name = "document_fk", precision = 10, scale = 0)
	public BigDecimal getDocumentFk() {
		return this.documentFk;
	}

	public void setDocumentFk(BigDecimal documentFk) {
		this.documentFk = documentFk;
	}

	@Column(name = "doc_status_type_fk", precision = 10, scale = 0)
	public BigDecimal getDocStatusTypeFk() {
		return this.docStatusTypeFk;
	}

	public void setDocStatusTypeFk(BigDecimal docStatusTypeFk) {
		this.docStatusTypeFk = docStatusTypeFk;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "status_begin", length = 29)
	public Date getStatusBegin() {
		return this.statusBegin;
	}

	public void setStatusBegin(Date statusBegin) {
		this.statusBegin = statusBegin;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "status_end", length = 29)
	public Date getStatusEnd() {
		return this.statusEnd;
	}

	public void setStatusEnd(Date statusEnd) {
		this.statusEnd = statusEnd;
	}

	@Column(name = "created_by", precision = 10, scale = 0)
	public BigDecimal getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(BigDecimal createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((docStatus == null) ? 0 : docStatus.hashCode());
		result = prime * result + ((docStatusTypeFk == null) ? 0 : docStatusTypeFk.hashCode());
		result = prime * result + ((documentFk == null) ? 0 : documentFk.hashCode());
		result = prime * result + ((statusBegin == null) ? 0 : statusBegin.hashCode());
		result = prime * result + ((statusEnd == null) ? 0 : statusEnd.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocStatus other = (DocStatus) obj;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (docStatus == null) {
			if (other.docStatus != null)
				return false;
		} else if (!docStatus.equals(other.docStatus))
			return false;
		if (docStatusTypeFk == null) {
			if (other.docStatusTypeFk != null)
				return false;
		} else if (!docStatusTypeFk.equals(other.docStatusTypeFk))
			return false;
		if (documentFk == null) {
			if (other.documentFk != null)
				return false;
		} else if (!documentFk.equals(other.documentFk))
			return false;
		if (statusBegin == null) {
			if (other.statusBegin != null)
				return false;
		} else if (!statusBegin.equals(other.statusBegin))
			return false;
		if (statusEnd == null) {
			if (other.statusEnd != null)
				return false;
		} else if (!statusEnd.equals(other.statusEnd))
			return false;
		return true;
	}

}
