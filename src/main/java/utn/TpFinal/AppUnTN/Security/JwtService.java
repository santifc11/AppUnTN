package utn.TpFinal.AppUnTN.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service // Lo registra como un Bean de Spring
public class JwtService {

    // Clave secreta (debe ser segura y lo suficientemente larga)
    private static final String SECRET_KEY = "4c697665732d7365637265742d6a77642d31323334353637383930";

    // Genera un token con claims opcionales
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Genera un token JWT con claims y el subject (username)
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims) // Información adicional
                .setSubject(userDetails.getUsername()) // El "usuario" del token
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de creación
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Expira en 1 día
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Firma con clave y algoritmo
                .compact(); // Construye el token
    }

    // Extrae el nombre de usuario del token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Valida si el token es válido para un usuario específico
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Revisa si el token ha expirado
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extrae la fecha de expiración del token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extrae un dato del token usando una función
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
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
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // Decodifica la clave (base64)
        return Keys.hmacShaKeyFor(keyBytes); // Genera la clave HMAC con SHA-256
    }
}