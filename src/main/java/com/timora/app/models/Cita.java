package com.timora.app.models;

import com.timora.app.models.enums.EstadoCita;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name= "cita")

public class Cita {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id_cita")
   private Long id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_cliente", nullable = false)
   private Cliente cliente;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_servicio", nullable = false)
   private Servicio servicio;

   @Column(name = "hora_inicio", nullable = false)
   private LocalTime horaInicio;

   @Column(name = "hora_fin", nullable = false)
   private LocalTime horaFin;

   @Enumerated(EnumType.STRING)
   @Column(name = "estado")
   private EstadoCita estado;

   @Column(name = "descripcion")
   private String descripcion;

   @Column(name = "fecha_creacion")
   private LocalDateTime fechaCreacion;

   public Cita() {
   }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
