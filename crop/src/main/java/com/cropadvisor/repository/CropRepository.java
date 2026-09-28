package com.cropadvisor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cropadvisor.entity.Crop;

public interface CropRepository extends JpaRepository<Crop, Long> {
	List<Crop> findByNameIgnoreCase(String name);
}