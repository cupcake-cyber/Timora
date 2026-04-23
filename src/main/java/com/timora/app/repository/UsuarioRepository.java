package com.timora.app.repository;

import com.timora.app.models.Usuario;
import com.timora.app.models.enums.EstadoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByEstado(EstadoUsuario estado);
    Optional<Usuario> findByEmail(String email);
}
