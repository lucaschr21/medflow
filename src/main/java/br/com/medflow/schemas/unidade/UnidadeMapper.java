package br.com.medflow.schemas.unidade;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.medflow.entities.Unidade;
import br.com.medflow.schemas.common.EnderecoMapper;
import br.com.medflow.schemas.config.MedflowMapperConfig;

/**
 * Mapper de unidades.
 */
@Mapper(config = MedflowMapperConfig.class, uses = EnderecoMapper.class)
public interface UnidadeMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "ativo", ignore = true)
  @Mapping(target = "organizacao", ignore = true)
  @Mapping(target = "consultorios", ignore = true)
  Unidade toEntity(UnidadeInput source);

  @Mapping(target = "organizacaoId", source = "organizacao.id")
  UnidadeOutput toOutput(Unidade source);

  @BeanMapping(ignoreByDefault = false)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "ativo", ignore = true)
  @Mapping(target = "organizacao", ignore = true)
  @Mapping(target = "consultorios", ignore = true)
  void updateEntity(UnidadeInput source, @MappingTarget Unidade target);
}
