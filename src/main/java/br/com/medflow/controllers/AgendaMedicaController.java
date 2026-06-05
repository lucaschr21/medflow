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
import br.com.medflow.schemas.agendamedica.AgendaMedicaInput;
import br.com.medflow.schemas.agendamedica.AgendaMedicaOutput;
import br.com.medflow.services.AgendaMedicaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;

/**
 * Endpoints HTTP para agendas médicas.
 */
@Validated
@RestController
@RequestMapping("/api/agendas-medicas")
@Tag(name = "Agendas Médicas")
public class AgendaMedicaController {

  private final AgendaMedicaService agendaMedicaService;

  /**
   * Cria o controller com suas dependências.
   *
   * @param agendaMedicaService serviço de agendas médicas
   */
  public AgendaMedicaController(AgendaMedicaService agendaMedicaService) {
    this.agendaMedicaService = agendaMedicaService;
  }

  @GetMapping
  @Operation(summary = "Listar agendas médicas")
  public PageResult<AgendaMedicaOutput> findAll(RsqlQuery query, @ParameterObject Pageable pageable) {
    return agendaMedicaService.findAll(query, pageable);
  }

  @GetMapping("/{agendaMedicaId}")
  @Operation(summary = "Buscar agenda médica por ID")
  public AgendaMedicaOutput findById(@PathVariable UUID agendaMedicaId) {
    return agendaMedicaService.findById(agendaMedicaId);
  }

  @PostMapping
  @Operation(summary = "Criar agenda médica")
  public ResponseEntity<AgendaMedicaOutput> create(@Valid @RequestBody AgendaMedicaInput input) {
    AgendaMedicaOutput output = agendaMedicaService.create(input);
    return ResponseEntity.created(location(output.id())).body(output);
  }

  @PutMapping("/{agendaMedicaId}")
  @Operation(summary = "Atualizar agenda médica")
  public AgendaMedicaOutput update(
      @PathVariable UUID agendaMedicaId,
      @Valid @RequestBody AgendaMedicaInput input) {
    return agendaMedicaService.update(agendaMedicaId, input);
  }

  @DeleteMapping("/{agendaMedicaId}")
  @Operation(summary = "Inativar agenda médica")
  public ResponseEntity<Void> deactivate(@PathVariable UUID agendaMedicaId) {
    agendaMedicaService.deactivate(agendaMedicaId);
    return ResponseEntity.noContent().build();
  }

  private URI location(UUID id) {
    return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
  }
}
