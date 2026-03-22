package br.com.medflow.controllers;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.medflow.schemas.atendimento.entrada.ProcessoClinicoPorIdEntradaSchema;
import br.com.medflow.schemas.atendimento.entrada.VincularProcessoClinicoEntradaSchema;
import br.com.medflow.schemas.atendimento.entrada.VincularProcessoClinicoRequestSchema;
import br.com.medflow.schemas.atendimento.saida.ProcessoClinicoSaidaSchema;
import br.com.medflow.services.atendimento.ProcessoClinicoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/organizacoes/{organizacaoId}")
@Tag(name = "Processo Clínico")
public class ProcessoClinicoController {

  private final ProcessoClinicoService processoClinicoService;

  @GetMapping("/processos-clinicos/{processoClinicoId}")
  public ProcessoClinicoSaidaSchema buscarPorId(
      @PathVariable UUID organizacaoId, @PathVariable UUID processoClinicoId) {
    return processoClinicoService.buscarPorIdDaOrganizacao(
        new ProcessoClinicoPorIdEntradaSchema(organizacaoId, processoClinicoId));
  }

  @PutMapping("/pacientes/{pacienteId}/processo-clinico")
  public ProcessoClinicoSaidaSchema vincularAoPaciente(
      @PathVariable UUID organizacaoId,
      @PathVariable UUID pacienteId,
      @Valid @RequestBody VincularProcessoClinicoRequestSchema body) {
    return processoClinicoService.vincularAoPaciente(
        new VincularProcessoClinicoEntradaSchema(
            organizacaoId, pacienteId, body.processoClinicoId()));
  }
}
