package br.com.medflow.controllers;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.medflow.entities.agenda.EscopoBloqueioAgenda;
import br.com.medflow.schemas.agenda.entrada.AgendaPorIdEntradaSchema;
import br.com.medflow.schemas.agenda.entrada.ManipularBloqueioAgendaEntradaSchema;
import br.com.medflow.schemas.agenda.entrada.ManipularBloqueioAgendaRequestSchema;
import br.com.medflow.schemas.agenda.entrada.RedefinirDiasTrabalhoEntradaSchema;
import br.com.medflow.schemas.agenda.entrada.RedefinirDiasTrabalhoRequestSchema;
import br.com.medflow.schemas.agenda.saida.AgendaSaidaSchema;
import br.com.medflow.services.agenda.AgendaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/organizacoes/{organizacaoId}/agendas")
@Tag(name = "Agendas")
public class AgendaController {

  private final AgendaService agendaService;

  @GetMapping("/{agendaId}")
  public AgendaSaidaSchema buscarPorId(
      @PathVariable UUID organizacaoId, @PathVariable UUID agendaId) {
    return agendaService.buscarPorIdDaOrganizacao(
        new AgendaPorIdEntradaSchema(organizacaoId, agendaId));
  }

  @PutMapping("/{agendaId}/dias-trabalho")
  public AgendaSaidaSchema redefinirDiasTrabalho(
      @PathVariable UUID organizacaoId,
      @PathVariable UUID agendaId,
      @Valid @RequestBody RedefinirDiasTrabalhoRequestSchema body) {
    return agendaService.redefinirDiasTrabalho(
        new RedefinirDiasTrabalhoEntradaSchema(
            new AgendaPorIdEntradaSchema(organizacaoId, agendaId), body.diasTrabalho()));
  }

  @PostMapping("/{agendaId}/bloqueios")
  public AgendaSaidaSchema adicionarBloqueio(
      @PathVariable UUID organizacaoId,
      @PathVariable UUID agendaId,
      @Valid @RequestBody ManipularBloqueioAgendaRequestSchema body) {
    return agendaService.adicionarBloqueio(
        new ManipularBloqueioAgendaEntradaSchema(
            new AgendaPorIdEntradaSchema(organizacaoId, agendaId),
            body.inicio(),
            body.fim(),
            body.escopo()));
  }

  @DeleteMapping("/{agendaId}/bloqueios")
  public AgendaSaidaSchema removerBloqueio(
      @PathVariable UUID organizacaoId,
      @PathVariable UUID agendaId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
      @RequestParam EscopoBloqueioAgenda escopo) {
    return agendaService.removerBloqueio(
        new ManipularBloqueioAgendaEntradaSchema(
            new AgendaPorIdEntradaSchema(organizacaoId, agendaId), inicio, fim, escopo));
  }
}
