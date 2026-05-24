package com.timora.app.service.impl;

import com.timora.app.models.Configuracion;
import com.timora.app.repository.ConfiguracionRepository;
import com.timora.app.service.ConfiguracionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio de configuraciones.
 */
@Service
@Transactional
public class ConfiguracionServiceImpl implements ConfiguracionService {

    private final ConfiguracionRepository configuracionRepository;

    public ConfiguracionServiceImpl(ConfiguracionRepository configuracionRepository) {
        this.configuracionRepository = configuracionRepository;
    }

    /**
     * Obtiene todas las configuraciones registradas.
     *
     * @return lista de configuraciones
     */
    @Override
    public List<Configuracion> findAll() {
        return configuracionRepository.findAll();
    }

    /**
     * Busca una configuración por su ID.
     *
     * @param id identificador de la configuración
     * @return configuración encontrada
     * @throws IllegalArgumentException si no existe
     */
    @Override
    public Configuracion findById(Long id) {
        return configuracionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Configuración no encontrada"));
    }

    /**
     * Busca la configuración asociada a un usuario.
     *
     * @param idUsuario identificador del usuario
     * @return configuración del usuario
     * @throws IllegalArgumentException si no existe
     */
    @Override
    public Configuracion findByUsuarioId(Long idUsuario) {
        return configuracionRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Configuración no encontrada para el usuario"));
    }

    /**
     * Guarda una nueva configuración en la base de datos.
     *
     * @param configuracion objeto configuración a guardar
     * @return configuración guardada
     */
    @Override
    public Configuracion guardar(Configuracion configuracion) {
        return configuracionRepository.save(configuracion);
    }

    /**
     * Actualiza una configuración existente.
     *
     * @param id identificador de la configuración
     * @param configuracion datos nuevos de la configuración
     * @return configuración actualizada
     */
    @Override
    public Configuracion actualizar(Long id, Configuracion configuracion) {
        Configuracion existente = findById(id);

        existente.setActivas(configuracion.getActivas());
        existente.setReservas(configuracion.getReservas());
        existente.setCancelaciones(configuracion.getCancelaciones());
        existente.setRecordatorios(configuracion.getRecordatorios());
        existente.setMinutosAntesRecordatorio(configuracion.getMinutosAntesRecordatorio());
        existente.setCanalApp(configuracion.getCanalApp());
        existente.setCanalEmail(configuracion.getCanalEmail());
        existente.setHoraInicioSilencio(configuracion.getHoraInicioSilencio());
        existente.setHoraFinSilencio(configuracion.getHoraFinSilencio());
        existente.setModoOscuro(configuracion.getModoOscuro());

        return configuracionRepository.save(existente);
    }

    /**
     * Elimina una configuración por su ID.
     *
     * @param id identificador de la configuración
     */
    @Override
    public void borrar(Long id) {
        configuracionRepository.delete(findById(id));
    }
}