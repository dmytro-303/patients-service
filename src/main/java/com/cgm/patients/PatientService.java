package com.cgm.patients;

import com.cgm.patients.domain.Patient;
import com.cgm.patients.domain.exception.PatientNotFoundException;
import com.cgm.patients.domain.repository.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Long createPatient(Patient patient) {
        return patientRepository.save(patient).getId();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
    }

}
