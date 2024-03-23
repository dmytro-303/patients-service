package com.cgm.patients.domain.exception;

import lombok.Getter;

@Getter
public class VisitNotFoundException extends IllegalArgumentException {

    private final Long visitId;
    private final Long patientId;

    public VisitNotFoundException(Long patientId, Long visitId) {
        super("Visit ID %s is not found for patient ID %s".formatted(visitId, patientId));
        this.visitId = visitId;
        this.patientId = patientId;
    }
}
