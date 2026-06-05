package br.com.medflow.schemas.especialidade;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.medflow.entities.Especialidade;
import br.com.medflow.schemas.config.MedflowMapperConfig;

/**
 * Mapper de especialidades.
 */
@Mapper(config = MedflowMapperConfig.class)
public interface EspecialidadeMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "medicos", ignore = true)
  Especialidade toEntity(EspecialidadeInput source);

  EspecialidadeOutput toOutput(Especialidade source);

  @BeanMapping(ignoreByDefault = false)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "medicos", ignore = true)
  void updateEntity(EspecialidadeInput source, @MappingTarget Especialidade target);
}
