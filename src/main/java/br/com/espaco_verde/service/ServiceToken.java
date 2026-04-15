package br.com.espaco_verde.service;

import br.com.espaco_verde.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class ServiceToken {

    @Value("${api.token.secret}")
    private String apiTokenSecret;

    public String generateToken(User user){

        try {
            Algorithm algorithm = Algorithm.HMAC256(apiTokenSecret);

            String token = JWT.create()
                    .withIssuer("espaco-verde")
                    .withSubject(user.getLogin())
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);
            return token;
        }catch (JWTCreationException exception){
            throw  new RuntimeException("Um erro ocorreu durante a autenticação");
        }
    }

    public String validatetoken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiTokenSecret);
            return JWT.require(algorithm)
                    .withIssuer("espaco-verde")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException exception){
            return null;
        }
    }

    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
