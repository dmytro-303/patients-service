package com.cgm.patients.domain.exception;

import lombok.Getter;

@Getter
public class PatientNotFoundException extends IllegalArgumentException {

    private final Long patientId;

    public PatientNotFoundException(Long patientId) {
        super("Patient ID %s is not found".formatted(patientId));
        this.patientId = patientId;
    }
}
