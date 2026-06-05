package br.com.medflow.schemas.medico;

import java.util.Set;
import java.util.UUID;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.medflow.entities.Especialidade;
import br.com.medflow.entities.Medico;
import br.com.medflow.schemas.config.MedflowMapperConfig;

/**
 * Mapper de médicos.
 */
@Mapper(config = MedflowMapperConfig.class)
public interface MedicoMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "ativo", ignore = true)
  @Mapping(target = "usuario", ignore = true)
  @Mapping(target = "especialidades", ignore = true)
  @Mapping(target = "alocacoesMedico", ignore = true)
  @Mapping(target = "bloqueiosAgenda", ignore = true)
  @Mapping(target = "consultas", ignore = true)
  @Mapping(target = "registrosAtendimento", ignore = true)
  Medico toEntity(MedicoInput source);

  @Mapping(target = "usuarioId", source = "usuario.id")
  @Mapping(target = "especialidadeIds", expression = "java(especialidadeIds(source))")
  MedicoOutput toOutput(Medico source);

  @BeanMapping(ignoreByDefault = false)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "ativo", ignore = true)
  @Mapping(target = "usuario", ignore = true)
  @Mapping(target = "especialidades", ignore = true)
  @Mapping(target = "alocacoesMedico", ignore = true)
  @Mapping(target = "bloqueiosAgenda", ignore = true)
  @Mapping(target = "consultas", ignore = true)
  @Mapping(target = "registrosAtendimento", ignore = true)
  void updateEntity(MedicoInput source, @MappingTarget Medico target);

  default Set<UUID> especialidadeIds(Medico source) {
    return source.getEspecialidades().stream().map(Especialidade::getId).collect(java.util.stream.Collectors.toSet());
  }
}
