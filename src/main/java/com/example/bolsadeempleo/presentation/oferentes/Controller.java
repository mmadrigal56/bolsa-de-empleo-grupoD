package com.example.bolsadeempleo.presentation.oferentes;

import com.example.bolsadeempleo.logic.oferente.ServiceO;
import com.example.bolsadeempleo.logic.oferente.Oferente;
import com.example.bolsadeempleo.logic.oferenteHabilidad.OferenteHabilidad;
import com.example.bolsadeempleo.logic.oferenteHabilidad.ServiceOH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;


@org.springframework.stereotype.Controller("oferentes")
public class Controller {
    @Autowired
    private ServiceO serviceO;

    private ServiceOH serviceOH;

    @GetMapping("/presentation/oferentes/show")
    public String show(Model model, HttpSession session)
    {
        Object usuario = session.getAttribute("usuario");
        if (usuario == null || !(usuario instanceof Oferente)) {
            return "redirect:/";
        }

        model.addAttribute("oferentes",serviceO.oferentesFindAll());
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
        model.addAttribute("oferenteHabilidad",serviceOH.oferenteHabilidadFindAll());
        model.addAttribute("usuario", usuario);

        return "/presentation/oferentes/ViewParaHabilidadesDelOferente";
    }


}

