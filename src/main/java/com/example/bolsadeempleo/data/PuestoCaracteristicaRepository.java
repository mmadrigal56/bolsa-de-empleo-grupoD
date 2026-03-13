package com.example.bolsadeempleo.data;

import com.example.bolsadeempleo.logic.PuestoCaracteristica;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PuestoCaracteristicaRepository extends CrudRepository<PuestoCaracteristica, Integer> {

}
