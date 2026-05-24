package com.timora.app.service.impl;

import com.timora.app.models.Notification;
import com.timora.app.models.Usuario;
import com.timora.app.repository.NotificacionRepository;
import com.timora.app.service.NotificacionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio de notificaciones.
 */
@Service
@Transactional
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionServiceImpl(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    /**
     * Obtiene todas las notificaciones registradas.
     *
     * @return lista de notificaciones
     */
    @Override
    public List<Notification> findAll() {
        return notificacionRepository.findAll();
    }

    /**
     * Busca una notificación por su ID.
     *
     * @param id identificador de la notificación
     * @return notificación encontrada
     * @throws IllegalArgumentException si no existe
     */
    @Override
    public Notification findById(Long id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notificación no encontrada"));
    }

    /**
     * Obtiene todas las notificaciones de un usuario.
     *
     * @param usuario usuario asociado
     * @return lista de notificaciones del usuario
     */
    @Override
    public List<Notification> findByUsuario(Usuario usuario) {
        return notificacionRepository.findByUsuario(usuario);
    }

    /**
     * Guarda una nueva notificación en la base de datos.
     *
     * @param notificacion objeto notificación a guardar
     * @return notificación guardada
     */
    @Override
    public Notification guardar(Notification notificacion) {
        return notificacionRepository.save(notificacion);
    }

    /**
     * Actualiza una notificación existente.
     *
     * @param id identificador de la notificación
     * @param notificacion datos nuevos de la notificación
     * @return notificación actualizada
     */
    @Override
    public Notification actualizar(Long id, Notification notificacion) {
        Notification existente = findById(id);

        existente.setUsuario(notificacion.getUsuario());
        existente.setTipo(notificacion.getTipo());
        existente.setMensaje(notificacion.getMensaje());
        existente.setEstado(notificacion.getEstado());
        existente.setFechaEnvio(notificacion.getFechaEnvio());
        existente.setObjetivo(notificacion.getObjetivo());

        return notificacionRepository.save(existente);
    }

    /**
     * Elimina una notificación por su ID.
     *
     * @param id identificador de la notificación
     */
    @Override
    public void borrar(Long id) {
        notificacionRepository.delete(findById(id));
    }

    /**
     * Marca una notificación como leída.
     *
     * @param id identificador de la notificación
     * @return notificación actualizada
     */
    @Override
    public Notification marcarComoLeida(Long id) {
        Notification notificacion = findById(id);

        notificacion.setEstado(
                com.timora.app.models.enums.EstadoNotificacion.LEIDA
        );

        return notificacionRepository.save(notificacion);
    }
}