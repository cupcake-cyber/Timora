package com.timora.app.service.impl;

import com.timora.app.models.Transaccion;
import com.timora.app.repository.TransaccionRepository;
import com.timora.app.service.TransaccionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
/**
 * Implementación del servicio de transacciones.
 */
@Service
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository repository;

    public TransaccionServiceImpl(TransaccionRepository repository) {
        this.repository = repository;
    }
    /**
     * Obtiene todas las transacciones registradas.
     *
     * @return lista de transacciones
     */
    @Override
    public List<Transaccion> findAll() {
        return repository.findAll();
    }
    /**
     * Busca una transacción por su ID.
     *
     * @param id identificador de la transacción
     * @return transacción encontrada
     */
    @Override
    public Optional<Transaccion> findById(Long id) {
        return repository.findById(id);
    }
    /**
     * Guarda una nueva transacción en la base de datos.
     *
     * @param transaccion objeto transacción a guardar
     * @return transacción guardada
     */
    @Override
    public Transaccion guardar(Transaccion transaccion) {
        return repository.save(transaccion);
    }
    /**
     * Actualiza una transacción existente.
     *
     * @param id identificador de la transacción
     * @param nueva datos nuevos de la transacción
     * @return transacción actualizada
     */
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
    /**
     * Elimina una transacción por su ID.
     *
     * @param id identificador de la transacción
     */
    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
    /**
     * Obtiene todas las transacciones asociadas a una cita.
     *
     * @param idCita identificador de la cita
     * @return lista de transacciones de la cita
     */
    @Override
    public List<Transaccion> findByCita(Long idCita) {
        return repository.findByCitaIdCita(idCita);
    }
}