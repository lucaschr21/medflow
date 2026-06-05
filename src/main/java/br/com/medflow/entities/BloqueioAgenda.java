package br.com.medflow.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.envers.Audited;
import org.hibernate.type.SqlTypes;

import br.com.medflow.core.audit.Auditable;
import br.com.medflow.entities.enums.TipoBloqueioAgenda;
import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa um bloqueio de agenda de um medico em determinado consultorio.
 */
@Getter
@Setter
@Entity
@Audited
@Table(name = "bloqueio_agenda", indexes = {
        @Index(name = "ix_bloqueio_agenda_medico_id", columnList = "medico_id"),
        @Index(name = "ix_bloqueio_agenda_consultorio_id", columnList = "consultorio_id"),
        @Index(name = "ix_bloqueio_agenda_medico_inicio", columnList = "medico_id, inicio"),
        @Index(name = "ix_bloqueio_agenda_consultorio_inicio", columnList = "consultorio_id, inicio")
}, check = @CheckConstraint(name = "ck_bloqueio_agenda_intervalo", constraint = "fim > inicio and char_length(trim(motivo)) > 0"))
@NoArgsConstructor
public class BloqueioAgenda extends Auditable {

    @NotNull(message = "O médico do bloqueio é obrigatório.")
    @ManyToOne(optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @NotNull(message = "O consultório do bloqueio é obrigatório.")
    @ManyToOne(optional = false)
    @JoinColumn(name = "consultorio_id", nullable = false)
    private Consultorio consultorio;

    @NotNull(message = "O início do bloqueio é obrigatório.")
    @Column(name = "inicio", nullable = false)
    private LocalDateTime inicio;

    @NotNull(message = "O fim do bloqueio é obrigatório.")
    @Column(name = "fim", nullable = false)
    private LocalDateTime fim;

    @NotBlank(message = "O motivo do bloqueio é obrigatório.")
    @Size(max = 500, message = "O motivo do bloqueio deve ter no máximo 500 caracteres.")
    @Column(name = "motivo", nullable = false, length = 500)
    private String motivo;

    @NotNull(message = "O tipo do bloqueio é obrigatório.")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "tipo", nullable = false)
    private TipoBloqueioAgenda tipo;
}
