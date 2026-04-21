package com.timora.app.controllers;

import com.timora.app.models.Usuario;
import com.timora.app.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @Test
    void getAll_DebeRetornarStatusOk() {
        when(usuarioService.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Usuario>> response = usuarioController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getByEmail_DebeRetornarUsuario() {
        String email = "test@mail.com";
        Usuario u = new Usuario();
        u.setEmail(email);
        when(usuarioService.findByEmail(email)).thenReturn(u);

        ResponseEntity<Usuario> response = usuarioController.getByEmail(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(email, response.getBody().getEmail());
    }

    @Test
    void borrar_DebeRetornarNoContent() {
        ResponseEntity<Void> response = usuarioController.borrar(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(usuarioService).borrar(1);
    }
}