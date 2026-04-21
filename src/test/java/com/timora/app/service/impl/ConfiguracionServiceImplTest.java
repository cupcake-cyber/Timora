package com.timora.app.service.impl;

import com.timora.app.models.Configuracion;
import com.timora.app.repository.ConfiguracionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfiguracionServiceImplTest {

    @Mock
    private ConfiguracionRepository configuracionRepository;

    @InjectMocks
    private ConfiguracionServiceImpl configuracionService;

    @Test
    void findAll_DebeRetornarLista() {
        when(configuracionRepository.findAll())
                .thenReturn(List.of(new Configuracion(), new Configuracion()));

        List<Configuracion> resultado = configuracionService.findAll();

        assertEquals(2, resultado.size());
        verify(configuracionRepository).findAll();
    }

    @Test
    void actualizar_DebeModificarConfiguracion() {
        Long id = 1;

        Configuracion existente = new Configuracion();
        existente.setIdConfiguracion(id);
        existente.setActivas(true);

        Configuracion nuevosDatos = new Configuracion();
        nuevosDatos.setActivas(false);
        nuevosDatos.setModoOscuro(true);
        nuevosDatos.setHoraInicioSilencio(LocalTime.of(22, 0));
        nuevosDatos.setHoraFinSilencio(LocalTime.of(7, 0));

        when(configuracionRepository.findById(id))
                .thenReturn(Optional.of(existente));
        when(configuracionRepository.save(any(Configuracion.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        Configuracion actualizado = configuracionService.actualizar(id, nuevosDatos);

        assertFalse(actualizado.getActivas());
        assertTrue(actualizado.getModoOscuro());
        verify(configuracionRepository).save(existente);
    }

    @Test
    void borrar_DebeEliminarSiExiste() {
        Integer id = 1;

        Configuracion config = new Configuracion();
        config.setIdConfiguracion(id);

        when(configuracionRepository.findById(id))
                .thenReturn(Optional.of(config));

        configuracionService.borrar(id);

        verify(configuracionRepository).delete(config);
    }
}