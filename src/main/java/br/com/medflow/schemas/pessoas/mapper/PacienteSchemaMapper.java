package br.com.medflow.schemas.pessoas.mapper;

import br.com.medflow.entities.pessoas.Paciente;
import br.com.medflow.schemas.mapper.MedflowMapperConfig;
import br.com.medflow.schemas.pessoas.saida.PacienteSaidaSchema;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MedflowMapperConfig.class)
public interface PacienteSchemaMapper {

  @Mapping(target = "organizacaoId", source = "organizacao.id")
  @Mapping(target = "utilizadorId", source = "utilizador.id")
  @Mapping(target = "processoClinicoId", source = "processoClinico.id")
  PacienteSaidaSchema toSaidaSchema(Paciente paciente);
}
