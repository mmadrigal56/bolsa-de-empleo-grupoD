package com.example.bolsadeempleo.data;

import com.example.bolsadeempleo.logic.OferenteHabilidad;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OferenteHabilidadRepository extends  CrudRepository<OferenteHabilidad, Integer> {

}
