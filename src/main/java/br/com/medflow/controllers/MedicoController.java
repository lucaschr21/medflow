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
import br.com.medflow.schemas.medico.MedicoInput;
import br.com.medflow.schemas.medico.MedicoOutput;
import br.com.medflow.services.MedicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;

import br.com.medflow.core.security.annotations.AuthorizeResource;
import br.com.medflow.entities.Medico;

/**
 * Endpoints HTTP para médicos.
 */
@Validated
@RestController
@RequestMapping("/api/medicos")
@Tag(name = "Médicos")
@AuthorizeResource(Medico.class)
public class MedicoController {

  private final MedicoService medicoService;

  /**
   * Cria o controller com suas dependências.
   *
   * @param medicoService serviço de médicos
   */
  public MedicoController(MedicoService medicoService) {
    this.medicoService = medicoService;
  }

  @GetMapping
  @Operation(summary = "Listar médicos")
  public PageResult<MedicoOutput> findAll(RsqlQuery query, @ParameterObject Pageable pageable) {
    return medicoService.findAll(query, pageable);
  }

  @GetMapping("/{medicoId}")
  @Operation(summary = "Buscar médico por ID")
  public MedicoOutput findById(@PathVariable UUID medicoId) {
    return medicoService.findById(medicoId);
  }

  @PostMapping
  @Operation(summary = "Criar médico")
  public ResponseEntity<MedicoOutput> create(@Valid @RequestBody MedicoInput input) {
    MedicoOutput output = medicoService.create(input);
    return ResponseEntity.created(location(output.id())).body(output);
  }

  @PutMapping("/{medicoId}")
  @Operation(summary = "Atualizar médico")
  public MedicoOutput update(@PathVariable UUID medicoId, @Valid @RequestBody MedicoInput input) {
    return medicoService.update(medicoId, input);
  }

  @DeleteMapping("/{medicoId}")
  @Operation(summary = "Inativar médico")
  public ResponseEntity<Void> deactivate(@PathVariable UUID medicoId) {
    medicoService.deactivate(medicoId);
    return ResponseEntity.noContent().build();
  }

  private URI location(UUID id) {
    return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
  }
}
