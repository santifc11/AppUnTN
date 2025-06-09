package utn.TpFinal.AppUnTN.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service // Lo registra como un Bean de Spring
public class JwtService {

    // Clave secreta (debe ser segura y lo suficientemente larga)
    private static final String SECRET_KEY = "4c697665732d7365637265742d6a77642d31323334353637383930";

    // Genera un token para un usuario autenticado
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // El "dueño" del token (ej: nombre de usuario)
                .claim("roles", userDetails.getAuthorities()) // Información adicional: los roles del usuario
                .setIssuedAt(new Date(System.currentTimeMillis())) // Cuándo fue generado
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Expira en 1 hora
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Firma el token con HS256 y la clave secreta
                .compact(); // Lo convierte a una cadena tipo JWT (header.payload.firma)
    }

    // Extrae el nombre de usuario del token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Valida si el token es válido para un usuario específico
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Revisa si el token ha expirado
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }


    // Parsea todo el contenido del token y valida la firma
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey()) // Usa la clave secreta para validar
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Devuelve la clave usada para firmar/validar el token
    private Key getSignInKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8); // Decodifica la clave (base64)
        return Keys.hmacShaKeyFor(keyBytes); // Genera la clave HMAC con SHA-256
    }
}