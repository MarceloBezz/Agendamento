package br.com.agendamento.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.agendamento.model.usuario.CadastroUsuarioDTO;
import br.com.agendamento.model.usuario.Usuario;
import br.com.agendamento.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));
    }

    public void cadastrarUsuario(CadastroUsuarioDTO usuariodto) throws Exception {
        if (repository.existsByEmailIgnoreCase(usuariodto.email())) {
            throw new Exception("Usuário já cadastrado!");
        }
        Usuario usuario = new Usuario(usuariodto);
        usuario.setSenha(encoder.encode(usuariodto.senha()));
        repository.save(usuario);
    }
}
