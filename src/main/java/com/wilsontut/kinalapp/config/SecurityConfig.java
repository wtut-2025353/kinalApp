package com.wilsontut.kinalapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean //metodo que devuelve un objeto administrado por Spring
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http //objeto para configurar las reglas principales de seguridad
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/login", "/usuarios/registro").permitAll() //autoriza acceso publico a estos recursos
                        .requestMatchers("/usuarios/**", "/productos/**").hasRole("ADMIN") //solo ADMIN puede acceder
                        .requestMatchers("/home", "/ventas/**", "/clientes/**", "/detalleventa/**").hasAnyRole("ADMIN", "USER") //ADMIN y USER pueden acceder
                        .anyRequest().authenticated() //cualquier otra ruta requiere autenticacion
                )
                .formLogin(form -> form
                        .loginPage("/login") //pagina personalizada de login
                        .usernameParameter("email") //indica que el login es con email en vez de username
                        .defaultSuccessUrl("/home", true) //redirige al home tras login exitoso
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") //redirige al login tras cerrar sesion
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //encriptador de passwords
    }
}
