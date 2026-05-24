package com.timora.app.repository;

import com.timora.app.models.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisponibilidadRepository extends JpaRepository<Availability, Long> {

    List<Availability> findByProveedorIdProveedor(Long idProveedor);
}
