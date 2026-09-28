package com.cropadvisor.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tickets", indexes = {
		@Index(name = "idx_tickets_farmer_id", columnList = "farmer_id"),
		@Index(name = "idx_tickets_crop_id", columnList = "crop_id"),
		@Index(name = "idx_tickets_region_status", columnList = "region_id,status"),
		@Index(name = "idx_tickets_officer_status", columnList = "assigned_officer_id,status"),
		@Index(name = "idx_tickets_created_at", columnList = "created_at"),
		@Index(name = "idx_tickets_escalated_at", columnList = "escalated_at")
}, uniqueConstraints = {
		@UniqueConstraint(name = "uk_tickets_ticket_number", columnNames = "ticket_number")
})
public class Ticket extends AuditedEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 36)
	@Column(name = "ticket_number", nullable = false, unique = true, updatable = false, length = 36)
	private String ticketNumber;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "farmer_id", nullable = false)
	private Farmer farmer;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "crop_id", nullable = false)
	private Crop crop;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "region_id", nullable = false)
	private Region region;

	@NotBlank
	@Size(max = 1000)
	@Column(nullable = false, length = 1000)
	private String symptoms;

	@NotBlank
	@Size(max = 5000)
	@Column(nullable = false, length = 5000)
	private String description;

	@Size(max = 2048)
	@Column(name = "photo_reference", length = 2048)
	private String photoReference;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assigned_officer_id")
	private Officer assignedOfficer;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 32)
	private TicketStatus status = TicketStatus.NEW;

	@Column(name = "resolved_at")
	private LocalDateTime resolvedAt;

	@Min(0)
	@Column(name = "resolution_time_minutes")
	private Long resolutionTimeMinutes;

	@Min(0)
	@Column(name = "escalation_level", nullable = false)
	private int escalationLevel;

	@Column(name = "escalated_at")
	private LocalDateTime escalatedAt;

	@Size(max = 1000)
	@Column(name = "escalation_reason", length = 1000)
	private String escalationReason;

	@OneToMany(mappedBy = "ticket", cascade = CascadeType.PERSIST)
	private List<TicketResponse> responses = new ArrayList<>();

	@OneToMany(mappedBy = "ticket", cascade = CascadeType.PERSIST)
	private List<TicketStatusHistory> statusHistory = new ArrayList<>();

	protected Ticket() {
	}

	public Ticket(Farmer farmer, Crop crop, Region region, String symptoms, String description) {
		this.farmer = farmer;
		this.crop = crop;
		this.region = region;
		this.symptoms = symptoms;
		this.description = description;
	}

	@PrePersist
	private void assignTicketNumber() {
		if (ticketNumber == null) {
			ticketNumber = UUID.randomUUID().toString();
		}
	}

	public void addResponse(TicketResponse response) {
		responses.add(response);
		response.setTicket(this);
	}

	public void addStatusHistory(TicketStatusHistory history) {
		statusHistory.add(history);
		history.setTicket(this);
	}

	public Long getId() { return id; }
	public String getTicketNumber() { return ticketNumber; }
	public Farmer getFarmer() { return farmer; }
	public void setFarmer(Farmer farmer) { this.farmer = farmer; }
	public Crop getCrop() { return crop; }
	public void setCrop(Crop crop) { this.crop = crop; }
	public Region getRegion() { return region; }
	public void setRegion(Region region) { this.region = region; }
	public String getSymptoms() { return symptoms; }
	public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public String getPhotoReference() { return photoReference; }
	public void setPhotoReference(String photoReference) { this.photoReference = photoReference; }
	public Officer getAssignedOfficer() { return assignedOfficer; }
	public void setAssignedOfficer(Officer assignedOfficer) { this.assignedOfficer = assignedOfficer; }
	public TicketStatus getStatus() { return status; }
	public void setStatus(TicketStatus status) { this.status = status; }
	public LocalDateTime getResolvedAt() { return resolvedAt; }
	public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
	public Long getResolutionTimeMinutes() { return resolutionTimeMinutes; }
	public void setResolutionTimeMinutes(Long resolutionTimeMinutes) { this.resolutionTimeMinutes = resolutionTimeMinutes; }
	public int getEscalationLevel() { return escalationLevel; }
	public void setEscalationLevel(int escalationLevel) { this.escalationLevel = escalationLevel; }
	public LocalDateTime getEscalatedAt() { return escalatedAt; }
	public void setEscalatedAt(LocalDateTime escalatedAt) { this.escalatedAt = escalatedAt; }
	public String getEscalationReason() { return escalationReason; }
	public void setEscalationReason(String escalationReason) { this.escalationReason = escalationReason; }
	public List<TicketResponse> getResponses() { return List.copyOf(responses); }
	public List<TicketStatusHistory> getStatusHistory() { return List.copyOf(statusHistory); }
}