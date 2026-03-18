package com.example.bolsadeempleo.presentation.empresas;

import com.example.bolsadeempleo.logic.caracteristica.Caracteristica;
import com.example.bolsadeempleo.logic.caracteristica.ServiceC;
import com.example.bolsadeempleo.logic.empresa.Empresa;
import com.example.bolsadeempleo.logic.empresa.ServiceE;
import com.example.bolsadeempleo.logic.oferente.Oferente;
import com.example.bolsadeempleo.logic.oferente.ServiceO;
import com.example.bolsadeempleo.logic.oferenteHabilidad.ServiceOH;
import com.example.bolsadeempleo.logic.puesto.Puesto;
import com.example.bolsadeempleo.logic.puesto.ServiceP;
import com.example.bolsadeempleo.logic.puestoCaracteristica.PuestoCaracteristica;
import com.example.bolsadeempleo.logic.puestoCaracteristica.ServicePC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toMap;

@org.springframework.stereotype.Controller("empresas")
public class Controller {
    @Autowired
    private ServiceE service;

    @Autowired
    private ServiceP serviceP;

    @Autowired
    private ServicePC servicePC;

    @Autowired
    private ServiceC serviceC;

    @Autowired
    private ServiceO serviceO;

    @Autowired
    private ServiceOH serviceOH;

    private boolean esEmpresa(HttpSession session) {
        return session.getAttribute("usuario") instanceof Empresa;
    }

    @GetMapping("/presentation/empresas/show")
    public String show(HttpSession session, Model model) {
        if (!esEmpresa(session)) return "redirect:/";
        model.addAttribute("usuario", session.getAttribute("usuario"));
        return "presentation/empresas/ViewDashboard";
    }

    @GetMapping("/empresa/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!esEmpresa(session)) return "redirect:/";
        model.addAttribute("usuario", session.getAttribute("usuario"));
        return "presentation/empresas/ViewDashboard";
    }

    @GetMapping("/empresa/puestos")
    public String misPuestos(HttpSession session, Model model) {
        if (!esEmpresa(session)) return "redirect:/";
        Empresa empresa = (Empresa) session.getAttribute("usuario");
        model.addAttribute("usuario", empresa);
        model.addAttribute("puestos", serviceP.findByEmpresa(empresa));
        return "presentation/empresas/viewPuestos";
    }

    @PostMapping("/empresa/puestos/{id}/desactivar")
    public String desactivarPuesto(@PathVariable Integer id, HttpSession session) {
        if (!esEmpresa(session)) return "redirect:/";
        Empresa empresa = (Empresa) session.getAttribute("usuario");
        serviceP.findById(id)
                .filter(p -> p.getEmpresa().getId().equals(empresa.getId()))
                .ifPresent(p -> serviceP.desactivarPuesto(p.getId()));
        return "redirect:/empresa/puestos";
    }


    @GetMapping("/presentation/empresas/puestos")
    public String puestos(Model model, HttpSession session) {
        Object usuario = session.getAttribute("usuario");
        if (usuario == null || !(usuario instanceof Empresa)) {
            return "redirect:/";
        }

        model.addAttribute("puestos", serviceP.puestosFindAll());
        model.addAttribute("usuario", usuario);

        return "/presentation/empresas/viewPuestos";
    }

    @GetMapping("/presentation/empresas/requisitos")
    public String requisitos(Model model, HttpSession session) {

        Object usuario = session.getAttribute("usuario");
        if (usuario == null || !(usuario instanceof Empresa)) {
            return "redirect:/";
        }

        model.addAttribute("puestoCaracteristica", servicePC.puestoCaracteristicaFindAll());
        model.addAttribute("usuario", usuario);

        return "/presentation/empresas/ViewPuestoCaracteristica";
    }

//    Puestos.

    @GetMapping("/empresa/puestos/nuevo")
    public String formNuevoPuesto(Model model, HttpSession session) {
        if (!esEmpresa(session)) return "redirect:/";
        model.addAttribute("usuario", session.getAttribute("usuario"));
        return "presentation/empresas/ViewNuevoPuesto";
    }

