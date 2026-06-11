package br.com.medflow.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.medflow.entities.AgendaMedica;
import br.com.medflow.entities.BloqueioAgenda;
import br.com.medflow.entities.Consulta;
import br.com.medflow.entities.Consultorio;
import br.com.medflow.entities.Medico;
import br.com.medflow.entities.enums.StatusConsulta;
import br.com.medflow.repositories.AgendaMedicaRepository;
import br.com.medflow.repositories.BloqueioAgendaRepository;
import br.com.medflow.repositories.ConsultaRepository;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

/**
 * Calcula horários disponíveis para agendamento de consultas.
 *
 * <p>
 * Considera agendas médicas, consultas existentes, bloqueios e
 * conflitos de médico e consultório.
 */
@Service
@Transactional(readOnly = true)
public class DisponibilidadeAgendaService {

    private static final int DURACAO_PADRAO_MINUTOS = 30;

    private final AgendaMedicaRepository agendaMedicaRepository;
    private final ConsultaRepository consultaRepository;
    private final BloqueioAgendaRepository bloqueioAgendaRepository;

    public DisponibilidadeAgendaService(
            AgendaMedicaRepository agendaMedicaRepository,
            ConsultaRepository consultaRepository,
            BloqueioAgendaRepository bloqueioAgendaRepository) {
        this.agendaMedicaRepository = agendaMedicaRepository;
        this.consultaRepository = consultaRepository;
        this.bloqueioAgendaRepository = bloqueioAgendaRepository;
    }

    /**
     * Retorna os horários disponíveis para um médico em um consultório em uma data.
     *
     * @param medico      médico a consultar
     * @param consultorio consultório a consultar
     * @param data        data desejada
     * @return lista de slots disponíveis (início, fim)
     */
    public List<SlotDisponivel> buscarHorariosDisponiveis(Medico medico, Consultorio consultorio, LocalDate data) {
        return buscarHorariosDisponiveis(medico, consultorio, data, DURACAO_PADRAO_MINUTOS);
    }

    /**
     * Retorna os horários disponíveis com duração personalizada.
     */
    public List<SlotDisponivel> buscarHorariosDisponiveis(
            Medico medico, Consultorio consultorio, LocalDate data, int duracaoMinutos) {
        DayOfWeek diaSemana = data.getDayOfWeek();
        List<AgendaMedica> agendas = buscarAgendasAtivas(medico, consultorio, diaSemana);
        if (agendas.isEmpty()) {
            return List.of();
        }

        List<SlotOcupado> ocupados = buscarOcupacoes(medico, consultorio, data);

        List<SlotDisponivel> disponiveis = new ArrayList<>();
        for (AgendaMedica agenda : agendas) {
            List<SlotDisponivel> slots = gerarSlots(agenda, data, duracaoMinutos);
            for (SlotDisponivel slot : slots) {
                if (!conflitaComOcupados(slot, ocupados) && slot.inicio().isAfter(LocalDateTime.now())) {
                    disponiveis.add(slot);
                }
            }
        }

        disponiveis.sort(Comparator.comparing(SlotDisponivel::inicio));
        return disponiveis;
    }

    /**
     * Verifica se um horário específico está disponível (para revalidação no
     * momento da criação).
     */
    public boolean isHorarioDisponivel(Medico medico, Consultorio consultorio,
            LocalDateTime inicio, LocalDateTime fim) {
        LocalDate data = inicio.toLocalDate();
        List<SlotOcupado> ocupados = buscarOcupacoes(medico, consultorio, data);
        return !conflitaComOcupados(new SlotDisponivel(inicio, fim), ocupados)
                && inicio.isAfter(LocalDateTime.now());
    }

