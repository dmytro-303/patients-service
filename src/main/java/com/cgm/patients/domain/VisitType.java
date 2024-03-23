package com.cgm.patients.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum VisitType {
    HOME("home"),
    OFFICE("office");

    @JsonValue
    public final String label;

    VisitType(String label) {
        this.label = label;
    }
}
