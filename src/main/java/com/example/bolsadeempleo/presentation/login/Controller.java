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
}