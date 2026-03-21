package com.example.bolsadeempleo.logic.postulacion;

import com.example.bolsadeempleo.data.*;
import com.example.bolsadeempleo.logic.oferente.Oferente;
import com.example.bolsadeempleo.logic.postulacion.Postulacion;
import com.example.bolsadeempleo.logic.puesto.Puesto;
import org.springframework.beans.factory.annotation.*;

import java.time.LocalDate;

@org.springframework.stereotype.Service
public class ServicePO {
    @Autowired
    private PostulacionRepository postulacionRepository;

    public Iterable<Postulacion> puestosFindAll () {
        return postulacionRepository.findAll();
    }

    public void postular(Oferente oferente, Puesto puesto) {
        if (yaPostulado(oferente, puesto)) return;

        Postulacion p = new Postulacion();
        p.setOferente(oferente);
        p.setPuesto(puesto);
        p.setFechaPostulacion(LocalDate.now());
        p.setEstado("PENDIENTE");
        postulacionRepository.save(p);
    }

    public boolean yaPostulado(Oferente oferente, Puesto puesto) {
        return postulacionRepository.findByOferenteAndPuesto(oferente, puesto).isPresent();
    }
}
