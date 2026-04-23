package com.timora.app.service.impl;

import com.timora.app.models.Usuario;
import com.timora.app.models.enums.EstadoUsuario;
import com.timora.app.models.enums.RolUsuario;
import com.timora.app.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    void findAll_DebeRetornarListaDeUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(new Usuario(), new Usuario()));

        List<Usuario> resultado = usuarioService.findAll();

        assertEquals(2, resultado.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    void actualizar_DebeModificarUsuarioExistente() {
        Long id = 1L;
        Usuario existente = new Usuario();
        existente.setIdUsuario(id);
        existente.setNombre("Viejo Nombre");

        Usuario nuevosDatos = new Usuario();
        nuevosDatos.setNombre("Nuevo Nombre");
        nuevosDatos.setRol(RolUsuario.ADMINISTRADOR);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);

        Usuario actualizado = usuarioService.actualizar(id, nuevosDatos);

        assertEquals("Nuevo Nombre", actualizado.getNombre());
        assertEquals(RolUsuario.ADMINISTRADOR, actualizado.getRol());
        verify(usuarioRepository).save(existente);
    }

    @Test
    void borrar_DebeDesactivarUsuarioSiExiste() {
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(id);
        usuario.setEstado(EstadoUsuario.ACTIVO);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        usuarioService.borrar(id);

        assertEquals(EstadoUsuario.INACTIVO, usuario.getEstado());

        verify(usuarioRepository).save(usuario);

        verify(usuarioRepository, never()).delete(any());
    }
}