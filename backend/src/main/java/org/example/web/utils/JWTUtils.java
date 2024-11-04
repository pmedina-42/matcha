package org.example.web.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.example.enums.Role;

import javax.crypto.SecretKey;
import java.sql.Timestamp;

public class JWTUtils {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public static String generateToken(String username, Role role) {
        String jws = Jwts.builder().issuer("yop").subject(username)
                .claim("userName", username)
                .claim("role", role.name())
                .issuedAt(new Timestamp(System.currentTimeMillis()))
                .expiration(new Timestamp(System.currentTimeMillis() + 100000000))
                .signWith(SECRET_KEY)
                .compact();
        return jws;
    }

    public static boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String username = claims.get("userName", String.class);
            String role = claims.get("role", String.class);

            return true;
        } catch (SignatureException e) {
            System.err.println("Token has an invalid signature.");
            return false;
        } catch (Exception e) {
            System.err.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    public static String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims != null ? claims.getSubject() : null;
    }
}
