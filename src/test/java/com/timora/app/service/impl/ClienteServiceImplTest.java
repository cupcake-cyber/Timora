package com.timora.app.service.impl;

import com.timora.app.models.Cliente;
import com.timora.app.models.Usuario;
import com.timora.app.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    void guardar_DebeRetornarClienteGuardado() {
        // Arrange
        Cliente cliente = new Cliente();
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        // Act
        Cliente resultado = clienteService.guardar(cliente);

        // Assert
        assertNotNull(resultado);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void findByUsuario_CuandoExiste_RetornaCliente() {
        // Arrange
        Integer idUsuario = 1;
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);

        Cliente cliente = new Cliente();
        cliente.setIdCliente(100);
        cliente.setUsuario(usuario);

        when(clienteRepository.findByUsuario_IdUsuario(idUsuario)).thenReturn(Optional.of(cliente));

        Cliente resultado = clienteService.findByUsuario(idUsuario);

        assertEquals(100, resultado.getIdCliente());
        assertEquals(idUsuario, resultado.getUsuario().getIdUsuario());
    }

    @Test
    void findByUsuario_CuandoNoExiste_LanzaExcepcion() {
        when(clienteRepository.findByUsuario_IdUsuario(99)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.findByUsuario(99);
        });
    }
}