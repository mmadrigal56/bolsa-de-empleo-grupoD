package com.example.bolsadeempleo.presentation.administrador;

import com.example.bolsadeempleo.logic.administrador.ServiceA;
import com.example.bolsadeempleo.logic.administrador.Administrador;
import com.example.bolsadeempleo.logic.caracteristica.Caracteristica;
import com.example.bolsadeempleo.logic.caracteristica.ServiceC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@org.springframework.stereotype.Controller("administrador")
public class Controller {
    //El admin controla el flujo de las operaciones de características.
    @Autowired
    private ServiceA service;

    @Autowired
    private ServiceC serviceC;

    private boolean esAdmin(HttpSession session) {
        Object u = session.getAttribute("usuario");
        return (u instanceof Administrador);
    }
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

        model.addAttribute("caracteristicas", serviceC.caracteristicaFindAll());
        model.addAttribute("usuario", usuario);
        return "/presentation/administrador/ViewCaracteristicasAdmin";
    }

    @GetMapping("/admin/caracteristicas")
    public String caracteristicas(
            @RequestParam(required = false) Integer actualId,
            HttpSession session,
            Model model) {

        if (!esAdmin(session)) return "redirect:/";
        cargarModeloCaracteristicas(actualId, model, session, null);
        return "presentation/administrador/ViewCaracteristicasAdmin";
    }

    @PostMapping("/admin/caracteristicas")
    public String crearCaracteristica(
            @RequestParam String nombre,
            @RequestParam(required = false) Integer padreId,
            @RequestParam(required = false) Integer actualId,
            HttpSession session,
            Model model) {

        if (!esAdmin(session)) return "redirect:/";

        try {
            serviceC.crearCaracteristica(nombre, padreId);
        } catch (IllegalArgumentException e) {
            cargarModeloCaracteristicas(actualId, model, session, e.getMessage());
            return "presentation/administrador/ViewCaracteristicasAdmin";
        }

        if (actualId != null) {
            return "redirect:/admin/caracteristicas?actualId=" + actualId;
        }
        return "redirect:/admin/caracteristicas";
    }


    private void cargarModeloCaracteristicas(Integer actualId, Model model,
                                             HttpSession session, String error) {
        Caracteristica actual = serviceC.findById(actualId);

        List<Caracteristica> categorias = (actual == null)
                ? serviceC.findRoots()
                : serviceC.findHijos(actual);

        List<Caracteristica> ruta        = serviceC.buildRuta(actual);
        List<Caracteristica> opcionesPadre = categorias;

        model.addAttribute("usuario", session.getAttribute("usuario"));
        model.addAttribute("actual", actual);
        model.addAttribute("categorias", categorias);
        model.addAttribute("ruta", ruta);
        model.addAttribute("opcionesPadre", opcionesPadre);

        if (error != null) {
            model.addAttribute("error", error);
        }
    }
}
