package br.com.medflow.core.security.authorization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.medflow.core.security.annotations.AuthorizeResource;
import br.com.medflow.core.security.annotations.ProtectedResource;

class AuthorizeResourceAuthorizationManagerTest {

  private final AuthorizeResourceAuthorizationManager authorizationManager = new AuthorizeResourceAuthorizationManager(
      new FunctionalAuthorizer(new ProtectedResourceResolver()));

  @AfterEach
  void tearDown() {
    RequestContextHolder.resetRequestAttributes();
  }

  @Test
  void shouldAuthorizeMethodAnnotatedResource() throws NoSuchMethodException {
    TestingAuthenticationToken authentication = new TestingAuthenticationToken(
        "user",
        "credentials",
        List.of(new SimpleGrantedAuthority("usuario:create")));
    RequestContextHolder.setRequestAttributes(
        new ServletRequestAttributes(new MockHttpServletRequest("POST", "/api/usuarios")));

    AuthorizationDecision decision = authorizationManager.authorize(
        () -> authentication,
        invocation(new ResourceController(), "create"));

    assertTrue(decision.isGranted());
  }

  @Test
  void shouldAuthorizeTypeAnnotatedResource() throws NoSuchMethodException {
    TestingAuthenticationToken authentication = new TestingAuthenticationToken(
        "user",
        "credentials",
        List.of(new SimpleGrantedAuthority("consulta:read")));
    RequestContextHolder.setRequestAttributes(
        new ServletRequestAttributes(new MockHttpServletRequest("GET", "/api/consultas/1")));

    AuthorizationDecision decision = authorizationManager.authorize(
        () -> authentication,
        invocation(new ConsultaController(), "findById"));

    assertTrue(decision.isGranted());
  }

  @Test
  void shouldDenyWhenAuthorityIsMissing() throws NoSuchMethodException {
    TestingAuthenticationToken authentication = new TestingAuthenticationToken(
        "user",
        "credentials",
        List.of(new SimpleGrantedAuthority("usuario:read")));
    RequestContextHolder.setRequestAttributes(
        new ServletRequestAttributes(new MockHttpServletRequest("DELETE", "/api/usuarios/1")));

    AuthorizationDecision decision = authorizationManager.authorize(
        () -> authentication,
        invocation(new ResourceController(), "delete"));

    assertFalse(decision.isGranted());
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

  @ProtectedResource("usuario")
  private static final class Usuario {
  }

  private static final class Consulta {
  }
}
