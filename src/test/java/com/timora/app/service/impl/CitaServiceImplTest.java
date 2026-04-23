package com.timora.app.service.impl;

import com.timora.app.models.Cita;
import com.timora.app.models.enums.EstadoCita;
import com.timora.app.repository.CitaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaServiceImplTest {

    @Mock
    private CitaRepository citaRepository;

    @InjectMocks
    private CitaServiceImpl citaService;

    private Cita cita;

    @BeforeEach
    void setUp() {
        cita = new Cita();
        cita.setId(1L);
        cita.setHoraInicio(LocalTime.of(10, 0));
        cita.setHoraFin(LocalTime.of(11, 0));
        cita.setEstado(EstadoCita.PENDIENTE);
        cita.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    void findAll() {
        when(citaRepository.findAll()).thenReturn(List.of(cita));

        List<Cita> resultado = citaService.findAll();

        assertThat(resultado).isNotEmpty();
        assertThat(resultado).hasSize(1);
        verify(citaRepository).findAll();
    }


    @Test
    void findById() {
        when(citaRepository.findById(1)).thenReturn(Optional.of(cita));

        Optional<Cita> resultado = citaService.findById(1);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEstado()).isEqualTo(EstadoCita.PENDIENTE);
        verify(citaRepository).findById(1);
    }


    @Test
    void obtenerPorId_NoExiste() {
        when(citaRepository.findById(99)).thenReturn(Optional.empty());

        Optional<Cita> resultado = citaService.findById(99);

        assertThat(resultado).isEmpty();
        verify(citaRepository).findById(99);
    }


    @Test
    void guardarCita() {
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        Cita resultado = citaService.guardar(cita);

        assertThat(resultado).isNotNull();
        verify(citaRepository).save(any(Cita.class));
    }


    @Test
    void actualizarCita() {

        Cita nueva = new Cita();
        nueva.setHoraInicio(LocalTime.of(12, 0));
        nueva.setHoraFin(LocalTime.of(13, 0));
        nueva.setEstado(EstadoCita.CONFIRMADA);
        nueva.setDescripcion("Actualizada");

        when(citaRepository.findById(1)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        Cita resultado = citaService.actualizar(1, nueva);

        assertThat(resultado.getEstado()).isEqualTo(EstadoCita.CONFIRMADA);
        verify(citaRepository).findById(1);
        verify(citaRepository).save(any(Cita.class));
    }


    @Test
    void actualizarCita_NoExiste() {
        when(citaRepository.findById(1)).thenReturn(Optional.empty());

        try {
            citaService.actualizar(1, new Cita());
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).contains("no encontrada");
        }

        verify(citaRepository).findById(1);
        verify(citaRepository, never()).save(any());
    }


    @Test
    void eliminarCita() {
        doNothing().when(citaRepository).deleteById(1);

        citaService.eliminar(1);

        verify(citaRepository).deleteById(1);
    }
}
