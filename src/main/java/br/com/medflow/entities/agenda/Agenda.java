package br.com.medflow.entities.agenda;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.com.medflow.entities.base.BaseEntity;
import br.com.medflow.entities.pessoas.Medico;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "agenda")
public class Agenda extends BaseEntity {

    @ElementCollection(fetch = FetchType.LAZY)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "agenda_horario_trabalho", joinColumns = @JoinColumn(name = "agenda_id"))
    @Column(name = "dia_semana", nullable = false, length = 16)
    private Set<@NotNull(message = "Dia de trabalho é obrigatório") DayOfWeek> horarioTrabalho = new LinkedHashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "agenda_bloqueio", joinColumns = @JoinColumn(name = "agenda_id"))
    @AttributeOverrides({
            @AttributeOverride(name = "inicio", column = @Column(name = "data_hora_inicio", nullable = false)),
            @AttributeOverride(name = "fim", column = @Column(name = "data_hora_fim", nullable = false))
    })
    private Set<@Valid BloqueioAgenda> bloqueios = new LinkedHashSet<>();

    @NotNull(message = "Médico é obrigatório")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medico_id", nullable = false, unique = true)
    private Medico medico;

    public void definirMedico(Medico medico) {
        this.medico = Objects.requireNonNull(medico, "Médico é obrigatório");
    }

    public void adicionarDiaTrabalho(DayOfWeek dia) {
        this.horarioTrabalho.add(Objects.requireNonNull(dia, "Dia de trabalho é obrigatório"));
    }

    public void removerDiaTrabalho(DayOfWeek dia) {
        this.horarioTrabalho.remove(Objects.requireNonNull(dia, "Dia de trabalho é obrigatório"));
    }

    public void adicionarBloqueio(LocalDateTime inicio, LocalDateTime fim) {
        this.bloqueios.add(new BloqueioAgenda(inicio, fim));
    }

    public void removerBloqueio(LocalDateTime inicio, LocalDateTime fim) {
        this.bloqueios.remove(new BloqueioAgenda(inicio, fim));
    }
}
