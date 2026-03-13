package com.example.bolsadeempleo.logic;

import com.example.bolsadeempleo.data.*;
import org.springframework.beans.factory.annotation.*;

//Para pruebas por el momento.
@org.springframework.stereotype.Service("service")
public class Service {
    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    @Autowired
    private AdministradorRepository administradorRepository ;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private OferenteHabilidadRepository oferenteHabilidadRepository;

    @Autowired
    private OferenteRepository ofrerenteRepository;

    @Autowired
    private PuestoCaracteristicaRepository puestoCaracteristicaRepository;

    @Autowired
    private PuestoRepository puestoRepository;

    public Iterable<Caracteristica> caracteristicaFindAll () {
        return caracteristicaRepository.findAll();
    }

    public Iterable<Administrador> administradorFindAll () {
        return administradorRepository.findAll();
    }

    public Iterable<Empresa> empresaFindAll () {
        return empresaRepository.findAll();
    }

    public Iterable<OferenteHabilidad> oferenteHabilidadFindAll () {
        return oferenteHabilidadRepository.findAll();
    }

    public Iterable<Oferente> oferentesFindAll() {
        return ofrerenteRepository.findAll();
    }

    public Iterable<PuestoCaracteristica> puestoCaracteristicaFindAll () {
        return puestoCaracteristicaRepository.findAll();
    }

    public Iterable<Puesto> puestosFindAll () {
        return puestoRepository.findAll();
    }

    public Object findUserByEmailAndPassword(String correo, String clave) {
        Administrador admin = administradorRepository.findAdministradorByCorreoAndClave(correo, clave);
        if (admin != null) return admin;

        Empresa empresa = empresaRepository.findEmpresaByCorreoAndClave(correo, clave);
        if (empresa != null) return empresa;

        Oferente oferente = ofrerenteRepository.findOferenteByCorreoAndClave(correo, clave);
        if (oferente != null) return oferente;

        return null;  
    }

}