    private List<AgendaMedica> buscarAgendasAtivas(Medico medico, Consultorio consultorio, DayOfWeek diaSemana) {
        return agendaMedicaRepository.findAll((root, query, cb) -> {
            var alocacao = root.join("alocacaoMedico", JoinType.INNER);
            return cb.and(
                    cb.equal(alocacao.get("medico"), medico),
                    cb.equal(alocacao.get("consultorio"), consultorio),
                    cb.equal(root.get("diaSemana"), diaSemana),
                    cb.isTrue(root.get("ativo")),
                    cb.isTrue(alocacao.get("ativo")));
        });
    }

    private List<SlotOcupado> buscarOcupacoes(Medico medico, Consultorio consultorio, LocalDate data) {
        LocalDateTime inicioDia = data.atStartOfDay();
        LocalDateTime fimDia = data.plusDays(1).atStartOfDay();

        List<Consulta> consultas = consultaRepository.findAll((root, query, cb) -> {
            Predicate noPeriodo = cb.and(
                    cb.greaterThanOrEqualTo(root.get("dataHoraInicio"), inicioDia),
                    cb.lessThan(root.get("dataHoraInicio"), fimDia));
            Predicate naoCancelada = root.get("status").in(
                    StatusConsulta.AGENDADA, StatusConsulta.CONFIRMADA,
                    StatusConsulta.EM_ESPERA, StatusConsulta.EM_ATENDIMENTO);
            Predicate mesmoMedico = cb.equal(root.get("medico"), medico);
            Predicate mesmoConsultorio = cb.equal(root.get("consultorio"), consultorio);
            return cb.and(noPeriodo, naoCancelada, cb.or(mesmoMedico, mesmoConsultorio));
        });

        List<BloqueioAgenda> bloqueios = bloqueioAgendaRepository.findAll((root, query, cb) -> {
            Predicate noPeriodo = cb.and(
                    cb.greaterThanOrEqualTo(root.get("inicio"), inicioDia),
                    cb.lessThan(root.get("inicio"), fimDia));
            Predicate mesmoMedico = cb.equal(root.get("medico"), medico);
            Predicate mesmoConsultorio = cb.equal(root.get("consultorio"), consultorio);
            return cb.and(noPeriodo, cb.or(mesmoMedico, mesmoConsultorio));
        });

        List<SlotOcupado> ocupados = new ArrayList<>();
        consultas.forEach(c -> ocupados.add(new SlotOcupado(c.getDataHoraInicio(), c.getDataHoraFim())));
        bloqueios.forEach(b -> ocupados.add(new SlotOcupado(b.getInicio(), b.getFim())));
        return ocupados;
    }

    private List<SlotDisponivel> gerarSlots(AgendaMedica agenda, LocalDate data, int duracaoMinutos) {
        List<SlotDisponivel> slots = new ArrayList<>();
        LocalTime cursor = agenda.getHoraInicio();
        LocalTime fimAgenda = agenda.getHoraFim();

        while (cursor.plusMinutes(duracaoMinutos).compareTo(fimAgenda) <= 0) {
            LocalDateTime inicio = LocalDateTime.of(data, cursor);
            LocalDateTime fim = inicio.plusMinutes(duracaoMinutos);
            slots.add(new SlotDisponivel(inicio, fim));
            cursor = cursor.plusMinutes(duracaoMinutos);
        }
        return slots;
    }

    private boolean conflitaComOcupados(SlotDisponivel slot, List<SlotOcupado> ocupados) {
        return ocupados.stream().anyMatch(o -> o.intersecta(slot.inicio(), slot.fim()));
    }

    /**
     * Representa um intervalo de horário disponível.
     */
    public record SlotDisponivel(LocalDateTime inicio, LocalDateTime fim) {
    }

    /**
     * Representa um intervalo ocupado (consulta ou bloqueio).
     */
    private record SlotOcupado(LocalDateTime inicio, LocalDateTime fim) {
        boolean intersecta(LocalDateTime inicioSlot, LocalDateTime fimSlot) {
            return inicio.isBefore(fimSlot) && fim.isAfter(inicioSlot);
        }
    }
}
