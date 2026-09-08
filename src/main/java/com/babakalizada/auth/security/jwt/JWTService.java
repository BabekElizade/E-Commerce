package com.babakalizada.auth.security.jwt;

import com.babakalizada.user.entity.User;
import com.babakalizada.user.repository.IUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;


import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JWTService {
    private final IUserRepository userRepository;

    private static final String SECRET_KEY = "S4SE9wUKeNzNhF8WXeOs05cIRezjF7ZOpa2/vGJviS4=";
    public static SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(UserDetails userDetails) {
        Optional<User> user = userRepository.findUsersByUsername(userDetails.getUsername());
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.map(User::getRole).orElse(null));
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims()
                .add(claims)
                .and()
                .signWith(secretKey, Jwts.SIG.HS256)
                .expiration(new Date(System.currentTimeMillis() + 1000 * 3600 * 2))
                .issuedAt(new Date())
                .compact();
    }

    public <T> T exportToken(String token, Function<Claims, T> function) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return function.apply(claims);
    }

    public String getUsernameByToken(String token) {
        return exportToken(token, Claims::getSubject);
    }

    public Date getExpirationDateByToken(String token) {return exportToken(token, Claims::getExpiration);}

    public boolean isValidToken(String token) {
        try {
            String username = getUsernameByToken(token);
            return (username.equals(getUsernameByToken(token)) && !isTokenExpired(token));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        Date expiration = exportToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }
}
