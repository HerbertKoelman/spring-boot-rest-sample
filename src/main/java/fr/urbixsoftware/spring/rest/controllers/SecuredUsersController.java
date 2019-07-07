/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urbixsoftware.spring.rest.controllers;

import fr.urbixsoftware.spring.rest.auth.services.UserAuthenticationService;
import fr.urbixsoftware.spring.rest.models.ExtendedUser;
import static java.lang.String.format;
import java.util.logging.Logger;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author herbert koelman
 */
@RestController
@RequestMapping("/users")
@FieldDefaults(level = PRIVATE, makeFinal = true)
//@AllArgsConstructor(access = PACKAGE) <- this a lombok ding-bat that creates a contructor, it's a bity too magic for me
public class SecuredUsersController {
  
  private static final Logger LOG = Logger.getLogger(SecuredUsersController.class.getName());
  
  @NonNull
  private UserAuthenticationService authenticationService;

  @GetMapping("/current")
  ExtendedUser getCurrent(@AuthenticationPrincipal final ExtendedUser user) {
    LOG.info(() -> format("current user infos (user: %s)",user));
    return user;
  }

  @GetMapping("/logout")
  boolean logout(@AuthenticationPrincipal final ExtendedUser user) {
    LOG.info(() -> format("logout user (user: %s)",user));
    
    authenticationService.logout(user);
    return true;
  }
  
  public SecuredUsersController(UserAuthenticationService authenticationService){
    this.authenticationService = authenticationService;
  }
}
