package com.project.back_end.controllers;

import com.project.back_end.dtos.Doctor;
import com.project.back_end.dtos.Login;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private Service sharedService;

    @Value("${api.path}")
    private String apiPath;

    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(
        @PathVariable String user,
        @PathVariable Long doctorId,
        @PathVariable String date,
        @PathVariable String token
    ) {
        if (!sharedService.validateToken(user, token)) {
            return ResponseEntity.badRequest().build();
        }

        boolean isAvailable = doctorService.getDoctorAvailability(
            doctorId,
            date
        );
        return ResponseEntity.ok(Map.of("available", isAvailable));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctor() {
        return ResponseEntity.ok(
            Map.of("doctors", doctorService.getAllDoctors())
        );
    }

    @PostMapping("/register/{token}")
    public ResponseEntity<Map<String, Object>> saveDoctor(
        @RequestBody Doctor doctor,
        @PathVariable String token
    ) {
        if (!sharedService.validateToken("admin", token)) {
            return ResponseEntity.badRequest().build();
        }

        if (doctorService.existsById(doctor.getId())) {
            return ResponseEntity.status(409).build();
        }

        doctorService.saveDoctor(doctor);
        return ResponseEntity.ok(
            Map.of("message", "Doctor registered successfully")
        );
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> doctorLogin(
        @RequestBody Login login
    ) {
        return ResponseEntity.ok(doctorService.doctorLogin(login));
    }

    @PutMapping("/update/{token}")
    public ResponseEntity<Map<String, Object>> updateDoctor(
        @RequestBody Doctor doctor,
        @PathVariable String token
    ) {
        if (!sharedService.validateToken("admin", token)) {
            return ResponseEntity.badRequest().build();
        }

        if (doctorService.existsById(doctor.getId())) {
            doctorService.updateDoctor(doctor);
            return ResponseEntity.ok(
                Map.of("message", "Doctor updated successfully")
            );
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{doctorId}/{token}")
    public ResponseEntity<Map<String, Object>> deleteDoctor(
        @PathVariable Long doctorId,
        @PathVariable String token
    ) {
        if (!sharedService.validateToken("admin", token)) {
            return ResponseEntity.badRequest().build();
        }

        if (doctorService.existsById(doctorId)) {
            doctorService.deleteDoctor(doctorId);
            return ResponseEntity.ok(
                Map.of("message", "Doctor deleted successfully")
            );
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filter(
        @PathVariable String name,
        @PathVariable String time,
        @PathVariable String speciality
    ) {
        return ResponseEntity.ok(
            Map.of(
                "doctors",
                sharedService.filterDoctors(name, time, speciality)
            )
        );
    }
}
