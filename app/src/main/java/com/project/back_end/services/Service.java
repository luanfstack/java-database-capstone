package com.project.back_end.services;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class Service {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public Service(
        TokenService tokenService,
        AdminRepository adminRepository,
        DoctorRepository doctorRepository,
        PatientRepository patientRepository,
        DoctorService doctorService,
        PatientService patientService
    ) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public boolean validateToken(String token, String role) {
        try {
            return tokenService.validateToken(token, role);
        } catch (Exception e) {
            return false;
        }
    }

    public String validateAdmin(String username, String password) {
        try {
            Admin admin = adminRepository.findByUsername(username);
            if (admin != null && admin.getPassword().equals(password)) {
                return tokenService.generateToken(username);
            } else {
                return "Invalid username or password";
            }
        } catch (Exception e) {
            return "Error validating admin: " + e.getMessage();
        }
    }

    public List<Doctor> filterDoctor(
        String name,
        String specialty,
        String time
    ) {
        if (name == null && specialty == null && time == null) {
            return doctorService.getDoctors();
        } else if (name != null && specialty != null && time != null) {
            return doctorService.filterDoctorsByNameSpecilityandTime(
                name,
                specialty,
                time
            );
        } else if (name != null && specialty != null) {
            return doctorService.filterDoctorByNameAndSpecility(
                name,
                specialty
            );
        } else if (name != null && time != null) {
            return doctorService.filterDoctorByNameAndTime(name, time);
        } else if (specialty != null && time != null) {
            return doctorService.filterDoctorByTimeAndSpecility(
                specialty,
                time
            );
        } else if (name != null) {
            return doctorService.findDoctorByName(name);
        } else if (specialty != null) {
            return doctorService.filterDoctorBySpecility(specialty);
        } else {
            return doctorService.filterDoctorsByTime(time);
        }
    }

    public int validateAppointment(Long doctorId, String date, String time) {
        try {
            Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
            if (doctor == null) {
                return -1;
            }

            List<String> availableTimes = doctorService.getDoctorAvailability(
                doctorId,
                LocalDate.parse(date)
            );

            for (String availableTime : availableTimes) {
                if (availableTime.equals(time)) {
                    return 1;
                }
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean validatePatient(String email, String phone) {
        try {
            Patient existingPatient = patientRepository.findByEmailOrPhone(
                email,
                phone
            );
            if (existingPatient != null) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String validatePatientLogin(String email, String password) {
        try {
            Patient patient = patientRepository.findByEmail(email);
            if (patient != null && patient.getPassword().equals(password)) {
                return tokenService.generateToken(email);
            } else {
                return "Invalid email or password";
            }
        } catch (Exception e) {
            return "Error validating patient: " + e.getMessage();
        }
    }

    public List<?> filterPatient(
        String condition,
        String doctorName,
        String token
    ) {
        try {
            Patient patient = patientService.getPatientDetails(token);
            return patientService.filterByDoctorAndCondition(
                patient.getId(),
                condition,
                doctorName
            );
        } catch (Exception e) {
            return List.of();
        }
    }
}
