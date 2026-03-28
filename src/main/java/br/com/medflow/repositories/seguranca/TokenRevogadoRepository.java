package br.com.medflow.repositories.seguranca;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medflow.entities.seguranca.TokenRevogado;

public interface TokenRevogadoRepository extends JpaRepository<TokenRevogado, UUID> {

  boolean existsByJti(String jti);
}
