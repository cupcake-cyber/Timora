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
    public List<Transaccion> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Transaccion> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Transaccion guardar(Transaccion transaccion) {
        return repository.save(transaccion);
    }

    @Override
    public Transaccion actualizar(Long id, Transaccion nueva) {
        return repository.findById(id).map(existente -> {
            existente.setTipo(nueva.getTipo());
            existente.setMonto(nueva.getMonto());
            existente.setEstado(nueva.getEstado());
            existente.setFecha(nueva.getFecha());
            existente.setCita(nueva.getCita());
            return repository.save(existente);
        }).orElse(null);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
    @Override
    public List<Transaccion> findByCita(Long idCita) {
        return repository.findByCitaIdCita(idCita);
    }
}