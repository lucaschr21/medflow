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
import br.com.medflow.schemas.unidade.UnidadeInput;
import br.com.medflow.schemas.unidade.UnidadeOutput;
import br.com.medflow.services.UnidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;

/**
 * Endpoints HTTP para unidades.
 */
@Validated
@RestController
@RequestMapping("/api/unidades")
@Tag(name = "Unidades")
public class UnidadeController {

  private final UnidadeService unidadeService;

  /**
   * Cria o controller com suas dependências.
   *
   * @param unidadeService serviço de unidades
   */
  public UnidadeController(UnidadeService unidadeService) {
    this.unidadeService = unidadeService;
  }

  @GetMapping
  @Operation(summary = "Listar unidades")
  public PageResult<UnidadeOutput> findAll(RsqlQuery query, @ParameterObject Pageable pageable) {
    return unidadeService.findAll(query, pageable);
  }

  @GetMapping("/{unidadeId}")
  @Operation(summary = "Buscar unidade por ID")
  public UnidadeOutput findById(@PathVariable UUID unidadeId) {
    return unidadeService.findById(unidadeId);
  }

  @PostMapping
  @Operation(summary = "Criar unidade")
  public ResponseEntity<UnidadeOutput> create(@Valid @RequestBody UnidadeInput input) {
    UnidadeOutput output = unidadeService.create(input);
    return ResponseEntity.created(location(output.id())).body(output);
  }

  @PutMapping("/{unidadeId}")
  @Operation(summary = "Atualizar unidade")
  public UnidadeOutput update(@PathVariable UUID unidadeId, @Valid @RequestBody UnidadeInput input) {
    return unidadeService.update(unidadeId, input);
  }

  @DeleteMapping("/{unidadeId}")
  @Operation(summary = "Inativar unidade")
  public ResponseEntity<Void> deactivate(@PathVariable UUID unidadeId) {
    unidadeService.deactivate(unidadeId);
    return ResponseEntity.noContent().build();
  }

  private URI location(UUID id) {
    return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
  }
}
