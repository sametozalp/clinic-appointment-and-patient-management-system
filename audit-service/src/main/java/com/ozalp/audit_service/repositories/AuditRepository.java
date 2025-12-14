package com.ozalp.audit_service.repositories;

import com.ozalp.audit_service.entities.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditRepository extends JpaRepository<AuditLog, UUID> {
}
