package com.cropadvisor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "regions", uniqueConstraints = {
		@UniqueConstraint(name = "uk_regions_code", columnNames = "code"),
		@UniqueConstraint(name = "uk_regions_name", columnNames = "name")
})
public class Region {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 100)
	@Column(nullable = false, length = 100)
	private String name;

	@NotBlank
	@Size(max = 20)
	@Column(nullable = false, length = 20)
	private String code;

	@Size(max = 500)
	@Column(length = 500)
	private String description;

	@Column(nullable = false)
	private boolean active = true;

	protected Region() {
	}

	public Region(String name, String code, String description) {
		this.name = name;
		this.code = code;
		this.description = description;
	}

	public Long getId() { return id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }
}