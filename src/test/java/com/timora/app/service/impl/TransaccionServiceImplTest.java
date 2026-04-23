package com.timora.app.service.impl;

import com.timora.app.models.Transaccion;
import com.timora.app.repository.TransaccionRepository;
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
class TransaccionServiceImplTest {

    @Mock
    private TransaccionRepository repository;

    @InjectMocks
    private TransaccionServiceImpl service;

    @Test
    void guardar_DebeRetornarTransaccion() {
        Transaccion transaccion = new Transaccion();
        transaccion.setMonto(50.0);

        when(repository.save(any(Transaccion.class))).thenReturn(transaccion);

        Transaccion resultado = service.guardar(transaccion);

        assertNotNull(resultado);
        assertEquals(50.0, resultado.getMonto());
        verify(repository).save(transaccion);
    }

    @Test
    void findAll_DebeRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(new Transaccion(), new Transaccion()));

        List<Transaccion> resultado = service.findAll();

        assertEquals(2, resultado.size());
        verify(repository).findAll();
    }

    @Test
    void findById_CuandoExiste_RetornaTransaccion() {
        Transaccion transaccion = new Transaccion();
        transaccion.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(transaccion));

        Optional<Transaccion> resultado = service.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void findById_CuandoNoExiste_RetornaEmpty() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<Transaccion> resultado = service.findById(99L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void actualizar_DebeModificarYGuardar() {
        Transaccion existente = new Transaccion();
        existente.setId(1L);
        existente.setMonto(20.0);

        Transaccion nueva = new Transaccion();
        nueva.setMonto(80.0);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Transaccion.class))).thenReturn(existente);

        Transaccion resultado = service.actualizar(1L, nueva);

        assertNotNull(resultado);
        assertEquals(80.0, resultado.getMonto());
        verify(repository).save(existente);
    }

    @Test
    void actualizar_CuandoNoExiste_RetornaNull() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Transaccion resultado = service.actualizar(1L, new Transaccion());

        assertNull(resultado);
    }

    @Test
    void eliminar_DebeLlamarDeleteById() {
        doNothing().when(repository).deleteById(1L);

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void findByCita_DebeRetornarLista() {
        when(repository.findByCitaIdCita(1L))
                .thenReturn(List.of(new Transaccion()));

        List<Transaccion> resultado = service.findByCita(1L);

        assertEquals(1, resultado.size());
        verify(repository).findByCitaIdCita(1L);
    }
}