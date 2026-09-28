package com.cropadvisor.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "ticket_responses")
public class TicketResponse {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ticket_id", nullable = false)
	private Ticket ticket;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "farmer_id")
	private Farmer farmer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "officer_id")
	private Officer officer;

	@NotBlank
	@Size(max = 5000)
	@Column(nullable = false, length = 5000)
	private String message;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	protected TicketResponse() {
	}

	public TicketResponse(Farmer farmer, Officer officer, String message) {
		this.farmer = farmer;
		this.officer = officer;
		this.message = message;
	}

	@AssertTrue(message = "A response must have exactly one farmer or officer author")
	public boolean hasExactlyOneAuthor() {
		return (farmer == null) != (officer == null);
	}

	public Long getId() { return id; }
	public Ticket getTicket() { return ticket; }
	public void setTicket(Ticket ticket) { this.ticket = ticket; }
	public Farmer getFarmer() { return farmer; }
	public Officer getOfficer() { return officer; }
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
	public LocalDateTime getCreatedAt() { return createdAt; }
}