package com.example.bolsadeempleo.data;

import com.example.bolsadeempleo.logic.Administrador;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorRepository extends CrudRepository<Administrador,Integer> {

}
