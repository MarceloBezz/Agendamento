package br.com.agendamento.controller;

import br.com.agendamento.model.evento.CadastrarEventoDTO;
import br.com.agendamento.model.evento.Evento;
import br.com.agendamento.model.usuario.Usuario;
import br.com.agendamento.service.EventoService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/evento")
public class EventoController {

    @Autowired
    private EventoService service;

    @PostMapping("/criar")
    public String criarEvento(@ModelAttribute CadastrarEventoDTO dto,
            @AuthenticationPrincipal Usuario usuario, RedirectAttributes redirectAttributes) throws GeneralSecurityException, IOException {
        try {
            Evento evento = service.cadastrarEvento(dto, usuario);
            redirectAttributes.addFlashAttribute("evento", evento);
            return "redirect:/evento-criado";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/logado?erro-criacao-evento";
        }
    }

    @PostMapping("/cancelar/{id}")
    public String deletarEvento(@AuthenticationPrincipal Usuario usuario, @PathVariable String id) {
        try {
            service.deletarEvento(usuario, id);
            return "redirect:/meus-agendamentos?cancelado";
        } catch (Exception e) {
            return "redirect:/meus-agendamentos?erro";
        }
    }
}
