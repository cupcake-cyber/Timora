package com.timora.app.controllers;

import com.timora.app.models.Notificacion;
import com.timora.app.models.Usuario;
import com.timora.app.models.enums.Estado;
import com.timora.app.models.enums.TipoNotificacion;
import com.timora.app.service.NotificacionService;
import com.timora.app.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionControllerTest {

    @Mock
    private NotificacionService notificacionService;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private NotificacionController notificacionController;

    @Test
    void listar_DebeRetornarStatusOk() {
        when(notificacionService.findAll()).thenReturn(Collections.emptyList());

        List<Notificacion> response = notificacionController.listar();

        assertNotNull(response);
        verify(notificacionService).findAll();
    }

    @Test
    void obtener_DebeRetornarNotificacion() {
        Notificacion noti = new Notificacion();
        noti.setIdNotificacion(1);

        when(notificacionService.findById(1)).thenReturn(noti);

        Notificacion response = notificacionController.obtener(1);

        assertEquals(1, response.getIdNotificacion());
    }

    @Test
    void porUsuario_DebeRetornarLista() {
        Integer idUsuario = 1;
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);

        when(usuarioService.findById(idUsuario)).thenReturn(usuario);
        when(notificacionService.findByUsuario(usuario))
                .thenReturn(Collections.emptyList());

        List<Notificacion> response = notificacionController.porUsuario(idUsuario);

        assertNotNull(response);
        verify(notificacionService).findByUsuario(usuario);
    }

    @Test
    void crear_DebeGuardarNotificacion() {
        Notificacion noti = new Notificacion();
        noti.setMensaje("Test");
        noti.setTipo(TipoNotificacion.GENERAL);
        noti.setEstado(Estado.ACTIVO);
        noti.setFechaEnvio(LocalDateTime.now());

        when(notificacionService.guardar(noti)).thenReturn(noti);

        Notificacion response = notificacionController.crear(noti);

        assertEquals("Test", response.getMensaje());
    }

    @Test
    void actualizar_DebeRetornarNotificacionActualizada() {
        Integer id = 1;
        Notificacion noti = new Notificacion();
        noti.setMensaje("Actualizado");

        when(notificacionService.actualizar(id, noti)).thenReturn(noti);

        Notificacion response = notificacionController.actualizar(id, noti);

        assertEquals("Actualizado", response.getMensaje());
    }

    @Test
    void eliminar_DebeLlamarService() {
        notificacionController.eliminar(1);

        verify(notificacionService).borrar(1);
    }
}