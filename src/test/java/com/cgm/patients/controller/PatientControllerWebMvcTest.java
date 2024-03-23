package com.cgm.patients.controller;

import com.cgm.patients.PatientService;
import com.cgm.patients.domain.Patient;
import com.cgm.patients.domain.Visit;
import com.cgm.patients.domain.VisitReason;
import com.cgm.patients.domain.VisitType;
import com.cgm.patients.dto.CreatePatientRequest;
import com.cgm.patients.dto.CreateVisitRequest;
import com.cgm.patients.dto.UpdateVisitRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

        var patientCaptor = ArgumentCaptor.forClass(Patient.class);
        given(patientService.createPatient(patientCaptor.capture())).willReturn(expectedPatientId);

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.patientId").value(expectedPatientId));

        verify(patientService, times(1)).createPatient(patientCaptor.capture());

        var patient = patientCaptor.getValue();
        assertEquals(request.name(), patient.getName());
        assertEquals(request.surname(), patient.getSurname());
        assertEquals(request.dateOfBirth(), patient.getDateOfBirth());
        assertEquals(request.socialSecurityNumber(), patient.getSocialSecurityNumber());
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

        var visitCaptor = ArgumentCaptor.forClass(Visit.class);
        given(patientService.createVisit(eq(patientId), visitCaptor.capture())).willReturn(expectedVisitId);

        mockMvc.perform(post("/api/patients/{patientId}/visits", patientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.visitId").value(expectedVisitId));

        verify(patientService, times(1)).createVisit(
                eq(patientId),
                visitCaptor.capture());

        var visit = visitCaptor.getValue();
        assertEquals(request.dateTime(), visit.getDateTime());
        assertEquals(request.reason(), visit.getReason());
        assertEquals(request.type(), visit.getType());
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

    @Test
    public void shouldUpdatePatientVisit() throws Exception {
        var patientId = 1L;
        var request = new UpdateVisitRequest(
                LocalDateTime.now(),
                VisitType.HOME,
                VisitReason.FIRST_VISIT,
                "Some family history");

        var visitId = 2L;

        mockMvc.perform(put("/api/patients/{patientId}/visits/{visitId}", patientId, visitId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        var visitCaptor = ArgumentCaptor.forClass(Visit.class);
        verify(patientService, times(1)).updateVisit(
                eq(patientId),
                visitCaptor.capture());

        var visit = visitCaptor.getValue();
        assertEquals(request.dateTime(), visit.getDateTime());
        assertEquals(request.reason(), visit.getReason());
        assertEquals(request.type(), visit.getType());
    }
}