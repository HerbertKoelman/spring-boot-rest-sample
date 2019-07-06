/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urbixsoftware.spring.rest.auth.token;

import fr.urbixsoftware.spring.rest.auth.services.UserAuthenticationService;
import static java.lang.String.format;
import java.util.Optional;
import java.util.logging.Logger;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/** responsible of finding the user by it’s authentication token.
 *
 * @author Herbert Koelman
 * @see UserAuthenticationService
 */
@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
  
  private static final Logger LOG = Logger.getLogger(TokenAuthenticationProvider.class.getName());
  
  @NonNull
  private UserAuthenticationService authenticationService;

  @Override
  protected void additionalAuthenticationChecks(final UserDetails d, final UsernamePasswordAuthenticationToken auth) {
    // Nothing to do
  }

  @Override
  protected UserDetails retrieveUser(final String username, final UsernamePasswordAuthenticationToken authentication) {
    LOG.info(() -> format("retrieve user (username: %s, authentication: %s)",username, authentication));
    
    final Object token = authentication.getCredentials();
    return Optional
      .ofNullable(token)
      .map(String::valueOf)
      .flatMap(authenticationService::findByToken)
      .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with authentication token=" + token));
  }
  
  public TokenAuthenticationProvider(UserAuthenticationService authenticationService){
    this.authenticationService = authenticationService;
  }
}
