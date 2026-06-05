package br.com.medflow.core.security.authorization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import br.com.medflow.core.security.annotations.ProtectedResource;

class ResourceNameResolverTest {

  private final ProtectedResourceResolver resolver = new ProtectedResourceResolver();

  @Test
  void shouldUseProtectedResourceAnnotationWhenPresent() {
    assertEquals("usuario", resolver.resolve(UsuarioEntity.class));
  }

  @Test
  void shouldConvertSimpleClassNameToKebabCaseWhenAnnotationIsAbsent() {
    assertEquals("registro-atendimento", resolver.resolve(RegistroAtendimento.class));
    assertEquals("anexo-consulta", resolver.resolve(AnexoConsulta.class));
  }

  @ProtectedResource("usuario")
  private static final class UsuarioEntity {}

  private static final class RegistroAtendimento {}

  private static final class AnexoConsulta {}
}
