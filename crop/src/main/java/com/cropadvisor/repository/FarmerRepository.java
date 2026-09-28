package com.cropadvisor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cropadvisor.entity.Farmer;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {
	Optional<Farmer> findByEmail(String email);
}