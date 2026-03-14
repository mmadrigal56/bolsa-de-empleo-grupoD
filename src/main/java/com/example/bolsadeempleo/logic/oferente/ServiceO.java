package com.example.bolsadeempleo.logic.oferente;

import com.example.bolsadeempleo.data.*;
import org.springframework.beans.factory.annotation.*;

@org.springframework.stereotype.Service
public class ServiceO {
    @Autowired
    private OferenteRepository ofrerenteRepository;

    public Iterable<Oferente> oferentesFindAll() {
        return ofrerenteRepository.findAll();
    }
}
