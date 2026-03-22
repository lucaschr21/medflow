package br.com.medflow.schemas.agenda.mapper;

import br.com.medflow.entities.agenda.Agenda;
import br.com.medflow.entities.agenda.BloqueioAgenda;
import br.com.medflow.schemas.agenda.saida.AgendaSaidaSchema;
import br.com.medflow.schemas.mapper.MedflowMapperConfig;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MedflowMapperConfig.class)
public interface AgendaSchemaMapper {

  @Mapping(
      target = "organizacaoId",
      source = "vinculacaoConsultorioMedico.consultorio.unidade.organizacao.id")
  @Mapping(target = "consultorioMedicoId", source = "vinculacaoConsultorioMedico.id")
  @Mapping(target = "diasTrabalho", source = "horarioTrabalho")
  @Mapping(
      target = "quantidadeBloqueios",
      source = "bloqueios",
      qualifiedByName = "contarBloqueios")
  AgendaSaidaSchema toSaidaSchema(Agenda agenda);

  @Named("contarBloqueios")
  default int contarBloqueios(Set<BloqueioAgenda> bloqueios) {
    return bloqueios == null ? 0 : bloqueios.size();
  }
}
