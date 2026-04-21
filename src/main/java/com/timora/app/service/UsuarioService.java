package com.timora.app.service;

import com.timora.app.models.Usuario;

import java.util.List;

public interface UsuarioService {

    List<Usuario> findAll();

    Usuario findById(Long id);

    Usuario findByEmail(String email);

    Usuario guardar(Usuario usuario);

    Usuario actualizar(Long id, Usuario usuario);

    void borrar(Long id);
}