package br.com.medflow.repositories.financeiro;

import br.com.medflow.entities.financeiro.ProcedimentoPreco;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcedimentoPrecoRepository extends JpaRepository<ProcedimentoPreco, UUID> {

  List<ProcedimentoPreco> findByConvenioId(UUID convenioId);

  boolean existsByConvenioId(UUID convenioId);
}
