package com.example.bolsadeempleo.data;

import com.example.bolsadeempleo.logic.puesto.Puesto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PuestoRepository extends CrudRepository<Puesto, Integer> {

}
