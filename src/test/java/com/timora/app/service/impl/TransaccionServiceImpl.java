package com.timora.app.service.impl;

import com.timora.app.models.Transaccion;
import com.timora.app.repository.TransaccionRepository;
import com.timora.app.service.TransaccionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository repository;

    public TransaccionServiceImpl(TransaccionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Transaccion> listarTodas() {
        return repository.findAll();
    }

    @Override
    public Optional<Transaccion> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public Transaccion guardar(Transaccion transaccion) {
        return repository.save(transaccion);
    }

    @Override
    public Transaccion actualizar(Long id, Transaccion transaccion) {
        return repository.findById(id).map(existente -> {
            existente.setTipo(transaccion.getTipo());
            existente.setMonto(transaccion.getMonto());
            existente.setEstado(transaccion.getEstado());
            existente.setFecha(transaccion.getFecha());
            existente.setCita(transaccion.getCita());
            return repository.save(existente);
        }).orElse(null);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}