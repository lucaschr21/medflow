package br.com.medflow.core.logging.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

import br.com.medflow.core.logging.RequestLoggingFilter;
import br.com.medflow.core.security.identity.CurrentAuthenticatedUser;

/**
 * Configura o logging de requisições HTTP da aplicação.
 */
@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class RequestLoggingConfig {

  /**
   * Publica o filtro responsável por registrar a linha final de cada
   * requisição da API.
   *
   * @param currentAuthenticatedUser acesso ao usuário autenticado atual
   * @return filtro de logging das requisições
   */
  @Bean
  RequestLoggingFilter requestLoggingFilter(CurrentAuthenticatedUser currentAuthenticatedUser) {
    return new RequestLoggingFilter(currentAuthenticatedUser);
  }
}
