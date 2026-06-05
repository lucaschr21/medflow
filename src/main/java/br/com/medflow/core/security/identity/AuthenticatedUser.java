package br.com.medflow.core.security.identity;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

/**
 * Representa a identidade autenticada do usuário no Medflow.
 *
 * <p>Este tipo concentra os atributos relevantes do token já convertidos para
 * uma estrutura tipada da aplicação, evitando que o restante do backend
 * dependa de claims cruas.
 */
public record AuthenticatedUser(
    String subject,
    String username,
    String email,
    String name,
    String cpf,
    String telefone,
    LocalDate dataNascimento,
    Set<String> realmRoles,
    Map<String, Set<String>> resourceRoles,
    Set<String> groups) {

  /**
   * Cria uma identidade autenticada imutável.
   *
   * @param subject        identificador externo do usuário no provedor de
   *                       identidade
   * @param username       nome de usuário preferencial
   * @param email          e-mail do usuário
   * @param name           nome completo do usuário
   * @param cpf            cpf do usuário
   * @param telefone       telefone do usuário
   * @param dataNascimento data de nascimento do usuário
   * @param realmRoles     roles globais do realm
   * @param resourceRoles  roles por client em {@code resource_access}
   * @param groups         grupos do usuário
   */
  public AuthenticatedUser {
    realmRoles = realmRoles == null ? Set.of() : Set.copyOf(realmRoles);
    resourceRoles = resourceRoles == null
        ? Map.of()
        : resourceRoles.entrySet().stream()
            .collect(
                java.util.stream.Collectors.toUnmodifiableMap(
                    Map.Entry::getKey, entry -> Set.copyOf(entry.getValue())));
    groups = groups == null ? Set.of() : Set.copyOf(groups);
  }

  /**
   * Retorna as roles do usuário em um client específico.
   *
   * @param clientId identificador do client
   * @return roles associadas ao client
   */
  public Set<String> clientRoles(String clientId) {
    if (clientId == null || clientId.isBlank()) {
      return Set.of();
    }

    return resourceRoles.getOrDefault(clientId, Set.of());
  }

  /**
   * Indica se o usuário possui uma role no realm.
   *
   * @param role role consultada
   * @return {@code true} quando a role estiver presente
   */
  public boolean hasRealmRole(String role) {
    return role != null && realmRoles.contains(role);
  }

  /**
   * Indica se o usuário possui uma role em um client específico.
   *
   * @param clientId identificador do client
   * @param role     role consultada
   * @return {@code true} quando a role estiver presente
   */
  public boolean hasClientRole(String clientId, String role) {
    return role != null && clientRoles(clientId).contains(role);
  }

}
