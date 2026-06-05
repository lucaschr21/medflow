package br.com.medflow.core.security.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define o nome funcional do recurso protegido no Medflow.
 *
 * <p>Quando ausente, o nome do recurso é derivado do nome simples da classe em
 * {@code kebab-case}.
 *
 * <p>Exemplo:
 *
 * <pre>{@code
 * @ProtectedResource("registro-atendimento")
 * public final class RegistroAtendimento {}
 * }</pre>
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProtectedResource {

  /**
   * Nome funcional do recurso.
   *
   * @return nome do recurso
   */
  String value();
}
