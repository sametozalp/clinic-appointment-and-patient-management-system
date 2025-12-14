package com.ozalp.appointment_service.dataAccess;

import com.ozalp.appointment_service.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    boolean existsByDoctorIdAndStartTimeLessThanAndEndTimeGreaterThan(
            UUID doctorId,
            LocalDateTime endTime,
            LocalDateTime startTime
    );
}

