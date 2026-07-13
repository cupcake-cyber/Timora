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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AvailabilityValidatorService {

    private final AvailabilityRepository availabilityRepository;
    private final BookingRepository bookingRepository;

    private static final List<BookingStatus> ACTIVE_BOOKING_STATUSES = List.of(
            BookingStatus.PENDING,
            BookingStatus.CONFIRMED,
            BookingStatus.COMPLETED
    );

    /**
     * Valida si un booking puede ser creado dentro de la disponibilidad del supplier
     */
    public void validateBookingAvailability(Long supplierId, LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Validating booking availability for supplier {}: {} - {}",
                supplierId, startTime, endTime);

        Availability availability = findMatchingAvailability(supplierId, startTime, endTime);

        if (availability == null) {
            throw new BusinessException(
                    "No availability found for the selected date anda time. " +
                            "Please check the supplier's schedule."
            );
        }

        log.debug("Found matching availability: ID {}, recurrence: {}",
                availability.getId(), availability.getRecurrenceType());

        validateCapacity(availability, startTime, endTime);
    }

    /**
     * Encuentra la availability que cubre un booking específico
     */
    public Availability findMatchingAvailability(Long supplierId, LocalDateTime startTime, LocalDateTime endTime) {
        LocalDate bookingDate = startTime.toLocalDate();
        LocalTime bookingStartTime = startTime.toLocalTime();
        LocalTime bookingEndTime = endTime.toLocalTime();
        long bookingDurationMinutes = Duration.between(startTime, endTime).toMinutes();

        List<Availability> availabilities = availabilityRepository.findBySupplierIdAndStatus(
                supplierId,
                AvailabilityStatus.ACTIVE
        );

        log.debug("Found {} active availabilities for supplier {}", availabilities.size(), supplierId);

        for (Availability availability : availabilities) {
            if (isBookingWithinAvailability(availability, bookingDate, bookingStartTime,
                    bookingEndTime, bookingDurationMinutes)) {
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
        if (!isDateInRange(availability, bookingDate)) {
            log.debug("❌ Falla date range");
            return false;
        }
        if (!isRecurrenceValid(availability, bookingDate)) {
            log.debug("❌ Falla recurrence");
            return false;
        }
        if (!isTimeWithinRange(availability, bookingStartTime, bookingEndTime)) {
            log.debug("❌ Falla time range");
            return false;
        }
        if (!isDurationValid(availability, bookingDurationMinutes)) {
            log.debug("❌ Falla duration");
            return false;
        }
        return true;
    }

    /**
     * 🔥 VALIDACIÓN DE RECURRENCIA (CORREGIDA)
     */
    private boolean isRecurrenceValid(Availability availability, LocalDate bookingDate) {
        AvailabilityRecurring recurrenceType = availability.getRecurrenceType();

        // Si no tiene recurrencia definida, usar NONE como default
        if (recurrenceType == null) {
            recurrenceType = AvailabilityRecurring.NONE;
        }

        switch (recurrenceType) {
            case NONE:
                // 🔴 SOLO permite la fecha exacta de startDate
                // Ej: startDate=15/07/2026 → Solo 15/07/2026
                return bookingDate.equals(availability.getStartDate());

            case DAILY:
                // ✅ Todos los días son válidos (ya validado por rango de fechas)
                // Ej: 01/07-31/07 → Cualquier día en ese rango
                return true;

            case WEEKLY:
                // ✅ Validar día de la semana específico
                // Ej: Lunes y Miércoles → Solo esos días cada semana
                return isDayOfWeekActive(availability, bookingDate);

            case MONTHLY:
                // 🔥 MISMO DÍA DEL MES (TODOS LOS MESES)
                // Ej: startDate=15/07/2026 → 15 de cada mes
                // ⚠️ Problema: ¿Qué pasa si el mes no tiene ese día?
                // Ej: 31 de febrero no existe → no debería permitirse
                return isSameDayOfMonth(availability, bookingDate);

            case YEARLY:
                // 🔥 MISMO DÍA Y MES (TODOS LOS AÑOS)
                // Ej: startDate=20/08/2026 → 20 de agosto de cada año
                return isSameDayAndMonth(availability, bookingDate);

            case CUSTOM:
                // 🔴 SIN IMPLEMENTACIÓN - Solo validación básica de rango
                // En el futuro podría implementarse con RRULE
                log.warn("CUSTOM recurrence not implemented for availability {}. " +
                        "Only range validation applied.", availability.getId());
                return true;

            default:
                return true;
        }
    }

    /**
     * Verifica que sea el mismo día del mes
     * ⚠️ Maneja casos donde el mes no tiene ese día (ej: 31 de febrero)
     */
    private boolean isSameDayOfMonth(Availability availability, LocalDate bookingDate) {
        int targetDay = availability.getStartDate().getDayOfMonth();
        int bookingDay = bookingDate.getDayOfMonth();

        // Si el día objetivo es 31, y el mes actual tiene menos de 31 días,
        // no debería permitirse (ej: 31 de febrero no existe)
        if (targetDay > 28) {
            YearMonth yearMonth = YearMonth.of(bookingDate.getYear(), bookingDate.getMonth());
            int maxDays = yearMonth.lengthOfMonth();

            // Si el mes no tiene el día objetivo, no es válido
            if (targetDay > maxDays) {
                log.trace("Day {} does not exist in month {}/{}",
                        targetDay, bookingDate.getMonth(), bookingDate.getYear());
                return false;
            }
        }

        return bookingDay == targetDay;
    }

    /**
     * Verifica que sea el mismo día y mes (para YEARLY)
     */
    private boolean isSameDayAndMonth(Availability availability, LocalDate bookingDate) {
        return bookingDate.getMonth() == availability.getStartDate().getMonth() &&
                isSameDayOfMonth(availability, bookingDate);
    }

    /**
     * Verifica que la fecha esté dentro del rango de la availability
     */
    private boolean isDateInRange(Availability availability, LocalDate date) {
        // Para NONE, solo permite la fecha exacta
        if (availability.getRecurrenceType() == AvailabilityRecurring.NONE) {
            return date.equals(availability.getStartDate());
        }

        if (date.isBefore(availability.getStartDate())) {
            return false;
        }

        if (availability.getEndDate() != null && date.isAfter(availability.getEndDate())) {
            return false;
        }

        return true;
    }

    /**
     * Verifica que el día de la semana esté activo (para WEEKLY)
     */
    private boolean isDayOfWeekActive(Availability availability, LocalDate date) {
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
            return true;
        }
        return bookingDurationMinutes <= availability.getSlotDurationMinutes();
    }

    /**
     * Valida que haya capacidad disponible en el slot
     */
    private void validateCapacity(Availability availability, LocalDateTime startTime, LocalDateTime endTime) {
        if (availability.getCapacity() == null) {
            return;
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
        List<Booking> bookings = bookingRepository.findBySupplierIdAndDateRange(
                supplierId,
                startTime,
                endTime
        );

        return (int) bookings.stream()
                .filter(b -> ACTIVE_BOOKING_STATUSES.contains(b.getStatus()))
                .filter(b -> isSameSlot(b, startTime, endTime))
                .count();
    }

    /**
     * Verifica si un booking ocupa exactamente el mismo slot
     */
    private boolean isSameSlot(Booking booking, LocalDateTime startTime, LocalDateTime endTime) {
        return booking.getStartTime().equals(startTime) &&
                booking.getEndTime().equals(endTime);
    }

    // =========================
    // MÉTODOS DE UTILIDAD PÚBLICOS
    // =========================

    public boolean hasAvailability(Long supplierId, LocalDateTime startTime, LocalDateTime endTime) {
        try {
            validateBookingAvailability(supplierId, startTime, endTime);
            return true;
        } catch (BusinessException e) {
            log.debug("Availability check failed: {}", e.getMessage());
            return false;
        }
    }

    public Availability getAvailabilityForBooking(Long supplierId, LocalDateTime startTime, LocalDateTime endTime) {
        return findMatchingAvailability(supplierId, startTime, endTime);
    }

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
    /**
     * Valida disponibilidad excluyendo un booking específico (para updates)
     */
    public void validateBookingAvailability(Long supplierId, LocalDateTime startTime,
                                            LocalDateTime endTime, Long excludeBookingId) {
        log.debug("Validating booking availability for supplier {}: {} - {}, excluding booking {}",
                supplierId, startTime, endTime, excludeBookingId);

        Availability availability = findMatchingAvailability(supplierId, startTime, endTime);

        if (availability == null) {
            throw new BusinessException(
                    "No availability found for the selected date and time. " +
                            "Please check the supplier's schedule."
            );
        }

        validateCapacity(availability, startTime, endTime, excludeBookingId);
    }

    /**
     * Valida capacidad excluyendo un booking específico
     */
    private void validateCapacity(Availability availability, LocalDateTime startTime,
                                  LocalDateTime endTime, Long excludeBookingId) {
        if (availability.getCapacity() == null) {
            return;
        }

        int currentBookings = countBookingsForSlot(
                availability.getSupplier().getId(),
                startTime,
                endTime,
                excludeBookingId  // 🔴 Pasar el ID a excluir
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
     * Cuenta bookings excluyendo uno específico
     */
    private int countBookingsForSlot(Long supplierId, LocalDateTime startTime,
                                     LocalDateTime endTime, Long excludeBookingId) {
        List<Booking> bookings = bookingRepository.findBySupplierIdAndDateRange(
                supplierId,
                startTime,
                endTime
        );

        return (int) bookings.stream()
                .filter(b -> ACTIVE_BOOKING_STATUSES.contains(b.getStatus()))
                .filter(b -> !b.getId().equals(excludeBookingId)) // 🔴 Excluir el booking actual
                .filter(b -> isSameSlot(b, startTime, endTime))
                .count();
    }
}