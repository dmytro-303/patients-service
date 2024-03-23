package com.cgm.patients;

import com.cgm.patients.domain.Patient;
import com.cgm.patients.domain.Visit;
import com.cgm.patients.domain.VisitReason;
import com.cgm.patients.domain.VisitType;
import com.cgm.patients.domain.exception.PatientNotFoundException;
import com.cgm.patients.domain.exception.VisitNotFoundException;
import com.cgm.patients.domain.repository.PatientRepository;
import com.cgm.patients.domain.repository.VisitRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Import(PatientService.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase
public class PatientServiceIntegrationTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private VisitRepository visitRepository;

    private final Patient patient = new Patient(null, "Chuck", "Norris", LocalDate.of(1950, 1, 1), "123-45-6789");

    @AfterEach
    public void cleanup() {
        visitRepository.deleteAll();
        patientRepository.deleteAll();
    }

    @Test
    public void shouldCreatePatient() {
        var patientId = patientService.createPatient(patient);
        assertNotNull(patientId);

        // Test getPatientById
        var found = patientRepository.findById(patientId).orElseThrow();
        assertNotNull(found);
        assertEquals(patient.getName(), found.getName());
        assertEquals(found.getName(), patient.getName());
        assertEquals(found.getSurname(), patient.getSurname());
        assertEquals(found.getDateOfBirth(), patient.getDateOfBirth());
        assertEquals(found.getSocialSecurityNumber(), patient.getSocialSecurityNumber());
    }

    @Test
    public void shouldGetPatient() {
        var patientId = patientRepository.save(patient).getId();
        assertNotNull(patientId);

        var found = patientService.getPatientById(patientId);
        assertNotNull(found);
        assertEquals(patient.getName(), found.getName());
        assertEquals(found.getName(), patient.getName());
        assertEquals(found.getSurname(), patient.getSurname());
        assertEquals(found.getDateOfBirth(), patient.getDateOfBirth());
        assertEquals(found.getSocialSecurityNumber(), patient.getSocialSecurityNumber());
    }

    @Test
    public void shouldThrowExceptionOnGetPatient_PatientNotFound() {
        var patientId = patientRepository.save(patient).getId();
        assertNotNull(patientId);

        var wrongId = 99L;
        var exception =
                assertThrows(PatientNotFoundException.class, () -> patientService.getPatientById(wrongId));
        assertEquals(wrongId, exception.getPatientId());
    }

    @Test
    public void shouldCreateVisit() {
        var patientId = patientRepository.save(patient).getId();
        assertNotNull(patientId);

        var visit = new Visit();
        visit.setDateTime(LocalDateTime.now());
        visit.setType(VisitType.HOME);
        visit.setReason(VisitReason.FIRST_VISIT);
        visit.setFamilyHistory("Some family history");

        var visitId = patientService.createVisit(patientId, visit);

        var savedVisit = visitRepository.findById(visitId).orElseThrow();

        assertEquals(patientId, savedVisit.getPatient().getId());
        assertEquals(visit.getDateTime(), savedVisit.getDateTime());
        assertEquals(visit.getReason(), savedVisit.getReason());
        assertEquals(visit.getType(), savedVisit.getType());
        assertEquals(visit.getFamilyHistory(), savedVisit.getFamilyHistory());
    }

    @Test
    public void shouldGetVisit() {
        var patientId = patientRepository.save(patient).getId();
        assertNotNull(patientId);

        var visit = new Visit();
        visit.setDateTime(LocalDateTime.now());
        visit.setType(VisitType.HOME);
        visit.setReason(VisitReason.FIRST_VISIT);
        visit.setFamilyHistory("Some family history");
        visit.setPatient(patient);

        var visitId = visitRepository.save(visit).getId();

        var savedVisit = patientService.getPatientVisit(patientId, visitId);

        assertEquals(patientId, savedVisit.getPatient().getId());
        assertEquals(visit.getDateTime(), savedVisit.getDateTime());
        assertEquals(visit.getReason(), savedVisit.getReason());
        assertEquals(visit.getType(), savedVisit.getType());
        assertEquals(visit.getFamilyHistory(), savedVisit.getFamilyHistory());
    }

    @Test
    public void shouldThrowExceptionOnGetVisit_VisitNotFound() {
        var patientId = patientRepository.save(patient).getId();
        var wrongVisitId = 99L;
        var exception =
                assertThrows(VisitNotFoundException.class, () -> patientService.getPatientVisit(patientId, wrongVisitId));
        assertEquals(wrongVisitId, exception.getVisitId());
        assertEquals(patientId, exception.getPatientId());
    }
}
