package com.example.bolsadeempleo.logic.puesto;

import com.example.bolsadeempleo.data.*;
import com.example.bolsadeempleo.logic.caracteristica.Caracteristica;
import com.example.bolsadeempleo.logic.empresa.Empresa;
import com.example.bolsadeempleo.logic.puestoCaracteristica.PuestoCaracteristica;
import org.springframework.beans.factory.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceP {
    @Autowired
    private PuestoRepository puestoRepository;

    @Autowired
    private PuestoCaracteristicaRepository puestoCaracteristicaRepository;

    public Iterable<Puesto> puestosFindAll () {
        return puestoRepository.findAll();
    }

    public List<Puesto> findByEmpresa(Empresa empresa) {
        return puestoRepository.findByEmpresa(empresa);
    }

    public Optional<Puesto> findById(Integer id) {
        return puestoRepository.findById(id);
    }

    public Puesto crearPuesto(Empresa empresa, String nombre, String descripcion, Double salario, Boolean esPublico, String moneda)
    {
        Puesto p = new Puesto();
        p.setEmpresa(empresa);
        p.setNombre(nombre.trim());
        p.setDescripcion(descripcion.trim());
        p.setSalario(salario);
        p.setEsPublico(esPublico);
        p.setMoneda(moneda != null ? moneda : "CRC");
        p.setActivo(true);
        p.setFechaRegistro(LocalDate.now());
        return puestoRepository.save(p);
    }

    public void desactivarPuesto(Integer id) {
        puestoRepository.findById(id).ifPresent(p -> {
            p.setActivo(false);
            puestoRepository.save(p);
        });
    }

    public List<PuestoCaracteristica> findRequisitosByPuesto(Puesto puesto) {
        return puestoCaracteristicaRepository.findByPuesto(puesto);
    }

    public void agregarOActualizarRequisito(Puesto puesto, Caracteristica c, int nivel) {
        Optional<PuestoCaracteristica> existente = puestoCaracteristicaRepository.findByPuestoAndCaracteristica(puesto, c);

        PuestoCaracteristica pc = existente.orElse(new PuestoCaracteristica());
        pc.setPuesto(puesto);
        pc.setCaracteristica(c);
        pc.setNivel(nivel);
        puestoCaracteristicaRepository.save(pc);
    }

    public void quitarRequisito(Integer pcId) {
        puestoCaracteristicaRepository.deleteById(pcId);
    }
}
