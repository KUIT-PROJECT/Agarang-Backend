package com.kuit.agarang.domain.login.util;

import com.kuit.agarang.global.common.exception.jwt.bad_request.JwtUnsupportedTokenException;
import com.kuit.agarang.global.common.exception.jwt.unauthorized.JwtInvalidTokenException;
import com.kuit.agarang.global.common.exception.jwt.unauthorized.JwtMalformedTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import static com.kuit.agarang.global.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Component
public class JwtProvider {

  @Value("${secret.jwt-secret-key}")
  private String JWT_SECRET_KEY;

  @Value("${secret.jwt-expired-in}")
  private long JWT_EXPIRED_IN;

  public String createToken(String userId) {

    Claims claims = Jwts.claims();

    Date now = new Date();
    Date validity = new Date(System.currentTimeMillis() + JWT_EXPIRED_IN);

    Key key = Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes());

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .claim("userId", userId)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean isExpiredToken(String token) throws JwtInvalidTokenException {
    try {
      Jws<Claims> claims = Jwts.parserBuilder()
          .setSigningKey(JWT_SECRET_KEY).build()
          .parseClaimsJws(token);
      return claims.getBody().getExpiration().before(new Date());

    } catch (ExpiredJwtException e) {
      return true;

    } catch (UnsupportedJwtException e) {
      throw new JwtUnsupportedTokenException(UNSUPPORTED_TOKEN_TYPE);
    } catch (MalformedJwtException e) {
      throw new JwtMalformedTokenException(MALFORMED_TOKEN);
    } catch (IllegalArgumentException e) {
      throw new JwtInvalidTokenException(INVALID_TOKEN);
    } catch (JwtException e) {
      log.error("[JwtTokenProvider.validateAccessToken]", e);
      throw e;
    }
  }

  public String getPrincipal(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(JWT_SECRET_KEY).build()
        .parseClaimsJws(token)
        .getBody().getSubject();
  }

}
