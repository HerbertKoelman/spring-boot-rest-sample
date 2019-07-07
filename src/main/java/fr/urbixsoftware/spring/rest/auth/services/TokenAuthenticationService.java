/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urbixsoftware.spring.rest.auth.services;

import com.google.common.collect.ImmutableMap;
import fr.urbixsoftware.spring.rest.models.ExtendedUser;
import static java.lang.String.format;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import lombok.NonNull;
import org.springframework.stereotype.Service;

/**
 *
 * @author herbert koelman
 */
@Service
public class TokenAuthenticationService implements UserAuthenticationService{
  
  private static final Logger LOG = Logger.getLogger(TokenAuthenticationService.class.getName());
  
  @NonNull private final TokenService tokens;
  @NonNull private final UserCrudService users;
  
  @Override
  public Optional<String> login(final String username, final String password) {
    LOG.info(() -> format("login user [%s]", username));
    return users
      .findByUsername(username)
      .filter(user -> Objects.equals(password, user.getPassword()))
      .map(user -> tokens.expiring(ImmutableMap.of("username", username)));
  }

  @Override
  public Optional<ExtendedUser> findByToken(final Object token) {
    LOG.info(() -> format("find by token (token [%s])", token));
    
    return Optional
      .of(tokens.verify(token.toString()))
      .map(map -> map.get("username"))
      .flatMap((userName) -> users.findByUsername(userName.toString()));
  }

  @Override
  public void logout(final ExtendedUser user) {
    LOG.info(() -> format("logout %s", user));
    // Nothing to do
  }
  
  public TokenAuthenticationService(TokenService tokenService, UserCrudService userService){
    LOG.info(() -> format("setting up %s", TokenAuthenticationService.class.getName()));
    
    this.tokens = tokenService;
    this.users = userService;
  }
}
