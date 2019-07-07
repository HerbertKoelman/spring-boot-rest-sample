/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urbixsoftware.spring.rest.models;

import fr.urbixsoftware.spring.rest.models.ExtendedUser;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author herbert koelman
 */
public class ExtendedUserTest {
  
  public ExtendedUserTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of isEnabled method, of class ExtendedUser.
   */
  @Test
  public void testIsEnabled() {
    System.out.println("isEnabled");
    ExtendedUser instance = new ExtendedUser("id", "username", "password");
    boolean expResult = true;
    boolean result = instance.isEnabled();
    assertEquals(expResult, result);
  }

  /**
   * Test of isAccountNonExpired method, of class ExtendedUser.
   */
  @Test
  public void testIsAccountNonExpired() {
    System.out.println("isAccountNonExpired");
    ExtendedUser instance = new ExtendedUser("id", "username", "password");
    boolean expResult = true;
    boolean result = instance.isAccountNonExpired();
    assertEquals(expResult, result);
  }

  /**
   * Test of isCredentialsNonExpired method, of class ExtendedUser.
   */
  @Test
  public void testIsCredentialsNonExpired() {
    System.out.println("isCredentialsNonExpired");
    ExtendedUser instance = new ExtendedUser("id", "username", "password");
    boolean expResult = true;
    boolean result = instance.isCredentialsNonExpired();
    assertEquals(expResult, result);
  }

  /**
   * Test of isAccountNonLocked method, of class ExtendedUser.
   */
  @Test
  public void testIsAccountNonLocked() {
    System.out.println("isAccountNonLocked");
    ExtendedUser instance = new ExtendedUser("id", "username", "password");
    boolean expResult = true;
    boolean result = instance.isAccountNonLocked();
    assertEquals(expResult, result);
  }

  /**
   * Test of getAuthorities method, of class ExtendedUser.
   */
//  @Test
  public void testGetAuthorities() {
    System.out.println("getAuthorities");
    ExtendedUser instance = null;
    Collection<GrantedAuthority> expResult = null;
    Collection<GrantedAuthority> result = instance.getAuthorities();
    assertNull(result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of builder method, of class ExtendedUser.
   */
  @Test
  public void testBuilder() {
    System.out.println("builder");
    ExtendedUser expResult = new ExtendedUser("id", "username", "password");
    ExtendedUser result = ExtendedUser
            .builder()
            .id("id")
            .username("username")
            .password("password")
            .build();
    
    assertEquals(expResult, result);
  }

  /**
   * Test of equals method, of class ExtendedUser.
   */
  @Test
  public void testEquals() {
    System.out.println("equals");
    
    ExtendedUser user = ExtendedUser
            .builder()
            .id("id")
            .username("username")
            .password("password")
            .build();

    boolean result = user.equals(new ExtendedUser("id", "username", "password"));
    assertTrue(result);
  }

  /**
   * Test of hashCode method, of class ExtendedUser.
   */
  @Test
  public void testHashCode() {
    System.out.println("hashCode");
    ExtendedUser instance = null;
    int expResult = 1281769081;
    int result = (new ExtendedUser("id", "username", "password")).hashCode();
    assertEquals(expResult, result);
  }

  /**
   * Test of toString method, of class ExtendedUser.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    ExtendedUser instance = null;
    String expResult = "ExtendedUser(id=id, username=username, password=password";
    String result = (new ExtendedUser("id", "username", "password")).toString();    
    assertTrue(result.startsWith(expResult));
  }
  
}