    @PostMapping("/empresa/puestos/nuevo")
    public String guardarNuevoPuesto(
            @RequestParam String nombre,
            @RequestParam String descripcion,
            @RequestParam String salario,
            @RequestParam String esPublico,
            @RequestParam String moneda,
            HttpSession session, Model model) {

        if (!esEmpresa(session)) return "redirect:/";
        Empresa empresa = (Empresa) session.getAttribute("usuario");

        String error = null;
        Double salarioDouble = null;

        if (nombre == null || nombre.trim().isEmpty())
            error = "El nombre del puesto no puede estar vacío.";

        if (error == null && (descripcion == null || descripcion.trim().length() < 10))
            error = "La descripción debe tener al menos 10 caracteres.";

        if (error == null) {
            try {
                salarioDouble = Double.parseDouble(salario.trim());
                if (salarioDouble <= 0) error = "El salario debe ser mayor a 0.";
            } catch (NumberFormatException e) {
                error = "El salario debe ser un número válido (ej: 1500000).";
            }
        }

        if (error == null && (esPublico == null || esPublico.isBlank()))
            error = "Debe seleccionar si el puesto es público o privado.";

        if (error != null) {
            model.addAttribute("usuario", empresa);
            model.addAttribute("error", error);
            model.addAttribute("tempNombre", nombre);
            model.addAttribute("tempDesc", descripcion);
            model.addAttribute("tempSalario", salario);
            model.addAttribute("tempPublico", esPublico);
            model.addAttribute("moneda", moneda);
            return "presentation/empresas/ViewNuevoPuesto";
        }

        Puesto puesto = serviceP.crearPuesto(empresa, nombre, descripcion,
                salarioDouble, "true".equals(esPublico), moneda);
        return "redirect:/empresa/puestos/" + puesto.getId() + "/requisitos";
    }

    @GetMapping("/empresa/puestos/{id}/requisitos")
    public String requisitos(@PathVariable Integer id, @RequestParam(required = false) Integer actualId, HttpSession session, Model model)
    {
        if (!esEmpresa(session)) return "redirect:/";
        Empresa empresa = (Empresa) session.getAttribute("usuario");

        Puesto puesto = serviceP.findById(id).filter(p -> p.getEmpresa().getId().equals(empresa.getId())).orElse(null);
        if (puesto == null) return "redirect:/empresa/dashboard";


        Caracteristica actual = serviceC.findById(actualId);
        List<Caracteristica> categorias = (actual == null) ? serviceC.findRoots() : serviceC.findHijos(actual);
        List<PuestoCaracteristica> requisitos = serviceP.findRequisitosByPuesto(puesto);

        model.addAttribute("usuario", empresa);
        model.addAttribute("puesto", puesto);
        model.addAttribute("actual", actual);
        model.addAttribute("categorias", categorias);
        model.addAttribute("ruta", serviceC.buildRuta(actual));
        model.addAttribute("requisitos", requisitos);
        model.addAttribute("rutasRequisitos", requisitos.stream().collect(toMap(r -> r.getId(), r -> serviceC.buildRutaString(r.getCaracteristica()))));
        return "presentation/empresas/ViewRequisitos";
    }

    @PostMapping("/empresa/puestos/{id}/requisitos/agregar")
    public String agregarRequisito(@PathVariable Integer id, @RequestParam Integer caracteristicaId, @RequestParam int nivel, @RequestParam(required = false) Integer actualId, HttpSession session)
    {
        if (!esEmpresa(session)) return "redirect:/";
        Empresa empresa = (Empresa) session.getAttribute("usuario");

        serviceP.findById(id)
                .filter(p -> p.getEmpresa().getId().equals(empresa.getId()))
                .ifPresent(puesto -> {
                    Caracteristica c = serviceC.findById(caracteristicaId);
                    if (c != null && nivel >= 1 && nivel <= 5)
                        serviceP.agregarOActualizarRequisito(puesto, c, nivel);
                });

        String redirect = "/empresa/puestos/" + id + "/requisitos";
        if (actualId != null) redirect += "?actualId=" + actualId;
        return "redirect:" + redirect;
    }

