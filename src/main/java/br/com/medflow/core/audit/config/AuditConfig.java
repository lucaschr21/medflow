package br.com.medflow.core.audit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import br.com.medflow.core.audit.CurrentAuditorAware;
import br.com.medflow.core.security.identity.CurrentAuthenticatedUser;

/**
 * Configuração do JPA Auditing do Medflow.
 */
@Configuration(proxyBeanMethods = false)
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditConfig {

  /**
   * Registra o provider do auditor atual usado pelo Spring Data JPA.
   *
   * @param currentAuthenticatedUser acesso ao usuário autenticado atual
   * @return provider do auditor atual
   */
  @Bean
  AuditorAware<String> auditorAware(CurrentAuthenticatedUser currentAuthenticatedUser) {
    return new CurrentAuditorAware(currentAuthenticatedUser);
  }
}
