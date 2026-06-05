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
import br.com.medflow.schemas.bloqueioagenda.BloqueioAgendaInput;
import br.com.medflow.schemas.bloqueioagenda.BloqueioAgendaOutput;
import br.com.medflow.services.BloqueioAgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;

/**
 * Endpoints HTTP para bloqueios de agenda.
 */
@Validated
@RestController
@RequestMapping("/api/bloqueios-agenda")
@Tag(name = "Bloqueios de Agenda")
public class BloqueioAgendaController {

  private final BloqueioAgendaService bloqueioAgendaService;

  /**
   * Cria o controller com suas dependências.
   *
   * @param bloqueioAgendaService serviço de bloqueios de agenda
   */
  public BloqueioAgendaController(BloqueioAgendaService bloqueioAgendaService) {
    this.bloqueioAgendaService = bloqueioAgendaService;
  }

  @GetMapping
  @Operation(summary = "Listar bloqueios de agenda")
  public PageResult<BloqueioAgendaOutput> findAll(RsqlQuery query, @ParameterObject Pageable pageable) {
    return bloqueioAgendaService.findAll(query, pageable);
  }

  @GetMapping("/{bloqueioAgendaId}")
  @Operation(summary = "Buscar bloqueio de agenda por ID")
  public BloqueioAgendaOutput findById(@PathVariable UUID bloqueioAgendaId) {
    return bloqueioAgendaService.findById(bloqueioAgendaId);
  }

  @PostMapping
  @Operation(summary = "Criar bloqueio de agenda")
  public ResponseEntity<BloqueioAgendaOutput> create(@Valid @RequestBody BloqueioAgendaInput input) {
    BloqueioAgendaOutput output = bloqueioAgendaService.create(input);
    return ResponseEntity.created(location(output.id())).body(output);
  }

  @PutMapping("/{bloqueioAgendaId}")
  @Operation(summary = "Atualizar bloqueio de agenda")
  public BloqueioAgendaOutput update(
      @PathVariable UUID bloqueioAgendaId,
      @Valid @RequestBody BloqueioAgendaInput input) {
    return bloqueioAgendaService.update(bloqueioAgendaId, input);
  }

  @DeleteMapping("/{bloqueioAgendaId}")
  @Operation(summary = "Excluir bloqueio de agenda")
  public ResponseEntity<Void> delete(@PathVariable UUID bloqueioAgendaId) {
    bloqueioAgendaService.delete(bloqueioAgendaId);
    return ResponseEntity.noContent().build();
  }

  private URI location(UUID id) {
    return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
  }
}
