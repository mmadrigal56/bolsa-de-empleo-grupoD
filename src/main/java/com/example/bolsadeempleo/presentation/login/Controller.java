package com.example.bolsadeempleo.presentation.login;

import com.example.bolsadeempleo.logic.administrador.ServiceA;
import com.example.bolsadeempleo.logic.administrador.Administrador;
import com.example.bolsadeempleo.logic.empresa.Empresa;
import com.example.bolsadeempleo.logic.empresa.ServiceE;
import com.example.bolsadeempleo.logic.oferente.Oferente;
import com.example.bolsadeempleo.logic.oferente.ServiceO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import jakarta.servlet.http.HttpSession;

@org.springframework.stereotype.Controller("usuario")
@SessionAttributes("usuario")
public class Controller {
    @Autowired
    private ServiceA serviceA;

    @Autowired
    private ServiceE serviceE;

    @Autowired
    private ServiceO serviceO;

    @GetMapping("/")
    public String index(Model model) {
        return "presentation/index";
    }

    @GetMapping("/presentation/login")
    public String loginForm(Model model) {
        return "/presentation/login/View";
    }

    @PostMapping("/presentation/login")
    public String autenticar(@RequestParam("correo") String correo,
                             @RequestParam("clave") String clave,
                             HttpSession session,
                             Model model)
    {

        Object usuario = serviceA.findUserByEmailAndPassword(correo, clave);
        System.out.println("Usuario encontrado: " + (usuario != null ? usuario.getClass().getSimpleName() : "NULL"));

        if (usuario == null) {
            model.addAttribute("error", "Correo o contraseña incorrectos");
            return "/presentation/login/View";
        }

        session.setAttribute("usuario", usuario);
        model.addAttribute("usuario", usuario);

        if (usuario instanceof Administrador) {
            return "redirect:/presentation/administrador/show";
        } else if (usuario instanceof Empresa) {
            return "redirect:/presentation/empresas/show";
        } else if (usuario instanceof Oferente) {
            return "redirect:/presentation/oferentes/show";
        }
        return "redirect:/";
    }

    @GetMapping("/presentation/login/logout")
    public String logout(HttpSession session, SessionStatus status) {
        session.invalidate();
        status.setComplete();
        return "redirect:/";
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(emailRegex);
    }

    @GetMapping("/registro")
    public String seleccionRegistro() {
        return "presentation/login/ViewSeleccion";
    }

    @GetMapping("/registro/empresa")
    public String formEmpresa() {
        return "presentation/empresas/ViewRegistro";
    }

    @PostMapping("/registro/empresa")
    public String registrarEmpresa(
            @RequestParam String nombre,
            @RequestParam String localizacion,
            @RequestParam String correo,
            @RequestParam String clave,
            @RequestParam String telefono,
            @RequestParam String descripcion) {

        Empresa empresa = new Empresa();
        empresa.setNombre(nombre);
        empresa.setLocalizacion(localizacion);
        empresa.setCorreo(correo);
        empresa.setClave(clave);
        empresa.setTelefono(telefono);
        empresa.setDescripcion(descripcion);
        empresa.setAutorizada(false);

        serviceE.registrarEmpresa(empresa);
        return "redirect:/presentation/login";
    }

    @GetMapping("/registro/oferente")
    public String formOferente() {
        return "presentation/oferentes/ViewRegistro";
    }

    @PostMapping("/registro/oferente")
    public String registrarOferente(
            @RequestParam String identificacion,
            @RequestParam String nombre,
            @RequestParam String primerApellido,
            @RequestParam String nacionalidad,
            @RequestParam String telefono,
            @RequestParam String correo,
            @RequestParam String clave,
            @RequestParam String lugarResidencia,
            @RequestParam(required = false) String rutaCurriculum) {

        Oferente oferente = new Oferente();
        oferente.setIdentificacion(identificacion);
        oferente.setNombre(nombre);
        oferente.setPrimerApellido(primerApellido);
        oferente.setNacionalidad(nacionalidad);
        oferente.setTelefono(telefono);
        oferente.setCorreo(correo);
        oferente.setClave(clave);
        oferente.setLugarResidencia(lugarResidencia);
        oferente.setRutaCurriculum(rutaCurriculum);
        oferente.setAutorizado(false);

        serviceO.registrarOferente(oferente);
        return "redirect:/presentation/login";
    }
}