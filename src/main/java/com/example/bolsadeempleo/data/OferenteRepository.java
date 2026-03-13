package com.example.bolsadeempleo.data;

import com.example.bolsadeempleo.logic.Oferente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OferenteRepository extends CrudRepository<Oferente, Integer> {

}
