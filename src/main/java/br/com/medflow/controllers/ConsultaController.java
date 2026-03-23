package br.com.medflow.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.medflow.schemas.atendimento.entrada.AgendarConsultaEntradaSchema;
import br.com.medflow.schemas.atendimento.entrada.AgendarConsultaRequestSchema;
import br.com.medflow.schemas.atendimento.entrada.CancelarConsultaEntradaSchema;
import br.com.medflow.schemas.atendimento.entrada.CancelarConsultaRequestSchema;
import br.com.medflow.schemas.atendimento.entrada.ConsultaPorIdEntradaSchema;
import br.com.medflow.schemas.atendimento.saida.ConsultaSaidaSchema;
import br.com.medflow.services.atendimento.ConsultaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/organizacoes/{organizacaoId}/consultas")
@Tag(name = "Consultas")
public class ConsultaController {

  private final ConsultaService consultaService;

  @GetMapping("/{consultaId}")
  public ConsultaSaidaSchema buscarPorId(
      @PathVariable UUID organizacaoId, @PathVariable UUID consultaId) {
    return consultaService.buscarPorIdDaOrganizacao(
        new ConsultaPorIdEntradaSchema(organizacaoId, consultaId));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ConsultaSaidaSchema agendar(
      @PathVariable UUID organizacaoId, @Valid @RequestBody AgendarConsultaRequestSchema body) {
    return consultaService.agendarConsulta(
        new AgendarConsultaEntradaSchema(
            organizacaoId,
            body.pacienteId(),
            body.medicoId(),
            body.consultorioId(),
            body.convenioId(),
            body.dataHoraMarcacao(),
            body.tipoConsulta()));
  }

  @PatchMapping("/{consultaId}/cancelamento")
  public ConsultaSaidaSchema cancelar(
      @PathVariable UUID organizacaoId,
      @PathVariable UUID consultaId,
      @Valid @RequestBody CancelarConsultaRequestSchema body) {
    return consultaService.cancelarConsulta(
        new CancelarConsultaEntradaSchema(
            organizacaoId, consultaId, body.motivoCancelamento(), body.utilizadorId()));
  }
}
