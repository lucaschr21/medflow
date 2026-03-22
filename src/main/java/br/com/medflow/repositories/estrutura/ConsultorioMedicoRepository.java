package br.com.medflow.repositories.estrutura;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medflow.entities.estrutura.ConsultorioMedico;

public interface ConsultorioMedicoRepository extends JpaRepository<ConsultorioMedico, UUID> {

  Optional<ConsultorioMedico> findByConsultorioIdAndMedicoId(UUID consultorioId, UUID medicoId);

  boolean existsByConsultorioIdAndMedicoId(UUID consultorioId, UUID medicoId);

  List<ConsultorioMedico> findByConsultorioId(UUID consultorioId);

  List<ConsultorioMedico> findByMedicoId(UUID medicoId);
}
