package com.timora.app.service.impl;
import com.timora.app.models.Availability;
import com.timora.app.repository.AppointmentRepository;
import com.timora.app.service.AppointmentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public List<Availability> findAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public Optional<Availability> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public Availability save(Availability appointment) {

        // Solo seteas la fecha (ya no validas entidades externas aquí)
        appointment.setCreatedAt(LocalDateTime.now());

        return appointmentRepository.save(appointment);
    }

    @Override
    public Availability update(Long id, Availability updated) {
        return appointmentRepository.findById(id).map(appointment -> {

            appointment.setCompanyId(updated.getCompanyId());
            appointment.setServiceId(updated.getServiceId());
            appointment.setCustomerId(updated.getCustomerId());
            appointment.setCreatedByUserId(updated.getCreatedByUserId());

            appointment.setStartTime(updated.getStartTime());
            appointment.setEndTime(updated.getEndTime());

            appointment.setStatus(updated.getStatus());
            appointment.setName(updated.getName());
            appointment.setDescription(updated.getDescription());

            return appointmentRepository.save(appointment);

        }).orElseThrow(() -> new RuntimeException("Appointment no encontrado con id: " + id));
    }

    @Override
    public void delete(Long id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public Availability confirm(Long id) {
        return appointmentRepository.findById(id).map(appointment -> {
            appointment.setStatus(AppointmentStatus.CONFIRMED);
            return appointmentRepository.save(appointment);
        }).orElseThrow(() -> new RuntimeException("Appointment no encontrado"));
    }

    @Override
    public Availability cancel(Long id) {
        return appointmentRepository.findById(id).map(appointment -> {
            appointment.setStatus(AppointmentStatus.CANCELLED);
            return appointmentRepository.save(appointment);
        }).orElseThrow(() -> new RuntimeException("Appointment no encontrado"));
    }

    @Override
    public List<Availability> findByCustomerId(Long customerId) {
        return appointmentRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Availability> findByServiceId(Long serviceId) {
        return appointmentRepository.findByServiceId(serviceId);
    }

    @Override
    public List<Availability> findByCompanyId(Long companyId) {
        return appointmentRepository.findByCompanyId(companyId);
    }
}