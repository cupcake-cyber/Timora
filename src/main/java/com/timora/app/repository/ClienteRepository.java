package com.timora.app.repository;

import com.timora.app.models.Cliente;
import com.timora.app.models.Proveedor;
import com.timora.app.models.enums.EstadoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByUsuario_IdUsuario(Long idUsuario);
    List<Proveedor> findByUsuario_Estado(EstadoUsuario estado);
}