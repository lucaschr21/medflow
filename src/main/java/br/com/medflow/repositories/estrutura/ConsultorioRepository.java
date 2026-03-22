package br.com.medflow.repositories.estrutura;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medflow.entities.estrutura.Consultorio;

public interface ConsultorioRepository extends JpaRepository<Consultorio, UUID> {

  List<Consultorio> findByUnidadeId(UUID unidadeId);

  boolean existsByUnidadeId(UUID unidadeId);
}
