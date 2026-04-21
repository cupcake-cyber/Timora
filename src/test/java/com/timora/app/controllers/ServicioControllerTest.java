package com.timora.app.controllers;

import com.timora.app.models.Servicio;
import com.timora.app.service.ServicioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicioControllerTest {
    @Mock
    private ServicioService servicioService;

    @InjectMocks
    private ServicioController servicioController;

    @Test
    void getAllServ() {
        List<Servicio> esperadoServicios = List.of(
                new Servicio("Limpieza facial", "Limpieza de rostro", 3, 120.00, "APROBADO"),
                new Servicio("Medicina General", "Consulta médica para Medicina General", 1, 80.00, "APROBADO")
        );

        when(servicioService.findAll()).thenReturn(esperadoServicios);

        ResponseEntity<List<Servicio>> response = servicioController.getAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(esperadoServicios, response.getBody());
        verify(servicioService).findAll();
    }

    @Test
    void getById() {
        Long id = 1L;
        Servicio esperadoServicio = new Servicio(id, "Limpieza facial", "Limpieza de rostro", 3, 120.00, "APROBADO");
        when(servicioService.findById(id)).thenReturn(esperadoServicio);

        ResponseEntity<Servicio> response = servicioController.getById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(esperadoServicio, response.getBody());
        verify(servicioService).findById(id);
    }

    @Test
    void getByNombre() {
        String nombre = "Limpieza facial";
        Servicio esperadoServicio = new Servicio(1L, nombre, "Limpieza de rostro", 3, 120.00, "APROBADO");
        when(servicioService.findByNombre(nombre)).thenReturn(esperadoServicio);

        ResponseEntity<Servicio> response = servicioController.getByNombre(nombre);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(esperadoServicio, response.getBody());
        verify(servicioService).findByNombre(nombre);
    }

    @Test
    void getByEstado() {
        String estado = "APROBADO";
        Servicio esperadoServicio = new Servicio(1L, "Limpieza facial", "Limpieza de rostro", 3, 120.00, estado);
        List<Servicio> esperadoServicios = List.of(esperadoServicio);
        when(servicioService.findByEstado(estado)).thenReturn(esperadoServicios);

        ResponseEntity<List<Servicio>> response = servicioController.getByEstado(estado);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(estado, response.getBody().get(0).getEstado());
        verify(servicioService).findByEstado(estado);
    }

    @Test
    void createServ() {
        Servicio servicioPorCrear = new Servicio(null, "Limpieza facial", "Limpieza de rostro", 3, 120.00, "APROBADO");
        Servicio servicioCreado = new Servicio(1L, "Limpieza facial", "Limpieza de rostro", 3, 120.00, "APROBADO");
        when(servicioService.guardar(servicioPorCrear)).thenReturn(servicioCreado);

        ResponseEntity<Servicio> response = servicioController.guardar(servicioPorCrear);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(servicioCreado, response.getBody());
        assertEquals(URI.create("/api/servicio/1"), response.getHeaders().getLocation());
        verify(servicioService).guardar(servicioPorCrear);
    }

    @Test
    void updateServ() {
        Long id = 1L;
        Servicio servicioPorActu = new Servicio(null, "Limpieza facial", "Limpieza de rostro", 3, 120.00, "APROBADO");
        Servicio servicioActu = new Servicio(1L, "Limpieza facial", "Limpieza de rostro", 3, 120.00, "APROBADO");
        when(servicioService.actualizar(id, servicioPorActu)).thenReturn(servicioActu);

        ResponseEntity<Servicio> response = servicioController.actualizar(id, servicioPorActu);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(servicioActu, response.getBody());
        verify(servicioService).actualizar(id, servicioPorActu);
    }

    @Test
    void deleteServ() {
        Long id = 1L;

        ResponseEntity<Void> response = servicioController.borrar(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(servicioService).borrar(id);
    }

}