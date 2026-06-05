package br.com.medflow.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.medflow.core.exceptions.EntityNotFoundException;
import br.com.medflow.core.persistence.query.PageResult;
import br.com.medflow.core.persistence.query.RsqlQuery;
import br.com.medflow.entities.Organizacao;
import br.com.medflow.entities.Usuario;
import br.com.medflow.repositories.UsuarioRepository;
import br.com.medflow.schemas.usuario.UsuarioInput;
import br.com.medflow.schemas.usuario.UsuarioMapper;
import br.com.medflow.schemas.usuario.UsuarioOutput;

/**
 * Serviço de domínio para operações de usuários.
 */
@Service
@Transactional(readOnly = true)
public class UsuarioService {

  private final UsuarioRepository usuarioRepository;
  private final OrganizacaoService organizacaoService;
  private final UsuarioMapper usuarioMapper;

  /**
   * Cria o serviço com suas dependências.
   *
   * @param usuarioRepository repositório de usuários
   * @param organizacaoService serviço de organizações
   * @param usuarioMapper mapper de usuários
   */
  public UsuarioService(
      UsuarioRepository usuarioRepository,
      OrganizacaoService organizacaoService,
      UsuarioMapper usuarioMapper) {
    this.usuarioRepository = usuarioRepository;
    this.organizacaoService = organizacaoService;
    this.usuarioMapper = usuarioMapper;
  }

  /**
   * Obtém um usuário pelo identificador.
   *
   * @param usuarioId identificador do usuário
   * @return usuário encontrado
   */
  public Usuario findByIdOrThrow(UUID usuarioId) {
    return usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + usuarioId));
  }

  /**
   * Lista usuários com filtros e paginação.
   *
   * @param query filtro RSQL
   * @param pageable paginação e ordenação
   * @return página de usuários
   */
  public PageResult<UsuarioOutput> findAll(RsqlQuery query, Pageable pageable) {
    return usuarioRepository.findAll(query.toCriteria(pageable)).map(usuarioMapper::toOutput);
  }

  /**
   * Obtém um usuário pela organização e pelo identificador do Keycloak.
   *
   * @param organizacaoId identificador da organização
   * @param keycloakId identificador do usuário no Keycloak
   * @return usuário encontrado
   */
  public Usuario findByOrganizacaoAndKeycloakIdOrThrow(UUID organizacaoId, UUID keycloakId) {
    return usuarioRepository.findByOrganizacaoIdAndKeycloakId(organizacaoId, keycloakId)
        .orElseThrow(
            () -> new EntityNotFoundException(
                "Usuário não encontrado para a organização e identificador informados."));
  }

  /**
   * Persiste um novo usuário associado a uma organização.
   *
   * @param input dados do usuário
   * @return usuário persistido
   */
  @Transactional
  public UsuarioOutput create(UsuarioInput input) {
    Usuario usuario = usuarioMapper.toEntity(input);
    usuario.setOrganizacao(organizacaoService.findByIdOrThrow(input.organizacaoId()));
    return usuarioMapper.toOutput(usuarioRepository.save(usuario));
  }

  /**
   * Atualiza os dados de um usuário existente.
   *
   * @param usuarioId identificador do usuário
   * @param input novos dados
   * @return usuário atualizado
   */
  @Transactional
  public UsuarioOutput update(UUID usuarioId, UsuarioInput input) {
    Usuario target = findByIdOrThrow(usuarioId);
    Organizacao organizacao = organizacaoService.findByIdOrThrow(input.organizacaoId());
    target.setOrganizacao(organizacao);
    usuarioMapper.updateEntity(input, target);
    return usuarioMapper.toOutput(target);
  }

  /**
   * Inativa um usuário existente.
   *
   * @param usuarioId identificador do usuário
   */
  @Transactional
  public void deactivate(UUID usuarioId) {
    usuarioRepository.delete(findByIdOrThrow(usuarioId));
  }
}
