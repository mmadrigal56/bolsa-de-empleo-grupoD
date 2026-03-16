package com.example.bolsadeempleo.logic.oferente;

import com.example.bolsadeempleo.data.*;
import com.example.bolsadeempleo.logic.empresa.Empresa;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

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

    public void actualizarOferente(Oferente oferente) {
        oferenteRepository.save(oferente);
    }

    public List<Oferente> findPendientes() {
        return oferenteRepository.findByAutorizadoFalse();
    }

    public void aprobarOferente(int id)
    {
        oferenteRepository.findById(id).ifPresent(oferente -> {
            oferente.setAutorizado(true);
            oferenteRepository.save(oferente);
        });
    }



}
