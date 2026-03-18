package com.example.bolsadeempleo.data;

import com.example.bolsadeempleo.logic.empresa.Empresa;
import com.example.bolsadeempleo.logic.puesto.Puesto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuestoRepository extends CrudRepository<Puesto, Integer> {
    List<Puesto> findByEmpresa(Empresa empresa);
    List<Puesto> findTop5ByEsPublicoTrueAndActivoTrueOrderByFechaRegistroDesc();
}
