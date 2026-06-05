package br.com.medflow.schemas.config;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

/**
 * Configuração base dos mappers da aplicação.
 */
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MedflowMapperConfig {
}
