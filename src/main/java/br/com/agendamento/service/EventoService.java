package br.com.agendamento.service;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.agendamento.model.evento.CadastrarEventoDTO;
import br.com.agendamento.model.evento.DadosEventoDTO;
import br.com.agendamento.model.evento.Evento;
import br.com.agendamento.model.usuario.Usuario;
import br.com.agendamento.repository.EventoRepository;
import jakarta.transaction.Transactional;

@Service
public class EventoService {

    @Autowired
    private CalendarAPIService calendarioService;

    @Autowired
    private EventoRepository repository;

    public Evento cadastrarEvento(CadastrarEventoDTO dto, Usuario usuario)
            throws GeneralSecurityException, IOException {
        var data = LocalDateTime.parse("%s-%s-%sT%s".formatted(dto.ano(), dto.mes(), dto.dia(), dto.inicio()));
        var id = calendarioService.cadastrarEvento(data, usuario.getNome());
        Evento eventoBD = new Evento(id, usuario, data, true, dto.atendente(), dto.servico());
        return repository.save(eventoBD);
    }

    public List<DadosEventoDTO> listarEventosUsuario(Usuario usuario) {
        return repository.findByUsuarioIdAndInicioAfterAndAtivoTrueOrderByInicioAsc(usuario, LocalDateTime.now())
                .stream()
                .map(DadosEventoDTO::new)
                .toList();
    }

    @Transactional
    public void deletarEvento(Usuario usuario, String id) throws Exception {
        Evento evento = repository.findById(id)
                .orElseThrow(() -> new Exception("Evento não encontrado!"));
        List<Evento> usuarioEventos = repository.findByUsuarioId(usuario);

        if (!usuarioEventos.contains(evento)) {
            throw new Exception("Você não tem permissão para alterar este evento!");
        }

        evento.setAtivo(false);
        calendarioService.excluirEvento(id);
    }

    public Evento buscarPorId(String idEvento, Usuario usuario) throws Exception {
         Evento evento = repository.findById(idEvento)
                .orElseThrow(() -> new Exception("Evento não encontrado!"));
        List<Evento> usuarioEventos = repository.findByUsuarioId(usuario);

        if (!usuarioEventos.contains(evento)) {
            throw new AccessDeniedException("Você não tem permissão para acessar este evento!");
        }

        return evento;
    }

}
