package com.cgm.patients.controller;

import com.cgm.patients.PatientService;
import com.cgm.patients.dto.CreatePatientRequest;
import com.cgm.patients.dto.CreatePatientResponse;
import com.cgm.patients.dto.CreateVisitRequest;
import com.cgm.patients.dto.CreateVisitResponse;
import com.cgm.patients.dto.GetPatientResponse;
import com.cgm.patients.dto.GetVisitResponse;
import com.cgm.patients.dto.UpdateVisitRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public CreatePatientResponse createPatient(@RequestBody CreatePatientRequest request) {
        var patientId = patientService.createPatient(request.toEntity());
        return new CreatePatientResponse(patientId);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public GetPatientResponse getPatient(@PathVariable Long id) {
        return GetPatientResponse.fromEntity(patientService.getPatientById(id));
    }

    @PostMapping(path = "/{patientId}/visits", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public CreateVisitResponse createVisit(@PathVariable Long patientId, @RequestBody CreateVisitRequest createVisitRequest) {
        var visitId = patientService.createVisit(patientId, createVisitRequest.toEntity());
        return new CreateVisitResponse(visitId);
    }

    @GetMapping(path = "/{patientId}/visits/{visitId}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public GetVisitResponse getPatientVisit(@PathVariable Long patientId, @PathVariable Long visitId) {
        return GetVisitResponse.fromEntity(patientService.getPatientVisit(patientId, visitId));
    }

    @PutMapping(path = "/{patientId}/visits/{visitId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updateVisit(@PathVariable Long patientId, @PathVariable Long visitId, @RequestBody UpdateVisitRequest createVisitRequest) {
        patientService.updateVisit(patientId, createVisitRequest.toEntity(visitId));
    }
}
