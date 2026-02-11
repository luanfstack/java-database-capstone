package com.project.back_end.controllers;

import com.project.back_end.dtos.Prescription;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.Service;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private Service sharedService;

    @Autowired
    private AppointmentService appointmentService;

    @Value("${api.path}")
    private String apiPath;

    @PostMapping("/save/{token}")
    public ResponseEntity<Map<String, Object>> savePrescription(
        @RequestBody Prescription prescription,
        @PathVariable String token
    ) {
        if (!sharedService.validateToken("doctor", token)) {
            return ResponseEntity.badRequest().build();
        }

        appointmentService.updateAppointmentStatus(
            prescription.getAppointmentId(),
            "prescribed"
        );

        try {
            prescriptionService.savePrescription(prescription);
            return ResponseEntity.ok(
                Map.of("message", "Prescription saved successfully")
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/get/{appointmentId}/{token}")
    public ResponseEntity<Map<String, Object>> getPrescription(
        @PathVariable Long appointmentId,
        @PathVariable String token
    ) {
        if (!sharedService.validateToken("doctor", token)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Prescription prescription =
                prescriptionService.getPrescriptionByAppointmentId(
                    appointmentId
                );
            return ResponseEntity.ok(Map.of("prescription", prescription));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
