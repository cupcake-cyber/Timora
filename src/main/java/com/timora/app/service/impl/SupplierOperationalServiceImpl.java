package com.timora.app.service.impl;

import com.timora.app.dto.ProviderOperationalDTO;
import com.timora.app.model.Booking;
import com.timora.app.model.Service;
import com.timora.app.model.enums.BookingStatus;
import com.timora.app.repository.BookingRepository;
import com.timora.app.repository.ServiceRepository;
import com.timora.app.service.SupplierOperationalService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class SupplierOperationalServiceImpl
        implements SupplierOperationalService {

    private final ServiceRepository serviceRepository;

    private final BookingRepository bookingRepository;

    @Override
    public List<Service> getSupplierServices(Long supplierId) {

        return serviceRepository.findBySupplierId(supplierId);
    }

    @Override
    public List<Booking> getSupplierBookings(Long supplierId) {

        return bookingRepository.findByServiceSupplierId(supplierId);
    }

    @Override
    public ProviderOperationalDTO getDashboard(Long supplierId) {

        List<Booking> bookings =
                bookingRepository.findByServiceSupplierId(supplierId);

        int pending = 0;
        int completed = 0;
        int cancelled = 0;

        for (Booking booking : bookings) {

            if (booking.getStatus() == BookingStatus.PENDING) {
                pending++;
            }

            if (booking.getStatus() == BookingStatus.COMPLETED) {
                completed++;
            }

            if (booking.getStatus() == BookingStatus.CANCELLED) {
                cancelled++;
            }
        }

        return new ProviderOperationalDTO(
                supplierId,
                bookings.size(),
                pending,
                completed,
                cancelled
        );
    }
}
