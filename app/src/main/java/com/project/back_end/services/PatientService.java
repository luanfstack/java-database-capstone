package com.project.back_end.services;

import com.project.back_end.dto.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repositories.AppointmentRepository;
import com.project.back_end.repositories.PatientRepository;
import com.project.back_end.services.TokenService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientService {

    private static final Logger logger = Logger.getLogger(
        PatientService.class.getName()
    );

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    @Autowired
    public PatientService(
        PatientRepository patientRepository,
        AppointmentRepository appointmentRepository,
        TokenService tokenService
    ) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            logger.severe("Error creating patient: " + e.getMessage());
            return 0;
        }
    }

    @Transactional
    public List<AppointmentDTO> getPatientAppointment(Long patientId) {
        try {
            List<Appointment> appointments =
                appointmentRepository.findByPatientId(patientId);
            List<AppointmentDTO> appointmentDTOs = new ArrayList<>();

            for (Appointment appointment : appointments) {
                appointmentDTOs.add(new AppointmentDTO(appointment));
            }

            return appointmentDTOs;
        } catch (Exception e) {
            logger.severe(
                "Error retrieving patient appointments: " + e.getMessage()
            );
            return new ArrayList<>();
        }
    }

    @Transactional
    public List<AppointmentDTO> filterByCondition(
        Long patientId,
        String condition
    ) {
        try {
            List<Appointment> appointments;

            if ("future".equalsIgnoreCase(condition)) {
                appointments = appointmentRepository.findByPatientIdAndStatus(
                    patientId,
                    0
                );
            } else if ("past".equalsIgnoreCase(condition)) {
                appointments = appointmentRepository.findByPatientIdAndStatus(
                    patientId,
                    1
                );
            } else {
                logger.warning("Invalid condition provided: " + condition);
                return new ArrayList<>();
            }

            List<AppointmentDTO> appointmentDTOs = new ArrayList<>();
            for (Appointment appointment : appointments) {
                appointmentDTOs.add(new AppointmentDTO(appointment));
            }

            return appointmentDTOs;
        } catch (Exception e) {
            logger.severe(
                "Error filtering appointments by condition: " + e.getMessage()
            );
            return new ArrayList<>();
        }
    }

    @Transactional
    public List<AppointmentDTO> filterByDoctor(
        Long patientId,
        String doctorName
    ) {
        try {
            List<Appointment> appointments =
                appointmentRepository.findByPatientIdAndDoctorName(
                    patientId,
                    doctorName
                );
            List<AppointmentDTO> appointmentDTOs = new ArrayList<>();

            for (Appointment appointment : appointments) {
                appointmentDTOs.add(new AppointmentDTO(appointment));
            }

            return appointmentDTOs;
        } catch (Exception e) {
            logger.severe(
                "Error filtering appointments by doctor: " + e.getMessage()
            );
            return new ArrayList<>();
        }
    }

    @Transactional
    public List<AppointmentDTO> filterByDoctorAndCondition(
        Long patientId,
        String doctorName,
        String condition
    ) {
        try {
            List<Appointment> appointments;

            if ("future".equalsIgnoreCase(condition)) {
                appointments =
                    appointmentRepository.findByPatientIdDoctorNameAndStatus(
                        patientId,
                        doctorName,
                        0
                    );
            } else if ("past".equalsIgnoreCase(condition)) {
                appointments =
                    appointmentRepository.findByPatientIdDoctorNameAndStatus(
                        patientId,
                        doctorName,
                        1
                    );
            } else {
                logger.warning("Invalid condition provided: " + condition);
                return new ArrayList<>();
            }

            List<AppointmentDTO> appointmentDTOs = new ArrayList<>();
            for (Appointment appointment : appointments) {
                appointmentDTOs.add(new AppointmentDTO(appointment));
            }

            return appointmentDTOs;
        } catch (Exception e) {
            logger.severe(
                "Error filtering appointments by doctor and condition: " +
                    e.getMessage()
            );
            return new ArrayList<>();
        }
    }

    public Patient getPatientDetails(String token) {
        try {
            String email = tokenService.extractEmailFromToken(token);
            return patientRepository.findByEmail(email);
        } catch (Exception e) {
            logger.severe(
                "Error retrieving patient details: " + e.getMessage()
            );
            return null;
        }
    }
}
