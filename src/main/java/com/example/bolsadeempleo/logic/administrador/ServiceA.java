package com.example.bolsadeempleo.logic.administrador;

import com.example.bolsadeempleo.data.*;
import com.example.bolsadeempleo.logic.empresa.Empresa;
import com.example.bolsadeempleo.logic.oferente.Oferente;
import org.springframework.beans.factory.annotation.*;

@org.springframework.stereotype.Service
public class ServiceA {
    @Autowired
    private AdministradorRepository administradorRepository;

    private EmpresaRepository empresaRepository;

    private OferenteRepository oferenteRepository;

    public Iterable<Administrador> administradorFindAll () {
        return administradorRepository.findAll();
    }

    public Object findUserByEmailAndPassword(String correo, String clave) {
        Administrador admin = administradorRepository.findAdministradorByCorreoAndClave(correo, clave);
        if (admin != null) return admin;

        Empresa empresa = empresaRepository.findEmpresaByCorreoAndClave(correo, clave);
        if (empresa != null) return empresa;

        Oferente oferente = oferenteRepository.findOferenteByCorreoAndClave(correo, clave);
        if (oferente != null) return oferente;

        return null;
    }
}
