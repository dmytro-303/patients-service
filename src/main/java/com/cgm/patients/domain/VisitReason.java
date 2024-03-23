package com.cgm.patients.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum VisitReason {
    FIRST_VISIT("first visit"),
    RECURRING("recurring"),
    URGENT("urgent");

    @JsonValue
    public final String label;

    VisitReason(String label) {
        this.label = label;
    }
}
