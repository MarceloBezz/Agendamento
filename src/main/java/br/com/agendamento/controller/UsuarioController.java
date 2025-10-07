package br.com.agendamento.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.agendamento.model.usuario.CadastroUsuarioDTO;
import br.com.agendamento.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    // A autenticação ficou no Controller mesmo para facilitar o manuseio da request
    @PostMapping("/cadastro")
    public String cadastrar(@ModelAttribute CadastroUsuarioDTO usuario, HttpServletRequest request) {
        try {
            service.cadastrarUsuario(usuario);
            
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(usuario.email(),
                    usuario.senha());
            Authentication auth = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            request.getSession(true)
                    .setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                            SecurityContextHolder.getContext());

            return "redirect:/logado";
        } catch (Exception e) {
            return "redirect:/cadastro?erro";
        }
    }
}
