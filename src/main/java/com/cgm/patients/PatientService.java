package com.cgm.patients;

import com.cgm.patients.domain.Patient;
import com.cgm.patients.domain.Visit;
import com.cgm.patients.domain.exception.PatientNotFoundException;
import com.cgm.patients.domain.exception.VisitNotFoundException;
import com.cgm.patients.domain.repository.PatientRepository;
import com.cgm.patients.domain.repository.VisitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;

    public PatientService(PatientRepository patientRepository, VisitRepository visitRepository) {
        this.patientRepository = patientRepository;
        this.visitRepository = visitRepository;
    }

    public Long createPatient(Patient patient) {
        var patientId = patientRepository.save(patient).getId();
        logger.info("Created new patient ID %s".formatted(patientId));
        return patientId;
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
    }

    public Long createVisit(Long patientId, Visit visit) {
        visit.setPatient(getPatientById(patientId));
        var visitId = visitRepository.save(visit).getId();
        logger.info("Created a new visit ID %s".formatted(visitId));
        return visitId;
    }

    public Visit getPatientVisit(Long patientId, Long visitId) {
        return visitRepository.findByIdAndPatientId(visitId, patientId)
                .orElseThrow(() -> new VisitNotFoundException(patientId, visitId));
    }

    public void updateVisit(Long patientId, Visit newVisit) {
        var oldVisit = getPatientVisit(patientId, newVisit.getId()); // check visit exists
        newVisit.setPatient(oldVisit.getPatient());
        logger.info("Updated visit ID %s".formatted(newVisit.getId()));
        visitRepository.save(newVisit); // update
    }
}
