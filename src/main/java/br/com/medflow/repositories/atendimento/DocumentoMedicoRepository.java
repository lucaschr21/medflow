package br.com.medflow.repositories.atendimento;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medflow.entities.atendimento.DocumentoMedico;

public interface DocumentoMedicoRepository extends JpaRepository<DocumentoMedico, UUID> {

  List<DocumentoMedico> findByConsultaId(UUID consultaId);
}
