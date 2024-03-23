package com.cgm.patients.domain.repository;

import com.cgm.patients.domain.Visit;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitRepository extends ListCrudRepository<Visit, Long> {

    Optional<Visit> findByIdAndPatientId(Long id, Long patientId);

}
