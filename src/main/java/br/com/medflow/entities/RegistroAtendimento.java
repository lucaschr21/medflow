package br.com.medflow.entities;

import org.hibernate.envers.Audited;

import br.com.medflow.core.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa o registro clinico produzido durante o atendimento de uma consulta.
 */
@Getter
@Setter
@Entity
@Audited
@Table(name = "registro_atendimento")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class RegistroAtendimento extends Auditable {

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "consulta_id", nullable = false, unique = true)
  private Consulta consulta;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "medico_id", nullable = false)
  private Medico medico;

  @Column(name = "queixa_principal", nullable = false)
  private String queixaPrincipal;

  @Lob
  @Column(name = "anamnese", nullable = false)
  private String anamnese;

  @Lob
  @Column(name = "conduta", nullable = false)
  private String conduta;

  @Lob
  @Column(name = "observacoes")
  private String observacoes;
}
