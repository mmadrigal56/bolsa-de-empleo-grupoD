package com.example.bolsadeempleo.logic.oferente;

import com.example.bolsadeempleo.data.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@org.springframework.stereotype.Service
public class ServiceO {
    @Autowired
    private OferenteRepository oferenteRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Iterable<Oferente> oferentesFindAll() {
        return oferenteRepository.findAll();
    }

    public void registrarOferente(Oferente oferente) {
        oferente.setClave(passwordEncoder.encode(oferente.getClave()));
        oferenteRepository.save(oferente);
    }
}
