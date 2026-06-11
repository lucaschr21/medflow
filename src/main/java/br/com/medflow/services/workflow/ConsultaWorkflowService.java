package br.com.medflow.services.workflow;

import java.util.Set;

import org.springframework.stereotype.Component;

import br.com.medflow.core.exceptions.BusinessRuleException;
import br.com.medflow.core.exceptions.ErrorCode;
import br.com.medflow.entities.Consulta;
import br.com.medflow.entities.enums.StatusConsulta;

/**
 * Controla as transições válidas do ciclo de vida de uma {@link Consulta}.
 *
 * <p>
 * Transições normais:
 * 
 * <pre>
 * AGENDADA → CONFIRMADA → EM_ESPERA → EM_ATENDIMENTO → FINALIZADA
 * </pre>
 *
 * <p>
 * Transições alternativas:
 * 
 * <pre>
 * AGENDADA/CONFIRMADA → CANCELADA
 * AGENDADA/CONFIRMADA/EM_ESPERA → NAO_COMPARECEU
 * </pre>
 */
@Component
public class ConsultaWorkflowService {

    private static final Set<StatusConsulta> CANCELAVEIS = Set.of(StatusConsulta.AGENDADA, StatusConsulta.CONFIRMADA);

    private static final Set<StatusConsulta> FALTOSOS = Set.of(StatusConsulta.AGENDADA, StatusConsulta.CONFIRMADA,
            StatusConsulta.EM_ESPERA);

    /**
     * Transita a consulta para {@link StatusConsulta#CONFIRMADA}.
     */
    public void confirmar(Consulta consulta) {
        transitar(consulta, StatusConsulta.CONFIRMADA, Set.of(StatusConsulta.AGENDADA));
    }

    /**
     * Transita a consulta para {@link StatusConsulta#EM_ESPERA}.
     */
    public void colocarEmEspera(Consulta consulta) {
        transitar(consulta, StatusConsulta.EM_ESPERA, Set.of(StatusConsulta.CONFIRMADA));
    }

    /**
     * Transita a consulta para {@link StatusConsulta#EM_ATENDIMENTO}.
     */
    public void iniciarAtendimento(Consulta consulta) {
        transitar(consulta, StatusConsulta.EM_ATENDIMENTO, Set.of(StatusConsulta.EM_ESPERA));
    }

    /**
     * Transita a consulta para {@link StatusConsulta#FINALIZADA}.
     */
    public void finalizar(Consulta consulta) {
        transitar(consulta, StatusConsulta.FINALIZADA, Set.of(StatusConsulta.EM_ATENDIMENTO));
    }

    /**
     * Transita a consulta para {@link StatusConsulta#CANCELADA}.
     */
    public void cancelar(Consulta consulta) {
        transitar(consulta, StatusConsulta.CANCELADA, CANCELAVEIS);
    }

    /**
     * Transita a consulta para {@link StatusConsulta#NAO_COMPARECEU}.
     */
    public void marcarNaoCompareceu(Consulta consulta) {
        transitar(consulta, StatusConsulta.NAO_COMPARECEU, FALTOSOS);
    }

    private void transitar(Consulta consulta, StatusConsulta destino, Set<StatusConsulta> origensPermitidas) {
        StatusConsulta atual = consulta.getStatus();
        if (!origensPermitidas.contains(atual)) {
            throw new BusinessRuleException(
                    ErrorCode.TRANSICAO_CONSULTA_INVALIDA,
                    "Transição inválida: " + atual + " → " + destino);
        }
        consulta.setStatus(destino);
    }
}
