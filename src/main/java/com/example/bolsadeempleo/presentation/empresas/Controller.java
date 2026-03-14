package com.example.bolsadeempleo.presentation.empresas;

import com.example.bolsadeempleo.logic.empresa.Empresa;
import com.example.bolsadeempleo.logic.empresa.ServiceE;
import com.example.bolsadeempleo.logic.puesto.ServiceP;
import com.example.bolsadeempleo.logic.puestoCaracteristica.ServicePC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@org.springframework.stereotype.Controller("empresas")
public class Controller {
    @Autowired
    private ServiceE service;

    private ServiceP serviceP;

    private ServicePC servicePC;

    @GetMapping("/presentation/empresas/show")
    public String show(Model model, HttpSession session)
    {
        Object usuario = session.getAttribute("usuario");

        if (usuario == null || !(usuario instanceof Empresa))
        {
            return "redirect:/";
        }

        model.addAttribute("empresas",service.empresaFindAll());
        model.addAttribute("usuario", usuario);
        return "/presentation/empresas/View";
    }

    @GetMapping("/presentation/empresas/puestos")
    public String puestos(Model model, HttpSession session)
    {
        Object usuario = session.getAttribute("usuario");
        if (usuario == null || !(usuario instanceof Empresa)) {
            return "redirect:/";
        }

        model.addAttribute("puestos",serviceP.puestosFindAll());
        model.addAttribute("usuario", usuario);

        return "/presentation/empresas/viewPuestos";
    }

    @GetMapping("/presentation/empresas/requisitos")
    public String requisitos(Model model, HttpSession session) {

        Object usuario = session.getAttribute("usuario");
        if (usuario == null || !(usuario instanceof Empresa)) {
            return "redirect:/";
        }

        model.addAttribute("puestoCaracteristica",servicePC.puestoCaracteristicaFindAll());
        model.addAttribute("usuario", usuario);

        return "/presentation/empresas/ViewPuestoCaracteristica";
    }


}

