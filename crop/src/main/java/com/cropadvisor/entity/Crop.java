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
@Table(name = "crops", uniqueConstraints = {
		@UniqueConstraint(name = "uk_crops_name_variety", columnNames = { "name", "variety" })
})
public class Crop {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 100)
	@Column(nullable = false, length = 100)
	private String name;

	@Size(max = 100)
	@Column(length = 100)
	private String variety;

	@Size(max = 500)
	@Column(length = 500)
	private String description;

	@Column(nullable = false)
	private boolean active = true;

	protected Crop() {
	}

	public Crop(String name, String variety, String description) {
		this.name = name;
		this.variety = variety;
		this.description = description;
	}

	public Long getId() { return id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getVariety() { return variety; }
	public void setVariety(String variety) { this.variety = variety; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }
}