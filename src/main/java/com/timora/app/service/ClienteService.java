package com.timora.app.service;

import com.timora.app.models.Cliente;
import com.timora.app.models.Proveedor;
import com.timora.app.models.Usuario;

import java.util.List;

public interface ClienteService {

    List<Cliente> findAll();

    List<Proveedor> findActivos();

    Cliente guardar(Cliente cliente);

    Cliente findById(Long id);

    Cliente findByUsuario(Long idUsuario);
}