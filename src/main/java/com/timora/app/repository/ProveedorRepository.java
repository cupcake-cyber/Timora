package com.timora.app.repository;
import com.timora.app.models.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {

    Optional<Proveedor> findByUsuario_IdUsuario(Integer idUsuario);

    Optional<Proveedor> findByNombreNegocio(String nombreNegocio);
}