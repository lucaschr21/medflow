package br.com.medflow.core.security.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.medflow.core.security.authorization.ResourceAction;

/**
 * Exige explicitamente uma permissão funcional do recurso informado.
 *
 * <p>Use esta anotação em endpoints especiais, nos quais o método HTTP não
 * representa bem a ação funcional do domínio.
 *
 * <p>Exemplo:
 *
 * <pre>{@code
 * @AuthorizePermission(resource = Consulta.class, action = ResourceAction.UPDATE)
 * @PostMapping("/{id}/cancelamento")
 * public void cancelar(@PathVariable UUID id) {
 *   ...
 * }
 * }</pre>
 */
@Documented
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizePermission {

  /**
   * Tipo que representa o recurso protegido.
   *
   * @return tipo do recurso
   */
  Class<?> resource();

  /**
   * Ação funcional exigida para o recurso.
   *
   * @return ação funcional requerida
   */
  ResourceAction action();
}
