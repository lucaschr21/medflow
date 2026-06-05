package br.com.medflow.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.medflow.core.exceptions.EntityNotFoundException;
import br.com.medflow.core.persistence.query.PageResult;
import br.com.medflow.core.persistence.query.RsqlQuery;
import br.com.medflow.entities.Consulta;
import br.com.medflow.entities.Medico;
import br.com.medflow.entities.RegistroAtendimento;
import br.com.medflow.repositories.RegistroAtendimentoRepository;
import br.com.medflow.schemas.registroatendimento.RegistroAtendimentoInput;
import br.com.medflow.schemas.registroatendimento.RegistroAtendimentoMapper;
import br.com.medflow.schemas.registroatendimento.RegistroAtendimentoOutput;
import br.com.medflow.services.rules.registroatendimento.RegistroAtendimentoRules;

/**
 * Serviço de domínio para operações de registros de atendimento.
 */
@Service
@Transactional(readOnly = true)
public class RegistroAtendimentoService {

  private final RegistroAtendimentoRepository registroAtendimentoRepository;
  private final ConsultaService consultaService;
  private final MedicoService medicoService;
  private final RegistroAtendimentoMapper registroAtendimentoMapper;
  private final RegistroAtendimentoRules registroAtendimentoRules;

  /**
   * Cria o serviço com suas dependências.
   *
   * @param registroAtendimentoRepository repositório de registros de atendimento
   * @param consultaService serviço de consultas
   * @param medicoService serviço de médicos
   * @param registroAtendimentoMapper mapper de registros de atendimento
   * @param registroAtendimentoRules regras de registros de atendimento
   */
  public RegistroAtendimentoService(
      RegistroAtendimentoRepository registroAtendimentoRepository,
      ConsultaService consultaService,
      MedicoService medicoService,
      RegistroAtendimentoMapper registroAtendimentoMapper,
      RegistroAtendimentoRules registroAtendimentoRules) {
    this.registroAtendimentoRepository = registroAtendimentoRepository;
    this.consultaService = consultaService;
    this.medicoService = medicoService;
    this.registroAtendimentoMapper = registroAtendimentoMapper;
    this.registroAtendimentoRules = registroAtendimentoRules;
  }

  /**
   * Obtém um registro de atendimento pelo identificador.
   *
   * @param registroAtendimentoId identificador do registro
   * @return registro encontrado
   */
  public RegistroAtendimento findByIdOrThrow(UUID registroAtendimentoId) {
    return registroAtendimentoRepository.findById(registroAtendimentoId).orElseThrow(
        () -> new EntityNotFoundException("Registro de atendimento não encontrado: " + registroAtendimentoId));
  }

  /**
   * Lista registros de atendimento com filtros e paginação.
   *
   * @param query filtro RSQL
   * @param pageable paginação e ordenação
   * @return página de registros de atendimento
   */
  public PageResult<RegistroAtendimentoOutput> findAll(RsqlQuery query, Pageable pageable) {
    return registroAtendimentoRepository.findAll(query.toCriteria(pageable)).map(registroAtendimentoMapper::toOutput);
  }

  /**
   * Persiste um novo registro de atendimento.
   *
   * @param input dados do registro
   * @return registro persistido
   */
  @Transactional
  public RegistroAtendimentoOutput create(RegistroAtendimentoInput input) {
    RegistroAtendimento registroAtendimento = registroAtendimentoMapper.toEntity(input);
    Consulta consulta = consultaService.findByIdOrThrow(input.consultaId());
    Medico medico = medicoService.findByIdOrThrow(input.medicoId());
    registroAtendimentoRules.validateAssociations(consulta, medico);
    registroAtendimento.setConsulta(consulta);
    registroAtendimento.setMedico(medico);
    return registroAtendimentoMapper.toOutput(registroAtendimentoRepository.save(registroAtendimento));
  }

  /**
   * Atualiza os dados de um registro de atendimento existente.
   *
   * @param registroAtendimentoId identificador do registro
   * @param input novos dados
   * @return registro atualizado
   */
  @Transactional
  public RegistroAtendimentoOutput update(UUID registroAtendimentoId, RegistroAtendimentoInput input) {
    RegistroAtendimento target = findByIdOrThrow(registroAtendimentoId);
    Consulta consulta = consultaService.findByIdOrThrow(input.consultaId());
    Medico medico = medicoService.findByIdOrThrow(input.medicoId());
    registroAtendimentoRules.validateAssociations(consulta, medico);
    target.setConsulta(consulta);
    target.setMedico(medico);
    registroAtendimentoMapper.updateEntity(input, target);
    return registroAtendimentoMapper.toOutput(target);
  }

  /**
   * Remove um registro de atendimento existente.
   *
   * @param registroAtendimentoId identificador do registro
   */
  @Transactional
  public void delete(UUID registroAtendimentoId) {
    registroAtendimentoRepository.delete(findByIdOrThrow(registroAtendimentoId));
  }
}
