package com.cropadvisor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cropadvisor.entity.Ticket;
import com.cropadvisor.entity.TicketStatus;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
	Optional<Ticket> findByTicketNumber(String ticketNumber);
	List<Ticket> findByFarmerIdOrderByCreatedAtDesc(Long farmerId);
	List<Ticket> findByAssignedOfficerIdAndStatus(Long officerId, TicketStatus status);
}