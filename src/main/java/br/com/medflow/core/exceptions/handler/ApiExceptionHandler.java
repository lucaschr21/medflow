package br.com.medflow.core.exceptions.handler;

import java.net.URI;
import java.util.List;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.medflow.core.exceptions.BusinessRuleException;
import br.com.medflow.core.exceptions.EntityNotFoundException;

/**
 * Tratamento global das exceções expostas pela API.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  private static final HttpStatus VALIDATION_STATUS = HttpStatus.UNPROCESSABLE_CONTENT;
  private static final URI ACCESS_DENIED_TYPE = URI.create("urn:medflow:problem:access-denied");
  private static final URI BUSINESS_RULE_TYPE = URI.create("urn:medflow:problem:business-rule");
  private static final URI NOT_FOUND_TYPE = URI.create("urn:medflow:problem:not-found");
  private static final URI VALIDATION_TYPE = URI.create("urn:medflow:problem:validation-error");
  private static final URI MESSAGE_NOT_READABLE_TYPE = URI.create("urn:medflow:problem:message-not-readable");
  private static final URI INTERNAL_SERVER_ERROR_TYPE = URI.create("urn:medflow:problem:internal-server-error");

  /**
   * Trata violações de regra de negócio.
   *
   * @param exception exceção capturada
   * @param request   requisição atual
   * @return resposta padronizada
   */
  @ExceptionHandler(BusinessRuleException.class)
  ResponseEntity<Object> handleBusinessRule(BusinessRuleException exception, WebRequest request) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    problemDetail.setType(BUSINESS_RULE_TYPE);
    problemDetail.setTitle("Regra de negócio violada");
    problemDetail.setProperty("code", exception.getCode().name());
    return handleErrorResponseException(
        new ErrorResponseException(HttpStatus.CONFLICT, problemDetail, exception),
        HttpHeaders.EMPTY,
        HttpStatus.CONFLICT,
        request);
  }

  /**
   * Trata entidades obrigatórias não encontradas.
   *
   * @param exception exceção capturada
   * @param request   requisição atual
   * @return resposta padronizada
   */
  @ExceptionHandler(EntityNotFoundException.class)
  ResponseEntity<Object> handleNotFound(EntityNotFoundException exception, WebRequest request) {
    return handleErrorResponseException(
        errorResponseException(HttpStatus.NOT_FOUND, NOT_FOUND_TYPE, "Recurso não encontrado", exception),
        HttpHeaders.EMPTY,
        HttpStatus.NOT_FOUND,
        request);
  }

  /**
   * Trata negações de acesso da camada de segurança.
   *
   * @param exception exceção capturada
   * @param request   requisição atual
   * @return resposta padronizada
   */
  @ExceptionHandler(AccessDeniedException.class)
  ResponseEntity<Object> handleAccessDenied(AccessDeniedException exception, WebRequest request) {
    return handleErrorResponseException(
        errorResponseException(HttpStatus.FORBIDDEN, ACCESS_DENIED_TYPE, "Acesso negado", exception),
        HttpHeaders.EMPTY,
        HttpStatus.FORBIDDEN,
        request);
  }

  /**
   * Trata falhas de validação em corpo de requisição.
   *
   * @param exception exceção capturada
   * @param headers   cabeçalhos da resposta
   * @param status    status HTTP
   * @param request   requisição atual
   * @return resposta padronizada
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    ProblemDetail problemDetail = problemDetail(
        exception,
        VALIDATION_STATUS,
        VALIDATION_TYPE,
        "Requisição inválida",
        "Um ou mais campos da requisição são inválidos.",
        request);
    problemDetail.setProperty("errors",
        exception.getBindingResult().getAllErrors().stream().map(this::toError).toList());
    return createResponseEntity(problemDetail, headers, VALIDATION_STATUS, request);
  }

  /**
   * Trata falhas de validação em parâmetros de método.
   *
   * @param exception exceção capturada
   * @param headers   cabeçalhos da resposta
   * @param status    status HTTP
   * @param request   requisição atual
   * @return resposta padronizada
   */
  @Override
  protected ResponseEntity<Object> handleHandlerMethodValidationException(
      HandlerMethodValidationException exception,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    ProblemDetail problemDetail = problemDetail(
        exception,
        VALIDATION_STATUS,
        VALIDATION_TYPE,
        "Requisição inválida",
        "Um ou mais parâmetros da requisição são inválidos.",
        request);
    problemDetail.setProperty("errors", exception.getParameterValidationResults().stream()
        .flatMap(result -> toErrors(result).stream())
        .toList());
    return createResponseEntity(problemDetail, headers, VALIDATION_STATUS, request);
  }

  /**
   * Trata falhas de desserialização do corpo da requisição.
   *
   * @param exception exceção capturada
   * @param headers   cabeçalhos da resposta
   * @param status    status HTTP
   * @param request   requisição atual
   * @return resposta padronizada
   */
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      org.springframework.http.converter.HttpMessageNotReadableException exception,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    ProblemDetail problemDetail = problemDetail(
        exception,
        HttpStatus.BAD_REQUEST,
        MESSAGE_NOT_READABLE_TYPE,
        "Corpo da requisição inválido",
        "Não foi possível interpretar o corpo da requisição.",
        request);
    return createResponseEntity(problemDetail, headers, HttpStatus.BAD_REQUEST, request);
  }

  /**
   * Trata falhas inesperadas não mapeadas especificamente.
   *
   * @param exception exceção capturada
   * @param request   requisição atual
   * @return resposta padronizada
   */
  @ExceptionHandler(Exception.class)
  ResponseEntity<Object> handleUnexpected(Exception exception, WebRequest request) {
    ProblemDetail problemDetail = problemDetail(
        exception,
        HttpStatus.INTERNAL_SERVER_ERROR,
        INTERNAL_SERVER_ERROR_TYPE,
        "Erro interno",
        "Ocorreu um erro inesperado ao processar a requisição.",
        request);
    return createResponseEntity(problemDetail, HttpHeaders.EMPTY, HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  @Override
  protected ResponseEntity<Object> createResponseEntity(
      Object body,
      HttpHeaders headers,
      HttpStatusCode statusCode,
      WebRequest request) {
    if (body instanceof ProblemDetail problemDetail && problemDetail.getInstance() == null) {
      problemDetail.setInstance(requestUri(request));
    }
    return super.createResponseEntity(body, headers, statusCode, request);
  }

  private ErrorResponseException errorResponseException(
      HttpStatus status,
      URI type,
      String title,
      RuntimeException exception) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
    problemDetail.setType(type);
    problemDetail.setTitle(title);
    return new ErrorResponseException(status, problemDetail, exception);
  }

  private ProblemDetail problemDetail(
      Exception exception,
      HttpStatus status,
      URI type,
      String title,
      String detail,
      WebRequest request) {
    ProblemDetail problemDetail = createProblemDetail(exception, status, detail, null, null, request);
    problemDetail.setType(type);
    problemDetail.setTitle(title);
    return problemDetail;
  }

  private ValidationError toError(ObjectError error) {
    return switch (error) {
      case FieldError fieldError -> new ValidationError(
          "#/" + fieldError.getField(),
          defaultMessage(fieldError),
          firstCode(fieldError.getCodes()));
      default -> new ValidationError("#", defaultMessage(error), firstCode(error.getCodes()));
    };
  }

  private List<ValidationError> toErrors(ParameterValidationResult result) {
    if (result instanceof ParameterErrors parameterErrors) {
      return parameterErrors.getAllErrors().stream().map(this::toError).toList();
    }

    String parameterName = result.getMethodParameter().getParameterName();
    String pointer = parameterName == null ? "#" : "#/" + parameterName;
    return result.getResolvableErrors().stream()
        .map(error -> new ValidationError(pointer, defaultMessage(error), firstCode(error.getCodes())))
        .toList();
  }

  private String defaultMessage(MessageSourceResolvable error) {
    return error.getDefaultMessage() == null ? "Valor inválido." : error.getDefaultMessage();
  }

  private String firstCode(String[] codes) {
    return codes == null || codes.length == 0 ? "invalid" : codes[0];
  }

  private URI requestUri(WebRequest request) {
    if (request instanceof ServletWebRequest servletWebRequest) {
      return URI.create(servletWebRequest.getRequest().getRequestURI());
    }
    return URI.create("about:blank");
  }

  /**
   * Erro individual de validação exposto no Problem Details.
   *
   * @param pointer ponteiro do campo ou parâmetro inválido
   * @param detail  mensagem da validação
   * @param code    código da validação
   */
  private record ValidationError(String pointer, String detail, String code) {
  }
}
