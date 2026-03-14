package com.example.bolsadeempleo.logic.empresa;

import com.example.bolsadeempleo.data.*;
import org.springframework.beans.factory.annotation.*;

@org.springframework.stereotype.Service
public class ServiceE {
    @Autowired
    private EmpresaRepository empresaRepository;

    public Iterable<Empresa> empresaFindAll () {
        return empresaRepository.findAll();
    }
}
