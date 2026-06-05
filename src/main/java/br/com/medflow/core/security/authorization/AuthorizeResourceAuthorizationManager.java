package br.com.medflow.core.security.authorization;

import java.lang.reflect.Method;
import java.util.function.Supplier;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.medflow.core.security.annotations.AuthorizePermission;
import br.com.medflow.core.security.annotations.AuthorizeResource;

/**
 * Autoriza métodos anotados com {@link AuthorizeResource} ou
 * {@link AuthorizePermission}.
 *
 * <p>A prioridade é sempre da anotação explícita {@link AuthorizePermission}.
 * Quando ela não estiver presente, o gerenciador usa
 * {@link AuthorizeResource} e infere a ação pelo método HTTP atual.
 */
@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class AuthorizeResourceAuthorizationManager
    implements AuthorizationManager<MethodInvocation> {

  private final FunctionalAuthorizer functionalAuthorizer;

  /**
   * Cria o gerenciador de autorizacao da anotacao {@link AuthorizeResource}.
   *
   * @param functionalAuthorizer autorizador funcional do projeto
   */
  public AuthorizeResourceAuthorizationManager(FunctionalAuthorizer functionalAuthorizer) {
    this.functionalAuthorizer = functionalAuthorizer;
  }

  /**
   * Autoriza a invocacao com base no recurso declarado na anotacao.
   *
   * @param authentication autenticacao atual
   * @param invocation     invocacao interceptada
   * @return decisao de autorizacao
   */
  @Override
  public AuthorizationDecision authorize(
      Supplier<? extends Authentication> authentication, MethodInvocation invocation) {
    RequiredPermission requiredPermission = resolveRequiredPermission(invocation);
    Authentication currentAuthentication = authentication.get();
    boolean granted = currentAuthentication != null
        && functionalAuthorizer.hasAccess(
            currentAuthentication, requiredPermission.resourceType(), requiredPermission.action());
    return new AuthorizationDecision(granted);
  }

  private static RequiredPermission resolveRequiredPermission(MethodInvocation invocation) {
    Method method = invocation.getMethod();
    AuthorizePermission methodPermission =
        AnnotatedElementUtils.findMergedAnnotation(method, AuthorizePermission.class);
    if (methodPermission != null) {
      return new RequiredPermission(methodPermission.resource(), methodPermission.action());
    }

    Class<?> targetClass = AopProxyUtils.ultimateTargetClass(invocation.getThis());
    if (targetClass == null) {
      targetClass = method.getDeclaringClass();
    }

    AuthorizePermission typePermission =
        AnnotatedElementUtils.findMergedAnnotation(targetClass, AuthorizePermission.class);
    if (typePermission != null) {
      return new RequiredPermission(typePermission.resource(), typePermission.action());
    }

    AuthorizeResource methodResource =
        AnnotatedElementUtils.findMergedAnnotation(method, AuthorizeResource.class);
    if (methodResource != null) {
      return new RequiredPermission(methodResource.value(), ResourceAction.from(currentHttpMethod()));
    }

    AuthorizeResource typeResource =
        AnnotatedElementUtils.findMergedAnnotation(targetClass, AuthorizeResource.class);
    if (typeResource != null) {
      return new RequiredPermission(typeResource.value(), ResourceAction.from(currentHttpMethod()));
    }

    throw new IllegalStateException(
        "Nenhuma anotação de autorização do recurso foi encontrada para o método: " + method.toGenericString());
  }

  private static HttpMethod currentHttpMethod() {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    if (!(requestAttributes instanceof ServletRequestAttributes servletRequestAttributes)) {
      throw new IllegalStateException("No current HTTP request is available");
    }

    return HttpMethod.valueOf(servletRequestAttributes.getRequest().getMethod());
  }

  private record RequiredPermission(Class<?> resourceType, ResourceAction action) {}
}
