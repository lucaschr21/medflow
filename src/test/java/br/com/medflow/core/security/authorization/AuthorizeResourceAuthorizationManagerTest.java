package br.com.medflow.core.security.authorization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.Set;

import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.annotations.SoftDelete;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.medflow.core.security.annotations.AuthorizePermission;
import br.com.medflow.core.security.annotations.AuthorizeResource;
import br.com.medflow.core.security.annotations.ProtectedResource;

class AuthorizeResourceAuthorizationManagerTest {

  private final ResourceActionResolver resourceActionResolver = new ResourceActionResolver();
  private final AuthorizeResourceAuthorizationManager authorizationManager = new AuthorizeResourceAuthorizationManager(
      new FunctionalAuthorizer(
          new ProtectedResourceResolver(),
          resourceActionResolver,
          (authentication, permission) -> grantedAuthorities(authentication).contains(permission.authority())),
      resourceActionResolver);

  @AfterEach
  void tearDown() {
    RequestContextHolder.resetRequestAttributes();
  }

  @Test
  void shouldAuthorizeMethodAnnotatedResource() throws NoSuchMethodException {
    TestingAuthenticationToken authentication = new TestingAuthenticationToken("user", "credentials", "usuario:create");
    RequestContextHolder.setRequestAttributes(
        new ServletRequestAttributes(new MockHttpServletRequest("POST", "/api/usuarios")));

    AuthorizationDecision decision = authorizationManager.authorize(
        () -> authentication, invocation(new ResourceController(), "create"));

    assertTrue(decision.isGranted());
  }

  @Test
  void shouldAuthorizeTypeAnnotatedResource() throws NoSuchMethodException {
    TestingAuthenticationToken authentication = new TestingAuthenticationToken("user", "credentials", "consulta:read");
    RequestContextHolder.setRequestAttributes(
        new ServletRequestAttributes(new MockHttpServletRequest("GET", "/api/consultas/1")));

    AuthorizationDecision decision = authorizationManager.authorize(
        () -> authentication, invocation(new ConsultaController(), "findById"));

    assertTrue(decision.isGranted());
  }

  @Test
  void shouldDenyWhenAuthorityIsMissing() throws NoSuchMethodException {
    TestingAuthenticationToken authentication = new TestingAuthenticationToken("user", "credentials", "usuario:read");
    RequestContextHolder.setRequestAttributes(
        new ServletRequestAttributes(new MockHttpServletRequest("DELETE", "/api/usuarios/1")));

    AuthorizationDecision decision = authorizationManager.authorize(
        () -> authentication, invocation(new ResourceController(), "delete"));

    assertFalse(decision.isGranted());
  }

  @Test
  void shouldInferDeactivateForSoftDeletedResourceOnDelete() throws NoSuchMethodException {
    TestingAuthenticationToken authentication = new TestingAuthenticationToken("user", "credentials",
        "usuario:deactivate");
    RequestContextHolder.setRequestAttributes(
        new ServletRequestAttributes(new MockHttpServletRequest("DELETE", "/api/usuarios/1")));

    AuthorizationDecision decision = authorizationManager.authorize(
        () -> authentication, invocation(new ResourceController(), "delete"));

    assertTrue(decision.isGranted());
  }

  @Test
  void shouldPrioritizeExplicitPermissionOverHttpMethodInference() throws NoSuchMethodException {
    TestingAuthenticationToken authentication = new TestingAuthenticationToken("user", "credentials",
        "consulta:update");
    RequestContextHolder.setRequestAttributes(
        new ServletRequestAttributes(
            new MockHttpServletRequest("POST", "/api/consultas/1/cancelamento")));

    AuthorizationDecision decision = authorizationManager.authorize(
        () -> authentication, invocation(new WorkflowController(), "cancel"));

    assertTrue(decision.isGranted());
  }

  @Test
  void shouldUseExplicitPermissionDeclaredAtTypeLevel() throws NoSuchMethodException {
    TestingAuthenticationToken authentication = new TestingAuthenticationToken("user", "credentials",
        "consulta:update");
    RequestContextHolder.setRequestAttributes(
        new ServletRequestAttributes(
            new MockHttpServletRequest("POST", "/api/consultas/1/check-in")));

    AuthorizationDecision decision = authorizationManager.authorize(
        () -> authentication, invocation(new CheckInController(), "checkIn"));

    assertTrue(decision.isGranted());
  }

  private static MethodInvocation invocation(Object target, String methodName)
      throws NoSuchMethodException {
    Method method = target.getClass().getDeclaredMethod(methodName);
    return new MethodInvocation() {
      @Override
      public Method getMethod() {
        return method;
      }

      @Override
      public Object[] getArguments() {
        return new Object[0];
      }

      @Override
      public Object proceed() {
        throw new UnsupportedOperationException();
      }

      @Override
      public Object getThis() {
        return target;
      }

      @Override
      public AccessibleObject getStaticPart() {
        return method;
      }
    };
  }

  private static final class ResourceController {

    @AuthorizeResource(Usuario.class)
    void create() {
    }

    @AuthorizeResource(Usuario.class)
    void delete() {
    }
  }

  @AuthorizeResource(Consulta.class)
  private static final class ConsultaController {

    void findById() {
    }
  }

  private static final class WorkflowController {

    @AuthorizePermission(resource = Consulta.class, action = ResourceAction.UPDATE)
    void cancel() {
    }
  }

  @AuthorizePermission(resource = Consulta.class, action = ResourceAction.UPDATE)
  private static final class CheckInController {

    void checkIn() {
    }
  }

  @SoftDelete
  @ProtectedResource("usuario")
  private static final class Usuario {
  }

  private static final class Consulta {
  }

  private static Set<String> grantedAuthorities(
      org.springframework.security.core.Authentication authentication) {
    return authentication.getAuthorities().stream()
        .map(org.springframework.security.core.GrantedAuthority::getAuthority)
        .collect(java.util.stream.Collectors.toUnmodifiableSet());
  }
}
