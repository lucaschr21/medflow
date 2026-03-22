package br.com.medflow.repositories.financeiro;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medflow.entities.financeiro.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, UUID> {

  Optional<Pagamento> findByConsultaId(UUID consultaId);

  boolean existsByConsultaId(UUID consultaId);
}
