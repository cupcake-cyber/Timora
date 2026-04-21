package com.timora.app.repository;

import com.timora.app.models.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Integer> {

    List<Disponibilidad> findByProveedorIdProveedor(Integer idProveedor);
}
