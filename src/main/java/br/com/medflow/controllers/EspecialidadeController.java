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
import br.com.medflow.schemas.especialidade.EspecialidadeInput;
import br.com.medflow.schemas.especialidade.EspecialidadeOutput;
import br.com.medflow.services.EspecialidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;

import br.com.medflow.core.security.annotations.AuthorizeResource;
import br.com.medflow.entities.Especialidade;

/**
 * Endpoints HTTP para especialidades.
 */
@Validated
@RestController
@RequestMapping("/api/especialidades")
@Tag(name = "Especialidades")
@AuthorizeResource(Especialidade.class)
public class EspecialidadeController {

  private final EspecialidadeService especialidadeService;

  /**
   * Cria o controller com suas dependências.
   *
   * @param especialidadeService serviço de especialidades
   */
  public EspecialidadeController(EspecialidadeService especialidadeService) {
    this.especialidadeService = especialidadeService;
  }

  @GetMapping
  @Operation(summary = "Listar especialidades")
  public PageResult<EspecialidadeOutput> findAll(RsqlQuery query, @ParameterObject Pageable pageable) {
    return especialidadeService.findAll(query, pageable);
  }

  @GetMapping("/{especialidadeId}")
  @Operation(summary = "Buscar especialidade por ID")
  public EspecialidadeOutput findById(@PathVariable UUID especialidadeId) {
    return especialidadeService.findById(especialidadeId);
  }

  @PostMapping
  @Operation(summary = "Criar especialidade")
  public ResponseEntity<EspecialidadeOutput> create(@Valid @RequestBody EspecialidadeInput input) {
    EspecialidadeOutput output = especialidadeService.create(input);
    return ResponseEntity.created(location(output.id())).body(output);
  }

  @PutMapping("/{especialidadeId}")
  @Operation(summary = "Atualizar especialidade")
  public EspecialidadeOutput update(
      @PathVariable UUID especialidadeId,
      @Valid @RequestBody EspecialidadeInput input) {
    return especialidadeService.update(especialidadeId, input);
  }

  @DeleteMapping("/{especialidadeId}")
  @Operation(summary = "Inativar especialidade")
  public ResponseEntity<Void> deactivate(@PathVariable UUID especialidadeId) {
    especialidadeService.deactivate(especialidadeId);
    return ResponseEntity.noContent().build();
  }

  private URI location(UUID id) {
    return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
  }
}
