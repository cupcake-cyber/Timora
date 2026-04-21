package com.timora.app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "Configuracion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configuracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_configuracion")
    private Integer idConfiguracion;

    // Relación 1 a 1 con Usuario
    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "activas")
    private Boolean activas;

    @Column(name = "reservas")
    private Boolean reservas;

    @Column(name = "cancelaciones")
    private Boolean cancelaciones;

    @Column(name = "recordatorios")
    private Boolean recordatorios;

    @Column(name = "minutos_antes_recordatorio")
    private Integer minutosAntesRecordatorio;

    @Column(name = "canal_app")
    private Boolean canalApp;

    @Column(name = "canal_email")
    private Boolean canalEmail;

    @Column(name = "hora_inicio_silencio")
    private LocalTime horaInicioSilencio;

    @Column(name = "hora_fin_silencio")
    private LocalTime horaFinSilencio;

    @Column(name = "modo_oscuro")
    private Boolean modoOscuro;
}