/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urbixsoftware.spring.rest.auth.services;

import fr.urbixsoftware.spring.rest.models.ExtendedUser;
import java.util.Optional;

/**
 * ExtendedUser security operations like login and logout, and CRUD operations on {@link ExtendedUser}.
 * 
 * @author Herbert Koelman
 */
public interface UserCrudService {
  
  /** save a user in the user store.
   * 
   * @param user user details
   * @return saved user
   */
  public ExtendedUser save(ExtendedUser user);

  /** search the user store using a given unique ID.
   * 
   * @param id unique user identifier.
   * @return a user instance (if one was found)
   */
  public Optional<ExtendedUser> find(String id);

  /** search the user store using a given name.
   * 
   * @param username user name to search for
   * @return a user instance (if one was found)
   */
  public Optional<ExtendedUser> findByUsername(String username);
  
}
