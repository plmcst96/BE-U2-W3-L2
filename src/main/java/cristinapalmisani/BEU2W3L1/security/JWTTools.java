package cristinapalmisani.BEU2W3L1.security;

import cristinapalmisani.BEU2W3L1.entities.User;
import cristinapalmisani.BEU2W3L1.exception.UnauthorizeException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
    public class JWTTools {

        @Value("${spring.jwt.secret}")
        private String secret;

        public String createToken(User user) {
            return Jwts.builder().subject(String.valueOf(user.getId())) // Subject <-- A chi appartiene il token (id dell'utente)
                    .issuedAt(new Date(System.currentTimeMillis())) // Data di emissione (IAT - Issued At)
                    .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // Data di scadenza (Expiration Date)
                    .signWith(Keys.hmacShaKeyFor(secret.getBytes())) // Firmo il token
                    .compact();
        }

        public void verifyToken(String token) { // Dato un token mi lancia eccezioni in caso di token manipolato/scaduto
            try {
                Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token);
            } catch (Exception ex) {
                throw new UnauthorizeException("Problemi col token! Per favore effettua di nuovo il login!");
            }
        }

        public String extractIdFromToken(String token) {
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseSignedClaims(token).getPayload().getSubject();
        }
}
