package com.timora.app.service;

import com.timora.app.dto.ProviderOperationalDTO;
import com.timora.app.model.Booking;
import com.timora.app.model.Service;

import java.util.List;

public interface SupplierOperationalService {

    List<Service> getSupplierServices(Long supplierId);

    List<Booking> getSupplierBookings(Long supplierId);

    ProviderOperationalDTO getDashboard(Long supplierId);
}
