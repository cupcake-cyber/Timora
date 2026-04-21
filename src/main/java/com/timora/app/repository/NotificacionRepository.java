package com.timora.app.repository;

import com.timora.app.models.Notificacion;
import com.timora.app.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    // Obtener notificaciones por usuario
    List<Notificacion> findByUsuario(Usuario usuario);

    // Opcional: por estado
    List<Notificacion> findByEstado(String estado);
}