package com.timora.app.service;

import com.timora.app.models.Cliente;

import java.util.List;

public interface ClienteService {

    Cliente guardar(Cliente cliente);

    Cliente findById(Long id);

    Cliente findByUsuario(Long idUsuario);
}