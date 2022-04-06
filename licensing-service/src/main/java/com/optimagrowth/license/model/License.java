package com.optimagrowth.license.model;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "license")
@Getter @Setter @ToString
public class License {
	
	@Id
	@Column(name = "id", nullable = false)
	private String licenseId;
	
	private String description;
	
	@Column(name = "organization_id", nullable = false)
	private String organizationId;
	
	@Column(name = "product_name", nullable = false)
	private String productName;
	
	@Column(name = "license_type", nullable = false)
	private String licenseType;
	
	@Column(name = "comment")
	private String comment;

	@Transient
	private String organizationName;

	@Transient
	private String contactName;

	@Transient
	private String contactPhone;

	@Transient
	private String contactEmail;
	
	public License withComment(String comment) {
		this.comment = comment;
		return this;
	}

}
