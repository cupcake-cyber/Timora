package com.timora.app.service.impl;

import com.timora.app.models.Notificacion;
import com.timora.app.models.Usuario;
import com.timora.app.models.enums.Estado;
import com.timora.app.models.enums.TipoNotificacion;
import com.timora.app.repository.NotificacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceImplTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionServiceImpl notificacionService;

    @Test
    void findAll_DebeRetornarLista() {
        when(notificacionRepository.findAll())
                .thenReturn(List.of(new Notificacion(), new Notificacion()));

        List<Notificacion> resultado = notificacionService.findAll();

        assertEquals(2, resultado.size());
        verify(notificacionRepository).findAll();
    }

    @Test
    void actualizar_DebeModificarNotificacion() {
        Integer id = 1;

        Notificacion existente = new Notificacion();
        existente.setIdNotificacion(id);
        existente.setMensaje("Viejo");

        Notificacion nuevosDatos = new Notificacion();
        nuevosDatos.setMensaje("Nuevo");
        nuevosDatos.setTipo(TipoNotificacion.RESERVA);
        nuevosDatos.setEstado(Estado.ACTIVO);
        nuevosDatos.setFechaEnvio(LocalDateTime.now());

        when(notificacionRepository.findById(id))
                .thenReturn(Optional.of(existente));
        when(notificacionRepository.save(any(Notificacion.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        Notificacion actualizado = notificacionService.actualizar(id, nuevosDatos);

        assertEquals("Nuevo", actualizado.getMensaje());
        assertEquals(TipoNotificacion.RESERVA, actualizado.getTipo());
        verify(notificacionRepository).save(existente);
    }

    @Test
    void borrar_DebeEliminarSiExiste() {
        Integer id = 1;

        Notificacion noti = new Notificacion();
        noti.setIdNotificacion(id);

        when(notificacionRepository.findById(id))
                .thenReturn(Optional.of(noti));

        notificacionService.borrar(id);

        verify(notificacionRepository).delete(noti);
    }
}