package br.com.agendamento.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.agendamento.model.evento.Evento;
import br.com.agendamento.model.usuario.Usuario;

@Repository
public interface EventoRepository extends JpaRepository<Evento, String>{
    List<Evento> findByUsuarioIdAndInicioAfterOrderByInicioAsc(Usuario usuario, LocalDateTime agora);
}
