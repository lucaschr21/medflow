package br.com.medflow.schemas.usuario;

import java.util.UUID;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.medflow.entities.Usuario;
import br.com.medflow.schemas.config.MedflowMapperConfig;

/**
 * Mapper de usuários.
 */
@Mapper(config = MedflowMapperConfig.class)
public interface UsuarioMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "ativo", ignore = true)
  @Mapping(target = "organizacao", ignore = true)
  @Mapping(target = "medico", ignore = true)
  @Mapping(target = "consultas", ignore = true)
  Usuario toEntity(UsuarioInput source);

  @Mapping(target = "organizacaoId", source = "organizacao.id")
  @Mapping(target = "medicoId", expression = "java(medicoId(source))")
  UsuarioOutput toOutput(Usuario source);

  @BeanMapping(ignoreByDefault = false)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "ativo", ignore = true)
  @Mapping(target = "organizacao", ignore = true)
  @Mapping(target = "medico", ignore = true)
  @Mapping(target = "consultas", ignore = true)
  void updateEntity(UsuarioInput source, @MappingTarget Usuario target);

  default UUID medicoId(Usuario source) {
    return source.getMedico() == null ? null : source.getMedico().getId();
  }
}
