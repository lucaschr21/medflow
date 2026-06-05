package br.com.medflow.core.security.authorization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import br.com.medflow.core.security.identity.AuthenticatedUser;
import br.com.medflow.core.security.identity.MedflowAuthenticatedPrincipal;

class TokenAuthoritiesMapperTest {

  @Test
  void shouldMapClientRolesAndAuthorizationPermissionsToAuthorities() {
    PermissionAuthoritiesMapper mapper = new PermissionAuthoritiesMapper();
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
                Map.of("medflow-backend", java.util.Set.of("MEDICO", "USUARIO")),
                java.util.Set.of("MEDICOS")),
            Map.of(
                "authorization",
                Map.of(
                    "permissions",
                    List.of(
                        Map.of("rsname", "usuario", "scopes", List.of("read", "update")),
                        Map.of("rsname", "consulta", "scopes", List.of("create"))))),
            List.of(new SimpleGrantedAuthority("SCOPE_profile")));

    List<String> authorities =
        mapper.convert(principal).stream().map(grantedAuthority -> grantedAuthority.getAuthority()).toList();

    assertTrue(authorities.contains("ROLE_MEDICO"));
    assertTrue(authorities.contains("ROLE_USUARIO"));
    assertTrue(authorities.contains("usuario:read"));
    assertTrue(authorities.contains("usuario:update"));
    assertTrue(authorities.contains("consulta:create"));
    assertTrue(authorities.contains("SCOPE_profile"));
  }
}
