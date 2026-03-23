package br.com.medflow.schemas.atendimento.mapper;

import br.com.medflow.entities.atendimento.ProcessoClinico;
import br.com.medflow.schemas.atendimento.saida.ProcessoClinicoSaidaSchema;
import br.com.medflow.schemas.mapper.MedflowMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MedflowMapperConfig.class)
public interface ProcessoClinicoSchemaMapper {

  @Mapping(target = "organizacaoId", source = "organizacao.id")
  @Mapping(target = "pacienteId", source = "paciente.id")
  ProcessoClinicoSaidaSchema toSaidaSchema(ProcessoClinico processoClinico);
}
