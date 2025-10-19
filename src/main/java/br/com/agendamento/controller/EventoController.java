package br.com.agendamento.controller;

import br.com.agendamento.model.evento.CadastrarEventoDTO;
import br.com.agendamento.model.evento.Evento;
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

    @PostMapping("/criar")
    public String criarEvento(@ModelAttribute CadastrarEventoDTO dto,
            @AuthenticationPrincipal Usuario usuario, RedirectAttributes redirectAttributes) throws GeneralSecurityException, IOException {
        try {
            Evento evento = service.cadastrarEvento(dto, usuario);
            // redirectAttributes.addFlashAttribute("evento", evento);
            return "redirect:/evento-criado/" + evento.getId();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/logado?erro-criacao-evento";
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
