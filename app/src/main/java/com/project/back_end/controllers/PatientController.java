package com.project.back_end.controllers;

import com.project.back_end.dtos.Login;
import com.project.back_end.dtos.Patient;
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
    public ResponseEntity<Map<String, Object>> getPatient(
        @PathVariable String token
    ) {
        if (!sharedService.validateToken("patient", token)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
            Map.of("patient", patientService.getPatientByToken(token))
        );
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createPatient(
        @RequestBody Patient patient
    ) {
        if (sharedService.patientExists(patient.getEmail())) {
            return ResponseEntity.status(409).build();
        }

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
    public ResponseEntity<Map<String, Object>> login(@RequestBody Login login) {
        Map<String, Object> result = sharedService.validatePatientLogin(login);
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
            Object appointments = patientService.getPatientAppointments(
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
            Object filteredAppointments =
                sharedService.filterPatientAppointments(condition, name, token);
            return ResponseEntity.ok(
                Map.of("appointments", filteredAppointments)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
