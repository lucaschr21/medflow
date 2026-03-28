package br.com.medflow.core.security.infrastructure.jwt;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import br.com.medflow.core.security.TokenClaims;
import br.com.medflow.services.seguranca.ports.TokenVerifierPort;

public class SpringJwtTokenVerifierAdapter implements TokenVerifierPort {

  private static final JwtGrantedAuthoritiesConverter SCOPE_CONVERTER = createScopeConverter();

  private final JwtDecoder jwtDecoder;

  public SpringJwtTokenVerifierAdapter(JwtDecoder jwtDecoder) {
    this.jwtDecoder = Objects.requireNonNull(jwtDecoder, "JwtDecoder é obrigatório");
  }

  @Override
  public TokenClaims verifyAccessToken(String accessToken) {
    Objects.requireNonNull(accessToken, "Access token é obrigatório");

    Jwt jwt = jwtDecoder.decode(accessToken);

    return new TokenClaims(
        jwt.getId(),
        issuerValue(jwt.getIssuer()),
        jwt.getSubject(),
        normalize(jwt.getAudience()),
        resolveScopes(jwt),
        jwt.getIssuedAt(),
        jwt.getNotBefore(),
        jwt.getExpiresAt());
  }

  private static String issuerValue(URL issuer) {
    return issuer == null ? null : issuer.toString();
  }

  private static Set<String> resolveScopes(Jwt jwt) {
    return normalize(
        SCOPE_CONVERTER.convert(jwt).stream().map(GrantedAuthority::getAuthority).toList());
  }

  private static Set<String> normalize(Iterable<String> values) {
    if (values == null) {
      return Set.of();
    }

    LinkedHashSet<String> normalized = new LinkedHashSet<>();
    for (String value : values) {
      if (value == null) {
        continue;
      }
      String trimmed = value.trim();
      if (!trimmed.isBlank()) {
        normalized.add(trimmed);
      }
    }

    return Set.copyOf(normalized);
  }

  private static JwtGrantedAuthoritiesConverter createScopeConverter() {
    JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
    converter.setAuthorityPrefix("");
    return converter;
  }
}
