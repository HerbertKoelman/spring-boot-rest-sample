/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urbixsoftware.spring.rest.controllers;

import fr.urbixsoftware.spring.rest.auth.services.UserAuthenticationService;
import fr.urbixsoftware.spring.rest.auth.services.UserCrudService;
import fr.urbixsoftware.spring.rest.models.ExtendedUser;
import static java.lang.String.format;
import java.util.logging.Logger;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** allows a user to login into the application.
 *
 * @author Herbert Koelman
 */
@RestController
@RequestMapping("/public/users")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
public class PublicUsersController {
  
  private static final Logger LOG = Logger.getLogger(PublicUsersController.class.getName());
  
  @NonNull
  private UserAuthenticationService authentication;
  @NonNull
  private UserCrudService users;

  @PostMapping("/register")
  String register(@RequestParam("username") final String username,@RequestParam("password") final String password) {
    LOG.info(() -> format("register user (username: %s, password: %s)",username, password));
    
    users
      .save(ExtendedUser
          .builder()
          .id(username)
          .username(username)
          .password(password)
          .build()
      );

    return login(username, password);
  }

  @PostMapping("/login")
  String login(@RequestParam("username") final String username, @RequestParam("password") final String password) {
    LOG.info(() -> format("login (username: %s, password: %s)",username, password));
    
    return authentication
      .login(username, password)
      .orElseThrow(() -> new RuntimeException("invalid login and/or password"));
  }
}
