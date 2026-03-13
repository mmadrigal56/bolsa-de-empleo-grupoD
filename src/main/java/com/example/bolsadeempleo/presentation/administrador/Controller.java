package com.example.bolsadeempleo.presentation.administrador;

import com.example.bolsadeempleo.logic.Service;
import com.example.bolsadeempleo.logic.Administrador;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import jakarta.servlet.http.HttpSession;

@org.springframework.stereotype.Controller("administrador")
public class Controller {
    //El admin controla el flujo de las operaciones de características.
    @Autowired
    private Service service;

    @GetMapping("/presentation/administrador/show")
    public String show(Model model, HttpSession session) {
        Object usuario = session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/";
        }

        if (!(usuario instanceof Administrador)) {
            return "redirect:/";
        }

        model.addAttribute("administrador", service.administradorFindAll());
        model.addAttribute("usuario", usuario);
        return "/presentation/administrador/View";

    }

    @GetMapping("/presentation/administrador/caracteristicas")
    public String caracteristicas(Model model, HttpSession session) {
        Object usuario = session.getAttribute("usuario");
        if (usuario == null || !(usuario instanceof Administrador)) {
            return "redirect:/";
        }

        model.addAttribute("caracteristicas", service.caracteristicaFindAll());
        model.addAttribute("usuario", usuario);
        return "/presentation/administrador/ViewParaCaracteristicas";
    }


}
