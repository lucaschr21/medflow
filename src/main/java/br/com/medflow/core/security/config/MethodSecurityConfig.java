package br.com.medflow.core.security.config;

import org.springframework.aop.Advisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.authorization.method.AuthorizationInterceptorsOrder;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

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
   * Publica o interceptor da anotacao {@link AuthorizeResource}.
   *
   * @param authorizationManager gerenciador da anotacao
   * @return advisor da anotacao
   */
  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  Advisor authorizeResourceMethodInterceptor(
      AuthorizeResourceAuthorizationManager authorizationManager) {
    AuthorizationManagerBeforeMethodInterceptor interceptor = new AuthorizationManagerBeforeMethodInterceptor(
        new AnnotationMatchingPointcut(
            AuthorizeResource.class,
            AuthorizeResource.class,
            true),
        authorizationManager);
    interceptor.setOrder(AuthorizationInterceptorsOrder.PRE_AUTHORIZE.getOrder() - 1);
    return interceptor;
  }
}
