/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urbixsoftware.spring.rest.auth.configurators;

import fr.urbixsoftware.spring.rest.auth.token.TokenAuthenticationFilter;
import fr.urbixsoftware.spring.rest.auth.token.TokenAuthenticationProvider;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import java.util.logging.Logger;
import static lombok.AccessLevel.PRIVATE;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * configure Spring Security with all the authenticatin services we defined.
 *
 * Spring Security is configured here:
 * <ul>
 * <li>URLs starting with /public/** are excluded from security, which means any url starting with /public will not be secured,</li>
 * <li>The TokenAuthenticationFilter is registered within the Spring Security Filter Chain very early. We want it to catch any authentication token passing by,</li>
 * <li>Most other login methods like formLogin or httpBasic have been disabled as we’re not willing to use them here (we want to use our own system),</li>
 * <li>Some boiler-plate code to disable automatic filter registration, related to Spring Boot.</li>
 * </ul>
 *
 * @author Herbert Koelman
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SecurityConfigurator extends WebSecurityConfigurerAdapter {

  private static final Logger LOG = Logger.getLogger(SecurityConfigurator.class.getName());
  
  private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(new AntPathRequestMatcher("/public/**"));
  private static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);
  private TokenAuthenticationProvider tokenAuthenticationProvider;

  public SecurityConfigurator(final TokenAuthenticationProvider provider) {
    super();
    this.tokenAuthenticationProvider = requireNonNull(provider);
    LOG.info(() -> format("using user cache: %s)", provider.getUserCache().getClass().getName()));
  }

  @Override
  protected void configure(final AuthenticationManagerBuilder auth) {
    LOG.info("configure authentication manager");
    auth.authenticationProvider(tokenAuthenticationProvider);
  }

  @Override
  public void configure(final WebSecurity web) {
    LOG.info("configure web security");
    web.ignoring().requestMatchers(PUBLIC_URLS);
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    LOG.info("configure HTTP security");
    http
            .sessionManagement()
            .sessionCreationPolicy(STATELESS)
            .and()
            .exceptionHandling()
            // this entry point handles when you request a protected page and you are not yet
            // authenticated
            .defaultAuthenticationEntryPointFor(forbiddenEntryPoint(), PROTECTED_URLS)
            .and()
            .authenticationProvider(tokenAuthenticationProvider)
            .addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter.class)
            .authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .logout().disable();
  }

  @Bean
  TokenAuthenticationFilter restAuthenticationFilter() throws Exception {
    LOG.info("create REST authentication filter");
    final TokenAuthenticationFilter filter = new TokenAuthenticationFilter(PROTECTED_URLS);
    filter.setAuthenticationManager(authenticationManager());
    filter.setAuthenticationSuccessHandler(successHandler());
    return filter;
  }

  @Bean
  SimpleUrlAuthenticationSuccessHandler successHandler() {
    LOG.info("create success handler");
    final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
    successHandler.setRedirectStrategy(new NoRedirectStrategy());
    return successHandler;
  }

  /**
   * Disable Spring boot automatic filter registration.
   */
  @Bean
  FilterRegistrationBean disableAutoRegistration(final TokenAuthenticationFilter filter) {
    final FilterRegistrationBean registration = new FilterRegistrationBean(filter);
    registration.setEnabled(false);
    return registration;
  }

  @Bean
  AuthenticationEntryPoint forbiddenEntryPoint() {
    return new HttpStatusEntryPoint(FORBIDDEN);
  }

//  @Bean
//  public CacheManager cacheManager() {
//    return new EhCacheCacheManager(ehCacheCacheManager().getObject());
//  }
//
//  @Bean
//  public EhCacheManagerFactoryBean ehCacheCacheManager() {
//    EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
//    cmfb.setConfigLocation(new ClassPathResource("ehcache.xml"));
//    cmfb.setShared(true);
//    return cmfb;
//  }
}
