package br.com.medflow.core.security.config;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.springframework.aop.Advisor;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.security.authorization.method.AuthorizationInterceptorsOrder;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.util.ClassUtils;

import br.com.medflow.core.security.annotations.AuthorizePermission;
import br.com.medflow.core.security.annotations.AuthorizeResource;
import br.com.medflow.core.security.authorization.AuthorizeResourceAuthorizationManager;

/**
 * Habilita a autorizacao por metodo do Spring Security.
 */
@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableMethodSecurity
public class MethodSecurityConfig {

  /**
   * Publica o interceptor das anotações de autorização funcional do projeto.
   *
   * @param authorizationManager gerenciador da anotacao
   * @return advisor da anotacao
   */
  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  Advisor authorizeResourceMethodInterceptor(
      AuthorizeResourceAuthorizationManager authorizationManager) {
    AuthorizationManagerBeforeMethodInterceptor interceptor = new AuthorizationManagerBeforeMethodInterceptor(
        new AuthorizationAnnotationPointcut(),
        authorizationManager);
    interceptor.setOrder(AuthorizationInterceptorsOrder.PRE_AUTHORIZE.getOrder() - 1);
    return interceptor;
  }

  private static final class AuthorizationAnnotationPointcut extends StaticMethodMatcherPointcut {

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
      Class<?> userClass = targetClass != null ? ClassUtils.getUserClass(targetClass) : method.getDeclaringClass();
      Method specificMethod = AopUtils.getMostSpecificMethod(method, userClass);
      return hasAuthorizationAnnotation(specificMethod) || hasAuthorizationAnnotation(userClass);
    }

    private static boolean hasAuthorizationAnnotation(AnnotatedElement element) {
      return AnnotatedElementUtils.hasAnnotation(element, AuthorizeResource.class)
          || AnnotatedElementUtils.hasAnnotation(element, AuthorizePermission.class);
    }
  }
}
