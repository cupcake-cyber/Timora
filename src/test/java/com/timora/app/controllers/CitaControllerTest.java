package com.timora.app.controllers;

import com.timora.app.models.Cita;
import com.timora.app.models.enums.EstadoCita;
import com.timora.app.service.CitaService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaControllerTest {

    @Mock
    private CitaService citaService;

    @InjectMocks
    private CitaController citaController;


    @Test
    void crear_DebeRetornarOk() {

        Cita cita = new Cita();
        cita.setEstado(EstadoCita.PENDIENTE);

        when(citaService.guardar(any(Cita.class))).thenReturn(cita);

        ResponseEntity<Cita> response = citaController.crear(cita);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(citaService).guardar(cita);
    }


    @Test
    void listarTodas_DebeRetornarLista() {

        Cita cita = new Cita();
        cita.setEstado(EstadoCita.PENDIENTE);

        when(citaService.listarTodas()).thenReturn(List.of(cita));

        ResponseEntity<List<Cita>> response = citaController.listarTodas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }


    @Test
    void obtenerPorId_DebeRetornarCita() {

        Cita cita = new Cita();
        cita.setId(1);
        cita.setEstado(EstadoCita.CONFIRMADA);

        when(citaService.obtenerPorId(1)).thenReturn(Optional.of(cita));

        ResponseEntity<Cita> response = citaController.obtenerPorId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(EstadoCita.CONFIRMADA, response.getBody().getEstado());
    }


    @Test
    void obtenerPorId_NoExiste() {

        when(citaService.obtenerPorId(1)).thenReturn(Optional.empty());

        ResponseEntity<Cita> response = citaController.obtenerPorId(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void eliminar_DebeRetornarNoContent() {

        doNothing().when(citaService).eliminar(1);

        ResponseEntity<Void> response = citaController.eliminar(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(citaService).eliminar(1);
    }
}