/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.urbixsoftware.spring.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Fire up application.
 *
 * @author Herbert Koelman
 */
@SpringBootApplication
@EnableCaching
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args); // NOSONAR
  }
}
