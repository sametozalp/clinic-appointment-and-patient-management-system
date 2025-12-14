package com.ozalp.doctor_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DoctorRepository extends JpaRepository<com.ozalp.doctor_service.entities.Doctor, UUID> {
}
