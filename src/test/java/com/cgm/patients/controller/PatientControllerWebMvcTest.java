package com.cgm.patients.controller;

import com.cgm.patients.PatientService;
import com.cgm.patients.domain.Patient;
import com.cgm.patients.domain.Visit;
import com.cgm.patients.domain.VisitReason;
import com.cgm.patients.domain.VisitType;
import com.cgm.patients.dto.CreatePatientRequest;
import com.cgm.patients.dto.CreateVisitRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
class PatientControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void resetMocks() {
        Mockito.reset(patientService);
    }

    @Test
    public void createPatientTest() throws Exception {
        var request = new CreatePatientRequest(null, "Chuck", "Norris", LocalDate.of(1950, 1, 1), "123-45-6789");
        var expectedPatientId = 1L;

        given(patientService.createPatient(any(Patient.class))).willReturn(expectedPatientId);

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.patientId").value(expectedPatientId));
    }

    @Test
    public void getPatientTest() throws Exception {
        var patientId = 1L;
        var patient = new Patient(patientId, "Chuck", "Norris", LocalDate.of(1990, 1, 1), "123-45-6789");

        given(patientService.getPatientById(patientId)).willReturn(patient);

        mockMvc.perform(get("/api/patients/{id}", patientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(patientId))
                .andExpect(jsonPath("$.name").value(patient.getName()))
                .andExpect(jsonPath("$.surname").value(patient.getSurname()))
                .andExpect(jsonPath("$.dateOfBirth").value(patient.getDateOfBirth().toString()))
                .andExpect(jsonPath("$.socialSecurityNumber").value(patient.getSocialSecurityNumber()));
    }


    @Test
    public void shouldCreatePatientVisit() throws Exception {
        var patientId = 1L;
        var request = new CreateVisitRequest(
                LocalDateTime.now(),
                VisitType.HOME,
                VisitReason.FIRST_VISIT,
                "Some family history");

        var expectedVisitId = 2L;

        given(patientService.createVisit(eq(patientId), any(Visit.class))).willReturn(expectedVisitId);

        mockMvc.perform(post("/api/patients/{patientId}/visits", patientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.visitId").value(expectedVisitId));
    }

    @Test
    public void shouldGetPatientVisit() throws Exception {
        Long patientId = 1L;
        Long visitId = 2L;

        var visit = new Visit();
        visit.setId(visitId);
        visit.setDateTime(LocalDateTime.now());
        visit.setType(VisitType.HOME);
        visit.setReason(VisitReason.FIRST_VISIT);
        visit.setFamilyHistory("Some family history");

        given(patientService.getPatientVisit(patientId, visitId)).willReturn(visit);

        mockMvc.perform(get("/api/patients/{patientId}/visits/{visitId}", patientId, visitId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(visitId))
                .andExpect(jsonPath("$.dateTime").value(visit.getDateTime().toString()))
                .andExpect(jsonPath("$.type").value(visit.getType().label))
                .andExpect(jsonPath("$.reason").value(visit.getReason().label))
                .andExpect(jsonPath("$.familyHistory").value(visit.getFamilyHistory()));
    }
}