package br.com.medflow.repositories.estrutura;

import br.com.medflow.entities.estrutura.Organizacao;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizacaoRepository extends JpaRepository<Organizacao, UUID> {

  Optional<Organizacao> findByCnpj(String cnpj);

  boolean existsByCnpj(String cnpj);
}
