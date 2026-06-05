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

import br.com.medflow.core.security.annotations.AuthorizeResource;

/**
 * Autoriza metodos anotados com {@link AuthorizeResource}.
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
    AuthorizeResource annotation = findAnnotation(invocation);
    Authentication currentAuthentication = authentication.get();
    boolean granted = currentAuthentication != null
        && functionalAuthorizer.hasAccess(
            currentAuthentication, annotation.value(), ResourceAction.from(currentHttpMethod()));
    return new AuthorizationDecision(granted);
  }

  private static AuthorizeResource findAnnotation(MethodInvocation invocation) {
    Method method = invocation.getMethod();
    AuthorizeResource methodAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, AuthorizeResource.class);
    if (methodAnnotation != null) {
      return methodAnnotation;
    }

    Class<?> targetClass = AopProxyUtils.ultimateTargetClass(invocation.getThis());
    if (targetClass == null) {
      targetClass = method.getDeclaringClass();
    }

    AuthorizeResource typeAnnotation = AnnotatedElementUtils.findMergedAnnotation(targetClass, AuthorizeResource.class);
    if (typeAnnotation != null) {
      return typeAnnotation;
    }

    throw new IllegalStateException(
        "@AuthorizeResource was not found for method: " + method.toGenericString());
  }

  private static HttpMethod currentHttpMethod() {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    if (!(requestAttributes instanceof ServletRequestAttributes servletRequestAttributes)) {
      throw new IllegalStateException("No current HTTP request is available");
    }

    return HttpMethod.valueOf(servletRequestAttributes.getRequest().getMethod());
  }
}
