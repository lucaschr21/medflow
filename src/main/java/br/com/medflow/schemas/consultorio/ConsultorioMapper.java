package br.com.medflow.schemas.consultorio;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.medflow.entities.Consultorio;
import br.com.medflow.schemas.config.MedflowMapperConfig;

/**
 * Mapper de consultórios.
 */
@Mapper(config = MedflowMapperConfig.class)
public interface ConsultorioMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "ativo", ignore = true)
  @Mapping(target = "unidade", ignore = true)
  @Mapping(target = "alocacoesMedico", ignore = true)
  @Mapping(target = "bloqueiosAgenda", ignore = true)
  @Mapping(target = "consultas", ignore = true)
  Consultorio toEntity(ConsultorioInput source);

  @Mapping(target = "unidadeId", source = "unidade.id")
  ConsultorioOutput toOutput(Consultorio source);

  @BeanMapping(ignoreByDefault = false)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "ativo", ignore = true)
  @Mapping(target = "unidade", ignore = true)
  @Mapping(target = "alocacoesMedico", ignore = true)
  @Mapping(target = "bloqueiosAgenda", ignore = true)
  @Mapping(target = "consultas", ignore = true)
  void updateEntity(ConsultorioInput source, @MappingTarget Consultorio target);
}
