package com.example.bolsadeempleo.logic.oferenteHabilidad;

import com.example.bolsadeempleo.data.*;
import org.springframework.beans.factory.annotation.*;

@org.springframework.stereotype.Service
public class ServiceOH {
    @Autowired
    private OferenteHabilidadRepository oferenteHabilidadRepository;

    public Iterable<OferenteHabilidad> oferenteHabilidadFindAll () {
        return oferenteHabilidadRepository.findAll();
    }


}
