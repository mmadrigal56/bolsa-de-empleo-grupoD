package com.example.bolsadeempleo.logic.puesto;

import com.example.bolsadeempleo.data.*;
import com.example.bolsadeempleo.logic.caracteristica.Caracteristica;
import com.example.bolsadeempleo.logic.caracteristica.ServiceC;
import com.example.bolsadeempleo.logic.empresa.Empresa;
import com.example.bolsadeempleo.logic.oferente.Oferente;
import com.example.bolsadeempleo.logic.puestoCaracteristica.PuestoCaracteristica;
import org.springframework.beans.factory.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@org.springframework.stereotype.Service
public class ServiceP {
    @Autowired
    private PuestoRepository puestoRepository;

    @Autowired
    private PuestoCaracteristicaRepository puestoCaracteristicaRepository;

    @Autowired
    private OferenteRepository oferenteRepository;

    @Autowired
    private OferenteHabilidadRepository oferenteHabilidadRepository;

    @Autowired
    private ServiceC serviceC;

    public Iterable<Puesto> puestosFindAll () {
        return puestoRepository.findAll();
    }

    public List<Puesto> findByEmpresa(Empresa empresa) {
        return puestoRepository.findByEmpresa(empresa);
    }

    public Optional<Puesto> findById(Integer id) {
        return puestoRepository.findById(id);
    }

//    public List<Puesto> getUltimosPuestosPublicos() {
//        return puestoRepository.findTop5ByEsPublicoTrueAndActivoTrueOrderByFechaRegistroDesc();
//    }

    public List<Puesto> getUltimosPuestosPublicos() {
        return puestoRepository.findTop5ByEsPublicoTrueAndActivoTrueOrderByFechaRegistroDescIdDesc();
    }


    public List<Puesto> findAllActivos() {
        return puestoRepository.findByActivoTrueOrderByFechaRegistroDescIdDesc();
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


    public List<Puesto> buscarPuestosPublicos(List<Integer> caracteristicaIds, String moneda) {
        if (caracteristicaIds == null || caracteristicaIds.isEmpty())
            return new ArrayList<>();

        List<Integer> expandidos = serviceC.expandirConDescendientes(caracteristicaIds);
        List<Puesto> resultados = puestoRepository.findDistinctByEsPublicoTrueAndActivoTrueAndRequisitosCaracteristicaIdIn(expandidos);

        if (moneda == null || moneda.isBlank()) return resultados;

        return resultados.stream().filter(p -> p.getMoneda().equals(moneda)).collect(toList());
    }

    public List<Puesto> buscarPuestosParaOferente(List<Integer> caracteristicaIds, String moneda) {
        if (caracteristicaIds == null || caracteristicaIds.isEmpty())
            return new ArrayList<>();

        List<Integer> expandidos = serviceC.expandirConDescendientes(caracteristicaIds);
        List<Puesto> resultados = puestoRepository.findDistinctByActivoTrueAndRequisitosCaracteristicaIdIn(expandidos);

        if (moneda == null || moneda.isBlank()) return resultados;

        return resultados.stream().filter(p -> p.getMoneda().equals(moneda)).collect(toList());
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

    public List<CandidatoResultado> buscarCandidatos(Puesto puesto) {
        List<PuestoCaracteristica> requisitos = puestoCaracteristicaRepository.findByPuesto(puesto);
        List<CandidatoResultado> resultados = new ArrayList<>();

        for (Oferente oferente : oferenteRepository.findAll()) {
            if (!oferente.getAutorizado()) continue;

            int cumplidos = 0;
            for (PuestoCaracteristica req : requisitos) {
                var habilidad = oferenteHabilidadRepository
                        .findByOferenteAndCaracteristica(oferente, req.getCaracteristica());
                if (habilidad.isPresent() && habilidad.get().getNivel() >= req.getNivel()) {
                    cumplidos++;
                }
            }

            if (cumplidos > 0) {
                resultados.add(new CandidatoResultado(oferente, cumplidos, requisitos.size()));
            }
        }

        resultados.sort((a, b) -> Double.compare(b.getPorcentaje(), a.getPorcentaje()));
        return resultados;
    }
}
