package com.timora.app.security;

import com.timora.app.exception.BusinessException;
import com.timora.app.model.Availability;
import com.timora.app.model.Booking;
import com.timora.app.model.enums.AvailabilityRecurring;
import com.timora.app.model.enums.AvailabilityStatus;
import com.timora.app.model.enums.BookingStatus;
import com.timora.app.repository.AvailabilityRepository;
import com.timora.app.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Servicio utilitario para validar disponibilidad de suppliers
 * Similar a AccessControlService pero para reglas de negocio de availability
 */
@Service
@RequiredArgsConstructor
public class AvailabilityValidatorService {

    private final AvailabilityRepository availabilityRepository;
    private final BookingRepository bookingRepository;

    // Estados activos para contar bookings
    private static final List<BookingStatus> ACTIVE_BOOKING_STATUSES = List.of(
            BookingStatus.PENDING,
            BookingStatus.CONFIRMED,
            BookingStatus.COMPLETED
    );

    /**
     * Valida si un booking puede ser creado dentro de la disponibilidad del supplier
     *
     * @param supplierId ID del supplier
     * @param startTime Inicio del booking
     * @param endTime Fin del booking
     * @throws BusinessException si la validación falla
     */
    public void validateBookingAvailability(Long supplierId, LocalDateTime startTime, LocalDateTime endTime) {

        // 1. Buscar availability que cubra el booking
        Availability availability = findMatchingAvailability(supplierId, startTime, endTime);

        if (availability == null) {
            throw new BusinessException(
                    "No availability found for the selected date and time. " +
                            "Please check the supplier's schedule."
            );
        }

        // 2. Validar capacidad
        validateCapacity(availability, startTime, endTime);
    }

    /**
     * Encuentra la availability que cubre un booking específico
     *
     * @return Availability que cubre el booking, o null si no existe
     */
    public Availability findMatchingAvailability(Long supplierId, LocalDateTime startTime, LocalDateTime endTime) {

        LocalDate bookingDate = startTime.toLocalDate();
        LocalTime bookingStartTime = startTime.toLocalTime();
        LocalTime bookingEndTime = endTime.toLocalTime();
        long bookingDurationMinutes = Duration.between(startTime, endTime).toMinutes();

        // Obtener todas las disponibilidades activas del supplier
        List<Availability> availabilities = availabilityRepository.findBySupplierIdAndStatus(
                supplierId,
                AvailabilityStatus.ACTIVE
        );

        for (Availability availability : availabilities) {
            if (isBookingWithinAvailability(availability, bookingDate, bookingStartTime, bookingEndTime, bookingDurationMinutes)) {
                return availability;
            }
        }

        return null;
    }

    /**
     * Verifica si un booking está dentro de una availability específica
     */
    private boolean isBookingWithinAvailability(
            Availability availability,
            LocalDate bookingDate,
            LocalTime bookingStartTime,
            LocalTime bookingEndTime,
            long bookingDurationMinutes) {

        // 1. Validar rango de fechas
        if (!isDateInRange(availability, bookingDate)) {
            return false;
        }

        // 2. Validar día de la semana (si aplica)
        if (!isDayOfWeekActive(availability, bookingDate)) {
            return false;
        }

        // 3. Validar horario
        if (!isTimeWithinRange(availability, bookingStartTime, bookingEndTime)) {
            return false;
        }

        // 4. Validar duración (solo si slot_duration está definido)
        if (!isDurationValid(availability, bookingDurationMinutes)) {
            return false;
        }

        return true;
    }

    /**
     * Verifica que la fecha esté dentro del rango de la availability
     */
    private boolean isDateInRange(Availability availability, LocalDate date) {
        if (date.isBefore(availability.getStartDate())) {
            return false;
        }

        if (availability.getEndDate() != null && date.isAfter(availability.getEndDate())) {
            return false;
        }

        return true;
    }

    /**
     * Verifica que el día de la semana esté activo (solo para WEEKLY)
     */
    private boolean isDayOfWeekActive(Availability availability, LocalDate date) {

        // Si no es WEEKLY, todos los días son válidos (o depende de la lógica)
        if (availability.getRecurrenceType() != AvailabilityRecurring.WEEKLY) {
            return true;
        }

        DayOfWeek dayOfWeek = date.getDayOfWeek();

        return switch (dayOfWeek) {
            case MONDAY -> Boolean.TRUE.equals(availability.getMonday());
            case TUESDAY -> Boolean.TRUE.equals(availability.getTuesday());
            case WEDNESDAY -> Boolean.TRUE.equals(availability.getWednesday());
            case THURSDAY -> Boolean.TRUE.equals(availability.getThursday());
            case FRIDAY -> Boolean.TRUE.equals(availability.getFriday());
            case SATURDAY -> Boolean.TRUE.equals(availability.getSaturday());
            case SUNDAY -> Boolean.TRUE.equals(availability.getSunday());
        };
    }

