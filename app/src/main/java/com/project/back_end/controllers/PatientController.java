package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private Service sharedService;

    @GetMapping("/get/{token}")
    public ResponseEntity<Map<String, Object>> getPatientDetails(
        @PathVariable String token
    ) {
        if (!sharedService.validateToken("patient", token)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
            Map.of("patient", sharedService.validateToken(token, "patient"))
        );
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createPatient(
        @RequestBody Patient patient
    ) {
        try {
            patientService.createPatient(patient);
            return ResponseEntity.ok(
                Map.of("message", "Patient created successfully")
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login login) {
        String result = sharedService.validatePatientLogin(
            login.getIdentifier(),
            login.getPassword()
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/appointment/{patientId}/{token}/{user}")
    public ResponseEntity<Map<String, Object>> getPatientAppointment(
        @PathVariable Long patientId,
        @PathVariable String token,
        @PathVariable String user
    ) {
        if (!sharedService.validateToken(user, token)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Object appointments = patientService.getPatientAppointment(
                patientId
            );
            return ResponseEntity.ok(Map.of("appointments", appointments));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/appointment/filter/{condition}/{name}/{token}")
    public ResponseEntity<Map<String, Object>> filterPatientAppointment(
        @PathVariable String condition,
        @PathVariable String name,
        @PathVariable String token
    ) {
        if (!sharedService.validateToken("patient", token)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Patient patient = patientService.getPatientDetails(token);
            Object filteredAppointments =
                patientService.filterByDoctorAndCondition(
                    patient.getId(),
                    name,
                    condition
                );
            return ResponseEntity.ok(
                Map.of("appointments", filteredAppointments)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
