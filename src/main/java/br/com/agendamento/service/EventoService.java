package br.com.agendamento.service;

import java.io.IOException;
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
        Evento eventoBD = new Evento(id, usuario, data, true);
        return repository.save(eventoBD);
    }

    public List<DadosEventoDTO> listarEventosUsuario(Usuario usuario) {
        return repository.findByUsuarioIdAndInicioAfterOrderByInicioAsc(usuario, LocalDateTime.now())
        .stream()
        .map(DadosEventoDTO::new)
        .toList();
    }

}
