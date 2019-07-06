/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urbixsoftware.spring.rest.auth.services;

import fr.urbixsoftware.spring.rest.models.ExtendedUser;
import java.util.Optional;

/** authentication service provider.
 *
 * @author Herbert Koelman
 */
public interface UserAuthenticationService {
  
  /**
   * Logs in with the given {@code username} and {@code password}.
   *
   * @param username user name (or principal)
   * @param password user's password
   * @return an {@link Optional} of a user when login succeeds
   */
  public Optional<String> login(String username, String password);

  /**
   * Finds a user by its dao-key.
   *
   * @param token user dao key
   * @return the related user (if any was found)
   */
  public Optional<ExtendedUser> findByToken(Object token);

  /**
   * Logs out the given input {@code user}.
   *
   * @param user the user to logout
   */
  public void logout(ExtendedUser user);
  
}
