package com.timora.app.service.impl;

import com.timora.app.models.Supplier;
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
        Supplier proveedor = new Supplier();
        proveedor.setNombreNegocio("Ferretería Central");

        when(proveedorRepository.save(any(Supplier.class))).thenReturn(proveedor);

        Supplier resultado = proveedorService.guardar(proveedor);

        assertNotNull(resultado);
        assertEquals("Ferretería Central", resultado.getNombreNegocio());
        verify(proveedorRepository).save(proveedor);
    }

    @Test
    void findByUsuario_CuandoExiste_RetornaProveedor() {
        Long idBusqueda = 10L;
        Usuario mockUsuario = new Usuario();
        mockUsuario.setIdUsuario(idBusqueda);

        Supplier mockProveedor = new Supplier();
        mockProveedor.setIdProveedor(1L);
        mockProveedor.setNombreNegocio("Alimentos S.A.");
        mockProveedor.setUsuario(mockUsuario);

        when(proveedorRepository.findByUsuario_IdUsuario(idBusqueda))
                .thenReturn(Optional.of(mockProveedor));

        Supplier resultado = proveedorService.findByUsuario(idBusqueda);

        assertNotNull(resultado);
        assertEquals("Alimentos S.A.", resultado.getNombreNegocio());
        assertEquals(idBusqueda, resultado.getUsuario().getIdUsuario());
    }

    @Test
    void findById_CuandoNoExiste_LanzaIllegalArgumentException() {
        when(proveedorRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            proveedorService.findById(999L);
        });

        assertEquals("Supplier no encontrado", exception.getMessage());
    }
}