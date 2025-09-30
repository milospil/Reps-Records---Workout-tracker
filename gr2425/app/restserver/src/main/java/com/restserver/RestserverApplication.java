package com.restserver;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.json.AppPersistence;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Launces the spring boot server.
 */
@SpringBootApplication
public class RestserverApplication {

  /**
   * Connects the AppPersistence objectmapper module to spring boot.

   * @return app module.
   */
  @Bean
  public SimpleModule objectMapperModule() {
    return AppPersistence.createJacksonModule();
  }

  /**
   * Launches the Spring Boot Server.

   * @param args args.
   */
  public static void main(String[] args) {
    SpringApplication.run(RestserverApplication.class, args);
  }

}
