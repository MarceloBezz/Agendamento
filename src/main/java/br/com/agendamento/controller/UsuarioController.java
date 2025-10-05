package br.com.agendamento.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.agendamento.model.usuario.CadastroUsuarioDTO;
import br.com.agendamento.service.UsuarioService;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping("/cadastro")
    public String cadastrar(@ModelAttribute CadastroUsuarioDTO usuario) {
        try {
            service.cadastrarUsuario(usuario);
            return "redirect:/login";
        } catch (Exception e) {
            return "redirect:/cadastro?erro";
        }
    }
}
