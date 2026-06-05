package br.com.medflow.core.security.authorization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.hibernate.annotations.SoftDelete;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.medflow.core.security.annotations.ProtectedResource;

class FunctionalAuthorizerTest {

  private final FunctionalAuthorizer functionalAuthorizer =
      new FunctionalAuthorizer(
          new ProtectedResourceResolver(),
          new ResourceActionResolver(),
          (authentication, permission) -> permittedAuthorities(authentication.getAuthorities())
              .contains(permission.authority()));

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
                "usuario:read",
                "ROLE_USUARIO"));

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
                "consulta:create",
                "ROLE_USUARIO"));

    MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/consultas");
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

    assertTrue(functionalAuthorizer.hasAccess(Consulta.class));
  }

  @Test
  void shouldInferDeactivateForSoftDeletedResourceOnDelete() {
    SecurityContextHolder.getContext()
        .setAuthentication(
            new TestingAuthenticationToken(
                "user",
                "credentials",
                "usuario:deactivate",
                "ROLE_USUARIO"));

    MockHttpServletRequest request = new MockHttpServletRequest("DELETE", "/api/usuarios/1");
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

    assertTrue(functionalAuthorizer.hasAccess(Usuario.class));
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
                "usuario:read"));

    assertThrows(
        org.springframework.security.access.AccessDeniedException.class,
        () -> functionalAuthorizer.checkAccess(Usuario.class, ResourceAction.DELETE));
  }

  @SoftDelete
  @ProtectedResource("usuario")
  private static final class Usuario {}

  private static final class Consulta {}

  private static Set<String> permittedAuthorities(
      java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> authorities) {
    return authorities.stream()
        .map(org.springframework.security.core.GrantedAuthority::getAuthority)
        .collect(java.util.stream.Collectors.toUnmodifiableSet());
  }
}
