package br.com.medflow.core.security.authorization;

import org.springframework.security.core.Authentication;

/**
 * Resolve se uma autenticação possui uma permissão funcional específica.
 */
@FunctionalInterface
public interface FunctionalPermissionDecisionService {

  /**
   * Indica se a autenticação possui a permissão funcional informada.
   *
   * @param authentication autenticação avaliada
   * @param permission permissão funcional desejada
   * @return {@code true} quando o acesso funcional for permitido
   */
  boolean isGranted(Authentication authentication, FunctionalPermission permission);
}
