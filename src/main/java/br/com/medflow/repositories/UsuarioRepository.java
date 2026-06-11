package br.com.medflow.repositories;

import java.util.Optional;
import java.util.UUID;

import br.com.medflow.core.persistence.CommonRepository;
import br.com.medflow.entities.Usuario;

/**
 * Repository da entidade {@link Usuario}.
 */
public interface UsuarioRepository extends CommonRepository<Usuario, UUID> {

  /**
   * Localiza o usuário da organização pelo identificador externo do Keycloak.
   *
   * @param organizacaoId identificador da organização
   * @param keycloakId    identificador do usuário no Keycloak
   * @return usuário encontrado, quando existir
   */
  Optional<Usuario> findByOrganizacaoIdAndKeycloakId(UUID organizacaoId, UUID keycloakId);

  /**
   * Localiza o usuário pelo identificador do Keycloak (subject).
   *
   * @param keycloakId identificador do usuário no Keycloak
   * @return usuário encontrado, quando existir
   */
  Optional<Usuario> findByKeycloakId(UUID keycloakId);
}
