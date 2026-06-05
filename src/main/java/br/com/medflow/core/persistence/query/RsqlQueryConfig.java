package br.com.medflow.core.persistence.query;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Registra o resolvedor MVC de {@link RsqlQuery}.
 */
@Configuration(proxyBeanMethods = false)
public class RsqlQueryConfig implements WebMvcConfigurer {

  /** {@inheritDoc} */
  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(new RsqlQueryArgumentResolver());
  }

  /**
   * Resolve o parâmetro HTTP {@code q} diretamente para {@link RsqlQuery}.
   */
  private static final class RsqlQueryArgumentResolver implements HandlerMethodArgumentResolver {

    /** {@inheritDoc} */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
      return parameter.getParameterType() == RsqlQuery.class;
    }

    /** {@inheritDoc} */
    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {
      return new RsqlQuery(webRequest.getParameter("q"));
    }
  }
}
