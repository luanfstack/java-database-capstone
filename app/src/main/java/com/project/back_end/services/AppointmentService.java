package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(
        AppointmentRepository appointmentRepository,
        PatientRepository patientRepository,
        DoctorRepository doctorRepository,
        TokenService tokenService
    ) {
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            Appointment savedAppointment = appointmentRepository.save(
                appointment
            );
            return savedAppointment != null ? 1 : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public String updateAppointment(Appointment appointment) {
        try {
            Appointment existingAppointment = appointmentRepository
                .findById(appointment.getId())
                .orElse(null);

            if (existingAppointment == null) {
                return "Appointment not found";
            }

            existingAppointment.setDoctor(appointment.getDoctor());
            existingAppointment.setPatient(appointment.getPatient());
            existingAppointment.setAppointmentTime(
                appointment.getAppointmentTime()
            );
            existingAppointment.setStatus(appointment.getStatus());

            appointmentRepository.save(existingAppointment);
            return "Appointment updated successfully";
        } catch (Exception e) {
            return "Error updating appointment: " + e.getMessage();
        }
    }

    @Transactional
    public String cancelAppointment(Long appointmentId, Long patientId) {
        try {
            Appointment appointment = appointmentRepository
                .findById(appointmentId)
                .orElse(null);

            if (appointment == null) {
                return "Appointment not found";
            }

            if (!appointment.getPatient().getId().equals(patientId)) {
                return "Unauthorized: Patient ID does not match";
            }

            appointmentRepository.deleteById(appointmentId);
            return "Appointment cancelled successfully";
        } catch (Exception e) {
            return "Error cancelling appointment: " + e.getMessage();
        }
    }

    @Transactional
    public List<Appointment> getAppointment(
        Long doctorId,
        LocalDateTime day,
        String patientName
    ) {
        if (patientName != null && !patientName.isEmpty()) {
            return appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                doctorId,
                patientName,
                day.with(LocalDateTime.MIN.toLocalDate()),
                day.with(LocalDateTime.MAX.toLocalDate())
            );
        } else {
            return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                doctorId,
                day.with(LocalDateTime.MIN.toLocalDate()),
                day.with(LocalDateTime.MAX.toLocalDate())
            );
        }
    }
}
