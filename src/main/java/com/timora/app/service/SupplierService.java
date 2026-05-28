package com.timora.app.service;

import com.timora.app.dto.CreatePersonRequest;
import com.timora.app.model.Person;
import com.timora.app.model.Supplier;

import java.util.List;
import java.util.Optional;

public interface SupplierService {

    Supplier createSupplier(Person person, CreatePersonRequest.SupplierData data);

    Supplier updateSupplier(Person person, CreatePersonRequest.SupplierData data);

    void deleteByPersonId(Long personId);

    boolean existsByPerson(Long personId);
}