package com.timora.app.service.impl;

import com.timora.app.models.Disponibilidad;
import com.timora.app.models.Proveedor;
import com.timora.app.repository.DisponibilidadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DisponibilidadServiceImplTest {

    @Mock
    private DisponibilidadRepository disponibilidadRepository;

    @InjectMocks
    private DisponibilidadServiceImpl disponibilidadService;

    private Disponibilidad disponibilidad;
    private Proveedor proveedor;

    @BeforeEach
    void setUp() {
        proveedor = new Proveedor();
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
    void findAll_debeRetornarListaDeDisponibilidades() {
        when(disponibilidadRepository.findAll()).thenReturn(List.of(disponibilidad));

        List<Disponibilidad> resultado = disponibilidadService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(disponibilidadRepository, times(1)).findAll();
    }

    @Test
    void findById_debeRetornarDisponibilidadCuandoExiste() {
        when(disponibilidadRepository.findById(1L)).thenReturn(Optional.of(disponibilidad));

        Disponibilidad resultado = disponibilidadService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdDisponibilidad());
        verify(disponibilidadRepository, times(1)).findById(1L);
    }

    @Test
    void findById_debeLanzarExcepcionCuandoNoExiste() {
        when(disponibilidadRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> disponibilidadService.findById(99L));

        assertEquals("Disponibilidad no encontrada con id: 99", ex.getMessage());
    }

    @Test
    void guardar_debeGuardarYRetornarDisponibilidad() {
        when(disponibilidadRepository.save(disponibilidad)).thenReturn(disponibilidad);

        Disponibilidad resultado = disponibilidadService.guardar(disponibilidad);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdDisponibilidad());
        verify(disponibilidadRepository, times(1)).save(disponibilidad);
    }

    @Test
    void actualizar_debeActualizarCamposCorrectamente() {
        Disponibilidad actualizado = Disponibilidad.builder()
                .proveedor(proveedor)
                .fechaInicio(LocalDate.of(2025, 2, 1))
                .fechaFin(LocalDate.of(2025, 2, 28))
                .tipoRecurrencia("DIARIO")
                .horaInicio(LocalTime.of(9, 0))
                .horaFin(LocalTime.of(18, 0))
                .build();

        when(disponibilidadRepository.findById(1L)).thenReturn(Optional.of(disponibilidad));
        when(disponibilidadRepository.save(any(Disponibilidad.class))).thenReturn(disponibilidad);

        Disponibilidad resultado = disponibilidadService.actualizar(1L, actualizado);

        assertNotNull(resultado);
        verify(disponibilidadRepository, times(1)).save(any(Disponibilidad.class));
    }

    @Test
    void borrar_debeEliminarDisponibilidad() {
        doNothing().when(disponibilidadRepository).deleteById(1L);

        disponibilidadService.borrar(1L);

        verify(disponibilidadRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByProveedor_debeRetornarListaPorProveedor() {
        when(disponibilidadRepository.findByProveedorIdProveedor(1L))
                .thenReturn(List.of(disponibilidad));

        List<Disponibilidad> resultado = disponibilidadService.findByProveedor(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(disponibilidadRepository, times(1)).findByProveedorIdProveedor(1L);
    }
}