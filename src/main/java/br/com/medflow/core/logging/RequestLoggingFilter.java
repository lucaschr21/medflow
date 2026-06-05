package br.com.medflow.core.logging;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Role;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.medflow.core.security.identity.CurrentAuthenticatedUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Registra uma linha de auditoria ao fim de cada requisição HTTP da API.
 *
 * <p>Exemplo de saída:
 *
 * <pre>{@code
 * jane.doe GET /api/usuarios?page=0 [200] 29ms
 * }</pre>
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class RequestLoggingFilter extends OncePerRequestFilter {

  static final String ANONYMOUS_USER = "anonymous";

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);

  private final CurrentAuthenticatedUser currentAuthenticatedUser;

  /**
   * Cria o filtro de auditoria das requisições HTTP.
   *
   * @param currentAuthenticatedUser acesso ao usuário autenticado atual
   */
  public RequestLoggingFilter(CurrentAuthenticatedUser currentAuthenticatedUser) {
    this.currentAuthenticatedUser = currentAuthenticatedUser;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String requestUri = request.getRequestURI();
    return !(requestUri.equals("/api") || requestUri.startsWith("/api/"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    long startedAt = System.nanoTime();
    String username = currentAuthenticatedUser.get()
        .map(user -> user.username().strip())
        .filter(usernameValue -> !usernameValue.isBlank())
        .orElse(ANONYMOUS_USER);
    String requestLine = requestLine(request);

    LOGGER.info("{} {}", username, requestLine);

    try {
      filterChain.doFilter(request, response);
    } finally {
      LOGGER.info(
          "{} {} [{}] {}ms",
          username,
          requestLine,
          response.getStatus(),
          elapsedMillis(startedAt));
    }
  }

  static String requestLine(HttpServletRequest request) {
    String queryString = request.getQueryString();
    if (queryString == null || queryString.isBlank()) {
      return request.getMethod() + " " + request.getRequestURI();
    }

    return request.getMethod() + " " + request.getRequestURI() + "?" + queryString;
  }

  private static long elapsedMillis(long startedAt) {
    return (System.nanoTime() - startedAt) / 1_000_000L;
  }
}
