package br.com.medflow.schemas.atendimento.mapper;

import br.com.medflow.entities.atendimento.Consulta;
import br.com.medflow.schemas.atendimento.saida.ConsultaSaidaSchema;
import br.com.medflow.schemas.mapper.MedflowMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MedflowMapperConfig.class)
public interface ConsultaSchemaMapper {

  @Mapping(target = "organizacaoId", source = "paciente.organizacao.id")
  @Mapping(target = "pacienteId", source = "paciente.id")
  @Mapping(target = "medicoId", source = "medico.id")
  @Mapping(target = "consultorioId", source = "consultorio.id")
  @Mapping(target = "convenioId", source = "convenio.id")
  @Mapping(target = "canceladoPorUtilizadorId", source = "canceladoPor.id")
  ConsultaSaidaSchema toSaidaSchema(Consulta consulta);
}
