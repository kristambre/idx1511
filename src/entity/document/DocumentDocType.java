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
 * DocumentDocType generated by hbm2java
 */
@Entity
@Table(name = "document_doc_type", schema = "public")
public class DocumentDocType implements java.io.Serializable {

	private BigDecimal documentDocType;
	private BigDecimal docTypeFk;
	private BigDecimal documentFk;

	public DocumentDocType() {
	}

	public DocumentDocType(BigDecimal documentDocType) {
		this.documentDocType = documentDocType;
	}

	public DocumentDocType(BigDecimal documentDocType, BigDecimal docTypeFk, BigDecimal documentFk) {
		this.documentDocType = documentDocType;
		this.docTypeFk = docTypeFk;
		this.documentFk = documentFk;
	}

	@Id
	@Column(name = "document_doc_type", unique = true, nullable = false, precision = 10, scale = 0)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public BigDecimal getDocumentDocType() {
		return this.documentDocType;
	}

	public void setDocumentDocType(BigDecimal documentDocType) {
		this.documentDocType = documentDocType;
	}

	@Column(name = "doc_type_fk", precision = 10, scale = 0)
	public BigDecimal getDocTypeFk() {
		return this.docTypeFk;
	}

	public void setDocTypeFk(BigDecimal docTypeFk) {
		this.docTypeFk = docTypeFk;
	}

	@Column(name = "document_fk", precision = 10, scale = 0)
	public BigDecimal getDocumentFk() {
		return this.documentFk;
	}

	public void setDocumentFk(BigDecimal documentFk) {
		this.documentFk = documentFk;
	}

}