package com.timora.app.service;

import com.timora.app.models.Cliente;

import java.util.List;

public interface ClienteService {

    Cliente guardar(Cliente cliente);

    Cliente findById(Integer id);

    Cliente findByUsuario(Integer idUsuario);
}