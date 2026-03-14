package com.example.bolsadeempleo.logic.puestoCaracteristica;

import com.example.bolsadeempleo.data.*;
import org.springframework.beans.factory.annotation.*;

@org.springframework.stereotype.Service
public class ServicePC {
    @Autowired
    private PuestoCaracteristicaRepository puestoCaracteristicaRepository;

    public Iterable<PuestoCaracteristica> puestoCaracteristicaFindAll () {
        return puestoCaracteristicaRepository.findAll();
    }
}
