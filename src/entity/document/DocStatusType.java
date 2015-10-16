package entity.document;

// Generated May 23, 2015 1:59:28 PM by Hibernate Tools 4.3.1

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DocStatusType generated by hbm2java
 */
@Entity
@Table(name = "doc_status_type", schema = "public")
public class DocStatusType implements java.io.Serializable {

	private BigDecimal docStatusType;
	private String typeName;

	public DocStatusType() {
	}

	public DocStatusType(BigDecimal docStatusType) {
		this.docStatusType = docStatusType;
	}

	public DocStatusType(BigDecimal docStatusType, String typeName) {
		this.docStatusType = docStatusType;
		this.typeName = typeName;
	}

	@Id
	@Column(name = "doc_status_type", unique = true, nullable = false, precision = 10, scale = 0)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public BigDecimal getDocStatusType() {
		return this.docStatusType;
	}

	public void setDocStatusType(BigDecimal docStatusType) {
		this.docStatusType = docStatusType;
	}

	@Column(name = "type_name", length = 200)
	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((docStatusType == null) ? 0 : docStatusType.hashCode());
		result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
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
		DocStatusType other = (DocStatusType) obj;
		if (docStatusType == null) {
			if (other.docStatusType != null)
				return false;
		} else if (!docStatusType.equals(other.docStatusType))
			return false;
		if (typeName == null) {
			if (other.typeName != null)
				return false;
		} else if (!typeName.equals(other.typeName))
			return false;
		return true;
	}

}
