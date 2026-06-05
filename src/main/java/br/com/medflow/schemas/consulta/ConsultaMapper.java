package br.com.medflow.schemas.consulta;

import java.util.UUID;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.medflow.entities.Consulta;
import br.com.medflow.schemas.config.MedflowMapperConfig;

/**
 * Mapper de consultas.
 */
@Mapper(config = MedflowMapperConfig.class)
public interface ConsultaMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "usuario", ignore = true)
  @Mapping(target = "medico", ignore = true)
  @Mapping(target = "consultorio", ignore = true)
  @Mapping(target = "alocacaoMedico", ignore = true)
  @Mapping(target = "registroAtendimento", ignore = true)
  @Mapping(target = "anexos", ignore = true)
  Consulta toEntity(ConsultaInput source);

  @Mapping(target = "usuarioId", source = "usuario.id")
  @Mapping(target = "medicoId", source = "medico.id")
  @Mapping(target = "consultorioId", source = "consultorio.id")
  @Mapping(target = "alocacaoMedicoId", source = "alocacaoMedico.id")
  @Mapping(target = "registroAtendimentoId", expression = "java(registroAtendimentoId(source))")
  ConsultaOutput toOutput(Consulta source);

  @BeanMapping(ignoreByDefault = false)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "usuario", ignore = true)
  @Mapping(target = "medico", ignore = true)
  @Mapping(target = "consultorio", ignore = true)
  @Mapping(target = "alocacaoMedico", ignore = true)
  @Mapping(target = "registroAtendimento", ignore = true)
  @Mapping(target = "anexos", ignore = true)
  void updateEntity(ConsultaInput source, @MappingTarget Consulta target);

  default UUID registroAtendimentoId(Consulta source) {
    return source.getRegistroAtendimento() == null ? null : source.getRegistroAtendimento().getId();
  }
}
