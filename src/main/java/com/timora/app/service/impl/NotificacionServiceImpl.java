package com.timora.app.service.impl;

import com.timora.app.models.Notificacion;
import com.timora.app.models.Usuario;
import com.timora.app.repository.NotificacionRepository;
import com.timora.app.service.NotificacionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionServiceImpl(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    public List<Notificacion> findAll() {
        return notificacionRepository.findAll();
    }

    @Override
    public Notificacion findById(Integer id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notificación no encontrada"));
    }

    @Override
    public List<Notificacion> findByUsuario(Usuario usuario) {
        return notificacionRepository.findByUsuario(usuario);
    }

    @Override
    public Notificacion guardar(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }

    @Override
    public Notificacion actualizar(Integer id, Notificacion notificacion) {
        Notificacion existente = findById(id);

        existente.setUsuario(notificacion.getUsuario());
        existente.setTipo(notificacion.getTipo());
        existente.setMensaje(notificacion.getMensaje());
        existente.setEstado(notificacion.getEstado());
        existente.setFechaEnvio(notificacion.getFechaEnvio());
        existente.setObjetivo(notificacion.getObjetivo());

        return notificacionRepository.save(existente);
    }

    @Override
    public void borrar(Integer id) {
        notificacionRepository.delete(findById(id));
    }
}