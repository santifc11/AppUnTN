package utn.TpFinal.AppUnTN.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import utn.TpFinal.AppUnTN.Security.JwtService;
import utn.TpFinal.AppUnTN.DTO.AuthRequest;
import utn.TpFinal.AppUnTN.DTO.AuthResponse;
import utn.TpFinal.AppUnTN.service.CustomUserDetailsService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {

        // 1. Autenticar las credenciales
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        // 2. Cargar el usuario desde la base de datos
        UserDetails user = userDetailsService.loadUserByUsername(authRequest.getUsername());

        // 3. Generar el token JWT
        String jwtToken = jwtService.generateToken(user);

        // 4. Devolver el token al cliente
        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }
}
