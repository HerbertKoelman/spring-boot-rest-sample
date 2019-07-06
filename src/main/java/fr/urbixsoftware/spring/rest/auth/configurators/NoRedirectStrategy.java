/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urbixsoftware.spring.rest.auth.configurators;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.web.RedirectStrategy;

/** As we’re securing a REST API, in case of authentication failure, the server 
 * should not redirect to any error page. The server will simply return an HTTP 401 (Unauthorized).
 *
 * @author Herbert Koelman
 */
public class NoRedirectStrategy implements RedirectStrategy{
  
  @Override
  public void sendRedirect(final HttpServletRequest request, final HttpServletResponse response, final String url) throws IOException {
      // No redirect is required with pure REST
  }
  
}
