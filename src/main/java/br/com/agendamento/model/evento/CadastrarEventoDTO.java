package br.com.agendamento.model.evento;

import java.time.LocalDateTime;

public record CadastrarEventoDTO(
    LocalDateTime inicio,
    String atendente,
    String servico
) {
    
}
