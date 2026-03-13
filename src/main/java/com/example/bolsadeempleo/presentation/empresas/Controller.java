package com.example.bolsadeempleo.presentation.empresas;

import com.example.bolsadeempleo.logic.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller("empresas")
public class Controller {
    @Autowired
    private Service service;

    @GetMapping("/presentation/empresas/show")
    public String show(Model model) {
        model.addAttribute("empresas",service.empresaFindAll());
        return "/presentation/empresas/View";
    }

    @GetMapping("/presentation/empresas/puestos")
    public String puestos(Model model) {
        model.addAttribute("puestos",service.puestosFindAll());
        return "/presentation/empresas/viewPuestos";
    }

    @GetMapping("/presentation/empresas/requisitos")
    public String requisitos(Model model) {
        model.addAttribute("puestoCaracteristica",service.puestoCaracteristicaFindAll());
        return "/presentation/empresas/ViewPuestoCaracteristica";
    }


}