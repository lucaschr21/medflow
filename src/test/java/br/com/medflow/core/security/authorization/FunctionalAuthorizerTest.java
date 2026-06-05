package br.com.medflow.core.security.authorization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.medflow.core.security.annotations.ProtectedResource;

class FunctionalAuthorizerTest {

  private final FunctionalAuthorizer functionalAuthorizer =
      new FunctionalAuthorizer(new ProtectedResourceResolver());

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
    RequestContextHolder.resetRequestAttributes();
  }

  @Test
  void shouldAuthorizeUsingExplicitAction() {
    SecurityContextHolder.getContext()
        .setAuthentication(
            new TestingAuthenticationToken(
                "user",
                "credentials",
                List.of(
                    new SimpleGrantedAuthority("usuario:read"),
                    new SimpleGrantedAuthority("ROLE_USUARIO"))));

    assertTrue(functionalAuthorizer.hasAccess(Usuario.class, ResourceAction.READ));
    assertFalse(functionalAuthorizer.hasAccess(Usuario.class, ResourceAction.UPDATE));
  }

  @Test
  void shouldAuthorizeUsingCurrentHttpMethod() {
    SecurityContextHolder.getContext()
        .setAuthentication(
            new TestingAuthenticationToken(
                "user",
                "credentials",
                List.of(
                    new SimpleGrantedAuthority("consulta:create"),
                    new SimpleGrantedAuthority("ROLE_USUARIO"))));

    MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/consultas");
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

    assertTrue(functionalAuthorizer.hasAccess(Consulta.class));
  }

  @Test
  void shouldResolvePermissionFromResourceType() {
    FunctionalPermission permission =
        functionalAuthorizer.permission(Usuario.class, ResourceAction.DELETE);

    assertEquals("usuario:delete", permission.authority());
  }

  @Test
  void shouldThrowWhenAccessIsDenied() {
    SecurityContextHolder.getContext()
        .setAuthentication(
            new TestingAuthenticationToken(
                "user",
                "credentials",
                List.of(new SimpleGrantedAuthority("usuario:read"))));

    assertThrows(
        org.springframework.security.access.AccessDeniedException.class,
        () -> functionalAuthorizer.checkAccess(Usuario.class, ResourceAction.DELETE));
  }

  @ProtectedResource("usuario")
  private static final class Usuario {}

  private static final class Consulta {}
}
