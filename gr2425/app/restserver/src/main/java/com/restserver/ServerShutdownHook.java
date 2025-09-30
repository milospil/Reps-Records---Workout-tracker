package com.restserver;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Automatically save the server data on shutdown.
 * ServerShutdownHook
 */
@Component
public class ServerShutdownHook implements DisposableBean {
  
  @Autowired
  private UserService userService;

  @Override
  public void destroy() {
    userService.autoSaveUserManager();
  }
}