    @PostMapping("/empresa/puestos/{id}/requisitos/quitar")
    public String quitarRequisito(@PathVariable Integer id, @RequestParam Integer pcId, @RequestParam(required = false) Integer actualId, HttpSession session)
    {
        if (!esEmpresa(session)) return "redirect:/";
        serviceP.quitarRequisito(pcId);

        String redirect = "/empresa/puestos/" + id + "/requisitos";
        if (actualId != null) redirect += "?actualId=" + actualId;
        return "redirect:" + redirect;
    }

   //End point para lo de carcaterísticas en puestos vacíos.
    @PostMapping("/empresa/puestos/{id}/finalizar")
    public String finalizarPuesto(@PathVariable Integer id, HttpSession session, Model model)
    {
        if (!esEmpresa(session)) return "redirect:/";
        Empresa empresa = (Empresa) session.getAttribute("usuario");

        Puesto puesto = serviceP.findById(id)
                .filter(p -> p.getEmpresa().getId().equals(empresa.getId()))
                .orElse(null);
        if (puesto == null) return "redirect:/empresa/dashboard";

        List<PuestoCaracteristica> requisitos = serviceP.findRequisitosByPuesto(puesto);

        if (requisitos.isEmpty()) {
            Caracteristica actual = serviceC.findById(null);
            List<Caracteristica> categorias = serviceC.findRoots();

            model.addAttribute("usuario", empresa);
            model.addAttribute("puesto", puesto);
            model.addAttribute("actual", null);
            model.addAttribute("categorias", categorias);
            model.addAttribute("ruta", serviceC.buildRuta(null));
            model.addAttribute("requisitos", requisitos);
            model.addAttribute("rutasRequisitos", new HashMap<>());
            model.addAttribute("error", "Debe agregar al menos una característica antes de publicar el puesto.");
            return "presentation/empresas/ViewRequisitos";
        }

        return "redirect:/empresa/dashboard";
    }

    @GetMapping("/empresa/candidatos/buscar")
    public String buscarCandidatos(@RequestParam Integer puestoId,
                                   HttpSession session, Model model) {
        if (!esEmpresa(session)) return "redirect:/";
        Empresa empresa = (Empresa) session.getAttribute("usuario");

        Puesto puesto = serviceP.findById(puestoId)
                .filter(p -> p.getEmpresa().getId().equals(empresa.getId()))
                .orElse(null);
        if (puesto == null) return "redirect:/empresa/puestos";

        model.addAttribute("usuario", empresa);
        model.addAttribute("puesto", puesto);
        model.addAttribute("candidatos", serviceP.buscarCandidatos(puesto));
        return "presentation/empresas/ViewCandidatos";
    }

    @GetMapping("/empresa/candidatos/{oferenteId}")
    public String verDetalleCandidato(@PathVariable Integer oferenteId,
                                      @RequestParam Integer puestoId,
                                      HttpSession session, Model model) {
        if (!esEmpresa(session)) return "redirect:/";
        Empresa empresa = (Empresa) session.getAttribute("usuario");

        Puesto puesto = serviceP.findById(puestoId)
                .filter(p -> p.getEmpresa().getId().equals(empresa.getId()))
                .orElse(null);
        if (puesto == null) return "redirect:/empresa/puestos";

        Oferente oferente = serviceO.findById(oferenteId);
        if (oferente == null) return "redirect:/empresa/candidatos/buscar?puestoId=" + puestoId;

        model.addAttribute("usuario", empresa);
        model.addAttribute("puesto", puesto);
        model.addAttribute("oferente", oferente);
        model.addAttribute("habilidades", serviceOH.findByOferente(oferente));
        return "presentation/empresas/ViewDetalleCandidato";
    }
}




