package com.example.spring_app.security;

import java.util.Date;
import java.util.function.Function;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    public static final String SECRET_KEY = "votre_cle_secrete_tres_longue_et_tres_securisee";
    
    public String generateToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10 ))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
                
    }
     

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims  = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);        
    }

    public boolean isTokenValid(String Token, UserDetails userDetails) {
        final String email = extractEmail(Token);

        return (email.equals(userDetails.getUsername()) && !isTokenExpired(Token));
    }

    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
