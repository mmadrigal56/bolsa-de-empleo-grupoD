package com.example.bolsadeempleo.data;

import com.example.bolsadeempleo.logic.Caracteristica;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaracteristicaRepository extends CrudRepository<Caracteristica, Integer> {

}
