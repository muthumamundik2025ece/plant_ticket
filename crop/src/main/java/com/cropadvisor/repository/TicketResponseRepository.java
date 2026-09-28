package com.cropadvisor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cropadvisor.entity.TicketResponse;

public interface TicketResponseRepository extends JpaRepository<TicketResponse, Long> {
	List<TicketResponse> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}