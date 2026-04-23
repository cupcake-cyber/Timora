package com.timora.app.service.impl;

import com.timora.app.models.Cita;
import com.timora.app.models.Cliente;
import com.timora.app.models.Servicio;
import com.timora.app.models.enums.EstadoCita;
import com.timora.app.repository.CitaRepository;
import com.timora.app.repository.ClienteRepository;
import com.timora.app.repository.ServicioRepository;
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

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ServicioRepository servicioRepository;

    @InjectMocks
    private CitaServiceImpl citaService;

    private Cita cita;

    @BeforeEach
    void setUp() {
        cita = new Cita();
        cita.setIdCita(1L);
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
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        Optional<Cita> resultado = citaService.findById(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEstado()).isEqualTo(EstadoCita.PENDIENTE);

        verify(citaRepository).findById(1L);
    }

    @Test
    void guardarCita() {

        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);

        Servicio servicio = new Servicio();
        servicio.setIdServicio(1L);

        cita.setCliente(cliente);
        cita.setServicio(servicio);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(servicioRepository.findById(1L)).thenReturn(Optional.of(servicio));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        Cita resultado = citaService.guardar(cita);

        assertThat(resultado).isNotNull();

        verify(clienteRepository).findById(1L);
        verify(servicioRepository).findById(1L);
        verify(citaRepository).save(any(Cita.class));
    }

    @Test
    void actualizarCita() {

        Cita nueva = new Cita();
        nueva.setHoraInicio(LocalTime.of(12, 0));
        nueva.setHoraFin(LocalTime.of(13, 0));
        nueva.setEstado(EstadoCita.CONFIRMADA);
        nueva.setDescripcion("Actualizada");

        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        Cita resultado = citaService.actualizar(1L, nueva);

        assertThat(resultado.getEstado()).isEqualTo(EstadoCita.CONFIRMADA);

        verify(citaRepository).findById(1L);
        verify(citaRepository).save(any(Cita.class));
    }

    @Test
    void eliminarCita() {
        doNothing().when(citaRepository).deleteById(1L);

        citaService.eliminar(1L);

        verify(citaRepository).deleteById(1L);
    }
}