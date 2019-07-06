/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urbixsoftware.spring.rest.auth.configurators;

import java.util.logging.Logger;
import org.springframework.boot.autoconfigure.ldap.LdapProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static java.lang.String.format;
import java.util.Arrays;
import org.springframework.context.annotation.Primary;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 *
 * @author Herbert Koelman
 */
@Configuration
@Primary
public class LdapContextConfigurator extends LdapProperties {

  private static final Logger LOG = Logger.getLogger(LdapContextConfigurator.class.getName());

  /**
   * Context source.
   *
   * @return the ldap context source
   */
  @Bean
  public LdapContextSource ldapContextSource() {
    LOG.info(format("setting up Spring LDAP source context (urls: %s, base: %s: user: %s)",
            Arrays.toString(getUrls()),
            getBase(),
            getUsername()));

    
    LdapContextSource contextSource = new LdapContextSource();
    contextSource.setUrls(getUrls());
    contextSource.setBase(getBase());
    contextSource.setUserDn(getUsername());
    contextSource.setPassword(getPassword());
    
    contextSource.setPooled(true);
    return contextSource;
  }

  /**
   * Ldap template.
   *
   * @return the ldap template
   */
  @Bean
  public LdapTemplate ldapTemplate() {
    LOG.info("setting up Spring LDAP template");
    LdapTemplate ldapTemplate = new LdapTemplate(ldapContextSource());

    return ldapTemplate;
  }
}
