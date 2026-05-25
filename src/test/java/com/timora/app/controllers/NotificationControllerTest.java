package com.timora.app.controllers;

import com.timora.app.models.Notification;
import com.timora.app.models.enums.NotificationType;
import com.timora.app.service.NotificationService;
import com.timora.app.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private NotificationController notificationController;

    @Test
    void listar_DebeRetornarStatusOk() {
        when(notificationService.findAll()).thenReturn(Collections.emptyList());

        List<Notification> response = notificationController.listar();

        assertNotNull(response);
        verify(notificationService).findAll();
    }

    @Test
    void obtener_DebeRetornarNotificacion() {
        Notification noti = new Notification();
        noti.setIdNotificacion(1L);

        when(notificationService.findById(1L)).thenReturn(noti);

        Notification response = notificationController.obtener(1L);

        assertEquals(1, response.getIdNotificacion());
    }

    @Test
    void porUsuario_DebeRetornarLista() {
        Long idUsuario = 1L;
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);

        when(usuarioService.findById(idUsuario)).thenReturn(usuario);
        when(notificationService.findByUsuario(usuario))
                .thenReturn(Collections.emptyList());

        List<Notification> response = notificationController.porUsuario(idUsuario);

        assertNotNull(response);
        verify(notificationService).findByUsuario(usuario);
    }

    @Test
    void crear_DebeGuardarNotificacion() {
        Notification noti = new Notification();
        noti.setMensaje("Test");
        noti.setTipo(NotificationType.PAGO);
        noti.setEstado(EstadoNotificacion.NO_LEIDA);
        noti.setFechaEnvio(LocalDateTime.now());

        when(notificationService.guardar(noti)).thenReturn(noti);

        Notification response = notificationController.crear(noti);

        assertEquals("Test", response.getMensaje());
    }

    @Test
    void actualizar_DebeRetornarNotificacionActualizada() {
        Long id = 1L;
        Notification noti = new Notification();
        noti.setMensaje("Actualizado");

        when(notificationService.actualizar(id, noti)).thenReturn(noti);

        Notification response = notificationController.actualizar(id, noti);

        assertEquals("Actualizado", response.getMensaje());
    }

    @Test
    void eliminar_DebeLlamarService() {
        notificationController.eliminar(1L);

        verify(notificationService).borrar(1L);
    }
}