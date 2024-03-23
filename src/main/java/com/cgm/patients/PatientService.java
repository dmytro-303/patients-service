package com.cgm.patients;

import com.cgm.patients.domain.Patient;
import com.cgm.patients.domain.Visit;
import com.cgm.patients.domain.exception.PatientNotFoundException;
import com.cgm.patients.domain.exception.VisitNotFoundException;
import com.cgm.patients.domain.repository.PatientRepository;
import com.cgm.patients.domain.repository.VisitRepository;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;

    public PatientService(PatientRepository patientRepository, VisitRepository visitRepository) {
        this.patientRepository = patientRepository;
        this.visitRepository = visitRepository;
    }

    public Long createPatient(Patient patient) {
        return patientRepository.save(patient).getId();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
    }

    public Long createVisit(Long patientId, Visit visit) {
        visit.setPatient(getPatientById(patientId));
        return visitRepository.save(visit).getId();
    }

    public Visit getPatientVisit(Long patientId, Long visitId) {
        return visitRepository.findByIdAndPatientId(visitId, patientId)
                .orElseThrow(() -> new VisitNotFoundException(patientId, visitId));
    }
}
