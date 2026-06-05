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
import br.com.medflow.schemas.organizacao.OrganizacaoInput;
import br.com.medflow.schemas.organizacao.OrganizacaoOutput;
import br.com.medflow.services.OrganizacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;

import br.com.medflow.core.security.annotations.AuthorizeResource;
import br.com.medflow.entities.Organizacao;

/**
 * Endpoints HTTP para organizações.
 */
@Validated
@RestController
@RequestMapping("/api/organizacoes")
@Tag(name = "Organizações")
@AuthorizeResource(Organizacao.class)
public class OrganizacaoController {

  private final OrganizacaoService organizacaoService;

  /**
   * Cria o controller com suas dependências.
   *
   * @param organizacaoService serviço de organizações
   */
  public OrganizacaoController(OrganizacaoService organizacaoService) {
    this.organizacaoService = organizacaoService;
  }

  @GetMapping
  @Operation(summary = "Listar organizações")
  public PageResult<OrganizacaoOutput> findAll(RsqlQuery query, @ParameterObject Pageable pageable) {
    return organizacaoService.findAll(query, pageable);
  }

  @GetMapping("/{organizacaoId}")
  @Operation(summary = "Buscar organização por ID")
  public OrganizacaoOutput findById(@PathVariable UUID organizacaoId) {
    return organizacaoService.findById(organizacaoId);
  }

  @PostMapping
  @Operation(summary = "Criar organização")
  public ResponseEntity<OrganizacaoOutput> create(@Valid @RequestBody OrganizacaoInput input) {
    OrganizacaoOutput output = organizacaoService.create(input);
    return ResponseEntity.created(location(output.id())).body(output);
  }

  @PutMapping("/{organizacaoId}")
  @Operation(summary = "Atualizar organização")
  public OrganizacaoOutput update(@PathVariable UUID organizacaoId, @Valid @RequestBody OrganizacaoInput input) {
    return organizacaoService.update(organizacaoId, input);
  }

  @DeleteMapping("/{organizacaoId}")
  @Operation(summary = "Inativar organização")
  public ResponseEntity<Void> deactivate(@PathVariable UUID organizacaoId) {
    organizacaoService.deactivate(organizacaoId);
    return ResponseEntity.noContent().build();
  }

  private URI location(UUID id) {
    return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
  }
}
