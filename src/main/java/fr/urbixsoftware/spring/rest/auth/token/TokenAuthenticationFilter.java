/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urbixsoftware.spring.rest.auth.token;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import java.io.IOException;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import java.util.logging.Logger;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static lombok.AccessLevel.PRIVATE;
import lombok.experimental.FieldDefaults;
import static org.apache.commons.lang3.StringUtils.removeStart;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * responsible of extracting the authentication token from the request headers.
 *
 * It takes the Authorization header value and attempts to extract the token from it.
 *
 * @author Herbert Koelman
 */
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  
  private static final Logger LOG = Logger.getLogger(TokenAuthenticationFilter.class.getName());
  
  private static final String BEARER = "Bearer";

  public TokenAuthenticationFilter(final RequestMatcher requiresAuth) {
    super(requiresAuth);
  }

  @Override
  public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) {
    LOG.info(() -> format("attempt token authentication"));
    
    // try to extract the token's value
    final String param = ofNullable(request.getHeader(AUTHORIZATION)).orElse(request.getParameter("t"));

    // remove the Bearer part of the token value, if we failed to do so, then an exception is thrown.
    final String token = ofNullable(param)
            .map(value -> removeStart(value, BEARER))
            .map(String::trim)
            .orElseThrow(() -> new BadCredentialsException("Missing Authentication Token"));

    // as we are using a token we use the token as principal/username
    final Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
    
    // let the authentication check credentials (aka token)
    return getAuthenticationManager().authenticate(auth);
  }

  @Override
  protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,  final FilterChain chain, final Authentication authResult) throws IOException, ServletException {
    LOG.info(() -> format("successfull authentication (authenicationresult: %s)", authResult));
    
    super.successfulAuthentication(request, response, chain, authResult);
    chain.doFilter(request, response);
  }
}
