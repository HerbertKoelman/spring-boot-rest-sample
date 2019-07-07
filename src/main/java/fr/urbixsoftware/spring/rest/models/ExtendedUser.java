/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urbixsoftware.spring.rest.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collection;
import static java.util.Objects.requireNonNull;
import javax.cache.annotation.CacheDefaults;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * represents a single user:
 *
 * @author herbert koelman
 */
@Value
@CacheDefaults(cacheName = "users")
@JsonIgnoreProperties({"password", "authorities", "accountNonExpired", "accountNonLocked", "accountNonLocked", "credentialsNonExpired"})
public class ExtendedUser implements UserDetails {

  private static final long serialVersionUID = 2396654715019746670L;

  @NonFinal private String id;
  @NonFinal private String username;
  @NonFinal private String password;
  @NonFinal private String fullName;
  @NonFinal private String groups;
  @NonFinal private String mail;

  @NonFinal private boolean enabled;
  @NonFinal private boolean accountNonExpired;
  @NonFinal private boolean credentialsNonExpired;
  @NonFinal private boolean accountNonLocked;
  @NonFinal private Collection<GrantedAuthority> authorities;

  @JsonCreator
  public ExtendedUser(@JsonProperty("id") final String id, @JsonProperty("username") final String username, @JsonProperty("password") final String password) {
    defaults();
    this.id = requireNonNull(id);
    this.username = requireNonNull(username);
    this.password = requireNonNull(password);
  }
  
  @Builder
  public ExtendedUser(String id, String username, String password, String fullName, String groups, String mail, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
    defaults();
    this.id = requireNonNull(id);
    this.username = requireNonNull(username);
    this.password = requireNonNull(password);
    this.fullName = fullName;
    this.groups   = groups;
    this.mail     = mail;
  }

  /** set default property values
   */
  private void defaults() {
    this.enabled = true;
    this.accountNonExpired = true;
    this.credentialsNonExpired = true;
    this.accountNonLocked = true;
    this.authorities = new ArrayList<>();
  }

}
