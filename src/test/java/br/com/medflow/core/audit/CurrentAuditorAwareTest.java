package br.com.medflow.core.audit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.medflow.core.security.identity.AuthenticatedUser;
import br.com.medflow.core.security.identity.CurrentAuthenticatedUser;
import br.com.medflow.core.security.identity.MedflowAuthenticatedPrincipal;

class CurrentAuditorAwareTest {

  private final CurrentAuditorAware currentAuditorAware = new CurrentAuditorAware(new CurrentAuthenticatedUser());

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void shouldReturnAuthenticatedUsername() {
    AuthenticatedUser user = new AuthenticatedUser(
        "user-id",
        "jane.doe",
        "jane@medflow.com",
        "Jane Doe",
        "12345678901",
        "91999999999",
        LocalDate.parse("1990-04-10"),
        Set.of(),
        Map.of(),
        Set.of());
    MedflowAuthenticatedPrincipal principal = new MedflowAuthenticatedPrincipal(user, Map.of(), Set.of());
    SecurityContextHolder.getContext()
        .setAuthentication(
            new TestingAuthenticationToken(
                principal, "credentials", List.of(new SimpleGrantedAuthority("usuario:read"))));

    String auditor = currentAuditorAware.getCurrentAuditor().orElseThrow();

    assertEquals("jane.doe", auditor);
  }

  @Test
  void shouldReturnSystemWhenThereIsNoAuthenticatedUser() {
    String auditor = currentAuditorAware.getCurrentAuditor().orElseThrow();

    assertEquals("SISTEMA", auditor);
  }
}
