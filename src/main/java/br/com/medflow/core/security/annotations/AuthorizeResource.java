package br.com.medflow.core.security.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Exige acesso funcional ao recurso informado, inferindo a ação a partir do
 * método HTTP atual.
 *
 * <p>Esta anotação foi pensada para controllers e outros pontos de entrada
 * HTTP do projeto, onde a convenção
 * {@code método HTTP -> ação funcional} já está padronizada.
 *
 * <p>Exemplo:
 *
 * <pre>{@code
 * @AuthorizeResource(Usuario.class)
 * @GetMapping("/{id}")
 * public UsuarioResponse buscarPorId(@PathVariable UUID id) {
 *   ...
 * }
 * }</pre>
 */
@Documented
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizeResource {

  /**
   * Tipo que representa o recurso protegido.
   *
   * @return tipo do recurso
   */
  Class<?> value();
}
