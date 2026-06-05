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
import br.com.medflow.schemas.registroatendimento.RegistroAtendimentoInput;
import br.com.medflow.schemas.registroatendimento.RegistroAtendimentoOutput;
import br.com.medflow.services.RegistroAtendimentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;

import br.com.medflow.core.security.annotations.AuthorizeResource;
import br.com.medflow.entities.RegistroAtendimento;

/**
 * Endpoints HTTP para registros de atendimento.
 */
@Validated
@RestController
@RequestMapping("/api/registros-atendimento")
@Tag(name = "Registros de Atendimento")
@AuthorizeResource(RegistroAtendimento.class)
public class RegistroAtendimentoController {

  private final RegistroAtendimentoService registroAtendimentoService;

  /**
   * Cria o controller com suas dependências.
   *
   * @param registroAtendimentoService serviço de registros de atendimento
   */
  public RegistroAtendimentoController(RegistroAtendimentoService registroAtendimentoService) {
    this.registroAtendimentoService = registroAtendimentoService;
  }

  @GetMapping
  @Operation(summary = "Listar registros de atendimento")
  public PageResult<RegistroAtendimentoOutput> findAll(RsqlQuery query, @ParameterObject Pageable pageable) {
    return registroAtendimentoService.findAll(query, pageable);
  }

  @GetMapping("/{registroAtendimentoId}")
  @Operation(summary = "Buscar registro de atendimento por ID")
  public RegistroAtendimentoOutput findById(@PathVariable UUID registroAtendimentoId) {
    return registroAtendimentoService.findById(registroAtendimentoId);
  }

  @PostMapping
  @Operation(summary = "Criar registro de atendimento")
  public ResponseEntity<RegistroAtendimentoOutput> create(@Valid @RequestBody RegistroAtendimentoInput input) {
    RegistroAtendimentoOutput output = registroAtendimentoService.create(input);
    return ResponseEntity.created(location(output.id())).body(output);
  }

  @PutMapping("/{registroAtendimentoId}")
  @Operation(summary = "Atualizar registro de atendimento")
  public RegistroAtendimentoOutput update(
      @PathVariable UUID registroAtendimentoId,
      @Valid @RequestBody RegistroAtendimentoInput input) {
    return registroAtendimentoService.update(registroAtendimentoId, input);
  }

  @DeleteMapping("/{registroAtendimentoId}")
  @Operation(summary = "Excluir registro de atendimento")
  public ResponseEntity<Void> delete(@PathVariable UUID registroAtendimentoId) {
    registroAtendimentoService.delete(registroAtendimentoId);
    return ResponseEntity.noContent().build();
  }

  private URI location(UUID id) {
    return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
  }
}
