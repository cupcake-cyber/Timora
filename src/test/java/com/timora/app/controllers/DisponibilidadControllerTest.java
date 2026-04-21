package com.timora.app.controllers;

import com.timora.app.models.Disponibilidad;
import com.timora.app.models.Proveedor;
import com.timora.app.service.DisponibilidadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DisponibilidadControllerTest {

    @Mock
    private DisponibilidadService disponibilidadService;

    @InjectMocks
    private DisponibilidadController disponibilidadController;

    private Disponibilidad disponibilidad;

    @BeforeEach
    void setUp() {
        Proveedor proveedor = new Proveedor();
        proveedor.setIdProveedor(1L);

        disponibilidad = Disponibilidad.builder()
                .idDisponibilidad(1L)
                .proveedor(proveedor)
                .fechaInicio(LocalDate.of(2025, 1, 1))
                .fechaFin(LocalDate.of(2025, 1, 31))
                .tipoRecurrencia("SEMANAL")
                .horaInicio(LocalTime.of(8, 0))
                .horaFin(LocalTime.of(17, 0))
                .build();
    }

    @Test
    void getAll_retornaListaConEstado200() {
        when(disponibilidadService.findAll()).thenReturn(List.of(disponibilidad));

        ResponseEntity<List<Disponibilidad>> response = disponibilidadController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(disponibilidadService, times(1)).findAll();
    }

    @Test
    void getById_retornaDisponibilidadConEstado200() {
        when(disponibilidadService.findById(1L)).thenReturn(disponibilidad);

        ResponseEntity<Disponibilidad> response = disponibilidadController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getIdDisponibilidad());
        verify(disponibilidadService, times(1)).findById(1L);
    }

    @Test
    void crear_retornaDisponibilidadConEstado201() {
        when(disponibilidadService.guardar(any(Disponibilidad.class))).thenReturn(disponibilidad);

        ResponseEntity<Disponibilidad> response = disponibilidadController.crear(disponibilidad);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getIdDisponibilidad());
        verify(disponibilidadService, times(1)).guardar(any(Disponibilidad.class));
    }

    @Test
    void actualizar_retornaDisponibilidadActualizada() {
        when(disponibilidadService.actualizar(any(Long.class), any(Disponibilidad.class)))
                .thenReturn(disponibilidad);

        ResponseEntity<Disponibilidad> response = disponibilidadController.actualizar(1L, disponibilidad);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(disponibilidadService, times(1)).actualizar(any(Long.class), any(Disponibilidad.class));
    }

    @Test
    void borrar_retornaEstado204() {
        doNothing().when(disponibilidadService).borrar(1L);

        ResponseEntity<Void> response = disponibilidadController.borrar(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(disponibilidadService, times(1)).borrar(1L);
    }
}