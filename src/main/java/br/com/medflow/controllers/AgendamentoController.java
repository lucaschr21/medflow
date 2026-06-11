package br.com.medflow.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.medflow.core.security.annotations.AuthorizeResource;
import br.com.medflow.entities.Consulta;
import br.com.medflow.services.ConsultorioService;
import br.com.medflow.services.DisponibilidadeAgendaService;
import br.com.medflow.services.MedicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/** Endpoints auxiliares para o fluxo de agendamento de consultas. */
@Validated
@RestController
@RequestMapping("/api/agendamento")
@Tag(name = "Agendamento")
@AuthorizeResource(Consulta.class)
public class AgendamentoController {

  private final DisponibilidadeAgendaService disponibilidadeService;
  private final MedicoService medicoService;
  private final ConsultorioService consultorioService;

  public AgendamentoController(
      DisponibilidadeAgendaService disponibilidadeService,
      MedicoService medicoService,
      ConsultorioService consultorioService) {
    this.disponibilidadeService = disponibilidadeService;
    this.medicoService = medicoService;
    this.consultorioService = consultorioService;
  }

  /** Retorna os horários disponíveis para um médico e consultório em uma data. */
  @GetMapping("/horarios-disponiveis")
  @Operation(summary = "Listar horários disponíveis para agendamento")
  public List<DisponibilidadeAgendaService.SlotDisponivel> horariosDisponiveis(
      @RequestParam UUID medicoId,
      @RequestParam UUID consultorioId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
    var medico = medicoService.findByIdOrThrow(medicoId);
    var consultorio = consultorioService.findByIdOrThrow(consultorioId);
    return disponibilidadeService.buscarHorariosDisponiveis(medico, consultorio, data);
  }
}
