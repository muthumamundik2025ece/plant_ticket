package com.cropadvisor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cropadvisor.entity.TicketStatusHistory;

public interface TicketStatusHistoryRepository extends JpaRepository<TicketStatusHistory, Long> {
	List<TicketStatusHistory> findByTicketIdOrderByChangedAtAsc(Long ticketId);
}