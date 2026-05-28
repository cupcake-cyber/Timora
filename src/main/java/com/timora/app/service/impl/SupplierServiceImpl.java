package com.timora.app.service.impl;

import com.timora.app.dto.CreatePersonRequest;
import com.timora.app.model.Person;
import com.timora.app.model.Supplier;
import com.timora.app.repository.SupplierRepository;
import com.timora.app.service.SupplierService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Override
    public Supplier createSupplier(Person person, CreatePersonRequest.SupplierData data) {

        if (supplierRepository.existsByPersonId(person.getId())) {
            throw new IllegalArgumentException("Person already is supplier");
        }

        Supplier supplier = new Supplier();
        supplier.setPerson(person);
        supplier.setCompany(person.getCompany());

        if (data != null) {
            supplier.setSpecialty(data.getSpecialty());
            supplier.setNotes(data.getNotes());
        }

        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier updateSupplier(Person person, CreatePersonRequest.SupplierData data) {

        Supplier supplier = supplierRepository.findByPersonId(person.getId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        if (data != null) {

            if (data.getSpecialty() != null) {
                supplier.setSpecialty(data.getSpecialty());
            }

            if (data.getNotes() != null) {
                supplier.setNotes(data.getNotes());
            }
        }

        return supplierRepository.save(supplier);
    }

    @Override
    public void deleteByPersonId(Long personId) {
        supplierRepository.deleteByPersonId(personId);
    }

    @Override
    public boolean existsByPerson(Long personId) {
        return supplierRepository.existsByPersonId(personId);
    }
}