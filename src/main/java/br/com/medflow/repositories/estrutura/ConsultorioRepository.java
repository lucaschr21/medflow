package br.com.medflow.repositories.estrutura;

import br.com.medflow.entities.estrutura.Consultorio;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultorioRepository extends JpaRepository<Consultorio, UUID> {

  List<Consultorio> findByUnidadeId(UUID unidadeId);

  boolean existsByUnidadeId(UUID unidadeId);
}
