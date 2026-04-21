package com.timora.app.service.impl;

import com.timora.app.models.Servicio;
import com.timora.app.repository.ServicioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicioServiceImplTest {
    @Mock
    private ServicioRepository servicioRepository;

    @InjectMocks
    private ServicioServiceImpl servicioService;

    @Test
    void findAllServRepositoryData() {
        List<Servicio> esperadoServicios = List.of(
                new Servicio("Limpieza facial", "Limpieza de rostro", 3, 120.00, "APROBADO"),
                new Servicio("Medicina General", "Consulta médica para Medicina General", 1, 80.00, "APROBADO")
        );
        when(servicioRepository.findAll()).thenReturn(esperadoServicios);

        List<Servicio> result = servicioService.findAll();
        assertEquals(esperadoServicios, result);
        verify(servicioRepository).findAll();
    }

    @Test
    void findByIdServWhenExists() {
        Long id = 1L;
        Servicio servicio = new Servicio(id, "Limpieza facial", "Limpieza de rostro", 3, 120.00, "APROBADO");
        when(servicioRepository.findById(id)).thenReturn(Optional.of(servicio));

        Servicio result = servicioService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(servicioRepository).findById(id);
    }

    @Test
    void findByIdServWhenNotExists() {
        Long id = 90L;
        when(servicioRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioService.findById(id);
        });

        assertEquals("Servicio con id: 90 no encontrado", exception.getMessage());
    }

    @Test
    void findByNombreServWhenExists() {
        String nombre = "Limpieza facial";
        Servicio servicio = new Servicio(1L, nombre, "Limpieza de rostro", 3, 120.00, "APROBADO");
        when(servicioRepository.findByNombre(nombre)).thenReturn(Optional.of(servicio));

        Servicio result = servicioService.findByNombre(nombre);

        assertNotNull(result);
        assertEquals(nombre, result.getNombre());
        verify(servicioRepository).findByNombre(nombre);
    }

    @Test
    void findByNombreServWhenNotExists() {
        String nombre = "No existe";
        when(servicioRepository.findByNombre(nombre)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioService.findByNombre(nombre);
        });

        assertEquals("Servicio con Nombre: No existe no encontrado", exception.getMessage());
    }

    @Test
    void findByEstadoServWhenExists() {
        String estado = "APROBADO";
        List<Servicio> esperadoServicios = List.of(
                new Servicio(1L, "Limpieza facial", "Limpieza de rostro", 3, 120.00, estado),
                new Servicio(2L, "Medicina General", "Consulta médica para Medicina General", 1, 80.00, estado)
        );
        when(servicioRepository.findByEstado(estado)).thenReturn(esperadoServicios);

        List<Servicio> result = servicioService.findByEstado(estado);
        assertEquals(2, result.size());
        assertEquals(estado, result.get(0).getEstado());
        verify(servicioRepository).findByEstado(estado);
    }

    @Test
    void findByEstadoServWhenNotExists() {
        String estado = "No existe";
        when(servicioRepository.findByEstado(estado)).thenReturn(List.of());

        List<Servicio> result = servicioService.findByEstado(estado);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
        verify(servicioRepository).findByEstado(estado);
    }

    @Test
    void saveServForcesNullIdBeforePersist() {
        Servicio servicioNuevo = new Servicio(null, "Limpieza facial", "Limpieza de rostro", 3, 120.00, "APROBADO");
        Servicio servicioGuardado = new Servicio(10L, "Limpieza facial", "Limpieza de rostro", 3, 120.00, "APROBADO");
        when(servicioRepository.save(servicioNuevo)).thenReturn(servicioGuardado);

        Servicio result = servicioService.guardar(servicioNuevo);

        assertEquals(10L, result.getId());
        assertEquals(servicioGuardado, result);
        verify(servicioRepository).save(servicioNuevo);
    }

    @Test
    void updateServChangesFieldsAndPersist() {
        Long id = 1L;
        Servicio servicioExistente = new Servicio(id, "Limpieza facial", "Limpieza de rostro", 3, 120.00, "APROBADO");
        Servicio servicioNuevo = new Servicio(null, "Limpieza facial", "Limpieza de rostro", 3, 120.00, "APROBADO");

        when(servicioRepository.findById(id)).thenReturn(Optional.of(servicioExistente));
        when(servicioRepository.save(servicioExistente)).thenReturn(servicioExistente);

        Servicio result = servicioService.actualizar(id, servicioNuevo);

        assertEquals("Limpieza facial", result.getNombre());
        assertEquals("APROBADO", result.getEstado());
        verify(servicioRepository).save(servicioExistente);
    }

    @Test
    void updateServThrowsWhenServNotExists() {
        Long id = 1L;
        when(servicioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            servicioService.actualizar(id, new Servicio());
        });
    }

    @Test
    void deleteServWhenExists() {
        Long id = 1L;
        Servicio servicio = new Servicio(id, "Limpieza facial", "Limpieza de rostro", 3, 120.00, "APROBADO");
        when(servicioRepository.findById(id)).thenReturn(Optional.of(servicio));

        servicioService.borrar(id);
        verify(servicioRepository).delete(servicio);
    }
}