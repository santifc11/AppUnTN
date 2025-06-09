package utn.TpFinal.AppUnTN.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component // Marca esta clase como un Bean para que Spring pueda inyectarla
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService; // Servicio para validar y extraer datos del token

    @Autowired
    private UserDetailsService userDetailsService; // Servicio que carga usuarios desde la BD

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();
        List<String> publicPaths = List.of("/api/auth", "/api/users/register");

        if (publicPaths.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraemos el encabezado Authorization (esperamos: Bearer token)
        final String authHeader = request.getHeader("Authorization");

        // Si no hay token o no empieza con Bearer, continúa sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraemos el token sin el prefijo "Bearer "
        String jwtToken = authHeader.substring(7);
        String username = jwtService.extractUsername(jwtToken); // Método del servicio para extraer el username

        // Si el usuario está en el token y no está ya autenticado
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validamos el token
            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                // Creamos la autenticación y la colocamos en el contexto de seguridad
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continuamos con el siguiente filtro
        filterChain.doFilter(request, response);
    }
}