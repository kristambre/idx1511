package entity.document;

// Generated May 23, 2015 1:59:28 PM by Hibernate Tools 4.3.1

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DocAttributeType generated by hbm2java
 */
@Entity
@Table(name = "doc_attribute_type", schema = "public")
public class DocAttributeType implements java.io.Serializable {

	private BigDecimal docAttributeType;
	private BigDecimal defaultSelectionIdFk;
	private String typeName;
	private String defaultValueText;
	private BigDecimal dataTypeFk;
	private String multipleAttributes;

	public DocAttributeType() {
	}

	public DocAttributeType(BigDecimal docAttributeType) {
		this.docAttributeType = docAttributeType;
	}

	public DocAttributeType(BigDecimal docAttributeType, BigDecimal defaultSelectionIdFk, String typeName, String defaultValueText, BigDecimal dataTypeFk, String multipleAttributes) {
		this.docAttributeType = docAttributeType;
		this.defaultSelectionIdFk = defaultSelectionIdFk;
		this.typeName = typeName;
		this.defaultValueText = defaultValueText;
		this.dataTypeFk = dataTypeFk;
		this.multipleAttributes = multipleAttributes;
	}

	@Id
	@Column(name = "doc_attribute_type", unique = true, nullable = false, precision = 10, scale = 0)
	public BigDecimal getDocAttributeType() {
		return this.docAttributeType;
	}

	public void setDocAttributeType(BigDecimal docAttributeType) {
		this.docAttributeType = docAttributeType;
	}

	@Column(name = "default_selection_id_fk", precision = 10, scale = 0)
	public BigDecimal getDefaultSelectionIdFk() {
		return this.defaultSelectionIdFk;
	}

	public void setDefaultSelectionIdFk(BigDecimal defaultSelectionIdFk) {
		this.defaultSelectionIdFk = defaultSelectionIdFk;
	}

	@Column(name = "type_name")
	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Column(name = "default_value_text")
	public String getDefaultValueText() {
		return this.defaultValueText;
	}

	public void setDefaultValueText(String defaultValueText) {
		this.defaultValueText = defaultValueText;
	}

	@Column(name = "data_type_fk", precision = 1, scale = 0)
	public BigDecimal getDataTypeFk() {
		return this.dataTypeFk;
	}

	public void setDataTypeFk(BigDecimal dataTypeFk) {
		this.dataTypeFk = dataTypeFk;
	}

	@Column(name = "multiple_attributes", length = 1)
	public String getMultipleAttributes() {
		return this.multipleAttributes;
	}

	public void setMultipleAttributes(String multipleAttributes) {
		this.multipleAttributes = multipleAttributes;
	}

	@Override
	public String toString() {
		return "DocAttributeType [docAttributeType=" + docAttributeType + ", defaultSelectionIdFk=" + defaultSelectionIdFk + ", typeName=" + typeName + ", defaultValueText=" + defaultValueText
				+ ", dataTypeFk=" + dataTypeFk + ", multipleAttributes=" + multipleAttributes + "]";
	}

}