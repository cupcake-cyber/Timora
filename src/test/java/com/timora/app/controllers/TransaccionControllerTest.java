package com.timora.app.controllers;

import com.timora.app.models.Transaccion;
import com.timora.app.service.impl.TransaccionServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransaccionControllerTest {

    private final TransaccionServiceImpl service = Mockito.mock(TransaccionServiceImpl.class);
    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new TransaccionController(service))
            .build();

    @Test
    void listar_deberaRetornar200() throws Exception {
        Mockito.when(service.listar()).thenReturn(List.of(new Transaccion()));

        mockMvc.perform(get("/api/transacciones"))
                .andExpect(status().isOk());
    }
}