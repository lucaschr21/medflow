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
import br.com.medflow.schemas.consulta.ConsultaInput;
import br.com.medflow.schemas.consulta.ConsultaOutput;
import br.com.medflow.services.ConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;

import br.com.medflow.core.security.annotations.AuthorizeResource;
import br.com.medflow.entities.Consulta;

/**
 * Endpoints HTTP para consultas.
 */
@Validated
@RestController
@RequestMapping("/api/consultas")
@Tag(name = "Consultas")
@AuthorizeResource(Consulta.class)
public class ConsultaController {

  private final ConsultaService consultaService;

  /**
   * Cria o controller com suas dependências.
   *
   * @param consultaService serviço de consultas
   */
  public ConsultaController(ConsultaService consultaService) {
    this.consultaService = consultaService;
  }

  @GetMapping
  @Operation(summary = "Listar consultas")
  public PageResult<ConsultaOutput> findAll(RsqlQuery query, @ParameterObject Pageable pageable) {
    return consultaService.findAll(query, pageable);
  }

  @GetMapping("/{consultaId}")
  @Operation(summary = "Buscar consulta por ID")
  public ConsultaOutput findById(@PathVariable UUID consultaId) {
    return consultaService.findById(consultaId);
  }

  @PostMapping
  @Operation(summary = "Criar consulta")
  public ResponseEntity<ConsultaOutput> create(@Valid @RequestBody ConsultaInput input) {
    ConsultaOutput output = consultaService.create(input);
    return ResponseEntity.created(location(output.id())).body(output);
  }

  @PutMapping("/{consultaId}")
  @Operation(summary = "Atualizar consulta")
  public ConsultaOutput update(@PathVariable UUID consultaId, @Valid @RequestBody ConsultaInput input) {
    return consultaService.update(consultaId, input);
  }

  @DeleteMapping("/{consultaId}")
  @Operation(summary = "Excluir consulta")
  public ResponseEntity<Void> delete(@PathVariable UUID consultaId) {
    consultaService.delete(consultaId);
    return ResponseEntity.noContent().build();
  }

  private URI location(UUID id) {
    return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
  }
}
