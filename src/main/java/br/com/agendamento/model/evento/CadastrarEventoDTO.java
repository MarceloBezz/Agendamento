package br.com.agendamento.model.evento;

public record CadastrarEventoDTO(
    String inicio,
    String dia,
    String mes,
    String ano,
    String atendente,
    String servico
) {
    
}
