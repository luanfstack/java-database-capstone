package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final Service service;

    @Autowired
    public AppointmentController(
        AppointmentService appointmentService,
        Service service
    ) {
        this.appointmentService = appointmentService;
        this.service = service;
    }

    @GetMapping("/doctor/{date}/{patientName}/{token}")
    public ResponseEntity<Map<String, Object>> getAppointments(
        @PathVariable String date,
        @PathVariable String patientName,
        @PathVariable String token
    ) {
        Map<String, Object> response = new HashMap<>();

        if (!service.validateToken(token, "doctor")) {
            response.put("status", "failure");
            response.put("message", "Invalid or expired token for doctor role");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            LocalDateTime day = LocalDateTime.parse(date);

            List<Appointment> appointments = appointmentService.getAppointment(
                1L,
                day,
                patientName
            );

            response.put("status", "success");
            response.put("appointments", appointments);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "failure");
            response.put(
                "message",
                "Error fetching appointments: " + e.getMessage()
            );
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/book/{token}")
    public ResponseEntity<Map<String, Object>> bookAppointment(
        @RequestBody Appointment appointment,
        @PathVariable String token
    ) {
        Map<String, Object> response = new HashMap<>();

        if (!service.validateToken(token, "patient")) {
            response.put("status", "failure");
            response.put(
                "message",
                "Invalid or expired token for patient role"
            );
            return ResponseEntity.badRequest().body(response);
        }

        try {
            int result = appointmentService.bookAppointment(appointment);

            if (result == 1) {
                response.put("status", "success");
                response.put("message", "Appointment booked successfully");
            } else {
                response.put("status", "failure");
                response.put("message", "Failed to book appointment");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "failure");
            response.put(
                "message",
                "Error booking appointment: " + e.getMessage()
            );
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/update/{token}")
    public ResponseEntity<Map<String, Object>> updateAppointment(
        @RequestBody Appointment appointment,
        @PathVariable String token
    ) {
        Map<String, Object> response = new HashMap<>();

        if (!service.validateToken(token, "patient")) {
            response.put("status", "failure");
            response.put(
                "message",
                "Invalid or expired token for patient role"
            );
            return ResponseEntity.badRequest().body(response);
        }

        try {
            String result = appointmentService.updateAppointment(appointment);

            if (result.contains("successfully")) {
                response.put("status", "success");
                response.put("message", result);
            } else {
                response.put("status", "failure");
                response.put("message", result);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "failure");
            response.put(
                "message",
                "Error updating appointment: " + e.getMessage()
            );
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/cancel/{appointmentId}/{token}")
    public ResponseEntity<Map<String, Object>> cancelAppointment(
        @PathVariable Long appointmentId,
        @PathVariable String token
    ) {
        Map<String, Object> response = new HashMap<>();

        if (!service.validateToken(token, "patient")) {
            response.put("status", "failure");
            response.put(
                "message",
                "Invalid or expired token for patient role"
            );
            return ResponseEntity.badRequest().body(response);
        }

        try {
            String result = appointmentService.cancelAppointment(
                appointmentId,
                1L
            );

            if (result.contains("successfully")) {
                response.put("status", "success");
                response.put("message", result);
            } else {
                response.put("status", "failure");
                response.put("message", result);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "failure");
            response.put(
                "message",
                "Error cancelling appointment: " + e.getMessage()
            );
            return ResponseEntity.badRequest().body(response);
        }
    }
}
