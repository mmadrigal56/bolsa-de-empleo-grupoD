package com.example.bolsadeempleo.logic.puesto;

import com.example.bolsadeempleo.data.*;
import org.springframework.beans.factory.annotation.*;

@org.springframework.stereotype.Service
public class ServiceP {
    @Autowired
    private PuestoRepository puestoRepository;

    public Iterable<Puesto> puestosFindAll () {
        return puestoRepository.findAll();
    }
}
