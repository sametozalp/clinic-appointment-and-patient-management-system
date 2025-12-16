package com.ozalp.doctor_service.business.concretes;

import com.ozalp.doctor_service.business.abstracts.DoctorService;
import com.ozalp.doctor_service.business.dtos.requests.CreateDoctorRequest;
import com.ozalp.doctor_service.business.dtos.responses.DoctorResponse;
import com.ozalp.doctor_service.business.mappers.DoctorMapper;
import com.ozalp.doctor_service.entities.Doctor;
import com.ozalp.doctor_service.repositories.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DoctorManager implements DoctorService {

    private final DoctorRepository repository;
    private final DoctorMapper mapper;

    @CacheEvict(value = "doctors", key = "'all'")
    public DoctorResponse create(CreateDoctorRequest request) {
        Doctor doctor = mapper.toEntity(request);
        doctor.setActive(true);
        return mapper.toResponse(repository.save(doctor));
    }

    @Cacheable(value = "doctors", key = "'all'")
    public List<DoctorResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public void deactivate(UUID id) {
        Doctor d = repository.findById(id).orElseThrow();
        d.setActive(false);
        repository.save(d);
    }
}
