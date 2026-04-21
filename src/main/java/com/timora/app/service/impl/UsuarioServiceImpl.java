package com.timora.app.service.impl;

import com.timora.app.models.Usuario;
import com.timora.app.repository.UsuarioRepository;
import com.timora.app.service.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio de usuarios.
 */
@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Obtiene todos los usuarios registrados.
     *
     * @return lista de usuarios
     */
    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca un usuario por su ID.
     *
     * @param id identificador del usuario
     * @return usuario encontrado
     * @throws IllegalArgumentException si no existe
     */
    @Override
    public Usuario findById(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    /**
     * Busca un usuario por su email.
     *
     * @param email correo del usuario
     * @return usuario encontrado
     * @throws IllegalArgumentException si no existe
     */
    @Override
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email no encontrado"));
    }

    /**
     * Guarda un nuevo usuario en la base de datos.
     *
     * @param usuario objeto usuario a guardar
     * @return usuario guardado
     */
    @Override
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    /**
     * Actualiza un usuario existente.
     *
     * @param id identificador del usuario
     * @param usuario datos nuevos
     * @return usuario actualizado
     */
    @Override
    public Usuario actualizar(Integer id, Usuario usuario) {
        Usuario existente = findById(id);

        existente.setNombre(usuario.getNombre());
        existente.setEmail(usuario.getEmail());
        existente.setTelefono(usuario.getTelefono());
        existente.setDireccion(usuario.getDireccion());
        existente.setEstado(usuario.getEstado());
        existente.setRol(usuario.getRol());

        return usuarioRepository.save(existente);
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id identificador del usuario
     */
    @Override
    public void borrar(Integer id) {
        usuarioRepository.delete(findById(id));
    }
}