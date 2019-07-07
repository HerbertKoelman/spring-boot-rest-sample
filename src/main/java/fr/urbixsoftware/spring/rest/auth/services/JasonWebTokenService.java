/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urbixsoftware.spring.rest.auth.services;

import com.google.common.collect.ImmutableMap;
import fr.urbixsoftware.spring.rest.auth.configurators.JsonWebTokenConfigurator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodec;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import static java.lang.String.format;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;
import lombok.NonNull;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;
import org.springframework.stereotype.Service;

/**
 *
 * @author herbert koelman
 */
@Service
//@FieldDefaults(level = PRIVATE, makeFinal = true)
public class JasonWebTokenService implements TokenService {

  private static final Logger LOG = Logger.getLogger(JasonWebTokenService.class.getName());
  
  private static final String DOT = ".";
  private static final CompressionCodec COMPRESSION_CODEC = CompressionCodecs.GZIP;

  @NonNull private final JsonWebTokenConfigurator configuration;
//  @NonNull private final String secretKey;
  @NonNull private final Key secretKey;

  public JasonWebTokenService(JsonWebTokenConfigurator configuration) {
    LOG.info("setup Web token service");
    
    this.configuration = configuration;
    this.secretKey = configuration.secretKey();
    
//    this.issuer = requireNonNull(issuer);
//    this.expirationSec = requireNonNull(expirationSec);
//    this.clockSkewSec = requireNonNull(clockSkewSec);
//    this.secretKey = BASE64.encode(requireNonNull(secret));
  }

  @Override
  public String permanent(final Map<String, Object> attributes) {
    LOG.info("create a permanent Token (it will never expire)");
    return newToken(attributes, 0);
  }

  @Override
  public String expiring(final Map<String, Object> attributes) {
    LOG.info("expiring token");
    return newToken(attributes, configuration.getExpiration());
  }

  @Override
  public Map<String, Object> verify(final String token) {
    LOG.info(()-> format("verifying token [%s]", token));
    
    // claims implements the Map interface
    Claims claims = Jwts
            .parser()
            .setSigningKey(secretKey)
            .setAllowedClockSkewSeconds(configuration.getClockSkew())
            .requireIssuer(configuration.getIssuer())
            .parseClaimsJws(token).getBody();
    
    LOG.info(()-> format("successfull verification (claims: [%s])", claims));
    
    return (Map<String, Object>) claims;
  }

  @Override
  public Map<String, Object> untrusted(final String token) {
    LOG.info(() -> format("untrusting token [%s]", token));
    
    final String withoutSignature = substringBeforeLast(token, DOT) + DOT;
    Map<String,Object> attributes = Jwts
            .parser()
            .requireIssuer(configuration.getIssuer())
            .setAllowedClockSkewSeconds(configuration.getClockSkew())
            .parseClaimsJwt(withoutSignature).getBody();

    return attributes ;
  }
  
  private String newToken(final Map<String, Object> attributes, int expiration) {
    LOG.info(() -> format("creating a new token (expires after %d seconds)", expiration));
    
    LocalDateTime now = LocalDateTime.now();
    Claims claims = Jwts
            .claims()
            .setIssuer(configuration.getIssuer())
            .setIssuedAt(Date.from(now.toInstant(ZoneOffset.UTC)));

    if (configuration.getExpiration() > 0) {
      LocalDateTime expiresAt = now.plusSeconds(expiration);
      claims.setExpiration(Date.from(expiresAt.toInstant(ZoneOffset.UTC)));
    }
    claims.putAll(attributes);
    
    LOG.fine(() -> format("claims [%s]", claims));

    String token = Jwts.builder()
            .setClaims(claims)
//            .signWith(SignatureAlgorithm.HS256,secretKey)
            .signWith(secretKey)
            .compressWith(COMPRESSION_CODEC)
            .compact();

    LOG.fine(() -> format("generated new token [%s]", token));
    
    return token;
  }

  /**
   * @deprecated we don't use this anymore.
   * @param toClaims
   * @return a map of attributes
   */
  private static Map<String, String> parseClaims(final Supplier<Claims> toClaims) {
    try {
      final Claims claims = toClaims.get();
      final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
      for (final Map.Entry<String, Object> e : claims.entrySet()) {
        builder.put(e.getKey(), String.valueOf(e.getValue()));
      }
      return builder.build();
    } catch (final IllegalArgumentException | JwtException e) {
      return ImmutableMap.of();
    }
  }
}
