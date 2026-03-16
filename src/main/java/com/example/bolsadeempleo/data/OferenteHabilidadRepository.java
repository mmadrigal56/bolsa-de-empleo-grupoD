package com.example.bolsadeempleo.data;

import com.example.bolsadeempleo.logic.caracteristica.Caracteristica;
import com.example.bolsadeempleo.logic.oferente.Oferente;
import com.example.bolsadeempleo.logic.oferenteHabilidad.OferenteHabilidad;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface OferenteHabilidadRepository extends  CrudRepository<OferenteHabilidad, Integer> {
    List<OferenteHabilidad> findByOferente(Oferente oferente);
    Optional<OferenteHabilidad> findByOferenteAndCaracteristica(Oferente oferente, Caracteristica caracteristica);

}
