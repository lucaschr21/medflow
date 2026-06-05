package br.com.medflow.schemas.bloqueioagenda;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.medflow.entities.BloqueioAgenda;
import br.com.medflow.schemas.config.MedflowMapperConfig;

/**
 * Mapper de bloqueios de agenda.
 */
@Mapper(config = MedflowMapperConfig.class)
public interface BloqueioAgendaMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "medico", ignore = true)
  @Mapping(target = "consultorio", ignore = true)
  BloqueioAgenda toEntity(BloqueioAgendaInput source);

  @Mapping(target = "medicoId", source = "medico.id")
  @Mapping(target = "consultorioId", source = "consultorio.id")
  BloqueioAgendaOutput toOutput(BloqueioAgenda source);

  @BeanMapping(ignoreByDefault = false)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "medico", ignore = true)
  @Mapping(target = "consultorio", ignore = true)
  void updateEntity(BloqueioAgendaInput source, @MappingTarget BloqueioAgenda target);
}
