package com.timora.app.service;

import com.timora.app.model.Supplier;

import java.util.List;
import java.util.Optional;

public interface SupplierService {
    List<Supplier> findAll();

    Optional<Supplier> findById(Long id);

    Supplier save(Supplier supplier);

    Supplier update(Long id, Supplier supplier);

    void delete(Long id);
}
