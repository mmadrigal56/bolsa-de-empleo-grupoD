package com.example.bolsadeempleo.logic.caracteristica;

import com.example.bolsadeempleo.data.*;
import org.springframework.beans.factory.annotation.*;

@org.springframework.stereotype.Service
public class ServiceC {
    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    public Iterable<Caracteristica> caracteristicaFindAll () {
        return caracteristicaRepository.findAll();
    }


}
