/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urbixsoftware.spring.rest.auth.services;

import fr.urbixsoftware.spring.rest.models.ExtendedUser;
import static java.lang.String.format;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import static java.util.Optional.ofNullable;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/** user data store interface.
 *
 * @author Herbert Koelman
 */
//@Service
public class MemoryMapUsersServices implements UserCrudService {

  private static final Logger LOG = Logger.getLogger(MemoryMapUsersServices.class.getName());

  private final Map<String, ExtendedUser> users = new HashMap<>();

  @Override
  public ExtendedUser save(final ExtendedUser user) {
    LOG.info(() -> format("save(user: %s)", user));
    return users.put(user.getId(), user);
  }

  @Override
  public Optional<ExtendedUser> find(final String id) {
    LOG.info(() -> format("find(id: %s)", id));
    return ofNullable(users.get(id));
  }

  @Override
  public Optional<ExtendedUser> findByUsername(final String username) {
    LOG.info(() -> format("find by user name(username: %s)", username));
    return users
            .values()
            .stream()
            .filter(u -> Objects.equals(username, u.getUsername()))
            .findFirst();
  }
}
