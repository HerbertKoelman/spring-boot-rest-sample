/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urbixsoftware.spring.rest.auth.services;

import fr.urbixsoftware.spring.rest.models.ExtendedUser;
import static java.lang.String.format;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import lombok.NonNull;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.ContainerCriteria;
import static org.springframework.ldap.query.LdapQueryBuilder.query;
import org.springframework.stereotype.Service;
import static java.util.Optional.ofNullable;
import javax.cache.annotation.CacheResult;

/**
 *
 * @author herbert koelman
 */
@Service
public class LdapUsersService implements UserCrudService{

  private static final Logger LOG = Logger.getLogger(LdapUsersService.class.getName());
  
  private final @NonNull LdapTemplate ldapTemplate;
  
  /** we are only interested in these attributes.
   */
  private static final String[] WANTED_ATTRIBUTES = new String[]{ 
    "uid", 
    "cn", // canonical name
    "fullname",
    "loginDisabled",
    "passwordExpirationTime",
    "passwordExpirationInterval",
    "mail",
    "groupMembership"
  };
  
  @Override
  public ExtendedUser save(ExtendedUser user) {
    UnsupportedOperationException ex = new UnsupportedOperationException("LDAP user service does NOT support updates (save).");
    LOG.warning(ex.getMessage());
    throw ex;
  }

  @CacheResult(cacheName = "users")
  @Override
  public Optional<ExtendedUser> find(String id) {
    LOG.info(() -> format("find by user ID (id: %s)", id));
    ContainerCriteria query = query()
            .attributes(WANTED_ATTRIBUTES)
            .where("uid").is(id);
   
    List<ExtendedUser> users = ldapTemplate.search(query, new ExtendUserLdapMapper());
    return ofNullable(users.get(0));
  }

  @CacheResult(cacheName = "users")
  @Override
  public Optional<ExtendedUser> findByUsername(String username) {
    
    LOG.info(() -> format("find by user name (user: %s)", username));
    
    ContainerCriteria query = query()
            .attributes(WANTED_ATTRIBUTES)
            .where("cn").is(username);

    List<ExtendedUser> users = ldapTemplate.search(query, new ExtendUserLdapMapper());
    return ofNullable(users.get(0));
  }
  
  public LdapUsersService( LdapTemplate ldapTemplate ){
    this.ldapTemplate = ldapTemplate;
  }
  
  /** This class initializes an ExtendedUser instance using LDAP attributes.
   * 
   */
  private class ExtendUserLdapMapper implements AttributesMapper<ExtendedUser> {

    @Override
    public ExtendedUser mapFromAttributes(Attributes attributes) throws NamingException {
      LOG.info(()-> format("create user instance (attributes: %s)", attributes));
      
      ExtendedUser user =  ExtendedUser.builder()
              .id(attributes.get("uid").get().toString())
              .username(attributes.get("cn").get().toString())
              .password(attributes.get("cn").get().toString())
              .fullName(attributes.get("fullname").get().toString())
              .mail(attributes.get("mail").get().toString())
              .groups(attributes.get("groupMembership").get().toString())
              .enabled(Boolean.parseBoolean(attributes.get("loginDisabled").get().toString()))
              .build();
      
      LOG.finer(() -> format("created user instance: %s",user));
      
      return user;
    }
    
  }
}
