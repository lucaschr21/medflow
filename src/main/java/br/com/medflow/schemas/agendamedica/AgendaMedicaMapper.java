package br.com.medflow.schemas.agendamedica;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.medflow.entities.AgendaMedica;
import br.com.medflow.schemas.config.MedflowMapperConfig;

/**
 * Mapper de agendas médicas.
 */
@Mapper(config = MedflowMapperConfig.class)
public interface AgendaMedicaMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "alocacaoMedico", ignore = true)
  AgendaMedica toEntity(AgendaMedicaInput source);

  @Mapping(target = "alocacaoMedicoId", source = "alocacaoMedico.id")
  AgendaMedicaOutput toOutput(AgendaMedica source);

  @BeanMapping(ignoreByDefault = false)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "alocacaoMedico", ignore = true)
  void updateEntity(AgendaMedicaInput source, @MappingTarget AgendaMedica target);
}
