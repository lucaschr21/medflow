package br.com.medflow.schemas.anexoconsulta;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.medflow.entities.AnexoConsulta;
import br.com.medflow.schemas.config.MedflowMapperConfig;

/**
 * Mapper de anexos de consulta.
 */
@Mapper(config = MedflowMapperConfig.class)
public interface AnexoConsultaMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "consulta", ignore = true)
  AnexoConsulta toEntity(AnexoConsultaInput source);

  @Mapping(target = "consultaId", source = "consulta.id")
  AnexoConsultaOutput toOutput(AnexoConsulta source);

  @BeanMapping(ignoreByDefault = false)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "consulta", ignore = true)
  void updateEntity(AnexoConsultaInput source, @MappingTarget AnexoConsulta target);
}
