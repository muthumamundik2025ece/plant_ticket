package com.cropadvisor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "officers", indexes = {
		@Index(name = "idx_officers_region_id", columnList = "region_id")
}, uniqueConstraints = {
		@UniqueConstraint(name = "uk_officers_email", columnNames = "email"),
		@UniqueConstraint(name = "uk_officers_employee_code", columnNames = "employee_code")
})
public class Officer extends AuditedEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 40)
	@Column(name = "employee_code", nullable = false, length = 40)
	private String employeeCode;

	@NotBlank
	@Size(max = 120)
	@Column(name = "full_name", nullable = false, length = 120)
	private String fullName;

	@NotBlank
	@Email
	@Size(max = 254)
	@Column(nullable = false, length = 254)
	private String email;

	@Size(max = 30)
	@Column(length = 30)
	private String phone;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "region_id", nullable = false)
	private Region region;

	@Column(nullable = false)
	private boolean active = true;

	protected Officer() {
	}

	public Officer(String employeeCode, String fullName, String email, String phone, Region region) {
		this.employeeCode = employeeCode;
		this.fullName = fullName;
		this.email = email;
		this.phone = phone;
		this.region = region;
	}

	public Long getId() { return id; }
	public String getEmployeeCode() { return employeeCode; }
	public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }
	public String getFullName() { return fullName; }
	public void setFullName(String fullName) { this.fullName = fullName; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }
	public Region getRegion() { return region; }
	public void setRegion(Region region) { this.region = region; }
	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }
}