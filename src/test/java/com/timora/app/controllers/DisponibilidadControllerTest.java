package com.timora.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timora.app.models.Disponibilidad;
import com.timora.app.service.DisponibilidadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DisponibilidadController.class)
class DisponibilidadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DisponibilidadService disponibilidadService;

    @Autowired
    private ObjectMapper objectMapper;

    private Disponibilidad disponibilidad;

    @BeforeEach
    void setUp() {
        disponibilidad = Disponibilidad.builder()
                .idDisponibilidad(1)
                .idProveedor(1)
                .fechaInicio(LocalDate.of(2025, 1, 1))
                .fechaFin(LocalDate.of(2025, 1, 31))
                .tipoRecurrencia("SEMANAL")
                .horaInicio(LocalTime.of(8, 0))
                .horaFin(LocalTime.of(17, 0))
                .build();
    }

    @Test
    void getAll_debeRetornar200ConLista() throws Exception {
        when(disponibilidadService.findAll()).thenReturn(List.of(disponibilidad));

        mockMvc.perform(get("/api/disponibilidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idDisponibilidad").value(1));
    }

    @Test
    void getById_debeRetornar200CuandoExiste() throws Exception {
        when(disponibilidadService.findById(1)).thenReturn(disponibilidad);

        mockMvc.perform(get("/api/disponibilidades/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDisponibilidad").value(1));
    }

    @Test
    void crear_debeRetornar201AlGuardar() throws Exception {
        when(disponibilidadService.guardar(any(Disponibilidad.class))).thenReturn(disponibilidad);

        mockMvc.perform(post("/api/disponibilidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(disponibilidad)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idDisponibilidad").value(1));
    }

    @Test
    void borrar_debeRetornar204() throws Exception {
        doNothing().when(disponibilidadService).borrar(1);

        mockMvc.perform(delete("/api/disponibilidades/1"))
                .andExpect(status().isNoContent());
    }
}