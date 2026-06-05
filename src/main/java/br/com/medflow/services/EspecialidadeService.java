package br.com.medflow.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.medflow.core.persistence.EntityNotFoundException;
import br.com.medflow.entities.Especialidade;
import br.com.medflow.repositories.EspecialidadeRepository;
import br.com.medflow.schemas.especialidade.EspecialidadeInput;
import br.com.medflow.schemas.especialidade.EspecialidadeMapper;
import br.com.medflow.schemas.especialidade.EspecialidadeOutput;

/**
 * Serviço de domínio para operações de especialidades.
 */
@Service
@Transactional(readOnly = true)
public class EspecialidadeService {

  private final EspecialidadeRepository especialidadeRepository;
  private final EspecialidadeMapper especialidadeMapper;

  /**
   * Cria o serviço com suas dependências.
   *
   * @param especialidadeRepository repositório de especialidades
   * @param especialidadeMapper mapper de especialidades
   */
  public EspecialidadeService(
      EspecialidadeRepository especialidadeRepository,
      EspecialidadeMapper especialidadeMapper) {
    this.especialidadeRepository = especialidadeRepository;
    this.especialidadeMapper = especialidadeMapper;
  }

  /**
   * Obtém uma especialidade pelo identificador.
   *
   * @param especialidadeId identificador da especialidade
   * @return especialidade encontrada
   */
  public Especialidade findByIdOrThrow(UUID especialidadeId) {
    return especialidadeRepository.findById(especialidadeId)
        .orElseThrow(() -> new EntityNotFoundException("Especialidade não encontrada: " + especialidadeId));
  }

  /**
   * Obtém especialidades pelos identificadores informados.
   *
   * @param especialidadeIds identificadores das especialidades
   * @return especialidades encontradas
   */
  public Set<Especialidade> findAllByIdsOrThrow(Set<UUID> especialidadeIds) {
    List<Especialidade> especialidades = especialidadeRepository.findAllById(especialidadeIds);
    if (especialidades.size() != especialidadeIds.size()) {
      throw new EntityNotFoundException("Uma ou mais especialidades não foram encontradas.");
    }
    return Set.copyOf(especialidades);
  }

  /**
   * Persiste uma nova especialidade.
   *
   * @param input dados da especialidade
   * @return especialidade persistida
   */
  @Transactional
  public EspecialidadeOutput create(EspecialidadeInput input) {
    Especialidade especialidade = especialidadeMapper.toEntity(input);
    return especialidadeMapper.toOutput(especialidadeRepository.save(especialidade));
  }

  /**
   * Atualiza os dados de uma especialidade existente.
   *
   * @param especialidadeId identificador da especialidade
   * @param input novos dados
   * @return especialidade atualizada
   */
  @Transactional
  public EspecialidadeOutput update(UUID especialidadeId, EspecialidadeInput input) {
    Especialidade target = findByIdOrThrow(especialidadeId);
    especialidadeMapper.updateEntity(input, target);
    return especialidadeMapper.toOutput(target);
  }

  /**
   * Remove uma especialidade existente.
   *
   * @param especialidadeId identificador da especialidade
   */
  @Transactional
  public void delete(UUID especialidadeId) {
    especialidadeRepository.delete(findByIdOrThrow(especialidadeId));
  }
}
