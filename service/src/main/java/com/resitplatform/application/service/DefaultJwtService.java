 
package com.resitplatform.application.service;

import com.resitplatform.domain.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class DefaultJwtService implements JwtService {

    private final Key key;

    public DefaultJwtService(@Value("${security.jwt.secret}") String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String getSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public String getToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .signWith(key)
                .compact();
    }
}
