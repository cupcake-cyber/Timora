package com.timora.app.service.impl;

import com.timora.app.models.Configuracion;
import com.timora.app.repository.ConfiguracionRepository;
import com.timora.app.service.ConfiguracionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ConfiguracionServiceImpl implements ConfiguracionService {

    private final ConfiguracionRepository configuracionRepository;

    public ConfiguracionServiceImpl(ConfiguracionRepository configuracionRepository) {
        this.configuracionRepository = configuracionRepository;
    }

    @Override
    public List<Configuracion> findAll() {
        return configuracionRepository.findAll();
    }

    @Override
    public Configuracion findById(Long id) {
        return configuracionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Configuración no encontrada"));
    }

    @Override
    public Configuracion findByUsuarioId(Long idUsuario) {
        return configuracionRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Configuración no encontrada para el usuario"));
    }

    @Override
    public Configuracion guardar(Configuracion configuracion) {
        return configuracionRepository.save(configuracion);
    }

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

    @Override
    public void borrar(Long id) {
        configuracionRepository.delete(findById(id));
    }
}