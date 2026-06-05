package br.com.medflow.core.security.authorization;

import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.context.annotation.Role;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Autoriza operacoes funcionais do Medflow a partir do tipo do recurso.
 *
 * <p>Este componente converte um tipo de recurso em authority no formato
 * {@code recurso:acao}, usando a convencao do projeto e, quando aplicavel, o
 * metodo HTTP da requisicao atual.
 */
@Component("functionalAuthorizer")
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class FunctionalAuthorizer {

  private final ProtectedResourceResolver protectedResourceResolver;

  /**
   * Cria o autorizador funcional.
   *
   * @param protectedResourceResolver resolvedor de nomes de recurso
   */
  public FunctionalAuthorizer(ProtectedResourceResolver protectedResourceResolver) {
    this.protectedResourceResolver = protectedResourceResolver;
  }

  /**
   * Indica se o usuario autenticado possui acesso ao recurso usando a acao
   * inferida do metodo HTTP atual.
   *
   * @param resourceType tipo do recurso
   * @return {@code true} quando o acesso funcional for permitido
   */
  public boolean hasAccess(Class<?> resourceType) {
    return hasAccess(resourceType, currentAction());
  }

  /**
   * Indica se o usuario autenticado possui acesso ao recurso para a acao
   * informada.
   *
   * @param resourceType tipo do recurso
   * @param action acao funcional desejada
   * @return {@code true} quando o acesso funcional for permitido
   */
  public boolean hasAccess(Class<?> resourceType, ResourceAction action) {
    return currentAuthentication()
        .map(authentication -> hasAccess(authentication, resourceType, action))
        .orElse(false);
  }

  /**
   * Indica se a autenticacao informada possui acesso ao recurso para a acao
   * desejada.
   *
   * @param authentication autenticacao a ser avaliada
   * @param resourceType tipo do recurso
   * @param action acao funcional desejada
   * @return {@code true} quando o acesso funcional for permitido
   */
  public boolean hasAccess(Authentication authentication, Class<?> resourceType, ResourceAction action) {
    return hasAuthority(authentication, permission(resourceType, action).authority());
  }

  /**
   * Valida o acesso funcional ao recurso usando a acao inferida do metodo HTTP
   * atual.
   *
   * @param resourceType tipo do recurso
   * @throws AccessDeniedException quando o acesso nao for permitido
   */
  public void checkAccess(Class<?> resourceType) {
    checkAccess(resourceType, currentAction());
  }

  /**
   * Valida o acesso funcional ao recurso para a acao informada.
   *
   * @param resourceType tipo do recurso
   * @param action acao funcional desejada
   * @throws AccessDeniedException quando o acesso nao for permitido
   */
  public void checkAccess(Class<?> resourceType, ResourceAction action) {
    currentAuthentication().ifPresentOrElse(
        authentication -> checkAccess(authentication, resourceType, action),
        () -> {
          throw new AccessDeniedException(
              "Access denied for authority: " + permission(resourceType, action).authority());
        });
  }

  /**
   * Valida o acesso funcional para a autenticacao informada.
   *
   * @param authentication autenticacao a ser avaliada
   * @param resourceType tipo do recurso
   * @param action acao funcional desejada
   * @throws AccessDeniedException quando o acesso nao for permitido
   */
  public void checkAccess(Authentication authentication, Class<?> resourceType, ResourceAction action) {
    FunctionalPermission permission = permission(resourceType, action);
    if (!hasAuthority(authentication, permission.authority())) {
      throw new AccessDeniedException("Access denied for authority: " + permission.authority());
    }
  }

  /**
   * Resolve a permissao funcional associada ao tipo informado.
   *
   * @param resourceType tipo do recurso
   * @param action acao funcional
   * @return permissao funcional resolvida
   */
  public FunctionalPermission permission(Class<?> resourceType, ResourceAction action) {
    return FunctionalPermission.of(protectedResourceResolver.resolve(resourceType), action);
  }

  private static Optional<Authentication> currentAuthentication() {
    return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .filter(Authentication::isAuthenticated)
        .filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken));
  }

  private static boolean hasAuthority(Authentication authentication, String authority) {
    return authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .anyMatch(authority::equals);
  }

  private static ResourceAction currentAction() {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    if (!(requestAttributes instanceof ServletRequestAttributes servletRequestAttributes)) {
      throw new IllegalStateException("No current HTTP request is available");
    }

    HttpServletRequest request = servletRequestAttributes.getRequest();
    return ResourceAction.from(org.springframework.http.HttpMethod.valueOf(request.getMethod()));
  }
}
