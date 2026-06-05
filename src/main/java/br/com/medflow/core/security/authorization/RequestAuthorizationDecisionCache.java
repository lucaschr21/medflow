package br.com.medflow.core.security.authorization;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Role;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Cacheia decisões de autorização durante a requisição atual.
 *
 * <p>Isso evita chamadas repetidas ao Keycloak quando o mesmo recurso é
 * validado mais de uma vez no mesmo fluxo HTTP.
 */
@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class RequestAuthorizationDecisionCache {

  private static final String ATTRIBUTE_NAME =
      RequestAuthorizationDecisionCache.class.getName() + ".DECISIONS";

  /**
   * Retorna a decisão em cache ou a calcula quando ainda não existir.
   *
   * @param authenticationKey chave que identifica a autenticação atual
   * @param permission permissão funcional consultada
   * @param loader cálculo da decisão quando o cache não tiver valor
   * @return decisão de autorização
   */
  public boolean getOrCompute(
      String authenticationKey, FunctionalPermission permission, BooleanSupplier loader) {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    if (!(requestAttributes instanceof ServletRequestAttributes servletRequestAttributes)) {
      return loader.getAsBoolean();
    }

    HttpServletRequest request = servletRequestAttributes.getRequest();
    @SuppressWarnings("unchecked")
    Map<DecisionCacheKey, Boolean> decisions =
        (Map<DecisionCacheKey, Boolean>) request.getAttribute(ATTRIBUTE_NAME);

    if (decisions == null) {
      decisions = new HashMap<>();
      request.setAttribute(ATTRIBUTE_NAME, decisions);
    }

    DecisionCacheKey cacheKey = new DecisionCacheKey(authenticationKey, permission);
    return decisions.computeIfAbsent(cacheKey, ignored -> loader.getAsBoolean());
  }

  private record DecisionCacheKey(String authenticationKey, FunctionalPermission permission) {

    private DecisionCacheKey {
      Objects.requireNonNull(authenticationKey);
      Objects.requireNonNull(permission);
    }
  }
}
