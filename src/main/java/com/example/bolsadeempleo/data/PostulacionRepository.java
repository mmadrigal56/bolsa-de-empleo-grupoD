package com.example.bolsadeempleo.data;
import com.example.bolsadeempleo.logic.postulacion.Postulacion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostulacionRepository extends CrudRepository<Postulacion, Integer> {

}
