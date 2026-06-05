package br.com.medflow.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.medflow.core.exceptions.EntityNotFoundException;
import br.com.medflow.core.persistence.query.PageResult;
import br.com.medflow.core.persistence.query.RsqlQuery;
import br.com.medflow.entities.AlocacaoMedico;
import br.com.medflow.entities.Consultorio;
import br.com.medflow.entities.Medico;
import br.com.medflow.repositories.AlocacaoMedicoRepository;
import br.com.medflow.schemas.alocacaomedico.AlocacaoMedicoInput;
import br.com.medflow.schemas.alocacaomedico.AlocacaoMedicoMapper;
import br.com.medflow.schemas.alocacaomedico.AlocacaoMedicoOutput;
import br.com.medflow.services.rules.alocacaomedico.AlocacaoMedicoRules;

/**
 * Serviço de domínio para operações de alocações médicas.
 */
@Service
@Transactional(readOnly = true)
public class AlocacaoMedicoService {

  private final AlocacaoMedicoRepository alocacaoMedicoRepository;
  private final MedicoService medicoService;
  private final ConsultorioService consultorioService;
  private final AlocacaoMedicoMapper alocacaoMedicoMapper;
  private final AlocacaoMedicoRules alocacaoMedicoRules;

  /**
   * Cria o serviço com suas dependências.
   *
   * @param alocacaoMedicoRepository repositório de alocações médicas
   * @param medicoService serviço de médicos
   * @param consultorioService serviço de consultórios
   * @param alocacaoMedicoMapper mapper de alocações médicas
   * @param alocacaoMedicoRules regras de alocações médicas
   */
  public AlocacaoMedicoService(
      AlocacaoMedicoRepository alocacaoMedicoRepository,
      MedicoService medicoService,
      ConsultorioService consultorioService,
      AlocacaoMedicoMapper alocacaoMedicoMapper,
      AlocacaoMedicoRules alocacaoMedicoRules) {
    this.alocacaoMedicoRepository = alocacaoMedicoRepository;
    this.medicoService = medicoService;
    this.consultorioService = consultorioService;
    this.alocacaoMedicoMapper = alocacaoMedicoMapper;
    this.alocacaoMedicoRules = alocacaoMedicoRules;
  }

  /**
   * Obtém uma alocação médica pelo identificador.
   *
   * @param alocacaoMedicoId identificador da alocação
   * @return alocação encontrada
   */
  public AlocacaoMedico findByIdOrThrow(UUID alocacaoMedicoId) {
    return alocacaoMedicoRepository.findById(alocacaoMedicoId)
        .orElseThrow(() -> new EntityNotFoundException("Alocação médica não encontrada: " + alocacaoMedicoId));
  }

  /**
   * Lista alocações médicas com filtros e paginação.
   *
   * @param query filtro RSQL
   * @param pageable paginação e ordenação
   * @return página de alocações médicas
   */
  public PageResult<AlocacaoMedicoOutput> findAll(RsqlQuery query, Pageable pageable) {
    return alocacaoMedicoRepository.findAll(query.toCriteria(pageable)).map(alocacaoMedicoMapper::toOutput);
  }

  /**
   * Persiste uma nova alocação médica.
   *
   * @param input dados da alocação
   * @return alocação persistida
   */
  @Transactional
  public AlocacaoMedicoOutput create(AlocacaoMedicoInput input) {
    AlocacaoMedico alocacaoMedico = alocacaoMedicoMapper.toEntity(input);
    Medico medico = medicoService.findByIdOrThrow(input.medicoId());
    Consultorio consultorio = consultorioService.findByIdOrThrow(input.consultorioId());
    alocacaoMedicoRules.validateAssociations(medico, consultorio);
    alocacaoMedico.setMedico(medico);
    alocacaoMedico.setConsultorio(consultorio);
    return alocacaoMedicoMapper.toOutput(alocacaoMedicoRepository.save(alocacaoMedico));
  }

  /**
   * Atualiza os dados de uma alocação médica existente.
   *
   * @param alocacaoMedicoId identificador da alocação
   * @param input novos dados
   * @return alocação atualizada
   */
  @Transactional
  public AlocacaoMedicoOutput update(UUID alocacaoMedicoId, AlocacaoMedicoInput input) {
    AlocacaoMedico target = findByIdOrThrow(alocacaoMedicoId);
    Medico medico = medicoService.findByIdOrThrow(input.medicoId());
    Consultorio consultorio = consultorioService.findByIdOrThrow(input.consultorioId());
    alocacaoMedicoRules.validateAssociations(medico, consultorio);
    target.setMedico(medico);
    target.setConsultorio(consultorio);
    alocacaoMedicoMapper.updateEntity(input, target);
    return alocacaoMedicoMapper.toOutput(target);
  }

  /**
   * Inativa uma alocação médica existente.
   *
   * @param alocacaoMedicoId identificador da alocação
   */
  @Transactional
  public void deactivate(UUID alocacaoMedicoId) {
    alocacaoMedicoRepository.delete(findByIdOrThrow(alocacaoMedicoId));
  }
}
