package com.timora.app.repository;
import com.timora.app.models.Proveedor;
import com.timora.app.models.enums.EstadoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    Optional<Proveedor> findByUsuario_IdUsuario(Long idUsuario);
    List<Proveedor> findByUsuario_Estado(EstadoUsuario estado);
    Optional<Proveedor> findByNombreNegocio(String nombreNegocio);
}