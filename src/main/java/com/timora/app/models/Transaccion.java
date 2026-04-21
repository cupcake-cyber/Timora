package com.timora.app.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.timora.app.models.enums.EstadoTransaccion;
import com.timora.app.models.enums.TipoTransaccion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaccion")
@Getter
@Setter
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaccion")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cita", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cita cita;

    @Enumerated(EnumType.STRING)
    private TipoTransaccion tipo;

    private Double monto;

    @Enumerated(EnumType.STRING)
    private EstadoTransaccion estado;

    private LocalDateTime fecha;

    public Transaccion() {}
}
