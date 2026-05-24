package com.timora.app.controllers;

import com.timora.app.models.Supplier;
import com.timora.app.service.ProveedorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProveedorControllerTest {

    @Mock
    private ProveedorService proveedorService;

    @InjectMocks
    private ProveedorController proveedorController;

    @Test
    void crear_DebeRetornarStatusCreatedYBody() {
        Supplier proveedor = new Supplier();
        proveedor.setNombreNegocio("Distribuidora X");

        when(proveedorService.guardar(any(Supplier.class))).thenReturn(proveedor);

        ResponseEntity<Supplier> response = proveedorController.crear(proveedor);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Distribuidora X", response.getBody().getNombreNegocio());
    }

    @Test
    void getById_DebeRetornarProveedor() {

        Supplier p = new Supplier();
        p.setIdProveedor(1L);
        p.setNombreNegocio("Insumos Pro");

        when(proveedorService.findById(1L)).thenReturn(p);

        ResponseEntity<Supplier> response = proveedorController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Insumos Pro", response.getBody().getNombreNegocio());
    }
}