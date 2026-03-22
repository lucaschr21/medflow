package br.com.medflow.entities.atendimento;

import java.time.LocalDateTime;
import java.util.Objects;

import br.com.medflow.entities.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "registro_atendimento")
public class RegistroAtendimento extends BaseEntity {

    @NotNull(message = "Data de registro é obrigatória")
    @Column(name = "data_registro", nullable = false)
    private LocalDateTime dataRegistro;

    @Column(columnDefinition = "text")
    private String anamnese;

    @Column(columnDefinition = "text")
    private String sintomas;

    @Column(columnDefinition = "text")
    private String diagnostico;

    @Column(name = "notas_clinicas", columnDefinition = "text")
    private String notasClinicas;

    @NotNull(message = "Processo clínico é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "processo_clinico_id", nullable = false)
    private ProcessoClinico processoClinico;

    @NotNull(message = "Consulta é obrigatória")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "consulta_id", nullable = false, unique = true)
    private Consulta consulta;

    public void definirDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = Objects.requireNonNull(dataRegistro, "Data de registro é obrigatória");
    }

    public void atualizarConteudoClinico(
            String anamnese,
            String sintomas,
            String diagnostico,
            String notasClinicas) {
        this.anamnese = anamnese;
        this.sintomas = sintomas;
        this.diagnostico = diagnostico;
        this.notasClinicas = notasClinicas;
    }

    void setProcessoClinico(ProcessoClinico processoClinico) {
        this.processoClinico = Objects.requireNonNull(processoClinico, "Processo clínico é obrigatório");
    }

    void setConsulta(Consulta consulta) {
        this.consulta = Objects.requireNonNull(consulta, "Consulta é obrigatória");
    }
}
