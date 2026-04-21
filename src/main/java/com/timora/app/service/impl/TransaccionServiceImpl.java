package com.timora.app.service.impl;

import com.timora.app.models.Transaccion;
import com.timora.app.repository.TransaccionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransaccionServiceImpl {

    private final TransaccionRepository repository;

    // Inyección por constructor (MEJOR práctica)
    public TransaccionServiceImpl(TransaccionRepository repository) {
        this.repository = repository;
    }

    public List<Transaccion> listar() {
        return repository.findAll();
    }

    public Transaccion guardar(Transaccion transaccion) {
        return repository.save(transaccion);
    }

    public Transaccion obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Transaccion actualizar(Long id, Transaccion nueva) {
        Optional<Transaccion> optional = repository.findById(id);

        if (optional.isPresent()) {
            Transaccion existente = optional.get();

            existente.setTipo(nueva.getTipo());
            existente.setMonto(nueva.getMonto());
            existente.setEstado(nueva.getEstado());
            existente.setFecha(nueva.getFecha());
            existente.setCita(nueva.getCita());

            return repository.save(existente);
        }

        return null;
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}