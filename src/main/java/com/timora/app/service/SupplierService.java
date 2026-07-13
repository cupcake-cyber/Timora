package com.timora.app.service;

import com.timora.app.dto.supplier.SupplierCreateDTO;
import com.timora.app.dto.supplier.SupplierDTO;
import com.timora.app.dto.supplier.SupplierPatchDTO;
import com.timora.app.model.Person;
import com.timora.app.model.Supplier;

import java.util.List;

public interface SupplierService {
    Supplier create(Person person, SupplierCreateDTO supplierDTO);
    Supplier findById(Long id);
    Supplier patch(Long id, SupplierPatchDTO dto);
    List<Supplier> findByUserId(Long userId);
}