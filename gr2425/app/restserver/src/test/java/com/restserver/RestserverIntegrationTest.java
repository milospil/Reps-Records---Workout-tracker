package com.restserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = RestserverApplication.class)
@ActiveProfiles("test")
class RestserverIntegrationTest {

  @Test
  public void testMain() {
    RestserverApplication.main(new String[] {});
  }

}
