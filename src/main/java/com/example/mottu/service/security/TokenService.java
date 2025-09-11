package com.example.mottu.service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.mottu.model.usuario.RoleUsuario;
import com.example.mottu.model.usuario.Usuario;
import com.example.mottu.dto.security.Token;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private Instant expiresAt = LocalDateTime.now().plusMinutes(120).toInstant(ZoneOffset.ofHours(-3));
    private Algorithm algorithm = Algorithm.HMAC256("secret");

    public Token createToken(Usuario user){
        var jwt = JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("email", user.getEmail())
                .withClaim("role", user.getRole().name())
                .withExpiresAt(expiresAt)
                .sign(algorithm);

        return new Token(jwt, user.getEmail());
    }

    public Usuario getUserFromToken(String jwt) {
        var jwtVerified = JWT.require(algorithm).build().verify(jwt);
        String role = jwtVerified.getClaim("role").asString();
        return Usuario.builder()
                .id(Long.valueOf(jwtVerified.getSubject()))
                .email(jwtVerified.getClaim("email").toString())
                .role(RoleUsuario.valueOf(role))
                .build();
    }
}
