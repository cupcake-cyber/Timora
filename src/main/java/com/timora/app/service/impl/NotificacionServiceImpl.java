package com.timora.app.service.impl;

import com.timora.app.models.Notificacion;
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
    public List<Notificacion> findAll() {
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
    public Notificacion findById(Long id) {
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
    public List<Notificacion> findByUsuario(Usuario usuario) {
        return notificacionRepository.findByUsuario(usuario);
    }

    /**
     * Guarda una nueva notificación en la base de datos.
     *
     * @param notificacion objeto notificación a guardar
     * @return notificación guardada
     */
    @Override
    public Notificacion guardar(Notificacion notificacion) {
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
    public Notificacion actualizar(Long id, Notificacion notificacion) {
        Notificacion existente = findById(id);

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
    public Notificacion marcarComoLeida(Long id) {
        Notificacion notificacion = findById(id);

        notificacion.setEstado(
                com.timora.app.models.enums.EstadoNotificacion.LEIDA
        );

        return notificacionRepository.save(notificacion);
    }
}