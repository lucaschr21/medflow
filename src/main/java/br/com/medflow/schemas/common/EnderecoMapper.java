package br.com.medflow.schemas.common;

import org.mapstruct.Mapper;

import br.com.medflow.entities.Endereco;
import br.com.medflow.schemas.config.MedflowMapperConfig;

/**
 * Mapper de endereços.
 */
@Mapper(config = MedflowMapperConfig.class)
public interface EnderecoMapper {

  Endereco toEntity(EnderecoInput source);

  EnderecoOutput toOutput(Endereco source);
}
