package com.example.bolsadeempleo.presentation.oferentes;

import com.example.bolsadeempleo.logic.caracteristica.Caracteristica;
import com.example.bolsadeempleo.logic.caracteristica.ServiceC;
import com.example.bolsadeempleo.logic.oferente.ServiceO;
import com.example.bolsadeempleo.logic.oferente.Oferente;
import com.example.bolsadeempleo.logic.oferenteHabilidad.OferenteHabilidad;
import com.example.bolsadeempleo.logic.oferenteHabilidad.ServiceOH;
import com.example.bolsadeempleo.logic.postulacion.ServicePO;
import com.example.bolsadeempleo.logic.puesto.Puesto;
import com.example.bolsadeempleo.logic.puesto.ServiceP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;
import java.util.HashMap;
import java.util.List;


@org.springframework.stereotype.Controller("oferentes")
public class Controller {
    @Autowired
    private ServiceO serviceO;

    @Autowired
    private ServiceOH serviceOH;

    @Autowired
    private ServiceC serviceC;

    @Autowired
    private ServiceP serviceP;

    @Autowired
    private ServicePO servicePO;

    private boolean esOferente(HttpSession session) {
        return session.getAttribute("usuario") instanceof Oferente;
    }

    @GetMapping("/presentation/oferentes/show")
    public String show(Model model, HttpSession session)
    {
        Object usuario = session.getAttribute("usuario");
        if (usuario == null || !(usuario instanceof Oferente)) {
            return "redirect:/";
        }

        model.addAttribute("oferentes",serviceO.oferentesFindAll());
        model.addAttribute("usuario", usuario);
        
        return "/presentation/oferentes/ViewDashboard";
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

    @GetMapping("/oferente/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof Oferente)) {
            return "redirect:/";
        }
        model.addAttribute("usuario", usuario);
        return "presentation/oferentes/ViewDashboard";
    }

    @GetMapping("/oferente/cv")
    public String verCV(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        if (!(usuario instanceof Oferente)) {
            return "redirect:/";
        }
        model.addAttribute("usuario", usuario);
        return "presentation/oferentes/ViewCV";
    }

    @PostMapping("/oferente/cv")
    public String guardarCV(@RequestParam String rutaCurriculum, HttpSession session) {
        Oferente oferente = (Oferente) session.getAttribute("usuario");
        oferente.setRutaCurriculum(rutaCurriculum);
        serviceO.actualizarOferente(oferente);
        session.setAttribute("usuario", oferente);
        return "redirect:/oferente/cv";
    }

//    Habilidades.

    @GetMapping("/oferente/habilidades")
    public String habilidades(@RequestParam(required = false) Integer actualId,
                              HttpSession session, Model model)
    {
        if (!esOferente(session)) return "redirect:/";
        Oferente oferente = (Oferente) session.getAttribute("usuario");

        Caracteristica actual = serviceC.findById(actualId);
        List<Caracteristica> categorias = (actual == null) ? serviceC.findRoots() : serviceC.findHijos(actual);

        List<OferenteHabilidad> habilidades = serviceOH.findByOferente(oferente);
        Map<Integer, String> rutasHabilidades = new HashMap<>();

        for (var h : habilidades) { rutasHabilidades.put(h.getId(), serviceC.buildRutaString(h.getCaracteristica())); }

        model.addAttribute("usuario", oferente);
        model.addAttribute("actual", actual);
        model.addAttribute("categorias", categorias);
        model.addAttribute("ruta", serviceC.buildRuta(actual));
        model.addAttribute("habilidades", habilidades);
        model.addAttribute("rutasHabilidades", rutasHabilidades);

        return "presentation/oferentes/ViewHabilidades";
    }


    @PostMapping("/oferente/habilidades/agregar")
    public String agregar(@RequestParam Integer caracteristicaId,
                          @RequestParam int nivel,
                          @RequestParam(required = false) Integer actualId,
                          HttpSession session)
    {
        if (!esOferente(session)) return "redirect:/";
        Oferente oferente = (Oferente) session.getAttribute("usuario");
        Caracteristica c = serviceC.findById(caracteristicaId);

        if (c != null) {
            serviceOH.agregarOActualizar(oferente, c, nivel);
        }

        String redirect = "/oferente/habilidades";
        if (actualId != null) redirect += "?actualId=" + actualId;
        return "redirect:" + redirect;
    }

    @PostMapping("/oferente/habilidades/eliminar")
    public String eliminar(@RequestParam Integer habilidadId, @RequestParam(required = false) Integer actualId, HttpSession session)
    {
        if (!esOferente(session)) return "redirect:/";
        serviceOH.eliminar(habilidadId);

        String redirect = "/oferente/habilidades";
        if (actualId != null) redirect += "?actualId=" + actualId;
        return "redirect:" + redirect;
    }

    @GetMapping("/oferente/postulacion")
    public String formPostulacion(HttpSession session, Model model) {
        if (!esOferente(session)) return "redirect:/";
        Oferente oferente = (Oferente) session.getAttribute("usuario");

        List<Puesto> puestosDisponibles = serviceP.findAllActivos().stream().filter(p -> !servicePO.yaPostulado(oferente, p)).toList();

        model.addAttribute("usuario", oferente);
        model.addAttribute("puestos", puestosDisponibles);
        return "presentation/oferentes/ViewPostulacion";
    }

    @PostMapping("/oferente/postulacion")
    public String guardarPostulacion(@RequestParam Integer puestoId, HttpSession session)
    {
        if (!esOferente(session)) return "redirect:/";

        Oferente oferente = (Oferente) session.getAttribute("usuario");

        serviceP.findById(puestoId).ifPresent(puesto -> servicePO.postular(oferente, puesto));

        return "redirect:/oferente/dashboard";
    }
}



