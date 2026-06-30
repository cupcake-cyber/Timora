package com.timora.app.service.impl;

import com.timora.app.dto.supplier.SupplierCreateDTO;
import com.timora.app.dto.supplier.SupplierDTO;
import com.timora.app.dto.supplier.SupplierPatchDTO;
import com.timora.app.model.Person;
import com.timora.app.model.Supplier;
import com.timora.app.repository.SupplierRepository;
import com.timora.app.security.AccessControlService;
import com.timora.app.security.SecurityHelper;
import com.timora.app.service.SupplierService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SecurityHelper securityHelper;
    private final AccessControlService auth;

    @Override
    public Supplier findById(Long id){
        return  supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
    }

    @Override
    @Transactional
    public Supplier create(Person person, SupplierCreateDTO supplierDTO) {

        Supplier supplier = new Supplier();
        supplier.setPerson(person);
        supplier.setCompany(person.getCompany());
        supplier.setSpecialty(supplierDTO.getSpecialty());
        supplier.setNotes(supplierDTO.getNotes());

        return supplierRepository.save(supplier);
    }

    @Override
    @Transactional
    public Supplier patch(Long id, SupplierPatchDTO dto) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));

        if (dto.getSpecialty() != null) {
            supplier.setSpecialty(dto.getSpecialty());
        }

        if (dto.getNotes() != null) {
            supplier.setNotes(dto.getNotes());
        }

        return supplierRepository.save(supplier);
    }

    private SupplierDTO toDTO(Supplier supplier) {

        SupplierDTO dto = new SupplierDTO();

        dto.setId(supplier.getId());
        dto.setCompanyId(supplier.getCompany().getId());
        dto.setPersonId(supplier.getPerson().getId());
        dto.setSpecialty(supplier.getSpecialty());
        dto.setNotes(supplier.getNotes());
        dto.setCreatedAt(supplier.getCreatedAt());

        return dto;
    }
}