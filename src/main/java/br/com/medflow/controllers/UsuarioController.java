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
import br.com.medflow.schemas.usuario.UsuarioInput;
import br.com.medflow.schemas.usuario.UsuarioOutput;
import br.com.medflow.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;

/**
 * Endpoints HTTP para usuários.
 */
@Validated
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários")
public class UsuarioController {

  private final UsuarioService usuarioService;

  /**
   * Cria o controller com suas dependências.
   *
   * @param usuarioService serviço de usuários
   */
  public UsuarioController(UsuarioService usuarioService) {
    this.usuarioService = usuarioService;
  }

  @GetMapping
  @Operation(summary = "Listar usuários")
  public PageResult<UsuarioOutput> findAll(RsqlQuery query, @ParameterObject Pageable pageable) {
    return usuarioService.findAll(query, pageable);
  }

  @GetMapping("/{usuarioId}")
  @Operation(summary = "Buscar usuário por ID")
  public UsuarioOutput findById(@PathVariable UUID usuarioId) {
    return usuarioService.findById(usuarioId);
  }

  @PostMapping
  @Operation(summary = "Criar usuário")
  public ResponseEntity<UsuarioOutput> create(@Valid @RequestBody UsuarioInput input) {
    UsuarioOutput output = usuarioService.create(input);
    return ResponseEntity.created(location(output.id())).body(output);
  }

  @PutMapping("/{usuarioId}")
  @Operation(summary = "Atualizar usuário")
  public UsuarioOutput update(@PathVariable UUID usuarioId, @Valid @RequestBody UsuarioInput input) {
    return usuarioService.update(usuarioId, input);
  }

  @DeleteMapping("/{usuarioId}")
  @Operation(summary = "Inativar usuário")
  public ResponseEntity<Void> deactivate(@PathVariable UUID usuarioId) {
    usuarioService.deactivate(usuarioId);
    return ResponseEntity.noContent().build();
  }

  private URI location(UUID id) {
    return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
  }
}
