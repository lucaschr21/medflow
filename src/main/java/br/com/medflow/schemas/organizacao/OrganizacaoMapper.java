package br.com.medflow.schemas.organizacao;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.medflow.entities.Organizacao;
import br.com.medflow.schemas.config.MedflowMapperConfig;

/**
 * Mapper de organizações.
 */
@Mapper(config = MedflowMapperConfig.class)
public interface OrganizacaoMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "unidades", ignore = true)
  @Mapping(target = "usuarios", ignore = true)
  Organizacao toEntity(OrganizacaoInput source);

  OrganizacaoOutput toOutput(Organizacao source);

  @BeanMapping(ignoreByDefault = false)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "unidades", ignore = true)
  @Mapping(target = "usuarios", ignore = true)
  void updateEntity(OrganizacaoInput source, @MappingTarget Organizacao target);
}
