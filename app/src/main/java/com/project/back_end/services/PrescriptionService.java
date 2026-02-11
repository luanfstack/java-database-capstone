package com.project.back_end.services;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.PrescriptionRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    @Autowired
    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    public Map<String, Object> savePrescription(Prescription prescription) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Prescription> existingPrescriptions =
                prescriptionRepository.findByAppointmentId(
                    prescription.getAppointmentId()
                );

            if (!existingPrescriptions.isEmpty()) {
                response.put("status", 400);
                response.put(
                    "message",
                    "Prescription already exists for this appointment"
                );
                return response;
            }

            Prescription savedPrescription = prescriptionRepository.save(
                prescription
            );

            response.put("status", 201);
            response.put("message", "Prescription saved successfully");
            response.put("prescription", savedPrescription);
            return response;
        } catch (Exception e) {
            response.put("status", 500);
            response.put(
                "message",
                "Error saving prescription: " + e.getMessage()
            );
            return response;
        }
    }

    public ResponseEntity<Map<String, Object>> getPrescription(
        Long appointmentId
    ) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Prescription> prescriptions =
                prescriptionRepository.findByAppointmentId(appointmentId);

            if (prescriptions.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // TODO
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
