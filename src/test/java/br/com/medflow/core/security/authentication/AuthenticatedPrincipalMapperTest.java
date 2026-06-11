package br.com.medflow.core.security.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;

import br.com.medflow.core.security.identity.MedflowAuthenticatedPrincipal;

class AuthenticatedPrincipalMapperTest {

  @Test
  void shouldMapKnownClaimsFromPrincipal() {
    AuthenticatedUserMapper mapper = new AuthenticatedUserMapper();
    DefaultOAuth2AuthenticatedPrincipal principal = new DefaultOAuth2AuthenticatedPrincipal(
        Map.of(
            "sub", "keycloak-user-id",
            "preferred_username", "jane.doe",
            "email", "jane@medflow.com",
            "name", "Jane Doe",
            "cpf", "12345678901",
            "telefone", "91999999999",
            "dataNascimento", "1990-04-10",
            "groups", List.of("/MEDICOS", "/UNIDADES/MATRIZ"),
            "realm_access", Map.of("roles", List.of("default-roles-medflow")),
            "resource_access",
            Map.of("medflow-backend", Map.of("roles", List.of("MEDICO", "USUARIO")))),
        List.of(new SimpleGrantedAuthority("ROLE_MEDICO")));

    MedflowAuthenticatedPrincipal mapped = mapper.map(principal);

    assertEquals("keycloak-user-id", mapped.user().subject());
    assertEquals("jane.doe", mapped.user().username());
    assertEquals("jane@medflow.com", mapped.user().email());
    assertEquals("Jane Doe", mapped.user().name());
    assertEquals("12345678901", mapped.user().cpf());
    assertEquals("91999999999", mapped.user().telefone());
    assertEquals("1990-04-10", mapped.user().dataNascimento().toString());
    assertTrue(mapped.user().hasClientRole("medflow-backend", "MEDICO"));
    assertTrue(mapped.user().groups().contains("MEDICOS"));
    assertTrue(mapped.user().groups().contains("UNIDADES/MATRIZ"));
    assertEquals("jane.doe", mapped.getName());
    assertEquals("12345678901", mapped.stringClaim("cpf").orElseThrow());
  }
}
