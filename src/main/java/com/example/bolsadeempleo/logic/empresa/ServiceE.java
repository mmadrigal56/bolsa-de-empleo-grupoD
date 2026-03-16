package com.example.bolsadeempleo.logic.empresa;

import com.example.bolsadeempleo.data.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;

@org.springframework.stereotype.Service
public class ServiceE {
    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Iterable<Empresa> empresaFindAll () {
        return empresaRepository.findAll();
    }

    public void registrarEmpresa(Empresa empresa) {
        empresa.setClave(passwordEncoder.encode(empresa.getClave()));
        empresaRepository.save(empresa);
    }

    public List<Empresa> findPendientes() {
        return empresaRepository.findByAutorizadaFalse();
    }

    public void aprobarEmpresa(int id)
    {
        empresaRepository.findById(id).ifPresent(empresa -> {
            empresa.setAutorizada(true);
            empresaRepository.save(empresa);
        });
    }

}
