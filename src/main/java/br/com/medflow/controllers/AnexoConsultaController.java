package br.com.medflow.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.medflow.core.persistence.query.PageResult;
import br.com.medflow.core.persistence.query.RsqlQuery;
import br.com.medflow.schemas.anexoconsulta.AnexoConsultaInput;
import br.com.medflow.schemas.anexoconsulta.AnexoConsultaOutput;
import br.com.medflow.services.AnexoConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;

import br.com.medflow.core.security.annotations.AuthorizeResource;
import br.com.medflow.entities.AnexoConsulta;

/**
 * Endpoints HTTP para anexos de consulta.
 */
@Validated
@RestController
@RequestMapping("/api/anexos-consulta")
@Tag(name = "Anexos de Consulta")
@AuthorizeResource(AnexoConsulta.class)
public class AnexoConsultaController {

  private final AnexoConsultaService anexoConsultaService;

  /**
   * Cria o controller com suas dependências.
   *
   * @param anexoConsultaService serviço de anexos de consulta
   */
  public AnexoConsultaController(AnexoConsultaService anexoConsultaService) {
    this.anexoConsultaService = anexoConsultaService;
  }

  @GetMapping
  @Operation(summary = "Listar anexos de consulta")
  public PageResult<AnexoConsultaOutput> findAll(RsqlQuery query, @ParameterObject Pageable pageable) {
    return anexoConsultaService.findAll(query, pageable);
  }

  @GetMapping("/{anexoConsultaId}")
  @Operation(summary = "Buscar anexo de consulta por ID")
  public AnexoConsultaOutput findById(@PathVariable UUID anexoConsultaId) {
    return anexoConsultaService.findById(anexoConsultaId);
  }

  @PostMapping
  @Operation(summary = "Criar anexo de consulta")
  public ResponseEntity<AnexoConsultaOutput> create(@Valid @RequestBody AnexoConsultaInput input) {
    AnexoConsultaOutput output = anexoConsultaService.create(input);
    return ResponseEntity.created(location(output.id())).body(output);
  }

  @PutMapping("/{anexoConsultaId}")
  @Operation(summary = "Atualizar anexo de consulta")
  public AnexoConsultaOutput update(
      @PathVariable UUID anexoConsultaId,
      @Valid @RequestBody AnexoConsultaInput input) {
    return anexoConsultaService.update(anexoConsultaId, input);
  }

  @DeleteMapping("/{anexoConsultaId}")
  @Operation(summary = "Excluir anexo de consulta")
  public ResponseEntity<Void> delete(@PathVariable UUID anexoConsultaId) {
    anexoConsultaService.delete(anexoConsultaId);
    return ResponseEntity.noContent().build();
  }

  private URI location(UUID id) {
    return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
  }
}
