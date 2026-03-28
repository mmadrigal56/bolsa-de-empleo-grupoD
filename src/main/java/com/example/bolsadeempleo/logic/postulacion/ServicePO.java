package com.example.bolsadeempleo.logic.postulacion;

import ch.qos.logback.core.model.Model;
import com.example.bolsadeempleo.data.*;
import com.example.bolsadeempleo.logic.empresa.Empresa;
import com.example.bolsadeempleo.logic.oferente.Oferente;
import com.example.bolsadeempleo.logic.postulacion.Postulacion;
import com.example.bolsadeempleo.logic.puesto.Puesto;
import org.springframework.beans.factory.annotation.*;


import java.time.LocalDate;
import java.util.List;

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


    //Cambio.
    public List<Postulacion> findByPuesto(Puesto puesto) { return postulacionRepository.findByPuesto(puesto);}

    public List<Postulacion> findByOferente(Oferente oferente)
    {
        return postulacionRepository.findByOferente(oferente);
    }

}
