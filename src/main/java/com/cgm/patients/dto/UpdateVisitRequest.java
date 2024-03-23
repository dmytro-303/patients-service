package com.cgm.patients.dto;

import com.cgm.patients.domain.Visit;
import com.cgm.patients.domain.VisitReason;
import com.cgm.patients.domain.VisitType;

import java.time.LocalDateTime;

public record UpdateVisitRequest(
        LocalDateTime dateTime,
        VisitType type,
        VisitReason reason,
        String familyHistory) {

    public Visit toEntity(Long visitId) {
        Visit visit = new Visit();
        visit.setDateTime(this.dateTime);
        visit.setType(this.type);
        visit.setReason(this.reason);
        visit.setFamilyHistory(this.familyHistory);
        visit.setId(visitId);
        return visit;
    }
}
