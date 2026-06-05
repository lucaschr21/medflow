package br.com.medflow.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.medflow.core.exceptions.EntityNotFoundException;
import br.com.medflow.core.persistence.query.PageResult;
import br.com.medflow.core.persistence.query.RsqlQuery;
import br.com.medflow.entities.AnexoConsulta;
import br.com.medflow.repositories.AnexoConsultaRepository;
import br.com.medflow.schemas.anexoconsulta.AnexoConsultaInput;
import br.com.medflow.schemas.anexoconsulta.AnexoConsultaMapper;
import br.com.medflow.schemas.anexoconsulta.AnexoConsultaOutput;

/**
 * Serviço de domínio para operações de anexos de consulta.
 */
@Service
@Transactional(readOnly = true)
public class AnexoConsultaService {

  private final AnexoConsultaRepository anexoConsultaRepository;
  private final ConsultaService consultaService;
  private final AnexoConsultaMapper anexoConsultaMapper;

  /**
   * Cria o serviço com suas dependências.
   *
   * @param anexoConsultaRepository repositório de anexos de consulta
   * @param consultaService serviço de consultas
   * @param anexoConsultaMapper mapper de anexos de consulta
   */
  public AnexoConsultaService(
      AnexoConsultaRepository anexoConsultaRepository,
      ConsultaService consultaService,
      AnexoConsultaMapper anexoConsultaMapper) {
    this.anexoConsultaRepository = anexoConsultaRepository;
    this.consultaService = consultaService;
    this.anexoConsultaMapper = anexoConsultaMapper;
  }

  /**
   * Obtém um anexo de consulta pelo identificador.
   *
   * @param anexoConsultaId identificador do anexo
   * @return anexo encontrado
   */
  public AnexoConsulta findByIdOrThrow(UUID anexoConsultaId) {
    return anexoConsultaRepository.findById(anexoConsultaId)
        .orElseThrow(() -> new EntityNotFoundException("Anexo de consulta não encontrado: " + anexoConsultaId));
  }

  /**
   * Lista anexos de consulta com filtros e paginação.
   *
   * @param query filtro RSQL
   * @param pageable paginação e ordenação
   * @return página de anexos de consulta
   */
  public PageResult<AnexoConsultaOutput> findAll(RsqlQuery query, Pageable pageable) {
    return anexoConsultaRepository.findAll(query.toCriteria(pageable)).map(anexoConsultaMapper::toOutput);
  }

  /**
   * Persiste um novo anexo de consulta.
   *
   * @param input dados do anexo
   * @return anexo persistido
   */
  @Transactional
  public AnexoConsultaOutput create(AnexoConsultaInput input) {
    AnexoConsulta anexoConsulta = anexoConsultaMapper.toEntity(input);
    anexoConsulta.setConsulta(consultaService.findByIdOrThrow(input.consultaId()));
    return anexoConsultaMapper.toOutput(anexoConsultaRepository.save(anexoConsulta));
  }

  /**
   * Atualiza os dados de um anexo de consulta existente.
   *
   * @param anexoConsultaId identificador do anexo
   * @param input novos dados
   * @return anexo atualizado
   */
  @Transactional
  public AnexoConsultaOutput update(UUID anexoConsultaId, AnexoConsultaInput input) {
    AnexoConsulta target = findByIdOrThrow(anexoConsultaId);
    target.setConsulta(consultaService.findByIdOrThrow(input.consultaId()));
    anexoConsultaMapper.updateEntity(input, target);
    return anexoConsultaMapper.toOutput(target);
  }

  /**
   * Remove um anexo de consulta existente.
   *
   * @param anexoConsultaId identificador do anexo
   */
  @Transactional
  public void delete(UUID anexoConsultaId) {
    anexoConsultaRepository.delete(findByIdOrThrow(anexoConsultaId));
  }
}
