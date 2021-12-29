package com.optimagrowth.license.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Entity
@Table(name = "license")
public class License {
	
	@Id
	@Column(name = "id", nullable = false)
	private String id;
	
	private String description;
	
	@Column(name = "organization_id", nullable = false)
	private String organizationId;
	
	@Column(name = "product_name", nullable = false)
	private String productName;
	
	@Column(name = "license_type", nullable = false)
	private String licenseType;
	
	@Column(name = "comment")
	private String comment;
	
	public License withComment(String comment) {
		this.comment = comment;
		return this;
	}

}
