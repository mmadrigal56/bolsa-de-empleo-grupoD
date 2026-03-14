package com.example.bolsadeempleo.logic.administrador;

import com.example.bolsadeempleo.data.*;
import com.example.bolsadeempleo.logic.empresa.Empresa;
import com.example.bolsadeempleo.logic.oferente.Oferente;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@org.springframework.stereotype.Service
public class ServiceA {
    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private OferenteRepository oferenteRepository;

    public Iterable<Administrador> administradorFindAll () {
        return administradorRepository.findAll();
    }

    public Object findUserByEmailAndPassword(String correo, String clave) {
        Empresa empresa = empresaRepository.findByCorreo(correo);
        if (empresa != null && passwordEncoder.matches(clave, empresa.getClave())) {
            return empresa;
        }

        Oferente oferente = oferenteRepository.findByCorreo(correo);
        if (oferente != null && passwordEncoder.matches(clave, oferente.getClave())) {
            return oferente;
        }

        Administrador administrador = administradorRepository.findByCorreo(correo);
        if (administrador != null && passwordEncoder.matches(clave, administrador.getClave())) {
            return administrador;
        }
        return null;
    }

    public void registrarAdministrador(Administrador administrador) {
        administrador.setClave(passwordEncoder.encode(administrador.getClave()));
        administradorRepository.save(administrador);
    }
}
