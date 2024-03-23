package com.cgm.patients.dto;

import com.cgm.patients.domain.Visit;
import com.cgm.patients.domain.VisitReason;
import com.cgm.patients.domain.VisitType;

import java.time.LocalDateTime;

public record GetVisitResponse(

        Long id,
        LocalDateTime dateTime,
        VisitType type,
        VisitReason reason,
        String familyHistory) {

    public static GetVisitResponse fromEntity(Visit visit) {
        return new GetVisitResponse(
                visit.getId(),
                visit.getDateTime(),
                visit.getType(),
                visit.getReason(),
                visit.getFamilyHistory()
        );
    }
}
