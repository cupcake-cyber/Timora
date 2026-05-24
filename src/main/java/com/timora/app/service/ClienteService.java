package com.timora.app.service;

import com.timora.app.models.Customer;
import com.timora.app.models.Supplier;

import java.util.List;

public interface ClienteService {

    List<Customer> findAll();

    List<Supplier> findActivos();

    Customer guardar(Customer cliente);

    Customer findById(Long id);

    Customer findByUsuario(Long idUsuario);
}