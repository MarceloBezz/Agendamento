package br.com.agendamento.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.agendamento.model.usuario.CadastroUsuarioDTO;
import br.com.agendamento.model.usuario.Usuario;
import br.com.agendamento.service.UsuarioService;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PageController {

    @Autowired
    private UsuarioService service;

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(@ModelAttribute CadastroUsuarioDTO usuario) {
        try {
            service.cadastrarUsuario(usuario);
            return "redirect:/login";
        } catch (Exception e) {
            return "redirect:/cadastro?erro";
        }
    }

    @GetMapping("/login")
    public String login(Model model,
            @RequestParam(value = "error", required = false) String mensagemErro,
            @RequestParam(value = "logout", required = false) String mensagemLogout) {
        if (mensagemErro != null) {
            model.addAttribute("mensagemErro", "Senha incorreta!");
        } else if (mensagemLogout != null) {
            model.addAttribute("mensagemLogout", "Logout realizado com sucesso!");
        }
        return "login";
    }

    @GetMapping("/logado")
    public String logado(Model model, @AuthenticationPrincipal Usuario usuario) {
        model.addAttribute("usuario", usuario);
        return "logado";
    }

}
