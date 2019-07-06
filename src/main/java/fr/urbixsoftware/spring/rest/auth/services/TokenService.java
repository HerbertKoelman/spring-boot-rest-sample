/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urbixsoftware.spring.rest.auth.services;

import java.util.Map;

/** A token service is responsible of generating and validating JWT tokens.
 *
 * @author Herbert Koelman
 */
public interface TokenService {
  
  String permanent(Map<String, Object> attributes);

  String expiring(Map<String, Object> attributes);

  /**
   * Checks the validity of the given credentials.
   *
   * @param token
   * @return attributes if verified
   */
  Map<String, Object> untrusted(String token);

  /**
   * Checks the validity of the given credentials.
   *
   * @param token
   * @return attributes if verified
   */
  Map<String, Object> verify(String token);
}
