package com.timora.app.service.impl;

import com.timora.app.models.Supplier;
import com.timora.app.repository.SupplierRepository;
import com.timora.app.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    @Override
    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    @Override
    public Optional<Supplier> findById(Long id) {
        return supplierRepository.findById(id);
    }

    @Override
    public Supplier save(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier update(Long id, Supplier supplier) {

        Supplier existing = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        existing.setCompany(supplier.getCompany());
        existing.setPerson(supplier.getPerson());
        existing.setSpecialty(supplier.getSpecialty());
        existing.setNotes(supplier.getNotes());

        return supplierRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        supplierRepository.deleteById(id);
    }
}
