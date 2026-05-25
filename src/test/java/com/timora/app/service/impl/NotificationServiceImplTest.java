package com.timora.app.service.impl;

import com.timora.app.models.Notification;
import com.timora.app.models.enums.NotificationType;
import com.timora.app.repository.NotificationRepository;
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
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificacionService;

    @Test
    void findAll_DebeRetornarLista() {
        when(notificationRepository.findAll())
                .thenReturn(List.of(new Notification(), new Notification()));

        List<Notification> resultado = notificacionService.findAll();

        assertEquals(2, resultado.size());
        verify(notificationRepository).findAll();
    }

    @Test
    void actualizar_DebeModificarNotificacion() {
        Long id = 1L;

        Notification existente = new Notification();
        existente.setIdNotificacion(id);
        existente.setMensaje("Viejo");

        Notification nuevosDatos = new Notification();
        nuevosDatos.setMensaje("Nuevo");
        nuevosDatos.setTipo(NotificationType.RESERVA);
        nuevosDatos.setEstado(EstadoNotificacion.LEIDA);
        nuevosDatos.setFechaEnvio(LocalDateTime.now());

        when(notificationRepository.findById(id))
                .thenReturn(Optional.of(existente));
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        Notification actualizado = notificacionService.actualizar(id, nuevosDatos);

        assertEquals("Nuevo", actualizado.getMensaje());
        assertEquals(NotificationType.RESERVA, actualizado.getTipo());
        verify(notificationRepository).save(existente);
    }

    @Test
    void borrar_DebeEliminarSiExiste() {
        Long id = 1L;

        Notification noti = new Notification();
        noti.setIdNotificacion(id);

        when(notificationRepository.findById(id))
                .thenReturn(Optional.of(noti));

        notificacionService.borrar(id);

        verify(notificationRepository).delete(noti);
    }
}