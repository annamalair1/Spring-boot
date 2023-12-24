package org.example.utilities;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JWTUtil {

    private final String secretKey = "VZGmDQeJ3tdq9kBNkwGh47Vt9bM3cmuqvrqW/fOMZIU="; // Replace with your own secret key

    public String generateToken(String username) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.builder()
                .setSubject(username)
                .signWith(key)
                .compact();
    }
}
