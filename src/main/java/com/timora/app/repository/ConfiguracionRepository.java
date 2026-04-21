package com.timora.app.repository;

import com.timora.app.models.Configuracion;
import com.timora.app.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfiguracionRepository extends JpaRepository<Configuracion, Long> {

    Optional<Configuracion> findByUsuario(Usuario usuario);

    Optional<Configuracion> findByUsuarioIdUsuario(Long idUsuario);
}