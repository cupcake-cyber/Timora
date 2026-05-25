package com.timora.app.repository;

import com.timora.app.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notification, Long> {

    // Obtener notificaciones por usuario
    List<Notification> findByUsuario(Usuario usuario);

    // Opcional: por estado
    List<Notification> findByEstado(String estado);
}