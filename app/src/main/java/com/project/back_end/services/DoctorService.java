package com.project.back_end.services;

import com.project.back_end.model.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    @Autowired
    public DoctorService(
        DoctorRepository doctorRepository,
        AppointmentRepository appointmentRepository,
        TokenService tokenService
    ) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    @Transactional
    public List<Doctor> findDoctorByName(String name) {
        return doctorRepository.findByNameLike("%" + name + "%");
    }

    @Transactional
    public List<Doctor> filterDoctorsByNameAndSpecility(
        String name,
        String specialty
    ) {
        return doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
            name,
            specialty
        );
    }

    @Transactional
    public List<Doctor> filterDoctorBySpecility(String specialty) {
        return doctorRepository.findBySpecialtyIgnoreCase(specialty);
    }

    @Transactional
    public List<Doctor> filterDoctorByTimeAndSpecility(
        String specialty,
        String time
    ) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(
            specialty
        );
        return filterDoctorByTime(doctors, time);
    }

    @Transactional
    public List<Doctor> filterDoctorByNameAndSpecility(
        String name,
        String specialty
    ) {
        return doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
            name,
            specialty
        );
    }

    @Transactional
    public List<Doctor> filterDoctorByNameAndTime(String name, String time) {
        List<Doctor> doctors = doctorRepository.findByNameLike(
            "%" + name + "%"
        );
        return filterDoctorByTime(doctors, time);
    }

    @Transactional
    public List<Doctor> filterDoctorByTime(List<Doctor> doctors, String time) {
        List<Doctor> filteredDoctors = new ArrayList<>();
        for (Doctor doctor : doctors) {
            if (doctor.getAvailableTimes() != null) {
                for (String availableTime : doctor.getAvailableTimes()) {
                    if (availableTime.contains(time)) {
                        filteredDoctors.add(doctor);
                        break;
                    }
                }
            }
        }
        return filteredDoctors;
    }

    @Transactional
    public List<Doctor> filterDoctorsByTime(String time) {
        List<Doctor> allDoctors = doctorRepository.findAll();
        return filterDoctorByTime(allDoctors, time);
    }

    @Transactional
    public List<String> getDoctorAvailability(
        Long doctorId,
        LocalDateTime date
    ) {
        LocalDateTime startOfDay = date.with(LocalDateTime.MIN.toLocalDate());
        LocalDateTime endOfDay = date.with(LocalDateTime.MAX.toLocalDate());

        List<com.project.back_end.model.Appointment> appointments =
            appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                doctorId,
                startOfDay,
                endOfDay
            );

        Optional<Doctor> doctorOptional = doctorRepository.findById(doctorId);
        List<String> availableTimes = new ArrayList<>();

        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();
            if (doctor.getAvailableTimes() != null) {
                availableTimes.addAll(doctor.getAvailableTimes());
            }
        }

        List<String> bookedTimes = new ArrayList<>();
        for (com.project.back_end.model.Appointment appointment : appointments) {
            bookedTimes.add(
                appointment.getAppointmentTime().toLocalTime().toString()
            );
        }

        availableTimes.removeAll(bookedTimes);
        return availableTimes;
    }

    public int saveDoctor(Doctor doctor) {
        try {
            Doctor existingDoctor = doctorRepository.findByEmail(
                doctor.getEmail()
            );
            if (existingDoctor != null) {
                return -1;
            }

            Doctor savedDoctor = doctorRepository.save(doctor);
            return savedDoctor != null ? 1 : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public int updateDoctor(Doctor doctor) {
        try {
            Optional<Doctor> existingDoctor = doctorRepository.findById(
                doctor.getId()
            );
            if (!existingDoctor.isPresent()) {
                return -1;
            }

            Doctor updatedDoctor = doctorRepository.save(doctor);
            return updatedDoctor != null ? 1 : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public int deleteDoctor(Long id) {
        try {
            Optional<Doctor> existingDoctor = doctorRepository.findById(id);
            if (!existingDoctor.isPresent()) {
                return -1;
            }

            LocalDateTime startOfDay = LocalDateTime.now().with(
                LocalDateTime.MIN.toLocalDate()
            );
            LocalDateTime endOfDay = LocalDateTime.now().with(
                LocalDateTime.MAX.toLocalDate()
            );

            List<com.project.back_end.model.Appointment> appointments =
                appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                    id,
                    startOfDay,
                    endOfDay
                );

            appointmentRepository.deleteAll(appointments);

            doctorRepository.deleteById(id);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public String validateDoctor(String email, String password) {
        try {
            Doctor doctor = doctorRepository.findByEmail(email);
            if (doctor != null && doctor.getPassword().equals(password)) {
                return tokenService.generateToken(email, "doctor");
            } else {
                return "Invalid email or password";
            }
        } catch (Exception e) {
            return "Error validating doctor: " + e.getMessage();
        }
    }

    @Transactional
    public List<Doctor> filterDoctorsByNameSpecilityandTime(
        String name,
        String specialty,
        String time
    ) {
        List<Doctor> doctors =
            doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
                name,
                specialty
            );
        return filterDoctorByTime(doctors, time);
    }
}
