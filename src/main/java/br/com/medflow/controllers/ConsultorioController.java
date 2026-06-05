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
import br.com.medflow.schemas.consultorio.ConsultorioInput;
import br.com.medflow.schemas.consultorio.ConsultorioOutput;
import br.com.medflow.services.ConsultorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;

/**
 * Endpoints HTTP para consultórios.
 */
@Validated
@RestController
@RequestMapping("/api/consultorios")
@Tag(name = "Consultórios")
public class ConsultorioController {

  private final ConsultorioService consultorioService;

  /**
   * Cria o controller com suas dependências.
   *
   * @param consultorioService serviço de consultórios
   */
  public ConsultorioController(ConsultorioService consultorioService) {
    this.consultorioService = consultorioService;
  }

  @GetMapping
  @Operation(summary = "Listar consultórios")
  public PageResult<ConsultorioOutput> findAll(RsqlQuery query, @ParameterObject Pageable pageable) {
    return consultorioService.findAll(query, pageable);
  }

  @GetMapping("/{consultorioId}")
  @Operation(summary = "Buscar consultório por ID")
  public ConsultorioOutput findById(@PathVariable UUID consultorioId) {
    return consultorioService.findById(consultorioId);
  }

  @PostMapping
  @Operation(summary = "Criar consultório")
  public ResponseEntity<ConsultorioOutput> create(@Valid @RequestBody ConsultorioInput input) {
    ConsultorioOutput output = consultorioService.create(input);
    return ResponseEntity.created(location(output.id())).body(output);
  }

  @PutMapping("/{consultorioId}")
  @Operation(summary = "Atualizar consultório")
  public ConsultorioOutput update(@PathVariable UUID consultorioId, @Valid @RequestBody ConsultorioInput input) {
    return consultorioService.update(consultorioId, input);
  }

  @DeleteMapping("/{consultorioId}")
  @Operation(summary = "Inativar consultório")
  public ResponseEntity<Void> deactivate(@PathVariable UUID consultorioId) {
    consultorioService.deactivate(consultorioId);
    return ResponseEntity.noContent().build();
  }

  private URI location(UUID id) {
    return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
  }
}
