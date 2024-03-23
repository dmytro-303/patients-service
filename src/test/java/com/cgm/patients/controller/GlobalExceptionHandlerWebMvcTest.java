package com.cgm.patients.controller;

import com.cgm.patients.PatientService;
import com.cgm.patients.domain.exception.PatientNotFoundException;
import com.cgm.patients.domain.exception.VisitNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {GlobalExceptionHandler.class, PatientController.class})
class GlobalExceptionHandlerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;
    private final Long patientId = 1L;

    @Test
    public void shouldHandlePatientNotFoundException() throws Exception {
        given(patientService.getPatientById(patientId))
                .willThrow(new PatientNotFoundException(patientId));

        var expectedErrorMessage = "Patient ID %s is not found".formatted(patientId);

        mockMvc.perform(get("/api/patients/{id}", patientId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));
    }

    @Test
    public void shouldHandleVisitNotFoundException() throws Exception {
        var visitId = 1L;
        given(patientService.getPatientVisit(patientId, visitId))
                .willThrow(new VisitNotFoundException(patientId, visitId));

        var expectedErrorMessage = "Visit ID %s is not found for patient ID %s".formatted(visitId, patientId);

        mockMvc.perform(get("/api/patients/{patientId}/visits/{visitId}", patientId, visitId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));
    }


    @Test
    public void shouldHandleIllegalArgumentExceptionException() throws Exception {
        var errorMessage = "error message";
        given(patientService.getPatientById(patientId))
                .willThrow(new IllegalArgumentException(errorMessage));

        mockMvc.perform(get("/api/patients/{id}", patientId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }


    @Test
    public void shouldHandleRuntimeExceptions() throws Exception {
        var errorMessage = "error message";
        given(patientService.getPatientById(patientId))
                .willThrow(new RuntimeException(errorMessage));

        var expectedErrorMessage = "Internal server error happened";
        mockMvc.perform(get("/api/patients/{id}", patientId))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));
    }
}