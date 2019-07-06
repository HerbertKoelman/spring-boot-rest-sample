/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urbixsoftware.spring.rest.auth.services;

import fr.urbixsoftware.spring.rest.models.ExtendedUser;
import static java.lang.String.format;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

/** UserAuthenticationService which is based on a simple random UUID.
 *
 * @author p094212
 */
//@Service
//@AllArgsConstructor(access = PACKAGE) <- this a lombok ding-bat that creates a contructor, it's a bity too magic for me
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UUIDAuthenticationService implements UserAuthenticationService {

  private static final Logger LOG = Logger.getLogger(UserAuthenticationService.class.getName());
  
  @NonNull
  private UserCrudService users;

  @Override
  public Optional<String> login(final String username, final String password) {
    LOG.info(() -> format("login(%s,%s)", username, password));
    
    final String uuid = UUID.randomUUID().toString();
    final ExtendedUser user = ExtendedUser
            .builder()
            .id(uuid)
            .username(username)
            .password(password)
            .build();

    users.save(user);
    
    LOG.info(() -> format("success (user: %s, uuid: %s)", user, uuid));
    
    return Optional.of(uuid);
  }

  @Override
  public Optional<ExtendedUser> findByToken(final Object token) {
    LOG.info(() -> format("find user by token(token: %s)", token));    
    return users.find(token.toString());
  }

  @Override
  public void logout(final ExtendedUser user) {

  }
  
  public UUIDAuthenticationService( UserCrudService userCrudService){
    this.users = userCrudService;
  }
}
