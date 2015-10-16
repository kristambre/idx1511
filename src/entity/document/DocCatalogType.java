package entity.document;

// Generated May 23, 2015 1:59:28 PM by Hibernate Tools 4.3.1

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DocCatalogType generated by hbm2java
 */
@Entity
@Table(name = "doc_catalog_type", schema = "public")
public class DocCatalogType implements java.io.Serializable {

	private BigDecimal docCatalogType;
	private String typeName;

	public DocCatalogType() {
	}

	public DocCatalogType(BigDecimal docCatalogType) {
		this.docCatalogType = docCatalogType;
	}

	public DocCatalogType(BigDecimal docCatalogType, String typeName) {
		this.docCatalogType = docCatalogType;
		this.typeName = typeName;
	}

	@Id
	@Column(name = "doc_catalog_type", unique = true, nullable = false, precision = 10, scale = 0)
	public BigDecimal getDocCatalogType() {
		return this.docCatalogType;
	}

	public void setDocCatalogType(BigDecimal docCatalogType) {
		this.docCatalogType = docCatalogType;
	}

	@Column(name = "type_name", length = 200)
	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
