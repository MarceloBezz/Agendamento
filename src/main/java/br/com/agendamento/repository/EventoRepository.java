package br.com.agendamento.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.agendamento.model.evento.Evento;
import br.com.agendamento.model.usuario.Usuario;

@Repository
public interface EventoRepository extends JpaRepository<Evento, String>{
    List<Evento> findByUsuarioIdAndInicioAfterAndAtivoTrueOrderByInicioAsc(Usuario usuario, LocalDateTime agora);

    List<Evento> findByUsuarioId(Usuario usuario);
}
