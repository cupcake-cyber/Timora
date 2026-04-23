package com.timora.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.timora.app.models.enums.EstadoServicio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "servicio")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServicio;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Integer duracion;

    @Column(nullable = false)
    private Double precio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoServicio estado;

    @JsonIgnore
    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL)
    private List<Cita> citas = new ArrayList<>();

    public Servicio(Proveedor proveedor, String nombre, String descripcion, Integer duracion, Double precio, EstadoServicio estado) {
        this.proveedor = proveedor;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.precio = precio;
        this.estado = estado;
    }
}