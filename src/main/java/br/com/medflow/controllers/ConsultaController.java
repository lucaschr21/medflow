package br.com.medflow.controllers;

import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.medflow.core.persistence.query.PageResult;
import br.com.medflow.core.persistence.query.RsqlQuery;
import br.com.medflow.core.security.annotations.AuthorizeResource;
import br.com.medflow.entities.Consulta;
import br.com.medflow.schemas.consulta.ConsultaInput;
import br.com.medflow.schemas.consulta.ConsultaOutput;
import br.com.medflow.schemas.registroatendimento.RegistroAtendimentoInput;
import br.com.medflow.schemas.registroatendimento.RegistroAtendimentoOutput;
import br.com.medflow.services.ConsultaService;
import br.com.medflow.services.RegistroAtendimentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

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
  private final RegistroAtendimentoService registroAtendimentoService;

  public ConsultaController(
      ConsultaService consultaService,
      RegistroAtendimentoService registroAtendimentoService) {
    this.consultaService = consultaService;
    this.registroAtendimentoService = registroAtendimentoService;
  }

  // ---- CRUD ----

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

  // ---- Endpoints operacionais ----

  @GetMapping("/minhas")
  @Operation(summary = "Listar consultas do usuário autenticado")
  public PageResult<ConsultaOutput> minhasConsultas(@ParameterObject Pageable pageable) {
    return consultaService.minhasConsultas(pageable);
  }

  @GetMapping("/minha-agenda")
  @Operation(summary = "Agenda do médico autenticado para uma data")
  public PageResult<ConsultaOutput> minhaAgenda(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
      @ParameterObject Pageable pageable) {
    return consultaService.minhaAgenda(data, pageable);
  }

  @GetMapping("/minha-fila")
  @Operation(summary = "Fila de espera do médico autenticado")
  public PageResult<ConsultaOutput> minhaFila(@ParameterObject Pageable pageable) {
    return consultaService.minhaFila(pageable);
  }

  @PostMapping("/agendar")
  @Operation(summary = "Agendar consulta com revalidação de disponibilidade")
  public ResponseEntity<ConsultaOutput> agendar(@Valid @RequestBody ConsultaInput input) {
    ConsultaOutput output = consultaService.agendar(input);
    return ResponseEntity.created(location(output.id())).body(output);
  }

  @PostMapping("/{consultaId}/check-in")
  @Operation(summary = "Realizar check-in da consulta")
  public ConsultaOutput checkIn(@PathVariable UUID consultaId) {
    return consultaService.checkIn(consultaId);
  }

  @PostMapping("/{consultaId}/cancelar")
  @Operation(summary = "Cancelar consulta")
  public ConsultaOutput cancelar(@PathVariable UUID consultaId) {
    return consultaService.cancelar(consultaId);
  }

  @PostMapping("/{consultaId}/reagendar")
  @Operation(summary = "Reagendar consulta com revalidação")
  public ConsultaOutput reagendar(@PathVariable UUID consultaId, @Valid @RequestBody ConsultaInput input) {
    return consultaService.reagendar(consultaId, input);
  }

  @PostMapping("/{consultaId}/nao-compareceu")
  @Operation(summary = "Marcar consulta como não comparecimento")
  public ConsultaOutput marcarNaoCompareceu(@PathVariable UUID consultaId) {
    return consultaService.marcarNaoCompareceu(consultaId);
  }

  @PostMapping("/{consultaId}/em-espera")
  @Operation(summary = "Colocar consulta em espera")
  public ConsultaOutput colocarEmEspera(@PathVariable UUID consultaId) {
    return consultaService.colocarEmEspera(consultaId);
  }

  @PostMapping("/{consultaId}/iniciar-atendimento")
  @Operation(summary = "Iniciar atendimento da consulta")
  public ConsultaOutput iniciarAtendimento(@PathVariable UUID consultaId) {
    return consultaService.iniciarAtendimento(consultaId);
  }

  @PostMapping("/{consultaId}/finalizar")
  @Operation(summary = "Finalizar atendimento da consulta")
  public ConsultaOutput finalizar(@PathVariable UUID consultaId) {
    return consultaService.finalizar(consultaId);
  }

  @PostMapping("/{consultaId}/registro-atendimento")
  @Operation(summary = "Criar registro de atendimento para a consulta")
  public RegistroAtendimentoOutput criarRegistro(
      @PathVariable UUID consultaId,
      @Valid @RequestBody RegistroAtendimentoInput input) {
    return registroAtendimentoService.criarRegistro(consultaId, input);
  }

  @PutMapping("/{consultaId}/registro-atendimento/{registroId}")
  @Operation(summary = "Atualizar registro de atendimento da consulta")
  public RegistroAtendimentoOutput atualizarRegistro(
      @PathVariable UUID consultaId,
      @PathVariable UUID registroId,
      @Valid @RequestBody RegistroAtendimentoInput input) {
    return registroAtendimentoService.atualizarRegistro(consultaId, registroId, input);
  }

  private URI location(UUID id) {
    return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
  }
}