    /**
     * Verifica que el horario esté dentro del rango de la availability
     */
    private boolean isTimeWithinRange(Availability availability, LocalTime startTime, LocalTime endTime) {
        return !startTime.isBefore(availability.getStartTime()) &&
                !endTime.isAfter(availability.getEndTime());
    }

    /**
     * Verifica que la duración coincida con slot_duration_minutes
     */
    private boolean isDurationValid(Availability availability, long bookingDurationMinutes) {
        if (availability.getSlotDurationMinutes() == null) {
            return true; // No hay restricción de duración
        }

        return bookingDurationMinutes == availability.getSlotDurationMinutes();
    }

    /**
     * Valida que haya capacidad disponible en el slot
     */
    private void validateCapacity(Availability availability, LocalDateTime startTime, LocalDateTime endTime) {
        if (availability.getCapacity() == null) {
            return; // No hay límite de capacidad
        }

        int currentBookings = countBookingsForSlot(
                availability.getSupplier().getId(),
                startTime,
                endTime
        );

        if (currentBookings >= availability.getCapacity()) {
            throw new BusinessException(
                    String.format(
                            "No capacity available for this time slot. " +
                                    "Current bookings: %d, Max capacity: %d",
                            currentBookings,
                            availability.getCapacity()
                    )
            );
        }
    }

    /**
     * Cuenta cuántos bookings activos hay en un slot específico
     */
    private int countBookingsForSlot(Long supplierId, LocalDateTime startTime, LocalDateTime endTime) {
        // Buscar bookings del supplier que se solapen con el slot
        List<Booking> bookings = bookingRepository.findBySupplierIdAndDateRange(
                supplierId,
                startTime,
                endTime
        );

        // Filtrar solo bookings activos
        return (int) bookings.stream()
                .filter(b -> ACTIVE_BOOKING_STATUSES.contains(b.getStatus()))
                .filter(b -> isSameSlot(b, startTime, endTime))
                .count();
    }

    /**
     * Verifica si un booking ocupa exactamente el mismo slot
     * (mismo inicio y fin)
     */
    private boolean isSameSlot(Booking booking, LocalDateTime startTime, LocalDateTime endTime) {
        return booking.getStartTime().equals(startTime) &&
                booking.getEndTime().equals(endTime);
    }

    // =========================
    // MÉTODOS DE UTILIDAD PÚBLICOS
    // =========================

    /**
     * Verifica si un supplier tiene disponibilidad para una fecha y hora específicas
     */
    public boolean hasAvailability(Long supplierId, LocalDateTime startTime, LocalDateTime endTime) {
        try {
            validateBookingAvailability(supplierId, startTime, endTime);
            return true;
        } catch (BusinessException e) {
            return false;
        }
    }

    /**
     * Obtiene la disponibilidad que cubre un booking (si existe)
     */
    public Availability getAvailabilityForBooking(Long supplierId, LocalDateTime startTime, LocalDateTime endTime) {
        return findMatchingAvailability(supplierId, startTime, endTime);
    }

    /**
     * Verifica si hay capacidad disponible en un slot
     */
    public boolean hasCapacity(Long supplierId, LocalDateTime startTime, LocalDateTime endTime) {
        Availability availability = findMatchingAvailability(supplierId, startTime, endTime);

        if (availability == null) {
            return false;
        }

        if (availability.getCapacity() == null) {
            return true;
        }

        int currentBookings = countBookingsForSlot(supplierId, startTime, endTime);
        return currentBookings < availability.getCapacity();
    }

    /**
     * Obtiene la capacidad restante en un slot
     */
    public int getRemainingCapacity(Long supplierId, LocalDateTime startTime, LocalDateTime endTime) {
        Availability availability = findMatchingAvailability(supplierId, startTime, endTime);

        if (availability == null) {
            return 0;
        }

        if (availability.getCapacity() == null) {
            return Integer.MAX_VALUE;
        }

        int currentBookings = countBookingsForSlot(supplierId, startTime, endTime);
        return Math.max(0, availability.getCapacity() - currentBookings);
    }
}