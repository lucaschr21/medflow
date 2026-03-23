package br.com.medflow.controllers;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.medflow.schemas.pessoas.entrada.PacientePorIdEntradaSchema;
import br.com.medflow.schemas.pessoas.saida.PacienteSaidaSchema;
import br.com.medflow.services.pessoas.PacienteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/organizacoes/{organizacaoId}/pacientes")
@Tag(name = "Pacientes")
public class PacienteController {

  private final PacienteService pacienteService;

  @GetMapping("/{pacienteId}")
  public PacienteSaidaSchema buscarPorId(
      @PathVariable UUID organizacaoId, @PathVariable UUID pacienteId) {
    return pacienteService.buscarPorIdDaOrganizacao(
        new PacientePorIdEntradaSchema(organizacaoId, pacienteId));
  }
}
