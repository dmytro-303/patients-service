package com.cgm.patients;

import com.cgm.patients.domain.Patient;
import com.cgm.patients.domain.exception.PatientNotFoundException;
import com.cgm.patients.domain.repository.PatientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;

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

    private final Patient patient = new Patient(null, "Chuck", "Norris", LocalDate.of(1950, 1, 1), "123-45-6789");

    @AfterEach
    public void cleanup() {
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
}
