package com.timora.app.repository;
import com.timora.app.models.Supplier;
import com.timora.app.models.enums.EstadoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<Supplier, Long> {

    Optional<Supplier> findByUsuario_IdUsuario(Long idUsuario);
    List<Supplier> findByUsuario_Estado(EstadoUsuario estado);
    Optional<Supplier> findByNombreNegocio(String nombreNegocio);
}