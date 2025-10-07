package br.com.agendamento.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.agendamento.model.evento.DadosEventoDTO;
import br.com.agendamento.model.evento.Evento;
import br.com.agendamento.model.usuario.Usuario;
import br.com.agendamento.service.EventoService;

@Controller
public class PageController {

    @Autowired
    private EventoService eventoService;

    @GetMapping("/cadastro")
    public String cadastro(Model model, @AuthenticationPrincipal Usuario usuario) {
        if (usuario != null) {
            return "redirect:/logado";
        }

        model.addAttribute("usuario", new Usuario());
        return "cadastro";
    }

    @GetMapping("/login")
    public String login(Model model,
            @RequestParam(value = "error", required = false) String mensagemErro,
            @RequestParam(value = "logout", required = false) String mensagemLogout,
            @AuthenticationPrincipal Usuario usuario) {
        if (usuario != null) {
            return "redirect:/logado";
        }

        if (mensagemErro != null) {
            model.addAttribute("mensagemErro", "Senha incorreta!");
        } else if (mensagemLogout != null) {
            model.addAttribute("mensagemLogout", "Logout realizado com sucesso!");
        }
        return "login";
    }

    @GetMapping("/logado")
    public String logado(Model model, @AuthenticationPrincipal Usuario usuario,
            @RequestParam(value = "erro-criacao-evento", required = false) String mensagemErro) {
        model.addAttribute("usuario", usuario);
        if (mensagemErro != null) {
            model.addAttribute("mensagemErro", "Erro ao criar evento!");
        }
        return "logado";
    }

    @GetMapping("/evento-criado")
    public String eventoCriado(@AuthenticationPrincipal Usuario usuario, Model model,
            @ModelAttribute Evento evento) {
        model.addAttribute("usuarioNome", usuario.getNome());
        model.addAttribute("inicio", evento.getInicio());
        return "evento-criado";
    }

    @GetMapping("/criar-evento")
    public String criarEvento() {
        return "criar-evento";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home() {
        return "index";
    }

    @GetMapping("/agendamento")
    public String agendamento() {
        return "agendamento";
    }

    @GetMapping("/meus-agendamentos")
    public String verAgendamentos(@AuthenticationPrincipal Usuario usuario,
            Model model,
            @RequestParam(value = "erro", required = false) String mensagemErro,
            @RequestParam(value = "cancelado", required = false) String mensagemCancelado) {
        List<DadosEventoDTO> eventos = eventoService.listarEventosUsuario(usuario);
        model.addAttribute("eventos", eventos);

        if (mensagemErro != null) {
            model.addAttribute("mensagemErro", "Erro ao cancelar evento!");
        }
        if (mensagemCancelado != null) {
            model.addAttribute("mensagemSucesso", "Evento cancelado com sucesso!");
        }
        return "meus-agendamentos";
    }

    @GetMapping("/admin")
    public String admin(@AuthenticationPrincipal Usuario usuario, Model model) {
        model.addAttribute("usuario", usuario);
        return "admin";
    }

    @GetMapping("/acesso-negado")
    public String acessoNegado() {
        return "acesso-negado";
    }

}
