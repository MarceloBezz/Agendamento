package br.com.agendamento.controller;

import br.com.agendamento.model.evento.CadastrarEventoDTO;
import br.com.agendamento.model.usuario.Usuario;
import br.com.agendamento.service.EventoService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/evento")
public class EventoController {

    @Autowired
    private EventoService service;

    @PostMapping("/confirmar")
    public String confirmarEvento(@ModelAttribute CadastrarEventoDTO dto,
            RedirectAttributes redirectAttributes) throws GeneralSecurityException, IOException {
        try {
            redirectAttributes.addFlashAttribute("evento", dto);
            return "redirect:/confirmar-evento";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/meus-dados?erro-criacao-evento";
        }
    }

    @PostMapping("/criar")
    public String criarEvento(@ModelAttribute CadastrarEventoDTO evento,
            @AuthenticationPrincipal Usuario usuario)
            throws GeneralSecurityException, IOException {
        try {
            service.cadastrarEvento(evento, usuario);
            return "redirect:/confirmado";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/meus-dados?erro-criacao-evento";
        }
    }

    @DeleteMapping("/cancelar/{id}")
    @ResponseBody
    public ResponseEntity<String> deletarEvento(@AuthenticationPrincipal Usuario usuario, @PathVariable String id) {
        try {
            service.deletarEvento(usuario, id);
            return ResponseEntity.ok().body("Evento cancelado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao cancelar evento!");
        }
    }
}
