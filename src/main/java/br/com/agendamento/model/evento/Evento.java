package br.com.agendamento.model.evento;

import br.com.agendamento.model.usuario.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "eventos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Evento {

    @Id
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuarioId;
    private String inicio;
    private boolean ativo;
}
