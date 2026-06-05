package br.com.medflow.services;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.medflow.core.exceptions.EntityNotFoundException;
import br.com.medflow.core.persistence.query.PageResult;
import br.com.medflow.core.persistence.query.RsqlQuery;
import br.com.medflow.entities.Especialidade;
import br.com.medflow.entities.Medico;
import br.com.medflow.entities.Usuario;
import br.com.medflow.repositories.MedicoRepository;
import br.com.medflow.schemas.medico.MedicoInput;
import br.com.medflow.schemas.medico.MedicoMapper;
import br.com.medflow.schemas.medico.MedicoOutput;

/**
 * Serviço de domínio para operações de médicos.
 */
@Service
@Transactional(readOnly = true)
public class MedicoService {

  private final MedicoRepository medicoRepository;
  private final UsuarioService usuarioService;
  private final EspecialidadeService especialidadeService;
  private final MedicoMapper medicoMapper;

  /**
   * Cria o serviço com suas dependências.
   *
   * @param medicoRepository repositório de médicos
   * @param usuarioService serviço de usuários
   * @param especialidadeService serviço de especialidades
   * @param medicoMapper mapper de médicos
   */
  public MedicoService(
      MedicoRepository medicoRepository,
      UsuarioService usuarioService,
      EspecialidadeService especialidadeService,
      MedicoMapper medicoMapper) {
    this.medicoRepository = medicoRepository;
    this.usuarioService = usuarioService;
    this.especialidadeService = especialidadeService;
    this.medicoMapper = medicoMapper;
  }

  /**
   * Obtém um médico pelo identificador.
   *
   * @param medicoId identificador do médico
   * @return médico encontrado
   */
  public Medico findByIdOrThrow(UUID medicoId) {
    return medicoRepository.findById(medicoId)
        .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado: " + medicoId));
  }

  /**
   * Lista médicos com filtros e paginação.
   *
   * @param query filtro RSQL
   * @param pageable paginação e ordenação
   * @return página de médicos
   */
  public PageResult<MedicoOutput> findAll(RsqlQuery query, Pageable pageable) {
    return medicoRepository.findAll(query.toCriteria(pageable)).map(medicoMapper::toOutput);
  }

  /**
   * Obtém um médico pelo identificador do usuário associado.
   *
   * @param usuarioId identificador do usuário
   * @return médico encontrado
   */
  public Medico findByUsuarioIdOrThrow(UUID usuarioId) {
    return medicoRepository.findByUsuarioId(usuarioId)
        .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado para o usuário: " + usuarioId));
  }

  /**
   * Persiste um novo médico associado a um usuário e a especialidades.
   *
   * @param input dados do médico
   * @return médico persistido
   */
  @Transactional
  public MedicoOutput create(MedicoInput input) {
    Medico medico = medicoMapper.toEntity(input);
    medico.setUsuario(usuarioService.findByIdOrThrow(input.usuarioId()));
    replaceEspecialidades(medico, input.especialidadeIds());
    return medicoMapper.toOutput(medicoRepository.save(medico));
  }

  /**
   * Atualiza os dados de um médico existente.
   *
   * @param medicoId identificador do médico
   * @param input novos dados
   * @return médico atualizado
   */
  @Transactional
  public MedicoOutput update(UUID medicoId, MedicoInput input) {
    Medico target = findByIdOrThrow(medicoId);
    Usuario usuario = usuarioService.findByIdOrThrow(input.usuarioId());
    target.setUsuario(usuario);
    medicoMapper.updateEntity(input, target);
    replaceEspecialidades(target, input.especialidadeIds());
    return medicoMapper.toOutput(target);
  }

  /**
   * Inativa um médico existente.
   *
   * @param medicoId identificador do médico
   */
  @Transactional
  public void deactivate(UUID medicoId) {
    medicoRepository.delete(findByIdOrThrow(medicoId));
  }

  private void replaceEspecialidades(Medico medico, Set<UUID> especialidadeIds) {
    Set<Especialidade> especialidades = especialidadeIds == null || especialidadeIds.isEmpty()
        ? Set.of()
        : especialidadeService.findAllByIdsOrThrow(especialidadeIds);
    medico.getEspecialidades().clear();
    medico.getEspecialidades().addAll(new LinkedHashSet<>(especialidades));
  }
}
