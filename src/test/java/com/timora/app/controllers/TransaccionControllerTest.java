package com.timora.app.controllers;

import com.timora.app.models.Transaccion;
import com.timora.app.service.TransaccionService;
import com.timora.app.service.impl.TransaccionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransaccionControllerTest {

    @Mock
    private TransaccionService service;

    @InjectMocks
    private TransaccionController controller;

    @Test
    void listar_DebeRetornarLista() {
        when(service.listarTodas()).thenReturn(List.of(new Transaccion()));

        List<Transaccion> resultado = controller.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(service).listarTodas();
    }

    @Test
    void obtener_DebeRetornarTransaccion() {
        Long id = 1L;
        Transaccion t = new Transaccion();

        when(service.obtenerPorId(id)).thenReturn(Optional.of(t));

        Transaccion resultado = controller.obtener(id);

        assertNotNull(resultado);
        verify(service).obtenerPorId(id);
    }

    @Test
    void crear_DebeGuardar() {
        Transaccion t = new Transaccion();

        when(service.guardar(t)).thenReturn(t);

        Transaccion resultado = controller.crear(t);

        assertNotNull(resultado);
        verify(service).guardar(t);
    }

    @Test
    void actualizar_DebeActualizar() {
        Long id = 1L;
        Transaccion t = new Transaccion();

        when(service.actualizar(id, t)).thenReturn(t);

        Transaccion resultado = controller.actualizar(id, t);

        assertNotNull(resultado);
        verify(service).actualizar(id, t);
    }

    @Test
    void eliminar_DebeEliminar() {
        Long id = 1L;

        controller.eliminar(id);

        verify(service).eliminar(id);
    }
}