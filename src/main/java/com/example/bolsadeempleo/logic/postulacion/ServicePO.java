package com.example.bolsadeempleo.logic.postulacion;

import com.example.bolsadeempleo.data.*;
import com.example.bolsadeempleo.logic.postulacion.Postulacion;
import org.springframework.beans.factory.annotation.*;

@org.springframework.stereotype.Service
public class ServicePO {
    @Autowired
    private PostulacionRepository postulacionRepository;

    public Iterable<Postulacion> puestosFindAll () {
        return postulacionRepository.findAll();
    }
}
