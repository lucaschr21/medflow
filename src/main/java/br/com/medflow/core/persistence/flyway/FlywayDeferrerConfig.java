package br.com.medflow.core.persistence.flyway;

import org.springframework.boot.flyway.autoconfigure.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManagerFactory;

/**
 * Garante que o Flyway execute somente depois da inicialização do Hibernate.
 */
@Configuration(proxyBeanMethods = false)
public class FlywayDeferrerConfig {

  /**
   * Força a criação do {@link EntityManagerFactory} antes da migração.
   *
   * @param entityManagerFactory entity manager factory da aplicação
   * @return estratégia de migração do Flyway
   */
  @Bean
  FlywayMigrationStrategy flywayMigrationStrategy(EntityManagerFactory entityManagerFactory) {
    return flyway -> {
      entityManagerFactory.getMetamodel();
      flyway.migrate();
    };
  }
}
