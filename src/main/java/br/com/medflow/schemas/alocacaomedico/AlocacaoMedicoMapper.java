package br.com.medflow.schemas.alocacaomedico;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.medflow.entities.AlocacaoMedico;
import br.com.medflow.schemas.config.MedflowMapperConfig;

/**
 * Mapper de alocações médicas.
 */
@Mapper(config = MedflowMapperConfig.class)
public interface AlocacaoMedicoMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "ativo", ignore = true)
  @Mapping(target = "medico", ignore = true)
  @Mapping(target = "consultorio", ignore = true)
  @Mapping(target = "agendasMedicas", ignore = true)
  @Mapping(target = "consultas", ignore = true)
  AlocacaoMedico toEntity(AlocacaoMedicoInput source);

  @Mapping(target = "medicoId", source = "medico.id")
  @Mapping(target = "consultorioId", source = "consultorio.id")
  AlocacaoMedicoOutput toOutput(AlocacaoMedico source);

  @BeanMapping(ignoreByDefault = false)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "ativo", ignore = true)
  @Mapping(target = "medico", ignore = true)
  @Mapping(target = "consultorio", ignore = true)
  @Mapping(target = "agendasMedicas", ignore = true)
  @Mapping(target = "consultas", ignore = true)
  void updateEntity(AlocacaoMedicoInput source, @MappingTarget AlocacaoMedico target);
}
