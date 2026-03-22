package br.com.medflow.core.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ProblemDetail handleNotFound(NotFoundException ex, HttpServletRequest request) {
    return buildProblemDetail(
        HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage(), request);
  }

  @ExceptionHandler(BusinessRuleException.class)
  public ProblemDetail handleBusinessRule(BusinessRuleException ex, HttpServletRequest request) {
    return buildProblemDetail(
        HttpStatus.UNPROCESSABLE_CONTENT, "Regra de negócio violada", ex.getMessage(), request);
  }

  @ExceptionHandler(ErrorResponseException.class)
  public ProblemDetail handleErrorResponseException(
      ErrorResponseException ex, HttpServletRequest request) {
    ProblemDetail body = ex.getBody();
    if (body.getTitle() == null || body.getTitle().isBlank()) {
      body.setTitle("Erro na requisição");
    }
    body.setInstance(URI.create(request.getRequestURI()));
    body.setProperty("timestamp", OffsetDateTime.now().toString());
    return body;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidation(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    ProblemDetail problem =
        buildProblemDetail(
            HttpStatus.BAD_REQUEST,
            "Dados de entrada inválidos",
            "Uma ou mais validações falharam",
            request);

    Map<String, String> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .collect(
                Collectors.toMap(
                    FieldError::getField,
                    fieldError ->
                        fieldError.getDefaultMessage() == null
                            ? "Valor inválido"
                            : fieldError.getDefaultMessage(),
                    (first, second) -> first));

    problem.setProperty("errors", errors);
    return problem;
  }

  @ExceptionHandler({
    MissingServletRequestParameterException.class,
    ConstraintViolationException.class
  })
  public ProblemDetail handleRequestConstraint(Exception ex, HttpServletRequest request) {
    return buildProblemDetail(
        HttpStatus.BAD_REQUEST, "Parâmetros inválidos", ex.getMessage(), request);
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleUnexpected(Exception ex, HttpServletRequest request) {
    return buildProblemDetail(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Erro interno do servidor",
        "Ocorreu um erro inesperado ao processar a requisição",
        request);
  }

  private ProblemDetail buildProblemDetail(
      HttpStatusCode status, String title, String detail, HttpServletRequest request) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
    problem.setTitle(title);
    problem.setType(URI.create("about:blank"));
    problem.setInstance(URI.create(request.getRequestURI()));
    problem.setProperty("timestamp", OffsetDateTime.now().toString());
    return problem;
  }
}
