package br.com.medflow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    properties = {
      "spring.security.oauth2.resourceserver.opaquetoken.introspection-uri=http://localhost:8085/realms/medflow/protocol/openid-connect/token/introspect",
      "spring.security.oauth2.resourceserver.opaquetoken.client-id=medflow-backend",
      "spring.security.oauth2.resourceserver.opaquetoken.client-secret=test-secret"
    })
class MedflowApplicationTests {

  @Test
  void contextLoads() {}
}
