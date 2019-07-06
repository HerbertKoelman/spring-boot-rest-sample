/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urbixsoftware.spring.rest.auth.configurators;

import io.jsonwebtoken.SignatureAlgorithm;
import static io.jsonwebtoken.io.Encoders.BASE64;
import io.jsonwebtoken.security.Keys;
import static java.lang.String.format;
import java.security.Key;
import java.util.logging.Logger;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author p094212
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.jwt")
public class JsonWebTokenConfigurator {

  private static final Logger LOG = Logger.getLogger(JsonWebTokenConfigurator.class.getName());
  
  private String issuer;

  /**
   * the token expires after this amount of seconds
   */
  private int expiration = 86400;

  /**
   * clock skew (en seconds)
   */
  private int clockSkew = 300;

  /**
   * a secret
   */
  private String secret ;

  public JsonWebTokenConfigurator(){
    LOG.info(() -> format("collecting JWT configuration properties (default issuer %s, expires after %d s and clockSkew is %d s)",              
            issuer, 
            expiration, 
            clockSkew));
  }
  
  /** secret key suitable to sign things.
   * 
   * when run in a high availability environment, the key used to sign must be known 
   * by all participants. Therefore the secret key should be passed and not generated.
   * 
   * @return a secret key
   */
//  public String secretKeyAsString(){
//    LOG.info(() -> format("create secret key [%s]", secret));             
//    return BASE64.encode(secret.getBytes()); 
//  }
  
  public Key secretKey(){
    Key key = Keys.hmacShaKeyFor(secret.getBytes());
//    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    LOG.info(() -> format("create secret key [%s]", key));             
    return key;
  }
}
