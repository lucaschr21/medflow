package br.com.medflow.core.openapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;

/**
 * Configuração central do SpringDoc/OpenAPI da aplicação.
 */
@Configuration(proxyBeanMethods = false)
@OpenAPIDefinition(
    info = @Info(
        title = "Medflow API",
        version = "v1",
        description = "Documentação da API HTTP do Medflow.",
        contact = @Contact(name = "Medflow")),
    security = @SecurityRequirement(name = SpringDocConfig.BEARER_AUTH_SCHEME))
@SecurityScheme(
    name = SpringDocConfig.BEARER_AUTH_SCHEME,
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "Opaque Bearer Token",
    description = "Token Bearer usado para autenticação no resource server.")
public class SpringDocConfig {

  static final String BEARER_AUTH_SCHEME = "bearerAuth";

  /**
   * Agrupa a API HTTP pública do backend.
   *
   * @return grupo principal da documentação OpenAPI
   */
  @Bean
  GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("public")
        .packagesToScan("br.com.medflow.controllers")
        .pathsToMatch("/api/**")
        .build();
  }
}
