package com.pitang.desafio.tcepe.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.pitang.desafio.tcepe.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenServiceImpl implements ITokenService {

    public static final String INVALIDE_TOKEN = "";
    @Value("${api.security.token.jwt.secret}")
    private String secret;

    public String generateToken(final User user) {
        try {
            return JWT
                    .create()
                    .withIssuer("auth-api")
                    .withSubject(user.getLogin())
                    .withExpiresAt(expirationDate())
                    .sign(Algorithm.HMAC256(secret));

        } catch (JWTCreationException e) {
            throw new RuntimeException("Error while generating token", e);
        }
    }

    public String validateToken(final String token) {
        try {
            return JWT
                    .require(Algorithm.HMAC256(secret))
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (TokenExpiredException e) {
            throw e;
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Unauthorized - Invalid session");
        }
    }

    private Instant expirationDate() {
        return LocalDateTime
                .now()
                .plusMinutes(10)
                .toInstant(ZoneOffset
                        .of("-03:00"));
    }
}
