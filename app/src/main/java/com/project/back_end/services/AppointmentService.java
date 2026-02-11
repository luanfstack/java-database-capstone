package com.project.back_end.services;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.model.Appointment;
import com.project.back_end.model.Doctor;
import com.project.back_end.model.Patient;
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
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Autowired
    public AppointmentService(
        AppointmentRepository appointmentRepository,
        PatientRepository patientRepository,
        DoctorRepository doctorRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional
    public int bookAppointment(AppointmentDTO appointmentDTO) {
        try {
            Appointment appointment = new Appointment();
            appointment.setDoctorId(appointmentDTO.getDoctorId());
            appointment.setPatientId(appointmentDTO.getPatientId());
            appointment.setAppointmentTime(appointmentDTO.getAppointmentTime());
            appointment.setStatus(appointmentDTO.getStatus());

            Appointment savedAppointment = appointmentRepository.save(
                appointment
            );
            return savedAppointment != null ? 1 : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public String updateAppointment(
        Long appointmentId,
        AppointmentDTO appointmentDTO,
        Long patientId
    ) {
        try {
            Appointment existingAppointment = appointmentRepository
                .findById(appointmentId)
                .orElse(null);

            if (existingAppointment == null) {
                return "Appointment not found";
            }

            if (!existingAppointment.getPatientId().equals(patientId)) {
                return "Unauthorized: Patient ID does not match";
            }

            List<Appointment> conflictingAppointments =
                appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                    appointmentDTO.getDoctorId(),
                    appointmentDTO.getAppointmentTime().minusHours(1),
                    appointmentDTO.getAppointmentTime().plusHours(1)
                );

            conflictingAppointments.removeIf(a ->
                a.getId().equals(appointmentId)
            );

            if (!conflictingAppointments.isEmpty()) {
                return "Doctor is not available at the specified time";
            }

            existingAppointment.setDoctorId(appointmentDTO.getDoctorId());
            existingAppointment.setPatientId(appointmentDTO.getPatientId());
            existingAppointment.setAppointmentTime(
                appointmentDTO.getAppointmentTime()
            );
            existingAppointment.setStatus(appointmentDTO.getStatus());

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

            if (!appointment.getPatientId().equals(patientId)) {
                return "Unauthorized: Patient ID does not match";
            }

            appointmentRepository.deleteById(appointmentId);
            return "Appointment cancelled successfully";
        } catch (Exception e) {
            return "Error cancelling appointment: " + e.getMessage();
        }
    }

    @Transactional
    public List<Appointment> getAppointments(
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

    @Transactional
    public void changeStatus(Long appointmentId, int status) {
        appointmentRepository.updateStatus(status, appointmentId);
    }
}
