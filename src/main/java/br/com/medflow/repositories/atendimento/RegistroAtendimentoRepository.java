package br.com.medflow.repositories.atendimento;

import br.com.medflow.entities.atendimento.RegistroAtendimento;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistroAtendimentoRepository extends JpaRepository<RegistroAtendimento, UUID> {

  Optional<RegistroAtendimento> findByConsultaId(UUID consultaId);

  boolean existsByConsultaId(UUID consultaId);

  List<RegistroAtendimento> findByProcessoClinicoId(UUID processoClinicoId);
}
