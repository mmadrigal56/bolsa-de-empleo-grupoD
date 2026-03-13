package com.example.bolsadeempleo.presentation.administrador;

import com.example.bolsadeempleo.logic.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller("administrador")
public class Controller {
    //El admin controla el flujo de las operaciones de características.
    @Autowired
    private Service service;

    @GetMapping("/presentation/administrador/show")
    public String show(Model model) {
        model.addAttribute("administrador",service.administradorFindAll());
        return "/presentation/administrador/View";
    }

    @GetMapping("/presentation/administrador/caracteristicas")
    public String caracteristicas(Model model) {
        model.addAttribute("caracteristicas",service.caracteristicaFindAll());
        return "/presentation/administrador/ViewParaCaracteristicas";
    }


}
