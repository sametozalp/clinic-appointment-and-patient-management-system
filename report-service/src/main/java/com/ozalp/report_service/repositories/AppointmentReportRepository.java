package com.ozalp.report_service.repositories;

import com.ozalp.report_service.entities.AppointmentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentReportRepository
        extends JpaRepository<AppointmentReport, UUID> {

    List<AppointmentReport> findByDoctorId(UUID doctorId);
}
