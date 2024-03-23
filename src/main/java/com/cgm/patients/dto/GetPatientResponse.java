package com.cgm.patients.dto;

import com.cgm.patients.domain.Patient;

import java.time.LocalDate;

public record GetPatientResponse(
        Long id,
        String name,
        String surname,
        LocalDate dateOfBirth,
        String socialSecurityNumber) {

    public static GetPatientResponse fromEntity(Patient patient) {
        return new GetPatientResponse(
                patient.getId(),
                patient.getName(),
                patient.getSurname(),
                patient.getDateOfBirth(),
                patient.getSocialSecurityNumber()
        );
    }
}
