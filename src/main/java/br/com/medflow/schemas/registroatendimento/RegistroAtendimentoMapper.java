package br.com.medflow.schemas.registroatendimento;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.medflow.entities.RegistroAtendimento;
import br.com.medflow.schemas.config.MedflowMapperConfig;

/**
 * Mapper de registros de atendimento.
 */
@Mapper(config = MedflowMapperConfig.class)
public interface RegistroAtendimentoMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "consulta", ignore = true)
  @Mapping(target = "medico", ignore = true)
  RegistroAtendimento toEntity(RegistroAtendimentoInput source);

  @Mapping(target = "consultaId", source = "consulta.id")
  @Mapping(target = "medicoId", source = "medico.id")
  RegistroAtendimentoOutput toOutput(RegistroAtendimento source);

  @BeanMapping(ignoreByDefault = false)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "consulta", ignore = true)
  @Mapping(target = "medico", ignore = true)
  void updateEntity(RegistroAtendimentoInput source, @MappingTarget RegistroAtendimento target);
}
