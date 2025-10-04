package br.com.agendamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.agendamento.model.evento.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, String>{
    
}
