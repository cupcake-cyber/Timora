package com.timora.app.service.impl;

import com.timora.app.models.Proveedor;
import com.timora.app.models.Usuario;
import com.timora.app.repository.ProveedorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProveedorServiceImplTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ProveedorServiceImpl proveedorService;

    @Test
    void guardar_DebeRetornarProveedorConNombreNegocio() {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombreNegocio("Ferretería Central");

        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedor);

        Proveedor resultado = proveedorService.guardar(proveedor);

        assertNotNull(resultado);
        assertEquals("Ferretería Central", resultado.getNombreNegocio());
        verify(proveedorRepository).save(proveedor);
    }

    @Test
    void findByUsuario_CuandoExiste_RetornaProveedor() {
        Integer idBusqueda = 10;
        Usuario mockUsuario = new Usuario();
        mockUsuario.setIdUsuario(idBusqueda);

        Proveedor mockProveedor = new Proveedor();
        mockProveedor.setIdProveedor(1);
        mockProveedor.setNombreNegocio("Alimentos S.A.");
        mockProveedor.setUsuario(mockUsuario);

        when(proveedorRepository.findByUsuario_IdUsuario(idBusqueda))
                .thenReturn(Optional.of(mockProveedor));

        Proveedor resultado = proveedorService.findByUsuario(idBusqueda);

        assertNotNull(resultado);
        assertEquals("Alimentos S.A.", resultado.getNombreNegocio());
        assertEquals(idBusqueda, resultado.getUsuario().getIdUsuario());
    }

    @Test
    void findById_CuandoNoExiste_LanzaIllegalArgumentException() {
        when(proveedorRepository.findById(999)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            proveedorService.findById(999);
        });

        assertEquals("Proveedor no encontrado", exception.getMessage());
    }
}