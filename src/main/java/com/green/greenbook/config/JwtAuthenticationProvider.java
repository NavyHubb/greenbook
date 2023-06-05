package com.green.greenbook.config;

import com.green.greenbook.domain.dto.MemberDto;
import com.green.greenbook.domain.type.MemberType;
import com.green.greenbook.util.Aes256Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtAuthenticationProvider {

    private final String secretKey = "secretKey";
    private final String TOKEN_NAME = "X-AUTH-TOKEN";
    private final long TOKEN_VALID_TIME = 1000 * 60 *60 * 24;

    public String createToken(String email, Long userId, MemberType userType) {
        Claims claims = Jwts.claims()
            .setSubject(Aes256Util.encrypt(email))
            .setId(Aes256Util.encrypt(userId.toString()));
        claims.put("roles", userType);
        Date now = new Date();

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + TOKEN_VALID_TIME))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token);

            return claimsJws.getBody().getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String refreshToken(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_NAME);

        Claims claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
        Date now = new Date();

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + TOKEN_VALID_TIME))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public MemberDto getMemberDto(String token) {
        Claims c = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();

        return new MemberDto(
            Long.valueOf(Objects.requireNonNull(Aes256Util.decrypted(c.getId())))  // id
            , Aes256Util.decrypted(c.getSubject()));  // email
    }
}