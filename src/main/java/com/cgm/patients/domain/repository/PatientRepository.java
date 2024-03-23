package com.cgm.patients.domain.repository;

import com.cgm.patients.domain.Patient;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends ListCrudRepository<Patient, Long> {
}
