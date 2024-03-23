package com.cgm.patients.dto;

import com.cgm.patients.domain.Patient;

import java.time.LocalDate;

public record CreatePatientRequest(
        Long id,
        String name,
        String surname,
        LocalDate dateOfBirth,
        String socialSecurityNumber
) {
    public Patient toEntity() {
        Patient patient = new Patient();
        patient.setId(this.id);
        patient.setName(this.name);
        patient.setSurname(this.surname);
        patient.setDateOfBirth(this.dateOfBirth);
        patient.setSocialSecurityNumber(this.socialSecurityNumber);
        return patient;
    }
}