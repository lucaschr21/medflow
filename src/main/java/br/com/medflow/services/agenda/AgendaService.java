package br.com.medflow.services.agenda;

import br.com.medflow.core.exceptions.NotFoundException;
import br.com.medflow.entities.agenda.Agenda;
import br.com.medflow.repositories.agenda.AgendaRepository;
import br.com.medflow.schemas.agenda.entrada.AgendaPorIdEntradaSchema;
import br.com.medflow.schemas.agenda.entrada.ManipularBloqueioAgendaEntradaSchema;
import br.com.medflow.schemas.agenda.entrada.RedefinirDiasTrabalhoEntradaSchema;
import br.com.medflow.schemas.agenda.mapper.AgendaSchemaMapper;
import br.com.medflow.schemas.agenda.saida.AgendaSaidaSchema;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgendaService {

  private final AgendaRepository agendaRepository;
  private final AgendaSchemaMapper agendaSchemaMapper;

  public AgendaSaidaSchema buscarPorIdDaOrganizacao(AgendaPorIdEntradaSchema entrada) {
    UUID organizacaoId =
        Objects.requireNonNull(entrada.organizacaoId(), "Id da organização é obrigatório");
    UUID agendaId = Objects.requireNonNull(entrada.agendaId(), "Id da agenda é obrigatório");
    return agendaSchemaMapper.toSaidaSchema(
        buscarEntidadePorIdDaOrganizacao(organizacaoId, agendaId));
  }

  @Transactional
  public AgendaSaidaSchema redefinirDiasTrabalho(RedefinirDiasTrabalhoEntradaSchema entrada) {
    AgendaPorIdEntradaSchema agendaEntrada =
        Objects.requireNonNull(entrada.agenda(), "Dados da agenda são obrigatórios");
    UUID organizacaoId =
        Objects.requireNonNull(agendaEntrada.organizacaoId(), "Id da organização é obrigatório");
    UUID agendaId = Objects.requireNonNull(agendaEntrada.agendaId(), "Id da agenda é obrigatório");
    Agenda agenda = buscarEntidadePorIdDaOrganizacao(organizacaoId, agendaId);
    agenda.getHorarioTrabalho().clear();
    Objects.requireNonNull(entrada.diasTrabalho(), "Dias de trabalho são obrigatórios")
        .forEach(agenda::adicionarDiaTrabalho);
    return agendaSchemaMapper.toSaidaSchema(agendaRepository.save(agenda));
  }

  @Transactional
  public AgendaSaidaSchema adicionarBloqueio(ManipularBloqueioAgendaEntradaSchema entrada) {
    AgendaPorIdEntradaSchema agendaEntrada =
        Objects.requireNonNull(entrada.agenda(), "Dados da agenda são obrigatórios");
    UUID organizacaoId =
        Objects.requireNonNull(agendaEntrada.organizacaoId(), "Id da organização é obrigatório");
    UUID agendaId = Objects.requireNonNull(agendaEntrada.agendaId(), "Id da agenda é obrigatório");
    Agenda agenda = buscarEntidadePorIdDaOrganizacao(organizacaoId, agendaId);
    agenda.adicionarBloqueio(
        Objects.requireNonNull(entrada.inicio(), "Início do bloqueio é obrigatório"),
        Objects.requireNonNull(entrada.fim(), "Fim do bloqueio é obrigatório"),
        Objects.requireNonNull(entrada.escopo(), "Escopo do bloqueio é obrigatório"));
    return agendaSchemaMapper.toSaidaSchema(agendaRepository.save(agenda));
  }

  @Transactional
  public AgendaSaidaSchema removerBloqueio(ManipularBloqueioAgendaEntradaSchema entrada) {
    AgendaPorIdEntradaSchema agendaEntrada =
        Objects.requireNonNull(entrada.agenda(), "Dados da agenda são obrigatórios");
    UUID organizacaoId =
        Objects.requireNonNull(agendaEntrada.organizacaoId(), "Id da organização é obrigatório");
    UUID agendaId = Objects.requireNonNull(agendaEntrada.agendaId(), "Id da agenda é obrigatório");
    Agenda agenda = buscarEntidadePorIdDaOrganizacao(organizacaoId, agendaId);
    agenda.removerBloqueio(
        Objects.requireNonNull(entrada.inicio(), "Início do bloqueio é obrigatório"),
        Objects.requireNonNull(entrada.fim(), "Fim do bloqueio é obrigatório"),
        Objects.requireNonNull(entrada.escopo(), "Escopo do bloqueio é obrigatório"));
    return agendaSchemaMapper.toSaidaSchema(agendaRepository.save(agenda));
  }

  private Agenda buscarEntidadePorIdDaOrganizacao(UUID organizacaoId, UUID agendaId) {
    return agendaRepository
        .findByIdAndVinculacaoConsultorioMedicoConsultorioUnidadeOrganizacaoId(
            agendaId, organizacaoId)
        .orElseThrow(() -> new NotFoundException("Agenda não encontrada na organização informada"));
  }
}
