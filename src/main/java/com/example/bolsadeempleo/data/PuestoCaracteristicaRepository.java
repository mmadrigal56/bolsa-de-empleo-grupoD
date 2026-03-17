package com.example.bolsadeempleo.data;

import com.example.bolsadeempleo.logic.caracteristica.Caracteristica;
import com.example.bolsadeempleo.logic.puesto.Puesto;
import com.example.bolsadeempleo.logic.puestoCaracteristica.PuestoCaracteristica;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PuestoCaracteristicaRepository extends CrudRepository<PuestoCaracteristica, Integer> {
    List<PuestoCaracteristica> findByPuesto(Puesto puesto);
    Optional<PuestoCaracteristica> findByPuestoAndCaracteristica(Puesto puesto, Caracteristica caracteristica);
}
