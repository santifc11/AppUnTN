package utn.TpFinal.AppUnTN.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import utn.TpFinal.AppUnTN.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider (UserDetailsService userDetailsService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthFilter jwtAuthFilter,
            UserDetailsService userDetailsService) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                                // Público (registro, login, estáticos)
                                .requestMatchers(
                                        "/",
                                        "/index.html",
                                        "/login.html",
                                        "/register.html",
                                        "/css/**",
                                        "/js/**",
                                        "/img/**",
                                        "/favicon.ico",
                                        "/api/auth/**",
                                        "/api/users/register",
                                        "/index_usuario_perfil.html",
                                        "/resume_upload.html",
                                        "/documents.html",
                                        "/document_preview.html",
                                        "/profile.html",
                                        "/admin_admins.html",
                                        "/admin_usuarios.html",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/swagger-ui.html",
                                        "/admin_materias.html",
                                        "api/subjects/all"
                                ).permitAll()

                                // Endpoints protegidos (JWT necesario)
                                .requestMatchers(
                                        "/api/users/me",
                                        "/api/users/subjects/**",
                                        "/api/documents/**",
                                        "/api/punctuations/**",
                                        "/api/commentaries/**",
                                        "/admin/**",
                                        "/api/users/getAllUsers",
                                        "/api/users/deleteUser"
                                ).authenticated()
                                .anyRequest().authenticated()

                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider(userDetailsService))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService () {
        return new CustomUserDetailsService();
    }
}


