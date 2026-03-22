package br.com.medflow.repositories.agenda;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medflow.entities.agenda.Agenda;

public interface AgendaRepository extends JpaRepository<Agenda, UUID> {

  Optional<Agenda> findByMedicoId(UUID medicoId);

  boolean existsByMedicoId(UUID medicoId);
}
