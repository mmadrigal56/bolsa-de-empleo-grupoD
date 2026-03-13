package com.example.bolsadeempleo.presentation.oferentes;

import com.example.bolsadeempleo.logic.Empresa;
import com.example.bolsadeempleo.logic.Service;
import com.example.bolsadeempleo.logic.Oferente;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import jakarta.servlet.http.HttpSession;


@org.springframework.stereotype.Controller("oferentes")
public class Controller {
    @Autowired
    private Service service;

    @GetMapping("/presentation/oferentes/show")
    public String show(Model model, HttpSession session)
    {
        Object usuario = session.getAttribute("usuario");
        if (usuario == null || !(usuario instanceof Oferente)) {
            return "redirect:/";
        }

        model.addAttribute("oferentes",service.oferentesFindAll());
        model.addAttribute("usuario", usuario);
        
        return "/presentation/oferentes/View";
    }

    @GetMapping("/presentation/oferentes/habilidades")
    public String habilidades(Model model, HttpSession session)
    {
        Object usuario = session.getAttribute("usuario");
        if (usuario == null || !(usuario instanceof Oferente)) {
            return "redirect:/";
        }
        model.addAttribute("oferenteHabilidad",service.oferenteHabilidadFindAll());
        model.addAttribute("usuario", usuario);

        return "/presentation/oferentes/ViewParaHabilidadesDelOferente";
    }


}

