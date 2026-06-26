package com.timora.app.service;

import com.timora.app.dto.supplier.SupplierCreateDTO;
import com.timora.app.model.Person;
import com.timora.app.model.Supplier;

import java.util.List;

public interface SupplierService {

    Supplier create(Person person, SupplierCreateDTO supplierDTO);

//    Supplier updateSupplier(Person person, CreatePersonRequest.SupplierData data);
//
//    void deleteByPersonId(Long personId);
//
//    boolean existsByPerson(Long personId);
}