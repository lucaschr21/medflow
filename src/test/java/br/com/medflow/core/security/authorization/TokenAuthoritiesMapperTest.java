package br.com.medflow.core.security.authorization;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import br.com.medflow.core.security.config.AuthorizationProperties;
import br.com.medflow.core.security.identity.AuthenticatedUser;
import br.com.medflow.core.security.identity.MedflowAuthenticatedPrincipal;

class TokenAuthoritiesMapperTest {

  @Test
  void shouldMapConfiguredClientRolesAndPreserveOriginalAuthorities() {
    TokenAuthoritiesMapper mapper = new TokenAuthoritiesMapper(
        new AuthorizationProperties("http://localhost:8085/token", "medflow-backend"));
    MedflowAuthenticatedPrincipal principal =
        new MedflowAuthenticatedPrincipal(
            new AuthenticatedUser(
                "keycloak-user-id",
                "jane.doe",
                "jane@medflow.com",
                "Jane Doe",
                "12345678901",
                "91999999999",
                LocalDate.parse("1990-04-10"),
                java.util.Set.of("default-roles-medflow"),
                Map.of(
                    "medflow-backend", java.util.Set.of("MEDICO", "USUARIO"),
                    "frontend-client", java.util.Set.of("ADMINISTRADOR")),
                java.util.Set.of("MEDICOS")),
            Map.of(),
            List.of(new SimpleGrantedAuthority("SCOPE_profile")));

    List<String> authorities =
        mapper.convert(principal).stream().map(grantedAuthority -> grantedAuthority.getAuthority()).toList();

    assertTrue(authorities.contains("ROLE_MEDICO"));
    assertTrue(authorities.contains("ROLE_USUARIO"));
    assertTrue(authorities.contains("SCOPE_profile"));
    assertFalse(authorities.contains("ROLE_ADMINISTRADOR"));
    assertFalse(authorities.contains("usuario:read"));
  }
}
