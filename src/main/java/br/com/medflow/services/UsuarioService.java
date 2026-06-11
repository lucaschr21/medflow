package br.com.medflow.services;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.medflow.core.exceptions.BusinessRuleException;
import br.com.medflow.core.exceptions.EntityNotFoundException;
import br.com.medflow.core.exceptions.ErrorCode;
import br.com.medflow.core.persistence.query.PageResult;
import br.com.medflow.core.persistence.query.RsqlQuery;
import br.com.medflow.core.security.identity.IdentityProviderAdminPort;
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

  private static final Set<String> TIPOS_VALIDOS = Set.of("ADMINISTRADOR", "RECEPCIONISTA", "MEDICO", "USUARIO");

  private final UsuarioRepository usuarioRepository;
  private final OrganizacaoService organizacaoService;
  private final UsuarioMapper usuarioMapper;
  private final IdentityProviderAdminPort identityProvider;

  public UsuarioService(
      UsuarioRepository usuarioRepository,
      OrganizacaoService organizacaoService,
      UsuarioMapper usuarioMapper,
      IdentityProviderAdminPort identityProvider) {
    this.usuarioRepository = usuarioRepository;
    this.organizacaoService = organizacaoService;
    this.usuarioMapper = usuarioMapper;
    this.identityProvider = identityProvider;
  }

  // ---- Operações de leitura ----

  public Usuario findByIdOrThrow(UUID usuarioId) {
    return usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + usuarioId));
  }

  public UsuarioOutput findById(UUID usuarioId) {
    return usuarioMapper.toOutput(findByIdOrThrow(usuarioId));
  }

  public PageResult<UsuarioOutput> findAll(RsqlQuery query, Pageable pageable) {
    return usuarioRepository.findAll(query.toCriteria(pageable)).map(usuarioMapper::toOutput);
  }

  public Usuario findByOrganizacaoAndKeycloakIdOrThrow(UUID organizacaoId, UUID keycloakId) {
    return usuarioRepository.findByOrganizacaoIdAndKeycloakId(organizacaoId, keycloakId)
        .orElseThrow(
            () -> new EntityNotFoundException(
                "Usuário não encontrado para a organização e identificador informados."));
  }

  // ---- Criação de usuário com integração Keycloak ----

  /**
   * Cria um usuário no Keycloak e persiste o vínculo local.
   */
  @Transactional
  public UsuarioOutput create(UsuarioInput input) {
    validarTipoAcesso(input.tipoAcesso());

    Organizacao organizacao = organizacaoService.findByIdOrThrow(input.organizacaoId());

    String keycloakId = identityProvider.criarUsuario(
        input.username(), input.email(), input.firstName(), input.lastName(),
        Set.of(input.tipoAcesso()));

    Usuario usuario = new Usuario();
    usuario.setOrganizacao(organizacao);
    usuario.setKeycloakId(UUID.fromString(keycloakId));

    return usuarioMapper.toOutput(usuarioRepository.save(usuario));
  }

  // ---- Atualização e inativação ----

  @Transactional
  public UsuarioOutput update(UUID usuarioId, UsuarioInput input) {
    Usuario target = findByIdOrThrow(usuarioId);

    identityProvider.atualizarUsuario(
        target.getKeycloakId().toString(),
        input.username(), input.email(), input.firstName(), input.lastName());

    return usuarioMapper.toOutput(target);
  }

  @Transactional
  public void deactivate(UUID usuarioId) {
    Usuario usuario = findByIdOrThrow(usuarioId);
    identityProvider.desabilitarUsuario(usuario.getKeycloakId().toString());
    usuarioRepository.delete(usuario);
  }

  private void validarTipoAcesso(String tipoAcesso) {
    if (!TIPOS_VALIDOS.contains(tipoAcesso)) {
      throw new BusinessRuleException(
          ErrorCode.VALIDATION_ERROR,
          "Tipo de acesso inválido: " + tipoAcesso + ". Use: ADMINISTRADOR, RECEPCIONISTA, MEDICO ou USUARIO.");
    }
  }
}
