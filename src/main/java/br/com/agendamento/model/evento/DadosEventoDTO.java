package br.com.agendamento.model.evento;

import java.time.LocalDateTime;

public record DadosEventoDTO(
    String id,
    LocalDateTime inicio
) {
    public DadosEventoDTO(Evento evento) {
        this(evento.getId(), evento.getInicio());
    }
}
