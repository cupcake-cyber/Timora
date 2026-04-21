package com.timora.app.controllers;

import com.timora.app.models.Cliente;
import com.timora.app.models.Usuario;
import com.timora.app.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @Test
    void crear_DebeRetornarStatusCreated() {
        Cliente clienteInput = new Cliente();
        when(clienteService.guardar(any(Cliente.class))).thenReturn(clienteInput);

        ResponseEntity<Cliente> response = clienteController.crear(clienteInput);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(clienteService).guardar(clienteInput);
    }

    @Test
    void getByUsuario_DebeRetornarClienteOk() {
        Usuario u = new Usuario();
        u.setIdUsuario(1);

        Cliente c = new Cliente();
        c.setIdCliente(50);
        c.setUsuario(u);

        when(clienteService.findByUsuario(1)).thenReturn(c);

        ResponseEntity<Cliente> response = clienteController.getByUsuario(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(50, response.getBody().getIdCliente());
        assertEquals(1, response.getBody().getUsuario().getIdUsuario());
    }
}