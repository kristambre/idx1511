package entity.subject;

// Generated May 23, 2015 1:59:28 PM by Hibernate Tools 4.3.1

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * AddressType generated by hbm2java
 */
@Entity
@Table(name = "address_type", schema = "public")
public class AddressType implements java.io.Serializable {

	private BigDecimal addressType;
	private String typeName;

	public AddressType() {
	}

	public AddressType(BigDecimal addressType) {
		this.addressType = addressType;
	}

	public AddressType(BigDecimal addressType, String typeName) {
		this.addressType = addressType;
		this.typeName = typeName;
	}

	@Id
	@Column(name = "address_type", unique = true, nullable = false, precision = 10, scale = 0)
	public BigDecimal getAddressType() {
		return this.addressType;
	}

	public void setAddressType(BigDecimal addressType) {
		this.addressType = addressType;
	}

	@Column(name = "type_name", length = 200)
	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
