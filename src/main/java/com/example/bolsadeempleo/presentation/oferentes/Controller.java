package com.example.bolsadeempleo.presentation.oferentes;

import com.example.bolsadeempleo.logic.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller("oferentes")
public class Controller {
    @Autowired
    private Service service;

    @GetMapping("/presentation/oferentes/show")
    public String show(Model model) {
        model.addAttribute("oferentes",service.oferentesFindAll());
        return "/presentation/oferentes/View";
    }

    @GetMapping("/presentation/oferentes/habilidades")
    public String habilidades(Model model) {
        model.addAttribute("oferenteHabilidad",service.oferenteHabilidadFindAll());
        return "/presentation/oferentes/ViewParaHabilidadesDelOferente";
    }


}