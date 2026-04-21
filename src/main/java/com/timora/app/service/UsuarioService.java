package com.timora.app.service;

import com.timora.app.models.Usuario;

import java.util.List;

public interface UsuarioService {

    List<Usuario> findAll();

    Usuario findById(Integer id);

    Usuario findByEmail(String email);

    Usuario guardar(Usuario usuario);

    Usuario actualizar(Integer id, Usuario usuario);

    void borrar(Integer id);
}