package com.cropadvisor.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

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
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "ticket_status_history", indexes = {
		@Index(name = "idx_status_history_ticket_changed", columnList = "ticket_id,changed_at")
})
public class TicketStatusHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ticket_id", nullable = false)
	private Ticket ticket;

	@Enumerated(EnumType.STRING)
	@Column(name = "previous_status", length = 32)
	private TicketStatus previousStatus;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "new_status", nullable = false, length = 32)
	private TicketStatus newStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "changed_by_farmer_id")
	private Farmer changedByFarmer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "changed_by_officer_id")
	private Officer changedByOfficer;

	@Size(max = 1000)
	@Column(length = 1000)
	private String note;

	@CreationTimestamp
	@Column(name = "changed_at", nullable = false, updatable = false)
	private LocalDateTime changedAt;

	protected TicketStatusHistory() {
	}

	public TicketStatusHistory(TicketStatus previousStatus, TicketStatus newStatus,
			Farmer changedByFarmer, Officer changedByOfficer, String note) {
		this.previousStatus = previousStatus;
		this.newStatus = newStatus;
		this.changedByFarmer = changedByFarmer;
		this.changedByOfficer = changedByOfficer;
		this.note = note;
	}

	@AssertTrue(message = "A status change cannot have both a farmer and officer actor")
	public boolean hasAtMostOneActor() {
		return changedByFarmer == null || changedByOfficer == null;
	}

	public Long getId() { return id; }
	public Ticket getTicket() { return ticket; }
	public void setTicket(Ticket ticket) { this.ticket = ticket; }
	public TicketStatus getPreviousStatus() { return previousStatus; }
	public TicketStatus getNewStatus() { return newStatus; }
	public Farmer getChangedByFarmer() { return changedByFarmer; }
	public Officer getChangedByOfficer() { return changedByOfficer; }
	public String getNote() { return note; }
	public LocalDateTime getChangedAt() { return changedAt; }
}