package com.cropadvisor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cropadvisor.entity.Officer;

public interface OfficerRepository extends JpaRepository<Officer, Long> {
	Optional<Officer> findByEmployeeCode(String employeeCode);
}