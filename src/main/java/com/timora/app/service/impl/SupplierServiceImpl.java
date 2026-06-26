package com.timora.app.service.impl;

import com.timora.app.dto.supplier.SupplierCreateDTO;
import com.timora.app.dto.supplier.SupplierDTO;
import com.timora.app.exception.BusinessException;
import com.timora.app.exception.ForbiddenException;
import com.timora.app.model.Person;
import com.timora.app.model.Supplier;
import com.timora.app.model.User;
import com.timora.app.repository.SupplierRepository;
import com.timora.app.security.AccessControlService;
import com.timora.app.security.SecurityHelper;
import com.timora.app.service.SupplierService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SecurityHelper securityHelper;
    private final AccessControlService auth;

    @Override
    public Supplier create(Person person, SupplierCreateDTO supplierDTO) {

        User user = securityHelper.getCurrentUser();

        if (!auth.isOwner(user)) {
            if(!user.getCompany().getId().equals(supplierDTO.getCompanyId())) {
                throw new ForbiddenException("You can not create entities outside of your company.");
            }
            if(!auth.isAdmin(user)){
                throw  new ForbiddenException("ADMIN can only create Suppliers");
            }
        }
        // TODO: Exponer un método en PersonService para validar la existencia de un Supplier.
        // Evitar usar el repositorio de otra entidad únicamente como utilidad.
        if (supplierRepository.existsByPersonId(supplierDTO.getPersonId())){
            throw new BusinessException("Supplier already exists");
        }

        Supplier supplier = new Supplier();
        supplier.setPerson(person);
        supplier.setCompany(person.getCompany());
        supplier.setSpecialty(supplierDTO.getSpecialty());
        supplier.setNotes(supplierDTO.getNotes());

        return supplierRepository.save(supplier);
    }

//    @Override
//    public Supplier updateSupplier(Person person, CreatePersonRequest.SupplierData data) {
//
//        Supplier supplier = supplierRepository.findByPersonId(person.getId())
//                .orElseThrow(() -> new RuntimeException("Supplier not found"));
//
//        if (data != null) {
//
//            if (data.getSpecialty() != null) {
//                supplier.setSpecialty(data.getSpecialty());
//            }
//
//            if (data.getNotes() != null) {
//                supplier.setNotes(data.getNotes());
//            }
//        }
//
//        return supplierRepository.save(supplier);
//    }
//
//    @Override
//    public void deleteByPersonId(Long personId) {
//        supplierRepository.deleteByPersonId(personId);
//    }
//
//    @Override
//    public boolean existsByPerson(Long personId) {
//        return supplierRepository.existsByPersonId(personId);
//    }

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