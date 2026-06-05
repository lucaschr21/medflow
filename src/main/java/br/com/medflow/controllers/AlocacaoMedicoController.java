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
import br.com.medflow.schemas.alocacaomedico.AlocacaoMedicoInput;
import br.com.medflow.schemas.alocacaomedico.AlocacaoMedicoOutput;
import br.com.medflow.services.AlocacaoMedicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;

/**
 * Endpoints HTTP para alocações médicas.
 */
@Validated
@RestController
@RequestMapping("/api/alocacoes-medicas")
@Tag(name = "Alocações Médicas")
public class AlocacaoMedicoController {

  private final AlocacaoMedicoService alocacaoMedicoService;

  /**
   * Cria o controller com suas dependências.
   *
   * @param alocacaoMedicoService serviço de alocações médicas
   */
  public AlocacaoMedicoController(AlocacaoMedicoService alocacaoMedicoService) {
    this.alocacaoMedicoService = alocacaoMedicoService;
  }

  @GetMapping
  @Operation(summary = "Listar alocações médicas")
  public PageResult<AlocacaoMedicoOutput> findAll(RsqlQuery query, @ParameterObject Pageable pageable) {
    return alocacaoMedicoService.findAll(query, pageable);
  }

  @GetMapping("/{alocacaoMedicoId}")
  @Operation(summary = "Buscar alocação médica por ID")
  public AlocacaoMedicoOutput findById(@PathVariable UUID alocacaoMedicoId) {
    return alocacaoMedicoService.findById(alocacaoMedicoId);
  }

  @PostMapping
  @Operation(summary = "Criar alocação médica")
  public ResponseEntity<AlocacaoMedicoOutput> create(@Valid @RequestBody AlocacaoMedicoInput input) {
    AlocacaoMedicoOutput output = alocacaoMedicoService.create(input);
    return ResponseEntity.created(location(output.id())).body(output);
  }

  @PutMapping("/{alocacaoMedicoId}")
  @Operation(summary = "Atualizar alocação médica")
  public AlocacaoMedicoOutput update(
      @PathVariable UUID alocacaoMedicoId,
      @Valid @RequestBody AlocacaoMedicoInput input) {
    return alocacaoMedicoService.update(alocacaoMedicoId, input);
  }

  @DeleteMapping("/{alocacaoMedicoId}")
  @Operation(summary = "Inativar alocação médica")
  public ResponseEntity<Void> deactivate(@PathVariable UUID alocacaoMedicoId) {
    alocacaoMedicoService.deactivate(alocacaoMedicoId);
    return ResponseEntity.noContent().build();
  }

  private URI location(UUID id) {
    return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
  }
}
