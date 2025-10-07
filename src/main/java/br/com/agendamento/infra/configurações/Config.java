package br.com.agendamento.infra.configurações;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import br.com.agendamento.infra.exception.CustomAccessDeniedHandler;

@Configuration
public class Config {

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(req -> {
            req
                    .requestMatchers("/login", "/cadastro", "/css/**", "/js/**", "/Img/**", "/home", "/", "/acesso-negado").permitAll()
                    .requestMatchers("/admin").hasRole("ADMIN")
                    .requestMatchers("/login", "/cadastro").anonymous()
                    .anyRequest().authenticated();
        }).formLogin(form -> {
            form
                    .loginPage("/login")
                    .usernameParameter("email")
                    .passwordParameter("senha")
                    .defaultSuccessUrl("/logado", true)
                    .permitAll();
        }).logout(logout -> {
            logout
                    .invalidateHttpSession(true)
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout");
        }).exceptionHandling(ex -> ex
                .accessDeniedHandler(new CustomAccessDeniedHandler()))
        .build();
    }

    @Bean
    protected PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    protected RoleHierarchy roleHierarchy() {
        String hierarquia = "ROLE_ADMIN > ROLE_CLIENTE";
        return RoleHierarchyImpl.fromHierarchy(hierarquia);
    }
}
