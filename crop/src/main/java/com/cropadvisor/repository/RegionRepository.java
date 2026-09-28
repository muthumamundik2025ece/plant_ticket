package com.cropadvisor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cropadvisor.entity.Region;

public interface RegionRepository extends JpaRepository<Region, Long> {
	Optional<Region> findByCode(String code);
}