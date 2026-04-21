package com.timora.app.repository;

import com.timora.app.models.Servicio;
import com.timora.app.models.enums.EstadoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    Optional<Servicio> findByNombre(String nombre);
    List<Servicio> findByEstado(EstadoServicio estado);
}
