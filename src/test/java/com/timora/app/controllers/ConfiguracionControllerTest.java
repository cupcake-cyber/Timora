package com.timora.app.controllers;

import com.timora.app.models.Configuracion;
import com.timora.app.service.ConfiguracionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfiguracionControllerTest {

    @Mock
    private ConfiguracionService configuracionService;

    @InjectMocks
    private ConfiguracionController configuracionController;

    @Test
    void getAll_DebeRetornarStatusOk() {
        when(configuracionService.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Configuracion>> response = configuracionController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getById_DebeRetornarConfiguracion() {
        Configuracion config = new Configuracion();
        config.setIdConfiguracion(1L);

        when(configuracionService.findById(1L)).thenReturn(config);

        ResponseEntity<Configuracion> response = configuracionController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getIdConfiguracion());
    }

    @Test
    void borrar_DebeRetornarNoContent() {
        ResponseEntity<Void> response = configuracionController.borrar(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(configuracionService).borrar(1L);
    }
}